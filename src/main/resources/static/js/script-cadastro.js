function cadastrar() {
    var formData = new FormData();
    var username = $("#username").val();
    var password = $("#password").val();
    var foto = $("#foto_perfil").val();

    try {
        var foto = document.getElementById('foto_perfil').files[0];
        formData.append("username", username);
        formData.append("password", password);
        formData.append("foto", foto);
    } catch (e) {
        formData.append("username", username);
        formData.append("password", password);
    }

    if (username == null || username != null && username.trim() == '') {
        alert('Produtos sem descrição não podem ser cadastrados.');
        return;
    }

    if (password == null || password != null && password.trim() == '') {
        alert('Produtos sem descrição não podem ser cadastrados.');
        return;
    }

    $.ajax({
        method: "POST",
        url: "http://localhost:8081/ferragem-avila/salvar_vendedor",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            document.getElementById('form_cadastro_vendedor').reset();
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao cadastrar vendedor: " + xhr.responseText);
    });
    
}
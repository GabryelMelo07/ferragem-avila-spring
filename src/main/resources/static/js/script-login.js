function logar() {
    var username = $("#username_login").val().toLowerCase();
    var password = $("#password_login").val().toLowerCase();
    var isWrongPassword = true;
    const url = window.location.protocol + "//" + window.location.host;

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/realizar_login",
        data: { username, password },
        success: function (response) {
            if (response.username.toLowerCase() == username.toLowerCase() && response.password.toLowerCase() == password.toLowerCase()) {
                isWrongPassword = false;
                localStorage.setItem("userId", response.id);
                localStorage.setItem("username", response.username);
                localStorage.setItem("userImage", response.foto);
            }
        },
        complete: function (response) {
            if (isWrongPassword == false) {
                window.location.replace(url + "/ferragem-avila/views/venda.html");
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Usuário inexistente e/ou senha incorreta");
    });
}
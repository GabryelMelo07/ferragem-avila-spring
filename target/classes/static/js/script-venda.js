function abrirVenda() {
    $.ajax({
        method: "POST",
        url: "/salvar_venda",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            alert("Venda cadastrada com sucesso.");
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao cadastrar produto: " + xhr.responseText);
    });
}

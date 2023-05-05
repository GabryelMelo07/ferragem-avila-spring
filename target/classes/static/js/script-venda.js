function listarVendas() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listartodos_vendas",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            $('#tabelaVendas > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                $("#tabelaVendas > tbody").append('<tr><td>' + response[i].id + '</td><td>' + response[i].data + '</td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao listar produtos: " + xhr.responseText);
    });
}

function pesquisarPorIdVenda() {
    var id = $("#pesquisa_id_venda").val();
    if (id != null && id.trim() != '') {
        $.ajax({
            method: "GET",
            url: "http://localhost:8081/ferragem-avila/buscar_venda",
            data: { idVenda: id },
            success: function (response) {
                $("#tabelaVendas > tbody > tr").remove();
                $("#tabelaVendas > tbody").append('<tr id="' + response.id + '"><td>' + response.id + '</td><td>' + response.data + '</td></tr>');
            }
        }).fail(function (xhr, status, errorThrown) {
            alert("Erro ao pesquisar produto por id: " + xhr.responseText);
        });
    } else {
        listarVendas();
    }
}
function listarVendas() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listartodos_vendas",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            $('#tabelaVendas > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                $("#tabelaVendas > tbody").append('<tr><td>' + response[i].id + '</td><td>' + response[i].data + '</td><td>' + response[i].forma_pagamento + '</td><td class="icon-centralized"><button type="button" class="btn btn-light" data-toggle="modal" data-target="#modalInfoVenda" onclick="listarItensVenda(' + response[i].id + ')"><i class="fa-solid fa-circle-info"></i></button></td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao listar produtos: " + xhr.responseText);
    });
}

function listarVendasPorData() {
    var data_selecionada = $("#data_select").val();

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listar_por_data",
        data: { data: data_selecionada },
        success: function (response) {
            $('#tabelaVendas > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                $("#tabelaVendas > tbody").append('<tr><td>' + response[i].id + '</td><td>' + response[i].data + '</td><td>' + response[i].forma_pagamento + '</td><td class="icon-centralized"><button type="button" class="btn btn-light" data-toggle="modal" data-target="#modalInfoVenda" onclick="listarItensVenda(' + response[i].id + ')"><i class="fa-solid fa-circle-info"></i></button></td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao pesquisar produto por id: " + xhr.responseText);
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

function listarItensVenda(id) {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listItensVenda",
        data: { id: id },
        success: function (response) {
            console.log(response);
            $('#tabelaResumoVenda > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                $("#tabelaResumoVenda > tbody").append('<tr><td>' + response[i].id + '</td><td>' + response[i].data + '</td><td>' + response[i].forma_pagamento + '</td><td class="wrap-cell">' + response[i].itensString + '</td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar informações da venda: " + xhr.responseText);
    });    
}
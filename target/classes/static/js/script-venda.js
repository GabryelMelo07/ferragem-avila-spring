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
                var data_venda = new Date(response[i].data);
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
            $('#tabelaResumoVenda > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                $("#tabelaResumoVenda > tbody").append('<tr><td>' + response[i].id + '</td><td>' + response[i].data + '</td><td>' + response[i].forma_pagamento + '</td><td class="wrap-cell">' + response[i].itensString + '</td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar informações da venda: " + xhr.responseText);
    });    
}

function gerarRelatorioDia() {
    var data_selecionada = $("#data_select_rel_dia").val();

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listarVendasPorDia",
        data: { data: data_selecionada },
        success: function (response) {

            const wb = XLSX.utils.book_new();

            wb.Props = {
                Title: 'Relatório de vendas diario',
                Subject: 'Vendas realizas na data selecionada',
                Author: 'Ferragem Avila',
                CreatedDate: new Date(),
            };

            wb.SheetNames.push('Relatório');

            var dados = [
                ['Venda', 'Data', 'Forma de pagamento', 'Itens'],
            ];

            for (var i = 0; i < response.length; i++) {
                dados.push([response[i].id, response[i].data, response[i].forma_pagamento, response[i].itensString]);
            }

            const ws = XLSX.utils.aoa_to_sheet(dados);

            wb.Sheets['Relatório'] = ws;

            XLSX.writeFile(wb, 'Relatorio_vendas_diario_' + data_selecionada + '.xlsx', { bookType: 'xlsx', type: 'bynary' });

            document.getElementById('form-relatorio-dia').reset();
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar informações da venda: " + xhr.responseText);
    });
}

function gerarRelatorioMes() {
    var data_selecionada = $("#input-rel-mes").val();
    var split = data_selecionada.split("/");
    var mes = split[0];
    var ano = split[1];
    
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listarVendasPorMes",
        data: { ano: ano, mes: mes },
        success: function (response) {
            const wb = XLSX.utils.book_new();

            wb.Props = {
                Title: 'Relatório de vendas mensal',
                Subject: 'Vendas realizas na data selecionada',
                Author: 'Ferragem Avila',
                CreatedDate: new Date(),
            };

            wb.SheetNames.push('Relatório');

            var dados = [
                ['Venda', 'Data', 'Forma de pagamento', 'Itens'],
            ];

            for (var i = 0; i < response.length; i++) {
                dados.push([response[i].id, response[i].data, response[i].forma_pagamento, response[i].itensString]);
            }

            const ws = XLSX.utils.aoa_to_sheet(dados);

            wb.Sheets['Relatório'] = ws;

            XLSX.writeFile(wb, 'Relatorio_vendas_mensal_' + data_selecionada + '.xlsx', { bookType: 'xlsx', type: 'bynary' });

            document.getElementById('form-relatorio-mes').reset();
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar informações da venda: " + xhr.responseText);
    });
}
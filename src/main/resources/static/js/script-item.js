function adicionarItem() {

    var venda_id = (($("#venda_id").val() === undefined) ? 0 : $("#venda_id").val());
    var id = $("#inserir_id").val();
    var idAnterior = id;
    var quantidade;

    if (id === idAnterior) {
        quantidade += 1;
    } else {
        quantidade = 1;
    }

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/salvar_item",
        data: {
            venda_id: venda_id,
            produto_id: id,
            quantidade_produto: quantidade
        },
        success: function (response) {
            alert(response);
            $("#venda_id").val(response.venda_id);
            $('#resumoVenda > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {               
            $('#resumoVenda > tbody').append('<tr id="' + response[i].id + '"><td id="tabela_id">' + response[i].id + '</td><td id="tabela_descricao">' + response[i].descricao + '</td><td id="tabela_valor">' + response[i].preco.toLocaleString("pt-BR",
             {style: "currency", currency: "BRL"}) + '</td><td id="tabela_quantidade">' + response[i].estoque +'</td><td id="tabela_cod_barras">'+response[i].cod_barras+'</td><td id="tabela_btn_editar" class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizaProduto" onclick="atualizarProduto('+response[i].id+')"><i class="fa-solid fa-pen-to-square"></i></button></td><td id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarProduto('+response[i].id+')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao listar produtos: " + xhr.responseText);
    });
}

function confirmarVenda() {
    $('#resumoVenda > tbody > tr').remove();
    $("#venda_id").val(0);
}

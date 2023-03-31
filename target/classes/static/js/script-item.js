function deletarItem(item_id) {

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/deletar_item",
        data: {
            item_id: item_id
        },
        success: function (response) {
            alert(response);
            $('#' + item_id).remove();
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro em adicionar item: " + xhr.responseText);
    });
}



function adicionarItem() {

    var venda_id = $("#venda_id").val();
    venda_id = ((venda_id !== "") ? venda_id : 0);
//    alert("venda_id:" + venda_id);



//    if (venda_id === 0) {
//        abrirVenda();
//    }

    var produto_id = $("#inserir_id").val();
//    alert("produto:" + produto_id);
//    var idAnterior = id;
    var quantidade = 1;

//    if (id === idAnterior) quantidade += 1;

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/salvar_item",
        data: {
            venda_id: venda_id,
            produto_id: produto_id,
            quantidade: quantidade
        },
        success: function (response) {
            var item = response;
            if (item.id !== 0) {
//                alert(item.id + ";" + item.venda.id)
                $("#venda_id").val(item.venda.id);
                listarItensVenda(item.venda.id);
            } else {
                alert("codigo invalido de produto");
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro em adicionar item: " + xhr.responseText);
    });
}

// melhorar esta funcao pra que atualize itens com qtdes > 1
function listarItensVenda(venda_id) {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listar_itens",
        data: {
            venda_id: venda_id
        },
        success: function (response) {
            for (var i = 0; i < response.length; i++) {
                $('#' + response[i].id).remove();
            }
            for (var i = 0; i < response.length; i++) {
                $('#resumoVenda').append('<tr style=\"color:white\" id="' + response[i].id + '"><td id="tabela_id">' + response[i].id + '</td><td id="tabela_descricao">' + response[i].produto.descricao + '</td><td id="tabela_valor">' + response[i].produto.preco.toLocaleString("pt-BR",
                        {style: "currency", currency: "BRL"}) + '</td><td id="tabela_quantidade">' + response[i].quantidade + '</td><td id="tabela_cod_barras">nao vem codigo de barra </td><td id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarItem(' + response[i].id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro em adicionar item: " + xhr.responseText);
    });


}

//function abrirVenda() {
//    $.ajax({
//        method: "POST",
//        url: "/salvar_venda",
//        contentType: "application/json; charset=utf-8",
//        success: function (response) {
//            alert("Venda cadastrada com sucesso.");
//        }
//    }).fail(function (xhr, status, errorThrown) {
//        alert("Erro ao cadastrar produto: " + xhr.responseText);
//    });
//}

//function confirmarVenda() {
//    $('#resumoVenda > tbody > tr').remove();
//    $("#venda_id").val(0);
//}

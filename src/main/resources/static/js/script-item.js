function loadVendaLocalStorage(){
    var venda_id = localStorage.getItem("venda_id");
//    alert(venda_id);
    if (venda_id !== null && venda_id !== "0") {
        $("#venda_id").val(venda_id);
        listarItensVenda(venda_id);
    }    
}

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
    var produto_id = $("#inserir_id").val();
    var quantidade = 1;
    var cod_barras = $("#cod_barras").val(); // código de barras

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/salvar_item",
        data: {
            venda_id: venda_id,
            produto_id: produto_id,
            quantidade: quantidade,
            cod_barras: cod_barras
        },
        success: function (response) {
            var item = response;
            if (item.id !== 0) {
                $("#venda_id").val(item.venda.id);
                localStorage.setItem("venda_id", item.venda.id);
                listarItensVenda(item.venda.id);
            } else {
                alert("Produto inexistente");
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
            $('#resumoVenda > tbody > tr').remove();

            for (var i = 0; i < response.length; i++) {
                $('#resumoVenda').append('<tr id="' + response[i].id + '"><td id="tabela_id">' + response[i].id + '</td><td id="tabela_descricao">' + response[i].produto.descricao + '</td><td class="icon-centralized" id="tabela_valor">' + response[i].produto.preco.toLocaleString("pt-BR", {style: "currency", currency: "BRL"}) + '</td><td class="icon-centralized" id="tabela_quantidade">' + response[i].quantidade + '</td><td class="icon-centralized" id="tabela_cod_barras">' + response[i].produto.cod_barras + '</td><td id="tabela_btn_editar" class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizarQtdItem" onclick="atualizarQtdItem(' + response[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarItem(' + response[i].id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
            }

            $("#totalPag").val(response[i].produto.preco * response[i].quantidade);
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro em listar item: " + xhr.responseText);
    });
}

function cancelarVenda(){    
      var venda_id = localStorage.getItem("venda_id");    
      $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/deletar_venda",
        data: {
            venda_id: venda_id
        },
        success: function (response) {
            listarItensVenda(venda_id);
            $('#resumoVenda > tbody > tr').remove();
            localStorage.setItem("venda_id", 0);     
            $("#venda_id").val("");
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro em adicionar item: " + xhr.responseText);
    });           
}

function confirmarVenda() {
//    tem que dar baixa no estoque => pendente
    $('#resumoVenda > tbody > tr').remove();
    $("#inserir_id").val("");
    $("#venda_id").val("");
    localStorage.removeItem("venda_id");      
}

function atualizarQtdItem(venda_id, produto_id, quantidade) {   // terminar método com atualizar item
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/atualizar_item",
        data: "idProduto=" + id,
        success: function (response) {
            $("#id2").val(response.id);
            $("#descricao2").val(response.descricao);
            $("#valor2").val(response.valor);
            $("#quantidade2").val(response.quantidade);
            $("#cod_barras2").val(response.cod_barras);
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao atualizar produto: " + xhr.responseText);
    });
}
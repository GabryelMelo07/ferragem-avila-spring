function loadVendaLocalStorage(){
    var venda_id = localStorage.getItem("venda_id");
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
    var preco_item = 0;

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_produto",
        data: { idProduto: produto_id },
        success: function (response) {
            preco_item = response.preco;

            $.ajax({
                method: "GET",
                url: "http://localhost:8081/ferragem-avila/salvar_item",
                data: {
                    venda_id: venda_id,
                    produto_id: produto_id,
                    quantidade: quantidade,
                    preco_item: preco_item
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
                alert("Produto inexistente");
            });
            
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar valor do item pelo id: " + xhr.responseText);
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
            var count_item = 1;
            var count_valor_itens = 0.0;
            $('#resumoVenda > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                var valor_item = response[i].produto.preco * response[i].quantidade;
                count_valor_itens += valor_item;
                $('#resumoVenda').append('<tr id="' + response[i].id + '"><td id="tabela_id">' + count_item + '</td><td id="tabela_descricao">' + response[i].produto.descricao + '</td><td class="icon-centralized" id="tabela_valor">' + valor_item.toLocaleString("pt-BR", {style: "currency", currency: "BRL"}) + '</td><td class="icon-centralized" id="tabela_quantidade">' + response[i].quantidade + '</td><td class="icon-centralized" id="tabela_cod_barras">' + response[i].produto.cod_barras + '</td><td id="tabela_btn_editar" class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizarQtdItem" onclick="atualizarQtdItem(' + response[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarItem(' + response[i].id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
                $("#nroItem").val(count_item);
                $("#descItem").val(response[i].produto.descricao);
                $("#qtd2").val(response[i].quantidade);
                $("#vlrUnit").val(response[i].produto.preco.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }));
                $("#vlrTotal").val(valor_item.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }));
                $("#totalPag").val(count_valor_itens.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }));
                count_item++;
            }
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

            // painel lateral
            $("#nroItem").val("");
            $("#descItem").val("");
            $("#qtd2").val("");
            $("#vlrUnit").val("");
            $("#vlrTotal").val("");
            $("#totalPag").val("");
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro em adicionar item: " + xhr.responseText);
    });           
}

function confirmarVenda() {
    var venda_id = $("#venda_id").val();
    $('#resumoVenda > tbody > tr').remove();
    $("#inserir_id").val("");
    $("#inserir_cod_barras").val("");
    $("#venda_id").val("");
    localStorage.removeItem("venda_id");      

    $.ajax({
        method: "PUT",
        url: "http://localhost:8081/ferragem-avila/concluir_venda",
        data: {
            id: venda_id
        },
        success: function (response) {
            if (response.id != 0) {
                alert("Venda concluida com sucesso.");
            } else {
                alert("Venda não concluida, estoque insuficiente.");
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao concluir a venda: " + xhr.responseText);
    });
}

// function atualizarQtdItem(venda_id, produto_id, quantidade) {   // terminar método com atualizar item
//     $.ajax({
//         method: "GET",
//         url: "http://localhost:8081/ferragem-avila/atualizar_item",
//         data: "idProduto=" + id,
//         success: function (response) {
//             $("#id2").val(response.id);
//             $("#descricao2").val(response.descricao);
//             $("#valor2").val(response.valor);
//             $("#quantidade2").val(response.quantidade);
//             $("#cod_barras2").val(response.cod_barras);
//         }
//     }).fail(function (xhr, status, errorThrown) {
//         alert("Erro ao atualizar produto: " + xhr.responseText);
//     });
// }
function loadVendaLocalStorage(){
    var venda_id = localStorage.getItem("venda_id");
    if (venda_id !== null && venda_id !== "0") {
        $("#venda_id").val(venda_id);
        listarItensVenda(venda_id);
        enableBtns();
    } else {
        disableBtns();
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

function adicionarItemPorId(id) {
    var venda_id = $("#venda_id").val();    
    venda_id = ((venda_id !== "") ? venda_id : 0);
    var quantidade = 1;
    var preco_item = 0;

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_produto",
        data: { idProduto: id },
        success: function (response) {
            preco_item = response.preco;

            $.ajax({
                method: "GET",
                url: "http://localhost:8081/ferragem-avila/salvar_item",
                data: {
                    venda_id: venda_id,
                    produto_id: id,
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
                        alert("Produto inexistente1");
                    }
                }
            }).fail(function (xhr, status, errorThrown) {
                alert("Produto inexistente2");
            });

            // $("#inserir_id").val("");
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar valor do item pelo id: " + xhr.responseText);
    });
}

function adicionarItemPorCodBarras(cod_barras) {
    var venda_id = $("#venda_id").val();
    venda_id = ((venda_id !== "") ? venda_id : 0);
    var quantidade = 1;
    var preco_item = 0;

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_por_cod_barras",
        data: { cod_barras: cod_barras },
        success: function (response) {
            produto_id = response.id;
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
                        alert("Produto inexistente1");
                    }
                }
            }).fail(function (xhr, status, errorThrown) {
                alert("Produto inexistente2");
            });

            $("#inserir_cod_barras").val("");
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar valor do item pelo codigo de barras: " + xhr.responseText);
    });
}

function adicionarItem() {
    var produto_id = $("#inserir_id").val();
    var produto_cod_barras = $("#inserir_cod_barras").val();

    if (produto_id != null && produto_id.trim() != '') {
        adicionarItemPorId(produto_id);
    } else if (produto_cod_barras != null && produto_cod_barras.trim() != '') {
        adicionarItemPorCodBarras(produto_cod_barras);
    }
}

function listarItensVenda(venda_id) {
    enableBtns();
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
                $('#resumoVenda').append('<tr id="' + response[i].id + '"><td id="tabela_id">' + count_item + '</td><td id="tabela_descricao">' + response[i].produto.descricao + '</td><td class="icon-centralized" id="tabela_valor">' + valor_item.toLocaleString("pt-BR", {style: "currency", currency: "BRL"}) + '</td><td class="icon-centralized" id="tabela_quantidade">' + response[i].quantidade + '</td><td class="icon-centralized" id="tabela_cod_barras">' + response[i].produto.cod_barras + '</td><td id="tabela_btn_editar" class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizarQtdItem" onclick="loadItem(' + response[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarItem(' + response[i].id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
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
            disableBtns();
            
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
    var select = $("#forma_pagamento").val();
    var option = "";

    if(select == 1) option = "Dinheiro";
    if(select == 2) option = "Cartão";
    if(select == 3) option = "Pix";
    
    $.ajax({
        method: "PUT",
        url: "http://localhost:8081/ferragem-avila/concluir_venda",
        data: {
            id: venda_id,
            forma_pagamento: option
        },
        success: function (response) {
            if (response.id != 0) {
                console.log("Venda concluida com sucesso.");
                $('#resumoVenda > tbody > tr').remove();
                $("#inserir_id").val("");
                $("#inserir_cod_barras").val("");
                $("#venda_id").val("");
                $("#nroItem").val("");
                $("#descItem").val("");
                $("#qtd2").val("");
                $("#vlrUnit").val("");
                $("#vlrTotal").val("");
                $("#totalPag").val("");
                localStorage.removeItem("venda_id");
                disableBtns();
            } else {
                alert("Venda não concluida, estoque insuficiente.");
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao concluir a venda: " + xhr.responseText);
    });
}

function loadItem(id) {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_item",
        data: "idItem=" + id,
        success: function (response) {
            $("#id_att_item").val(response.id);
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao carregar item: " + xhr.responseText);
    });
}

function atualizarItem() {
    var id = $("#id_att_item").val();
    var quantidade = $("#quantidadeProd").val();

    $.ajax({
        method: "PUT",
        url: "http://localhost:8081/ferragem-avila/atualizar_quantidade",
        data: {id: id, quantidade:quantidade},
        success: function (response) {
            loadVendaLocalStorage();
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao carregar item: " + xhr.responseText);
    });
}

function listarPaginaAtual() {
    listar_pagina(parseInt($("#pagina-atual").text()));
}

document.addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        adicionarItem();
    }
});

function disableBtns() {
    document.getElementById("botao_concluir_venda").disabled = true;
    document.getElementById("botao_cancelar_venda").disabled = true;
}

function enableBtns() {
    document.getElementById("botao_concluir_venda").disabled = false;
    document.getElementById("botao_cancelar_venda").disabled = false;
}

$(document).ready(function () {
    // Captura o evento de alteração do select
    $('#forma_pagamento').change(function () {
        // Obtém o valor selecionado
        var selectedOption = $(this).val();
        var elemento = document.getElementById("div_troco");
        var elemento2 = document.getElementById("div_troco2");

        if(selectedOption == 1) {
            elemento.style.display = "flex";
            elemento2.style.display = "flex";
        } else {
            elemento.style.display = "none";
            elemento2.style.display = "none";
        }
    });
});

$(document).ready(function () {
    $('#valor_pago').on('input', function () {
        var total = $("#vlrTotal").val();
        total = parseFloat(total.replace('R$', '').replace(',', '.').trim());
        var valor_pago = parseFloat($(this).val());
        var troco = 0.0;

        if (valor_pago !== '' && valor_pago > total) {
            troco = (valor_pago - total).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
        } else {
            troco = "Valor pago é insuficiente.";
        }

        $("#troco").val(troco);
    });
});

function limpa_modal_forma_pagamento() {
    document.getElementById('formFormaPagamento').reset();
    $('#modalCupomFiscal').modal({ backdrop: 'static', keyboard: false });
}

function preencherCupomFiscal() {
    const data_atual = new Date();
    var venda_id = $("#venda_id").val();
    var vlr_pago_modal = $("#valor_pago").val();
    var select = $("#forma_pagamento").val();
    var forma_pagamento_modal = "";
    var troco_modal = $("#troco").val();

    if (select == 1) forma_pagamento_modal = "DINHEIRO";
    if (select == 2) forma_pagamento_modal = "CARTÃO";
    if (select == 3) forma_pagamento_modal = "PIX";

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listar_itens",
        data: {
            venda_id: venda_id
        },
        success: function (response) {

            $('#data_atual').text(data_atual.toLocaleDateString() + ' ' + data_atual.toLocaleTimeString());
            $('#nro_venda').text(venda_id);
            
            var count_item = 1;
            var count_valor_itens = 0.0;
            $('#itens_cupom > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                var valor_item = response[i].produto.preco * response[i].quantidade;
                count_valor_itens += valor_item;
                $('#itens_cupom').append('<tr id="' + response[i].id + '"><td>' + count_item + '</td><td idabela_descricao">' + response[i].produto.descricao + '</td><td class="icon-centralized">' + response[i].quantidade + 'x ' + valor_item.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }) + '</td><td>' + valor_item.toLocaleString("pt-BR", { style: "currency", currency: "BRL" }) + '</td></tr>');
                count_item++;
            }

            $('#vlr_total_cupom').text(count_valor_itens.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 }));
            $('#vlr_total_cupom2').text(count_valor_itens.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 }));
            $('#valor_recebido').text(vlr_pago_modal);
            $('#vlr_troco').text(troco_modal);
            $('#data_atual_cupom').text(data_atual.toLocaleDateString());
            $('#vlr_total_cupom').text(count_valor_itens.toLocaleString("pt-BR", { minimumFractionDigits: 2, maximumFractionDigits: 2 }));
            $('#tipo_pagamento').text(forma_pagamento_modal);
            
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro em listar item: " + xhr.responseText);
    });
}
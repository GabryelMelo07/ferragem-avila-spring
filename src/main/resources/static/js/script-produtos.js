// Função que lista todos produtos.
function listarProdutos() {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listartodos_produto",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            $('#tabelaProdutos > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                $('#tabelaProdutos > tbody').append('<tr id="' + response[i].id + '"><td id="tabela_id">' + response[i].id + '</td><td id="tabela_descricao">' + response[i].descricao + '</td><td id="tabela_valor">' + response[i].preco.toLocaleString("pt-BR",
                        {style: "currency", currency: "BRL"}) + '</td><td id="tabela_estoque">' + response[i].estoque + '</td><td id="tabela_cod_barras">' + response[i].cod_barras + '</td><td id="tabela_btn_editar" class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizaProduto" onclick="loadProduto(' + response[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarProduto(' + response[i].id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao listar produtos: " + xhr.responseText);
    });
}

// ====================== PESQUISAR PRODUTOS ====================== \\

// Função que pesquisa o produto por nome.
function pesquisarPorNome(descricao) {
    console.log("chegou aqui")
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_por_nome",
        data: "descricao=" + descricao,
        success: function (response) {
            $('#tabelaProdutos > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                $('#tabelaProdutos > tbody').append('<tr id="' + response[i].id + '"><td id="tabela_id">' + response[i].id + '</td><td id="tabela_descricao">' + response[i].descricao + '</td><td id="tabela_valor">' + response[i].preco.toLocaleString("pt-BR",
                    { style: "currency", currency: "BRL" }) + '</td><td id="tabela_estoque">' + response[i].estoque + '</td><td id="tabela_cod_barras">' + response[i].cod_barras + '</td><td id="tabela_btn_editar" class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizaProduto" onclick="loadProduto(' + response[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarProduto(' + response[i].id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar produto por nome: " + xhr.responseText);
    });
}

// Função que pesquisa o produto por id.
function pesquisarPorId(id) {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_produto",
        data: {idProduto: id},
        success: function (response) {
            $("#tabelaProdutos > tbody > tr").remove();
            $("#tabelaProdutos > tbody").append('<tr id="' + response.id + '"><td>' + response.id + '</td><td>' + response.descricao + '</td><td>' + response.preco.toLocaleString("pt-BR",
                { style: "currency", currency: "BRL" }) + '</td><td>' + response.estoque + '</td><td>' + response.cod_barras + '</td><td class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizaProduto" onclick="loadProduto(' + response.id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarProduto(' + response.id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
        }
    }).fail(function(xhr, status, errorThrown) {
        alert("Erro ao pesquisar produto por id: " + xhr.responseText);
    });
}

// Função que pesquisa o produto por código de barras.
function pesquisarPorCodBarras(cod_barras) {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_por_cod_barras",
        data: {cod_barras: cod_barras},
        success: function (response) {
            $('#tabelaProdutos > tbody > tr').remove();
            $("#tabelaProdutos > tbody").append('<tr id="' + response.id + '"><td>' + response.id + '</td><td>' + response.descricao + '</td><td>' + response.preco.toLocaleString("pt-BR",
                { style: "currency", currency: "BRL" }) + '</td><td>' + response.estoque + '</td><td>' + response.cod_barras + '</td><td class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizaProduto" onclick="loadProduto(' + response.id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarProduto(' + response.id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar produto por código de barras: " + xhr.responseText);
    });
}

// Função que pesquisa o produto pelo filtro escolhido (usa as 3 anteriores e pesquisa pelo primeiro input que contenha dados para realizar a pesquisa).
function pesquisarPorFiltros() {

    var id = $("#pesquisa_id").val();
    var nome = $("#pesquisa_nome").val();
    var cod_barras = $("#pesquisa_cod_barras").val();

    if (id != null && id.trim() != '') {
        pesquisarPorId(id);
    } else if (nome != null && nome.trim() != '') {
        pesquisarPorNome(nome);
    } else if (cod_barras != null && cod_barras.trim() != '') {
        pesquisarPorCodBarras(cod_barras);
    } else {
        listar_pagina(parseInt($("#pagina-atual").text()));
    }
}

// ================================================================= \\



// ====================== CADASTRO DE PRODUTOS ====================== \\

function salvarProduto() {
    var formData = new FormData();
    var descricao = $("#descricao").val();
    var preco = $("#valor").val();
    var estoque = $("#quantidade").val();
    var cod_barras = $("#cod_barras").val();

    try {
        var foto = document.getElementById('foto_produto').files[0];
        formData.append("descricao", descricao);
        formData.append("preco", preco);
        formData.append("estoque", estoque);
        formData.append("cod_barras", cod_barras);
        formData.append("foto", foto);
    } catch(e) {        
        formData.append("descricao", descricao);
        formData.append("preco", preco);
        formData.append("estoque", estoque);
        formData.append("cod_barras", cod_barras);
    }    

    if (descricao == null || descricao != null && descricao.trim() == '') {
        alert('Produtos sem descrição não podem ser cadastrados.');
        return;
    }

    $.ajax({
        method: "POST",
        url: "http://localhost:8081/ferragem-avila/salvar_produto",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            $("#id").val(response.id);
            document.getElementById('formCadastroProduto').reset();
            listar_pagina(parseInt($("#pagina-atual").text()));
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao cadastrar produto: " + xhr.responseText);
    });

}

// Função que salva o produto no Banco de dados após ser atualizado.
function atualizarProduto() {
    var formData = new FormData();
    var id = $("#id2").val();
    var descricao = $("#descricao2").val();
    var preco = $("#valor2").val();
    var estoque = $("#quantidade2").val();
    var cod_barras = $("#cod_barras2").val();

    if (descricao == null || descricao != null && descricao.trim() == '') {
        alert('Produtos sem descrição não podem ser cadastrados.');
        return;
    }

    try {
        var foto = document.getElementById('foto_produto2').files[0];
        // pra saber
        formData.append("id", id);
        formData.append("descricao", descricao);
        formData.append("preco", preco);
        formData.append("estoque", estoque);
        formData.append("cod_barras", cod_barras);
        formData.append("foto", foto);
    } catch (e) {
        // pra saber
        formData.append("id", id);
        formData.append("descricao", descricao);
        formData.append("preco", preco);
        formData.append("estoque", estoque);
        formData.append("cod_barras", cod_barras);
    } 

    $.ajax({
        method: "POST",
        url: "http://localhost:8081/ferragem-avila/atualizar_produto",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            document.getElementById('formCadastroProduto').reset();
            // listar_pagina(parseInt($("#pagina-atual").text()));
        },
        complete: function (r) {
            setTimeout(() => listar_pagina(1), 150);
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao atualizar produto: " + xhr.responseText);
    });
}

// Função que atualiza o produto.
function loadProduto(id) {
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/buscar_produto",
        data: "idProduto=" + id,
        success: function (response) {
            $("#id2").val(response.id);
            $("#descricao2").val(response.descricao);
            $("#valor2").val(response.preco);
            $("#quantidade2").val(response.estoque);
            $("#cod_barras2").val(response.cod_barras);
            $("#foto2").val(response.foto);
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao atualizar produto: " + xhr.responseText);
    });
}

// Função que deletar produto do Banco de dados.
function deletarProduto(id) {

    if (confirm('Deletar produto?')) {
        $.ajax({
            method: "DELETE",
            url: "http://localhost:8081/ferragem-avila/deletar_produto",
            data: "idProduto=" + id,
            success: function (response) {
                $('#' + id).remove();
                listar_pagina(parseInt($("#pagina-atual").text()));
            }
        }).fail(function (xhr, status, errorThrown) {
            alert("Erro ao deletar produto: " + xhr.responseText);
        });
    }
}

// =================================================================== \\

function listar_pagina(page) {
    $("#pagina-atual").text(page);
   
    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/listar_por_pagina",
        data: "page=" + page,
        success: function (response) {
            $('#tabelaProdutos > tbody > tr').remove();
            for (var i = 0; i < response.length; i++) {
                if (response[i].foto != undefined){
                    $('#tabelaProdutos > tbody').append('<tr id="' + response[i].id + '"><td id="tabela_id">' + response[i].id + '</td><td class="icon-centralized" id="tabela_foto"><img class="foto_produto_class" id="foto_produto_' + response[i].id +'" src="./../img/imagens_produtos/' + response[i].foto + '" alt="foto_produto"></td><td class="icon-centralized" id="tabela_descricao">' + response[i].descricao + '</td><td class="icon-centralized" id="tabela_valor">' + response[i].preco.toLocaleString("pt-BR",
                    { style: "currency", currency: "BRL" }) + '</td><td class="icon-centralized" id="tabela_estoque">' + response[i].estoque + '</td><td class="icon-centralized" id="tabela_cod_barras">' + response[i].cod_barras + '</td><td class="icon-centralized" id="tabela_btn_editar" class="icon-centralized"><button type="button" class="btn btn-warning" data-toggle="modal" data-target="#modalAtualizaProduto" onclick="loadProduto(' + response[i].id + ')"><i class="fa-solid fa-pen-to-square"></i></button></td><td class="icon-centralized" id="tabela_btn_deletar" class="icon-centralized"><button type="button" class="btn btn-danger" onclick="deletarProduto(' + response[i].id + ')"><i class="fa-solid fa-trash-can"></i></button></td></tr>');
                }
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao listar página de produtos: " + xhr.responseText);
    });
}

function next_page() {
    var pagina_atual = parseInt($("#pagina-atual").text());

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/total_paginas",
        success: function (response) {
            if (pagina_atual < response) {
                listar_pagina(pagina_atual + 1);
            } else {
                // fazer notificação via toast
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar o total de páginas" + xhr.responseText);
    });
}

function previous_page() {
    var pagina_atual = parseInt($("#pagina-atual").text());

    $.ajax({
        method: "GET",
        url: "http://localhost:8081/ferragem-avila/total_paginas",
        success: function (response) {
            if (pagina_atual > 1) {
                listar_pagina(pagina_atual - 1);
            } else {
                // fazer notificação via toast
            }
        }
    }).fail(function (xhr, status, errorThrown) {
        alert("Erro ao buscar o total de páginas" + xhr.responseText);
    });
}

function loadUserArea() {
    var userId = localStorage.getItem("userId");
    var username = localStorage.getItem("username");
    var userImage = localStorage.getItem("userImage");

    $("#userId").text("Id:" + userId);
    $("#username").text(username);

    const userActualImage = document.querySelector("#userImage");
    if (userImage.trim() == "") {
        userActualImage.setAttribute("src", "../img/imagens_perfil/sem_imagem_user.png");
    } else {
        userActualImage.setAttribute("src", "../img/imagens_perfil/" + userImage);
    }
}
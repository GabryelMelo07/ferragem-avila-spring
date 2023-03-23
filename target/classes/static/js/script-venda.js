function abrirVenda() {
	$.ajax({
    	method: "POST",
    	url: "http://localhost:8081/ferragem-avila/salvar_venda",
    	contentType: "application/json; charset=utf-8",
    	success: function(response) {
        	alert("Venda cadastrada com sucesso.");
    	}
		}).fail(function(xhr, status, errorThrown) {
    		alert("Erro ao cadastrar produto: " + xhr.responseText);
		});
}

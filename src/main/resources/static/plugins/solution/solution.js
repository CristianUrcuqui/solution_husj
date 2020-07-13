//variables
let nodes = document.querySelectorAll('.alert-success, .alert-danger')



//funciones, metodos, validaciones
if (nodes != null){
	setTimeout(() => {
		for(let i = 0, j = nodes.length; i < j; i++) {
		    nodes[i].remove()
		    //nodes[i].style.visibility = "hidden";		    
		}	
	}, 15000);
}


$(document).ready(function() {
	//animacion para los card principales
	$('.animacion').css('left', function(){ 
		return $(this).offset().left; 
	})
	.animate({"left":"0px"}, "slow");
	
	//habilita tooltips en datatables
	$('[data-toggle="tooltip"]').tooltip();
	$('a').tooltip('update');	
});


window.onload = function(){
	let contenedor = document.getElementById('contenedor_carga');
	if(contenedor != null){
		contenedor.style.visibility = 'hidden';
		contenedor.style.opacity = '0';
	}	
}


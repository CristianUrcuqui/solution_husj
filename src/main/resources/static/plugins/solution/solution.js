//variables
let nodes = document.querySelectorAll('.alert-success, .alert-danger')



//funciones, metodos, validaciones
if (nodes != null){
	setTimeout(() => {
		for(let i = 0, j = nodes.length; i < j; i++) {
		    nodes[i].remove()
		}	
	}, 7000);
}


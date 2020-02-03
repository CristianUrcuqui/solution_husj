//variables
let mensajesNotificacion = document.querySelector('.alert');




//funciones, metodos, validaciones
if (mensajesNotificacion != null){
	setTimeout(() => {
		mensajesNotificacion.remove()
	}, 7000);
}
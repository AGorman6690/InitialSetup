$(document).ready(function(){
	window.onclick = function(event) {
	    if ($(event.target).hasClass("mod") == 1) {
	       closeModal($(".mod"));
	    }
	}	
	
	$(".mod-header .glyphicon-remove").click(function(){
		closeModal($(".mod"));
	})
})

function closeModal($modal){
	$modal.hide();
	removeInvalidCss($modal);
}

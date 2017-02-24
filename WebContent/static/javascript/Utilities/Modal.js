$(document).ready(function(){
	window.onclick = function(event) {
	    if ($(event.target).hasClass("mod") == 1) {
	       closeModal($(".mod"));
	    }
	}	
	
	$(".mod-header .glyphicon-remove").click(function(){
		closeModal($(".mod"));
	})
	
	$("#jobInfoModal .mod-header .glyphicon-remove").click(function(){
		$(this).closest(".mod").hide();
	})
	
	$("[data-toggle-mod-id]").click(function(){
		var id = $(this).attr("data-toggle-mod-id");
		$("html").find("#" + id).eq(0).show();
		
	})
})

function closeModal($modal){
	$modal.hide();
	removeInvalidCss($modal);
}

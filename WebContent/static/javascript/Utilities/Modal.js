$(document).ready(function(){
	
	$("body").on("click", ".mod-body .cancel", function() {
		$(this).closest(".mod").hide();
	})
	
	window.onclick = function(event) {
	    if ($(event.target).hasClass("mod") == 1) {
	       closeModal($(".mod"));
	    }
	}	
	
	$(document).on("click", ".mod.simple-header .mod-header", function(){
//		closeModal($(".mod"));
		$(this).closest(".mod").hide();
	})
	
	$(document).on("click", ".mod-header .glyphicon-remove", function(){
//		closeModal($(".mod"));
		$(this).closest(".mod").hide();
	})
	
//	$("#jobInfoModal .mod-header .glyphicon-remove").click(function(){
//		$(this).closest(".mod").hide();
//	})
	
	$("[data-toggle-mod-id]").click(function(){
		var id = $(this).attr("data-toggle-mod-id");
		$("html").find("#" + id).eq(0).show();
		
	})
})

function closeModal($modal){
	$modal.hide();
//	removeInvalidCss($modal);
}

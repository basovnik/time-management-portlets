function facesMessagesHide() {
	setTimeout(function() {
	    $('.rf-msgs *').each(function() {
	    	$(this).fadeOut('slow', function(){
		        $(this).remove();
		    });
	    });
	}, 7000);
}


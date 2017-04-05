$( document ).ready(function() {

  $('.wrapper')[0].style.minHeight = window.innerHeight - 55 +"px";
  $('.container-fluid')[0].style.minHeight = window.innerHeight - 100 +"px";
  
  if($('.new-messages').length > 0) {
	  refreshUnreadAmount();
	  setInterval(refreshUnreadAmount, 30000);
  }
  
});

function getDomainPath(){
	var domainPath;
	var necessarySlash = 3;
	if(location.href.indexOf('Projectica') != -1) necessarySlash = 4
	
	var destination;
	var slashCounter = 0;
	for(var i = 0; i< location.href.length; i++){
		if(location.href[i]=='/') slashCounter++;
		if(slashCounter == necessarySlash){
			domainPath = location.href.substring(0, i);
			break;
		}
	}
	return domainPath;
}

function refreshUnreadAmount() {
	
	$.ajax({
		  url: getDomainPath() + "/dialogs",
		  type: 'GET',
		  success: function(amount){
			  console.log(amount);
			  if(amount > 0) {
				  $('.new-messages').text(amount);
			  } else {
				  $('.new-messages').text("")
			  }
		  }
		});
}

;(function($){
    $.fn.extend({
        donetyping: function(callback,timeout){
            timeout = timeout || 1e3; // 1 second default timeout
            var timeoutReference,
                doneTyping = function(el){
                    if (!timeoutReference) return;
                    timeoutReference = null;
                    callback.call(el);
                };
            return this.each(function(i,el){
                var $el = $(el);
                $el.is(':input') && $el.on('keyup keypress paste',function(e){
                	if (e.type=='keyup' && e.keyCode!=8) return;

                    if (timeoutReference) clearTimeout(timeoutReference);
                    timeoutReference = setTimeout(function(){
                        doneTyping(el);
                    }, timeout);
                }).on('blur',function(){
                    doneTyping(el);
                });
            });
        }
    });
})(jQuery);
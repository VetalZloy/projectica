$( document ).ready(function() {

  $('.wrapper')[0].style.minHeight = window.innerHeight - 55 +"px";
  $('.container-fluid')[0].style.minHeight = window.innerHeight - 100 +"px";
  
  if($('.new-messages').length > 0 && $.cookie('authorized') == 'true') {
	  refreshUnreadAmount();
	  setInterval(refreshUnreadAmount, 30000);
	  
	  setInterval(extendSession, 12 * 60 * 1000); // 12 minutes
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
	
	var req1 = $.ajax({
		url: getDomainPath() + "/messaging/dialogs/unread-messages",
		type: 'GET'
	});
	
	var req2 = $.ajax({
		url: getDomainPath() + "/messaging/chats/unread-messages",
		type: 'GET'
	});
	
	$.when.apply($, [req1, req2]).then(function(data1, data2){
		var amount1 = data1[0].unreadMessagesAmount;
		var amount2 = data2[0].unreadMessagesAmount;
		var amount = amount1 + amount2;
		if(amount > 0) {
			$('.new-messages').text(amount);
		} else {
			$('.new-messages').text("")
		}
	});
}

function extendSession() {
	
	$.ajax({
		  url: getDomainPath() + "/session",
		  type: 'PUT',
		  success: function(){
			  console.log('Session is extended');
		  }
		});
}

function openPanel(className) {
  $(className).show(500);
  
  $('.bg_layer').css("z-index", 10);
  $('.bg_layer').show();
  $('.bg_layer').animate({
    opacity: 0.8
  }, "slow");
}
  
$('.bg_layer').click(function(){
  $('.bg_layer').css("z-index", -1);
  $('.openable, .bg_layer').hide(500);
});

	function openResetPasswordPanel() {
	  $(".log-in").hide(500, function(){
	    $(".reset-password").show(500);
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
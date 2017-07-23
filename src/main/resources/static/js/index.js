var usernameRegExp = /([A-Za-z]|\d){5,}/;
var emailRegExp =  /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
var passwordRegExp = /([A-Za-z]|\d){5,}/;

$( document ).ready(function() {
	
  if(location.href.indexOf('login-error') != -1) {
    $(".log-in .panel-body p")[0].innerText = 'ERROR';
  }
  if(location.href.indexOf('login') != -1)
	  openPanel(".log-in");
  
  $(".sign-up button")[0].disabled = true;
  
  if($(".sign-up p").length > 0)
	  openPanel(".sign-up");	

  $(".sign-up input").donetyping(check);
  $(".sign-up input").focusout(check);
  
  $(".sign-up input[name='username']").donetyping(checkUsername);
  $(".sign-up input[name='username']").focusout(checkUsername);
  
  $(".sign-up input[name='email']").donetyping(checkEmail);
  $(".sign-up input[name='email']").focusout(checkEmail);
  
  $(".sign-up input[name='password']").donetyping(checkPassword);
  $(".sign-up input[name='password']").focusout(checkPassword);
  
  $(".sign-up input[name='passwordConf']").donetyping(checkConfirmation);
  $(".sign-up input[name='passwordConf']").focusout(checkConfirmation);
});

function checkUsername() {
  $(".username-error").remove();

  var username = $(".sign-up input[name='username']").val();
  if(username == ''){
	$(".sign-up input[name='username']:first").next()[0].className = "invalid";
	check();
	return;
  }
  
  if(usernameRegExp.test(username)) {
	$.ajax({
	  url: location.href + "/users/check/" + username,
	  contentType: 'text/plain',
	  type: 'GET',
	  success: function(result) {
		if(! result) 
		  $(".sign-up input[name='username']:first").next()[0].className = "valid";
		else {
		  $(".sign-up input[name='username']:first").next()[0].className = "invalid";
		  $(".sign-up button")[0].disabled = true;
		  $(".sign-up .panel-body:first").append("<p class='username-error'>Error: user with such username already exists</p>");
		}  
	  }
	});
  }
  else {
	$(".sign-up input[name='username']:first").next()[0].className = "invalid";
	$(".sign-up button")[0].disabled = true;
	$(".sign-up .panel-body").append("<p class='username-error'>Error: bad username format</p>");
	$(".sign-up .panel-body").append("<p class='username-error'>Format: At least 5 English letters and digits</p>");
  }
  check();
}


function checkEmail() {
  $(".email-error").remove();

  var email = $(".sign-up input[name='email']").val();
  
  if(email == ''){
	$(".sign-up input[name='email']:first").next()[0].className = "invalid";
	check();
	return;
  }
  
  if(emailRegExp.test(email)) {
	$.ajax({
	  url: location.href + "/users/check/" + email.replace('.', '&2E'),
	  data: $(".sign-up input[name='email']").val(),
	  contentType: 'text/plain',
	  type: 'GET',
	  success: function(result) {
		if(! result) 
		  $(".sign-up input[name='email']:first").next()[0].className = "valid";
		else {
		  $(".sign-up input[name='email']:first").next()[0].className = "invalid";
		  $(".sign-up button")[0].disabled = true;
		  $(".sign-up .panel-body").append("<p class='email-error'>Error: user with such email already exists</p>");
		}  
	  }
	});
  }    
  else {
	$(".sign-up input[name='email']:first").next()[0].className = "invalid";
	$(".sign-up button")[0].disabled = true;
	$(".sign-up .panel-body").append("<p class='email-error'>Error: bad email format</p>");
  }
  check();
}

function checkPassword() {
  $('.password-error').remove();

  if($(".sign-up input[name='password']").val() == ''){
	$(".sign-up input[name='password']:first").next()[0].className = "invalid";
	check();
	return;
  }
  
  if(passwordRegExp.test($(".sign-up input[name='password']")[0].value))
	$(".sign-up input[name='password']:first").next()[0].className = "valid";
  else {
	$(".sign-up input[name='password']:first").next()[0].className = "invalid";
	$(".sign-up button")[0].disabled = true;
	$(".sign-up .panel-body").append("<p class='password-error'>Error: bad password format</p>");
	$(".sign-up .panel-body").append("<p class='password-error'>Format: At least 5 English letters and digits</p>");
  }
  check();
}

function checkConfirmation() {
  $(".confirm-error").remove();

  if($(".sign-up input[name='passwordConf']").val() == ''){
	$(".sign-up input[name='passwordConf']:first").next()[0].className = "invalid";
	check();
	return;
  }  
  
  var password = $(".sign-up input[name='password']")[0].value;
  var passwordConf = $(".sign-up input[name='passwordConf']")[0].value;

  if(password == passwordConf && password != '')
    $(".sign-up input[name='passwordConf']:first").next()[0].className = "valid";
  else {
    $(".sign-up input[name='passwordConf']:first").next()[0].className = "invalid";
    $(".sign-up button")[0].disabled = true;

    $(".sign-up .panel-body").append("<p class='confirm-error'>Error: password and confirmation are not equal</p>");
  }
  check();
}

function check() {
  $(".sign-up button")[0].disabled = false;
	
  $(".sign-up input").each(function() {
    if($(this).next()[0].className == "invalid") 
      $(".sign-up button")[0].disabled = true;
  });
}

$(".wrapper>div>div").mouseenter(function(){
  $(this).stop().animate({ fontSize : '40px' });
});

$(".wrapper>div>div").mouseleave(function(){
  $(this).stop().animate({ fontSize : '50px' });
});

$(".about").mouseenter(function(){
  $(this).stop().animate({ padding : '18px' }, 'fast', 'linear');
});

$(".github").mouseenter(function(){
  $(this).stop().animate({ padding : '18px' }, 'fast', 'linear');
});

$(".about").mouseleave(function(){
  $(this).stop().animate({ padding : '20px' }, 'fast', 'linear');
});

$(".github").mouseleave(function(){
  $(this).stop().animate({ padding : '20px' }, 'fast', 'linear');
});

$('.projects').click(function(){window.location.href = '#';});
$('.vacancies').click(function(){window.location.href = '#';});

function openPanel(className) {
  $(className).show(500);
  
  $('.bg_layer').css("z-index", 5);
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

var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var earliestId = -1;
var firstPageLoaded = false;
var earliestMassageDate, latestMassageDate;
var webSocket;

window.onbeforeunload = function(){
  if(webSocket != null) webSocket.close();
}

$(document).ready(function() {
	
  $("textarea").donetyping(checkTextArea);	
  $("textarea").focusout(checkTextArea);	
  $('textarea').keydown(function (e) {
	  if (e.ctrlKey && e.keyCode == 13) {
		$('textarea').val(function(i, text) {
		  return text + '\n';
		});		
		return;
	  }
	  if (e.keyCode == 13) {
		  e.preventDefault();
		  send();
	  }
  });
  
  
  scrollBarSetting();
  webSocket = new WebSocket(location.href.replace("http", "ws"));  
  webSocket.onmessage = function(message){processMessage(message)};
});

function checkTextArea() {
	if($("textarea").val().trim() == '') {
		$(".send").hide();
	} else $(".send").show();
}

function send() {
  if($("textarea").val().trim() == '') return;
  
  webSocket.send($("textarea").val());
  $(".send").hide();
  $("textarea").val('');
}

function scrollBarSetting() {

	$(".participants-list").mCustomScrollbar();
	$(".participants-list").css({height: "254px"});
	
	$('.dialog').height($(".wrapper").height()-20);
	var generalHeight = $('.dialog').height();
	var interlocutorHeight = $('.name').height();
	var textareaHeight = $('textarea').height();
	var scrollPaneHeight = generalHeight - interlocutorHeight - textareaHeight - 35;
	

	
	$(".scrool-pane").mCustomScrollbar({
	  setHeight: scrollPaneHeight,
	  mouseWheel:{ scrollAmount: 200 },
	  callbacks: {
		  onTotalScroll: function(){
	    	firstPageLoaded = true;  
	      },
		  onTotalScrollBack: function(){
			if(earliestId == -1) return;
			var oldContentHeight=$(".scroll-pane .mCSB_container").innerHeight();
			loadPreviousPage(oldContentHeight);			
	    }
	  }
	});

	$(".mCSB_dragger_bar").css({backgroundColor: "#A9A9A4"});
}

function processMessage(message){
  var jsonData = JSON.parse(message.data);
  
  if(jsonData.messageType == 'usersMessage') {
	  $(".participants-list .mCSB_container").empty();
	  var users = jsonData.users;	  
	  $(".participants-title span").text(users.length);
	  
	  for(var i = 0; i < users.length; i++){
		  var user = users[i];
		  var userUrl = location.href.substring(0, location.href.indexOf('chatrooms'));
		  userUrl += "users/" + user.username;
		  var appendableBlock = '<div class="participant">'+
		  						  '<div class="avatar"><img src='+user.gravatarUrl+'></div>'+
		  						  '<a href="'+userUrl+'" class="username">'+user.username+'</a>'+
		  						'</div>';
		  
		  $(".participants-list .mCSB_container").append(appendableBlock);
	  }
  }
  if(jsonData.messageType == 'chatMessages') {
	  
	var messages = jsonData.messages;			
	for(var i = 0; i < messages.length; i++){
	  var message = messages[i];
	  var userUrl = location.href.substring(0, location.href.indexOf('chatrooms'));
	  userUrl += "users/" + message.senderUsername;
	  var date = new Date(Date.parse(message.date.replace('T', ' ')));
	  var hours = date.getHours();
	  var minutes = date.getMinutes();
	  if(minutes < 10) minutes = "0" + minutes;
	  
	  var dayLine = "";	  
	  if(latestMassageDate != null) {
		  if(latestMassageDate.getMonth() != date.getMonth() || latestMassageDate.getDay() != date.getDay()) {
			  var month = monthNames[date.getMonth()];
			  var day = date.getDate();
			  dayLine = "<div class='day-line'><span>"+month+" "+day+"</span></div>";
		  }		  
	  }
	  latestMassageDate = date;
	  
	  var text = message.text.replace(/</g, "&lt;")
							 .replace(/>/g, "&gt;")
							 .replace(/\n/g, "<br>");
	  
	  var appendableBlock = dayLine + 
	  						'<div class="message">'+
	  						 '<div class="avatar"><img src='+message.gravatarUrl+'></div>'+
	  						 '<div class="right-side">'+
	  						   '<a href='+userUrl+' class="username">'+message.senderUsername+'</a>'+
	  						   '<span class="date">'+hours+':'+minutes+'</span>'+
	  						   '<div class="text"><p>'+text+'</p></div>'+
	  						 '</div>'+
	  					    '</div>';
	  $('.scroll-pane .mCSB_container').append(appendableBlock);
	}
	
	$(".scroll-pane").mCustomScrollbar("scrollTo","bottom", {scrollInertia: 0, timeout: 0});
	
	if(!firstPageLoaded) {
    	earliestId = jsonData.earliestId;      	

		var container = $('.scroll-pane .mCSB_container');
		var scrollPane = $('.scroll-pane');
		if(container.height() < scrollPane.height()) {
			$('.cssload-container').remove();
		}			
    }
  }
}

function loadPreviousPage(oldContentHeight) {
	$.ajax({
	  url: location.href + "/?earliestId=" + earliestId,
	  type: 'GET',
	  success: function(jsonData){
	    if(jsonData.messageType == 'chatMessages') {
		  earliestId = jsonData.earliestId;
		  var messages = jsonData.messages;
		  
		  var blocksToPrepend = "";
		  
		  for(var i = 0; i < messages.length; i++){
		    var message = messages[i];
		    var userUrl = location.href.substring(0, location.href.indexOf('chatrooms'));
		    userUrl += "users/" + message.senderUsername;
		    
		    var date = new Date(Date.parse(message.date));
			var hours = date.getHours();
			var minutes = date.getMinutes();
			if(minutes < 10) minutes = "0" + minutes;
		    
			var dayLine = "";	  
			if(earliestMassageDate != null) {
			  if(earliestMassageDate.getMonth() != date.getMonth() || earliestMassageDate.getDay() != date.getDay()) {
			    var month = monthNames[date.getMonth()];
				  var day = date.getDate();
				  dayLine = "<div class='day-line'><span>"+month+" "+day+"</span></div>";
				}		  
			}
			earliestMassageDate = date;
			
			var text = message.text.replace(/</g, "&lt;")
	  			 				   .replace(/>/g, "&gt;")
	  			 				   .replace(/\n/g, "<br>");
			
		    var prependableBlock = dayLine + 
		    					   '<div class="message">'+
				  				    '<div class="avatar"><img src='+message.gravatarUrl+'></div>'+
				  				    '<div class="right-side">'+
				  					  '<a href='+userUrl+' class="username">'+message.senderUsername+'</a>'+
				  					  '<span class="date">'+hours+':'+minutes+'</span>'+
				  					  '<div class="text"><p>'+text+'</p></div>'+
				  				    '</div>'+
				  				  '</div>';		    
		    blocksToPrepend += prependableBlock;
		  }
		  
		  $('.cssload-container').after(blocksToPrepend);
		  
		  var heightDiff=$(".scroll-pane .mCSB_container").innerHeight()-oldContentHeight;
		  $(".scroll-pane").mCustomScrollbar("scrollTo","-="+heightDiff,{scrollInertia:0,timeout:0});
		  

		  if(earliestId == -1) $('.cssload-container').remove();
	    }
	  }
	});
}
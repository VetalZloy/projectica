var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var earliestMassageDate, latestMassageDate;
var socket;
var users = [];
var lastMessagesLoaded = false;

$(document).ready(function() {
  
  $('.invisible .user .username').each(function(index){
	  var username = $(this).text();
	  var gravatarUrl = $(this).next().attr('src');
	  var id = $(this).prev().text();
	  
	  users.push({
		  id: id,
		  username: username, 
		  gravatarUrl: gravatarUrl
	  });
  });
	
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
  
  var lastSlash = location.href.lastIndexOf('/')
  var interlocutorUsername = location.href.substring(lastSlash + 1)
  var id = users.filter(u => u.username == interlocutorUsername)[0].id
  socket = io('/dialog', {query: 'interlocutorId='+id});
  
  socket.on('connect', function(){
  });

  socket.on('messages', function(data){
	  if(!lastMessagesLoaded) {
		  lastMessagesLoaded = true;
		  refreshUnreadAmount();
	  }
	  var messagesBlock = "";
	  if(data.length == 0) 
		  $('.cssload-container').remove();
	  
	  while(data.length > 0) {
		  messagesBlock += createMessageBlock(data.pop());    	
	  }
	  $('.scroll-pane .mCSB_container').append(messagesBlock);
	  $(".scroll-pane").mCustomScrollbar("scrollTo","bottom", {scrollInertia: 0, timeout: 0});
  });
  
  socket.on('previous', function(data){
	  var messagesBlock = "";
	  if(data.length == 0) 
		  $('.cssload-container').remove();
	  
	  while(data.length > 0) {
		  messagesBlock += createMessageBlock(data.pop());    	
	  }
	  
	  var oldContentHeight=$(".scroll-pane .mCSB_container").innerHeight();
	  $('.scroll-pane .cssload-container').after(messagesBlock);
	  var heightDiff=$(".scroll-pane .mCSB_container").innerHeight()-oldContentHeight;
	  $(".scroll-pane").mCustomScrollbar("scrollTo","-="+heightDiff,{scrollInertia:0,timeout:0});

  })

  socket.on('error', function(data){
    alert(data);
  });
  
});

function checkTextArea() {
	if($("textarea").val().trim() == '') {
		$(".send").hide();
	} else $(".send").show();
}

function send() {	
	if($("textarea").val().trim() == '') return;
	
	socket.emit('create_message', {text: $("textarea").val()})
	
	$(".send").hide();
	$("textarea").val('');
}

function scrollBarSetting() {
	$('.dialog').height($(".wrapper").height()-20);
	var generalHeight = $('.dialog').height();
	var interlocutorHeight = $('.interlocutor').height();
	var textareaHeight = $('textarea').height();
	var scrollPaneHeight = generalHeight - interlocutorHeight - textareaHeight - 15;
	
	$(".scrool-pane").mCustomScrollbar({
	  setHeight: scrollPaneHeight,
	  mouseWheel:{ scrollAmount: 200 },
	  callbacks: {
		  onTotalScroll: function(){
	    	firstPageLoaded = true;  
	      },
		  onTotalScrollBack: function(){
			if($('.cssload-container').length == 0) // all messages had been already extracted
				return;
			
			var earliestId = $('.message:first .messageId').text();		
			socket.emit('get_previous', {earliestId: earliestId});				
	    }
	  }
	});

	$(".mCSB_dragger_bar").css({backgroundColor: "#A9A9A4"});
}

function createMessageBlock(message){
	var userUrl = location.href.substring(0, location.href.indexOf('dialogs'));
	var senderUsername = users.filter(u => u.id == message.senderId)[0].username
	userUrl += "users/" + senderUsername;
	
	var senderGravatar = users.filter(u => u.id == message.senderId)[0].gravatarUrl
	
	var date = new Date(Date.parse(message.date));
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
	  
	var messageBlock = dayLine + 
						'<div class="message">'+
							'<span class="messageId">'+message._id+'</span>'+
							'<div class="avatar"><img src='+senderGravatar+'></div>'+
								'<div class="right-side">'+
								'<a href='+userUrl+' class="username">'+senderUsername+'</a>'+
								'<span class="date">'+hours+':'+minutes+'</span>'+
								'<div class="text">'+text+'</div>'+
	  						'</div>'+
	  					'</div>';
	
	return messageBlock;
}

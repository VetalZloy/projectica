var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
var earliestMassageDate, latestMassageDate;
var socket;
var preloadedUsers = [];
var lastMessagesLoaded = false;

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
  
  var lastSlash = location.href.lastIndexOf('/')
  var chatId = location.href.substring(lastSlash + 1)
  socket = io('/chat', {query: 'chatId='+chatId});
  
  socket.on('connect', function(){
  });

  socket.on('messages', function(messages){
	  if(!lastMessagesLoaded) {
		  lastMessagesLoaded = true;
		  refreshUnreadAmount();
	  }
	  var messagesBlock = "";
	  if(messages.length == 0) 
		  $('.cssload-container').remove();
	  
	  loadUsersData(messages)
	  .then(function(messages){
		  while(messages.length > 0) {
			  messagesBlock += createMessageBlock(messages.pop());    	
		  }
		  $('.scroll-pane .mCSB_container').append(messagesBlock);
		  $(".scroll-pane").mCustomScrollbar("scrollTo","bottom", {scrollInertia: 0, timeout: 0});
	  });
  });
  
  socket.on('previous', function(messages){
	  var messagesBlock = "";
	  if(messages.length == 0) 
		  $('.cssload-container').remove();
	  
	  loadUsersData(messages)
	  .then(function(messages){
		  while(messages.length > 0) {
			  messagesBlock += createMessageBlock(messages.pop());    	
		  }
		  
		  var oldContentHeight=$(".scroll-pane .mCSB_container").innerHeight();
		  $('.scroll-pane .cssload-container').after(messagesBlock);
		  var heightDiff=$(".scroll-pane .mCSB_container").innerHeight()-oldContentHeight;
		  $(".scroll-pane").mCustomScrollbar("scrollTo","-="+heightDiff,{scrollInertia:0,timeout:0});
	  });

  })
  
  socket.on('users', function(onlineUsers){
	  var requests = []
	  onlineUsers.forEach(function(userId){
		  if (preloadedUsers.filter(u => u.userId == userId).length == 0) {		  
			  var req = $.ajax({
				  url: getDomainPath() + '/users/' + userId,
				  type: 'GET',
				  success: function(data){
					  preloadedUsers.push({
						 userId: userId,
						 username: data.username,
						 gravatarUrl: data.gravatarUrl
					  });
				  }
			  });
			  requests.push(req)
		  }	  
	  });
	  $.when.apply($, requests).then(function(){
		  $(".participants-list .mCSB_container").empty();
		  $(".participants-title span").text(onlineUsers.length);
		  
		  for(var userId of onlineUsers){
			  var user = preloadedUsers.filter(u => u.userId == userId)[0];
			  var userUrl = location.href.substring(0, location.href.indexOf('chatrooms'));
			  userUrl += "users/" + user.username;
			  var appendableBlock = '<div class="participant">'+
			  						  '<div class="avatar"><img src='+user.gravatarUrl+'></div>'+
			  						  '<a href="'+userUrl+'" class="username">'+user.username+'</a>'+
			  						'</div>';
			  
			  $(".participants-list .mCSB_container").append(appendableBlock);
		  }
	  });

	  
  });
  
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
	var userUrl = location.href.substring(0, location.href.indexOf('chatrooms'));
	userUrl += "users/" + message.sender.username;
	
	var senderGravatar = message.sender.gravatarUrl
	
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
								'<a href='+userUrl+' class="username">'+message.sender.username+'</a>'+
								'<span class="date">'+hours+':'+minutes+'</span>'+
								'<div class="text">'+text+'</div>'+
	  						'</div>'+
	  					'</div>';
	
	return messageBlock;
}

function loadUsersData (messages) {
	return new Promise(function(resolve, reject){
		var requests = []
		var senderIds = messages.map(mes => mes.senderId)
		var idSet = new Set(senderIds)
		idSet.forEach(function(id){
			if (preloadedUsers.filter(u => u.userId == id).length == 0) {
				var req = $.ajax({
					url: getDomainPath() + '/users/' + id,
					type: 'GET',
					success: function(data){
						preloadedUsers.push({
							userId: id,
							username: data.username,
							gravatarUrl: data.gravatarUrl
						});
					}
				});
				requests.push(req)
			}
		});
		 
		$.when.apply($, requests).then(function(){
			messages.forEach(function(mes){
				mes.sender = preloadedUsers.filter(u => u.userId == mes.senderId)[0]
			});
			resolve(messages);
		});
	});
}

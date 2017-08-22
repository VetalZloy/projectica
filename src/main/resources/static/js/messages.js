var messages = []

$(document).ready(function(){	
	var req1 = $.ajax({
		url: getDomainPath() + "/messaging/dialogs",
		type: 'GET',
		success: function(data){
			messages = messages.concat(data.interlocutors);
		}
	});
	var req2 = $.ajax({
		url: getDomainPath() + "/messaging/chats",
		type: 'GET',
		success: function(data){
			messages = messages.concat(data.chats);
		}
	});
	$.when.apply($, [req1, req2]).then(function(){
		messages.forEach(m => m.date = moment(m.date));
		messages.sort((m1, m2) => m1.date < m2.date);
		
		var requests = []
		messages.forEach(function(mes){
			var req;
			if(mes.hasOwnProperty('chatId')) {
				req = $.ajax({
					url: getDomainPath() + "/chatrooms/" + mes.chatId,
					type: 'GET',
					success: function(data) {
						mes.chat = data;
					}
				});
			} else {
				req = $.ajax({
					url: getDomainPath() + "/users/" + mes.interlocutorId,
					type: 'GET',
					success: function(data) {
						mes.interlocutor = data;
					}
				});
			}
			requests.push(req);
		})
		$.when.apply($, requests).then(function(){
			var appendableBlocks = "";
			messages.forEach(function(mes){
				if(mes.hasOwnProperty('chatId'))
					appendableBlocks += createChatBlock(mes);
				else 
					appendableBlocks += createDialogBlock(mes);
			});
			$('.messages').append(appendableBlocks);
			
			$(".message").click(function(){
				location.href = $(this).find("a").attr("href");
			})
		});
	});
})

function createChatBlock(mes) {
	var chatUrl = getDomainPath() + '/chatrooms/' + mes.chatId;
	var fullChatName = mes.chat.projectName + ': ' + mes.chat.name;
	var unread = '';
	var text = mes.text;
	if(mes.text.length > 15)
		text = mes.text.substring(0, 15) + '...';
	if(mes.read == false)
		unread = 'unread-by-user';
	
	var block = '<div class="message chat">'+
					'<div class="letter">' + mes.chat.name[0] + '</div>'+
        			'<a href="'+chatUrl+'">' + fullChatName + '</a>'+
        			'<p class="last-message ' + unread + '">' + text + '</p>'+
        		'</div>';
	return block;
}

function createDialogBlock(mes) {
	var interlocutorUrl = getDomainPath() + '/dialogs/' + mes.interlocutor.username;
	var unread = '';
	var text = mes.text;
	if(mes.text.length > 15)
		text = mes.text.substring(0, 15) + '...';
	if(mes.read == false)
		if(mes.senderId == mes.interlocutorId)
			unread = 'unread-by-user';
		else
			unread = 'unread-by-interlocutor';
	
	var block = '<div class="message">'+
					'<img src="'+mes.interlocutor.gravatarUrl+'" />'+
        			'<a href="'+interlocutorUrl+'">' + mes.interlocutor.username + '</a>'+
        			'<p class="last-message ' + unread + '">' + text + '</p>'+
        		'</div>';
	return block;
}
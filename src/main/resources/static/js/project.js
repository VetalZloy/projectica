function init(){
  $('.user p>span').click(function() {
    if(this.className == "cover"){
      this.className = "reveal";
      $(this).parent().next()[0].style.display = 'none';
    } else {
      this.className = "cover";
      $(this).parent().next()[0].style.display = 'block';
    }
  });

  $('.added-tags .close').click(function() {
    var tagName = $(this).parent()[0].innerText;
    $(this).parent().remove();
  });

}

$( document ).ready(function() {
  init();
  $("input[name='tagName']:first").donetyping(getSimilarTags);
});

function addTagWrapper(){
  var newTag = $("input[name='tagName']")[0].value;
  addTag(newTag);
}

function addTag(newTag){
  if(newTag == '') {
    alert('FAIL');
    return;
  }
  
  $("input[name='tagName']")[0].value = "";
  $(".similarTag").each(function(){this.remove()});

  $('.added-tags').append('<div>' + newTag + '<span class="close"></span></div>');
  init();

  return;
}

function getSimilarTags(){  
  var tag = $("input[name='tagName']")[0].value;
  $('.similar-tags p').remove();
  
  $.ajax({
    url: getDomainPath() + '/tag/' + tag,
	contentType: 'text/plain',
	type: 'GET',
	success: function(result) {
	  for(var i = 0; i < result.length; i++)
		$('.similar-tags').append('<p class="similarTag" onclick="addTag(this.innerText)">' + result[i] + '</p>');
	}
  });
}

function closeAddChatroomPanel() {
  $('.add-chatroom:first').fadeTo("slow", 0, "linear", function () {
    $('.add-chatroom')[0].style.display = 'none'}
  );
  $('.container-fluid:first').animate({opacity: "1"}, "slow", "linear");
}

function openAddChatroomPanel() {
  $('.add-chatroom')[0].style.display = 'block';
  $('.add-chatroom')[0].style.opacity = 0;
  $('.container-fluid')[0].style.opacity = 1;

  $('.container-fluid:first').fadeTo("slow", 0.1, "linear");
  $('.add-chatroom:first').animate({opacity: "1"}, "slow", "linear");
}

function closeCreatePositionPanel() {
  $('.create-position:first').fadeTo("slow", 0, "linear", function () {
    $('.create-position')[0].style.display = 'none'}
  );
  $('.container-fluid:first').animate({opacity: "1"}, "slow", "linear");
}

function openCreatePositionPanel() {
  $('.create-position')[0].style.display = 'block';
  $('.create-position')[0].style.opacity = 0;
  $('.container-fluid')[0].style.opacity = 1;

  $('.container-fluid:first').fadeTo("slow", 0.1, "linear");
  $('.create-position:first').animate({opacity: "1"}, "slow", "linear");
}

function openEditPanel() {
	  $('.edit-panel')[0].style.display = 'block';
	  $('.edit-panel')[0].style.opacity = 0;
	  $('.container-fluid')[0].style.opacity = 1;

	  $('.container-fluid:first').fadeTo("slow", 0.1, "linear");
	  $('.edit-panel:first').animate({opacity: "1"}, "slow", "linear");
}

function closeEditPanel() {
	  $('.edit-panel:first').fadeTo("slow", 0, "linear", function () {
	    $('.edit-panel')[0].style.display = 'none'}
	  );
	  $('.container-fluid:first').animate({opacity: "1"}, "slow", "linear");
}

function edit() {
	  $(".edit .error").text("");
	  var name = $(".edit-panel input[name='name']").val();
	  var description = $(".edit-panel textarea").val();
	  
	  if(name.trim() == '')  
		  $(".edit .error").text("Bad position name format");
	  
	  var body = {};
	  body.name = name;
	  body.description = description;
	  
	  console.log(body);
	  
	  $.ajax({
		url: location.href,
		contentType: 'application/json',
		data: JSON.stringify(body),
		type: 'PUT',
		success: function(result) {
		  location.reload();
		}
	  });
}


function createPosition(){
	var name = $(".create-position input[name='positionName']").val();
	var description = $(".create-position textarea").val();
	
	if(name == '' || description == '') {
		alert('FAIL');
		return;
	}
	
	var tags = [];
	$('.added-tags>div').each(function(){
	  tags[tags.length] = $(this).text();
	});
	
	var temp = location.href.split('/');
	var projectId = parseInt(temp[temp.length-1]);

	var requestBody = {};
	requestBody["projectId"] = projectId;
	requestBody["name"] = name;
	requestBody["description"] = description;
	requestBody["tags"] = tags;
	
	$.ajax({
	  url: getDomainPath() + '/positions',
	  data: JSON.stringify(requestBody),
	  contentType: 'application/json',
	  type: 'POST',
	  success: function(result) {
	    location.href = getDomainPath() + '/positions/' + result;
	  },
	  error: function(textStatus) {
	    alert(textStatus);
	  }
	});
}

function addChatroom() {
  var newChatroom = $("input[name='chatroomName']")[0].value;
  $("input[name='chatroomName']")[0].value = '';
  if(newChatroom == '') alert("FAIL");
  
  var alreadyExists = false;
  $('.chatrooms a').each(function(){
	  if($(this).text() == newChatroom) alreadyExists = true;
  });
  if(alreadyExists) {
	  alert("Already exists");
	  return;
  }
  
  var temp = location.href.split('/');
  var projectId = parseInt(temp[temp.length-1]);
  
  var requestBody = {};
  requestBody["projectId"] = projectId;
  requestBody["name"]  = newChatroom;
  
  $.ajax({
	url: getDomainPath() + '/chatrooms',
	data: JSON.stringify(requestBody),
	contentType: 'application/json',
	type: 'POST',
	success: function(result) {
	  location.reload();
	},
	error: function(textStatus) {
		alert(textStatus);
	}
  });
}

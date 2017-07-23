function init() {
  $('.tags .close').click(function() {
    var tagName = $(this).parent()[0].innerText;
    var tagBlock = $(this).parent();
    
    var temp = location.href.split('/');
    var positionId = parseInt(temp[temp.length-1]);
    
    $.ajax({
	  url: getDomainPath() + '/tag/' + positionId + '/' + tagName,
	  contentType: 'text/plain',
	  type: 'DELETE',
	  success: function(result) {
	    tagBlock.remove();
	  }
	});
  });
}

$(document).ready(function() {
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
  //ANYWAY
  $("input[name='tagName']")[0].value = "";
  $(".similar-tags p").each(function(){this.remove()});

  var temp = location.href.split('/');
  var positionId = parseInt(temp[temp.length-1]);
  
  // adding tag
  $.ajax({
    url: getDomainPath() + '/tag/' + positionId,
	data: newTag,
	contentType: 'text/plain',
	type: 'PUT',
	success: function(result) {
	  $('.tags').append('<div>' + newTag + '<span class="close"></span></div>');
	  init();
	},
	error: function() {
	  alert('FAIL');
	}
  });

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

function createRequest() {
  var userAdditions = $('.request textarea')[0].value;
  if(userAdditions == '') {
    alert('FAIL');
    return;
  }
  
  $.ajax({
	url: location.href + '/request',
	data: userAdditions,
	contentType: 'text/plain',
	type: 'POST',
	success: function(result) {
	  location.reload();
	}
  }); 
}

function closePosition(estimation) {
  var comment = $('.close-position textarea')[0].value;
  if(comment == '') {
    alert('FAIL');
    return;
  }

  var requestBody = {};
  requestBody["estimation"] = estimation;
  requestBody["comment"] = comment;

  $.ajax({
    url: location.href,
  	data: JSON.stringify(requestBody),
  	contentType: 'application/json',
  	type: 'DELETE',
  	success: function(result) {
  	  location.reload();
  	}
  });  
}

function join(username) {
  $.ajax({
	url: location.href + "/" + username,
	contentType: 'text/plain',
	type: 'PUT',
	success: function(result) {
	  location.reload();
	}
  });
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

function openAddTagPanel() {
  $('.tag-add-panel')[0].style.display = 'block';
  $('.tag-add-panel')[0].style.opacity = 0;
  $('.container-fluid')[0].style.opacity = 1;

  $('.container-fluid:first').fadeTo("slow", 0.1, "linear");
  $('.tag-add-panel:first').animate({opacity: "1"}, "slow", "linear");
}

function closeTagAddPanel() {
  $('.tag-add-panel:first').fadeTo("slow", 0, "linear", function () {
    $('.tag-add-panel')[0].style.display = 'none'}
  );
  $('.container-fluid:first').animate({opacity: "1"}, "slow", "linear");
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

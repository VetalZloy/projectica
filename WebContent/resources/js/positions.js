var loading = false;
var page = 1;

function init() {
  $('.added-tags .close').click(function() {
    var tagName = $(this).parent()[0].innerText;
    $(this).parent().remove();
  });
}

window.onscroll = function() {

  if (window.pageYOffset > 55) {
      $('.search')[0].style.paddingTop  = window.pageYOffset - 55 + 'px';
  }

  if($(document).height() - window.pageYOffset < 2*$(window).height() && !loading && page != -1){
    loading = true;
    $.ajax({
      url: location.href + "?page=" + page,
      async: false,
      contentType: 'text/plain',
      type: 'GET',
      success: function(result) {
    	if(result.length == 0) {
    	  page = -1;
    	  return;
    	}
    	page++;
    	for(var i = 0; i < result.length; i++){
    	  var positionUrl = "'" + location.href + "/" + result[i].id + "'";
    	  var projectUrl = "'" + location.href.replace("positions", "projects") + "/" + result[i].projectId + "'";
    	  $('.vacancies').append(
	    	    '<div class="vacancy">' +
	    		  '<h2><a href='+positionUrl+'>' + result[i].name + '</a></h2>' +
	    		  '<a class="project-link" href='+projectUrl+'>' + result[i].projectName + '</a>' +
	    		  '<p>' + result[i].description + '</p>'+
	    		'</div>');
    	}
      }
    });
    loading = false;
  }
}

$(document).ready(function() {
  init();
  $("input[name='tagName']").donetyping(getSimilarTags);
});

function search() {
  
  var positionName = $('input[name="search"]')[0].value;
  var tags = [];
  var addedTags = $('.added-tags div');
  for(var i = 0; i < addedTags.length; i++)
    tags[i] = addedTags[i].innerText;
  
  var body = {};
  body.namePattern = positionName;
  body.tags = tags;
  
  page = -1;
  
  $.ajax({
      url: location.href,
      data: body,
      contentType: 'application/json',
      type: 'GET',
      success: function(result) {
    	console.log(true);
    	$(".vacancy").remove();
    	for(var i = 0; i < result.length; i++){
    	  var positionUrl = "'" + location.href + "/" + result[i].id + "'";
    	  var projectUrl = "'" + location.href.replace("positions", "projects") + "/" + result[i].projectId + "'";
    	  $('.vacancies').append(
	    	    '<div class="vacancy">' +
	    		  '<h2><a href='+positionUrl+'>' + result[i].name + '</a></h2>' +
	    		  '<a class="project-link" href='+projectUrl+'>' + result[i].projectName + '</a>' +
	    		  '<p>' + result[i].description + '</p>'+
	    		'</div>');
    	}
      }
    });
}

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

  // adding tag




  //ajax wrong
  /*alert('FAIL');
  return false;*/

  //ajax successful
  $('.added-tags').append('<div>' + newTag + '<span class="close"></span></div>');
  init();

  return;
}

function getSimilarTags(){

  var tag = $("input[name='tagName']")[0].value;
  $(".similar-tags").empty();
  
  if(tag == '') return;
  
  
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

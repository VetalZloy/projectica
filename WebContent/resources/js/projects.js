var loading = false;
var positionValid = false;
var projectNameValid = false;
var page = 1;

window.onscroll = function() {

  if (window.pageYOffset > 55) {
      $('.search')[0].style.paddingTop  = window.pageYOffset - 55 + 'px';
      $('.search-button')[0].style.top  = window.pageYOffset - 10  + 'px';
  }

  if($(document).height() - window.pageYOffset < 2*$(window).height() && !loading && page != -1){
	console.log(page);
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
  		  var url = location.href + result[i].id;
  		  $('.projects').append(
  			'<div class="project">'+
  			   '<h2><a href="' + url + '">'+ result[i].name + '</a></h2>'+
  			   '<h3>'+ result[i].vacanciesAmount + ' vacancies</h3>'+
  			   '<p>'+ result[i].description + '</p>'+
  			'</div>'
  		  );
  		}
  	  }
  	});
    
    loading = false;
  }
}

$(document).ready(function() {
  $('.create-panel .close').click(closeCreatePanel);

  $('.create-panel input[name="name"]').donetyping(checkProjectName);
  $('.create-panel input[name="name"]').focusout(checkProjectName);

  $('.create-panel input[name="position"]').donetyping(checkPosition);
  $('.create-panel input[name="position"]').focusout(checkPosition);
});

function checkPosition() {

  var positionRegExp = /([A-Za-z]|\d|\s|\.){3,}/;
  var position = $('.create-panel input[name="position"]').val();

  $(".create-panel .status").text("");

  if(position == '') {
    positionValid = false;
    return;
  } else if(! positionRegExp.test(position)) {
    positionValid = false;
    $(".create-panel .status").text("Error: position can contain only English letters, digits, spaces and dots. At lest 3 symbols.");
  } else positionValid = true;

  check();
}

function checkProjectName() {
  var projectNameRegExp = /([A-Za-z]|\d|\s|\.){3,}/;
  var projectName = $('.create-panel input[name="name"]').val();

  $(".create-panel .status").text("");

  if(projectName == '') {
    projectNameValid = false;
    return;
  } else if(! projectNameRegExp.test(projectName)) {
    projectNameValid = false;
    $(".create-panel .status").text("Error: project name can contain only English letters, digits, spaces and dots. At lest 3 symbols.");
  } else {
	  $.ajax({
	  	  url: location.href + "/check/" + projectName,
	  	  contentType: 'text/plain',
	  	  type: 'GET',
	  	  success: function(result) {
	  		console.log(result);
	  		if(result) {
	  			$(".create-panel .status").text("Error: project with such name already exists.");
	  			projectNameValid = false;
	  		} 
	  		else projectNameValid = true;
	  	  }
	  	});
  }

  check();
}

function check() {
  if(projectNameValid && positionValid)
    $(".create-panel button").prop("disabled", false);
  else
    $(".create-panel button").prop("disabled", true);
}

function search() {
  var pattern = $('input[name="search"]')[0].value;

  if(pattern == ''){
    alert('FAIL');
    return;
  }
  
  page = -1;
  
  $.ajax({
  	url: location.href + "/" + pattern,
  	contentType: 'text/plain',
  	type: 'GET',
  	success: function(result) {
  	  $('.projects').empty();
  	  for(var i = 0; i < result.length; i++){
  		var url = location.href + "/" + result[i].id;
  		$('.projects').append(
  			'<div class="project">'+
  			   '<h2><a href="' + url + '">'+ result[i].name + '</a></h2>'+
  			   '<h3>'+ result[i].vacanciesAmount + ' vacancies</h3>'+
  			   '<p>'+ result[i].description + '</p>'+
  			'</div>'
  		);
  	  }
  	}
  });
}

function openCreatePanel() {
  $('.create-panel')[0].style.display = 'block';
  $('.create-panel')[0].style.opacity = 0;
  $('.container-fluid')[0].style.opacity = 1;

  $('.container-fluid').fadeTo("slow", 0.1, "linear");
  $('.create-panel').animate({opacity: "1"}, "slow", "linear");
}

function closeCreatePanel() {
  $('.create-panel').fadeTo("slow", 0, "linear", function () {
    $('.create-panel')[0].style.display = 'none'}
  );
  $('.container-fluid').animate({opacity: "1"}, "slow", "linear");
}

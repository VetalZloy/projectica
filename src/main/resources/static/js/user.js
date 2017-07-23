var urlRegExp = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)/;
var nameRegExp = /\w{2,}-{0,}\w{0,}/;

function init(){
  $('.tags .close').click(function() {
    var tagName = $(this).parent()[0].innerText;    
    var tag = $(this).parent();
	console.log(tagName);
    //deleting tag via ajax
    $.ajax({
  	    url: getDomainPath() + '/tag/'+tagName,
  	    contentType: 'text/plain',
  	    type: 'DELETE',
  	    success: function(result) {
  	    	tag.remove();
  	    },
  	    error: function(textStatus) {
  	      alert(textStatus);
  	      return false;
  	    }
    });

  });
}

$( document ).ready(function() {
    init();
    
    $("input[name='cv-link']:first").donetyping(checkURL);
    $("input[name='name']:first").donetyping(checkName);
    $("input[name='surname']:first").donetyping(checkSurname);
    $("input[name='tagName']:first").donetyping(getSimilarTags);
    
});

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

function update() {
  if(!checkURL() || !checkName() || !checkSurname()) {
    alert('FAIL');
    return;
  }
  
  var name= $("input[name='name']").val();
  var surname = $("input[name='surname']").val();
  var cvLink = $("input[name='cv-link']").val();
  
  var body = {};
  body.name = name;
  body.surname = surname;
  body.cvLink = cvLink;
  
  
  $.ajax({
	    url: location.href,
		data: JSON.stringify(body),
		contentType: 'application/json',
		type: 'PUT',
		success: function(result) {
		  location.reload();
		},
		error: function(textStatus) {
		  location.reload();
		}
	  });  
}

function addTagWrapper(){
  var newTag = $("input[name='tagName']")[0].value;
  addTag(newTag);
}

function addTag(newTag){
  //ANYWAY
  $("input[name='tagName']")[0].value = "";
  $(".similarTag").each(function(){this.remove()});

  // adding tag
  $.ajax({
    url: getDomainPath() + '/tag',
	data: newTag,
	contentType: 'text/plain',
	type: 'PUT',
	success: function(result) {
		$('<div>' + newTag + '<span class="close" onclick=""></span></div>').insertBefore(".tags .add");
		init();
	},
	error: function(textStatus) {
	  alert(textStatus);
	  return false;
	}
  });

  $(".bg_layer").click();
}

function getSimilarTags(){
  var tag = $("input[name='tagName']")[0].value;
  $('.similarTag').remove();
  if(tag.trim() == "") return;
  
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

 function checkURL() {
  if($("input[name='cv-link']").val() == '') {
	  $("input[name='cv-link']:first").next()[0].className = "valid";
	  $(".update-button")[0].disabled = false;
	  return true;
  }
  if(! urlRegExp.test($("input[name='cv-link']").val())){
    $("input[name='cv-link']:first").next()[0].className = "invalid";
    $(".update-button")[0].disabled = true;
    return false;
  } else {
    $("input[name='cv-link']:first").next()[0].className = "valid";
    $(".update-button")[0].disabled = false;
    return true;
  }  
}
 
function checkName() {
  if($("input[name='name']").val() == '') {
	$("input[name='name']:first").next()[0].className = "valid";
	$(".update-button")[0].disabled = false;
	return true;
  }
  if(! nameRegExp.test($("input[name='name']").val())){
	$("input[name='name']:first").next()[0].className = "invalid";
	$(".update-button")[0].disabled = true;
	return false;
  } else {
	$("input[name='name']:first").next()[0].className = "valid";
	$(".update-button")[0].disabled = false;
	return true;
  }  
}

function checkSurname() {
  if($("input[name='surname']").val() == '') {
	$("input[name='surname']:first").next()[0].className = "valid";
	$(".update-button")[0].disabled = false;
	return true;
  }
  if(! nameRegExp.test($("input[name='surname']").val())){
	$("input[name='surname']:first").next()[0].className = "invalid";
	$(".update-button")[0].disabled = true;
	return false;
  } else {
	$("input[name='surname']:first").next()[0].className = "valid";
	$(".update-button")[0].disabled = false;
    return true;
  }  
}
 
 
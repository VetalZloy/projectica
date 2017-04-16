var urlRegExp = /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/

function init(){
  $('.tags .close').click(function() {
    var tagName = $(this).parent()[0].innerText;    
    var tag = $(this).parent();
	console.log(tagName);
    //deleting tag via ajax
    $.ajax({
  	    url: getDomainPath() + '/tag',
  	    data: tagName,
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
    
    var name = surname = '';
    
    $("input[name='cv-link']:first").donetyping(checkURL);
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
  if(! checkURL()) {
    alert('FAIL');
    return;
  }

  var name= $("input[name='name']")[0].value
  var surname = $("input[name='surname']")[0].value;
  var cvLink = $("input[name='cv-link']")[0].value;
  
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

function openTagAddPanel() {
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

  closeTagAddPanel();
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
var passwordRegExp = /([A-Za-z]|\d){5,}/;

$(document).ready(function(){
  $("input[type='password']").donetyping(check);
});

function check(){
  $('.change-password-panel p').remove();

  var password = $("input[name='password']")[0].value;
  var passwordConf = $("input[name='passwordConfirmation']")[0].value;

  if(passwordRegExp.test(password))
    $("input[name='password']").next()[0].className = "valid";
  else {
    $("input[name='password']").next()[0].className = "invalid";
    $("input[type='submit']")[0].disabled = true;

    if(password == '') return;

    $(".panel-body").append("<p>Error: bad password format</p>");
    $(".panel-body").append("<p>Format: At least 5 English letters and digits</p>");

    return;
  }

  if(password == passwordConf && password != '')
    $("input[name='passwordConfirmation']").next()[0].className = "valid";
  else {
    $("input[name='passwordConfirmation']").next()[0].className = "invalid";
    $("input[type='submit']")[0].disabled = true;

    if(passwordConf == '') return;

    $(".panel-body").append("<p>Error: password and confirmation are not equal</p>");
    return;
  }

  $("input[type='submit']")[0].disabled = false;
}

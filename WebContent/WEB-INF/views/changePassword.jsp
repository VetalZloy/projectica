<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>${position.name}</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/changePassword.css" />'>
</head>
<body>
  <header>
    <a class="logo" href='<c:url value="/" />'></a>
    <ul>
      <li><a class="logout" href='<c:url value="/logout" />'></a></li>
      <li><a href='<c:url value="/users" />'>Users</a></li>
      <li><a href='<c:url value="/projects" />'>Projects</a></li>
      <li><a href='<c:url value="/positions" />'>Vacancies</a></li>
      <li><a href='<c:url value="/dialogs" />'>Dialogs</a></li>
    </ul>
  </header>

  <div class="wrapper">
    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-6 col-xs-offset-3">
          <div class="change-password-panel panel">
            <div class="panel-title-big">Change password</div>
            <div class="panel-body">
              <form action="" method="POST">
            		<input type="password" name="password" placeholder="Type new password...">
            		<span class="invalid"></span>
            		<input type="password" name="passwordConfirmation" placeholder="One more time...">
            		<span class="invalid"></span>
            		<input type="hidden" name="passwordToken" value="${passwordToken}">
            		<input type="submit" value="Change password" disabled>
            	</form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="js/base.js" />'></script>
  <script src='<c:url value="js/changePassword.js" />'></script>
	
</body>
</html>
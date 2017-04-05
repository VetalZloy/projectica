<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="gravatar" tagdir="/WEB-INF/tags/gravatar" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>${user.username}</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>  
  <link rel="stylesheet" href='<c:url value="/css/jquery.mCustomScrollbar.min.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/dialog.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/double-torus.css" />'>
</head>
<body>
  <header>
    <a class="logo" href='<c:url value="/" />'></a>
    <ul>
      <li><a class="logout" href='<c:url value="/logout" />'></a></li>
      <li><a href='<c:url value="/users" />'>Users</a></li>
      <li><a href='<c:url value="/projects" />'>Projects</a></li>
      <li><a href='<c:url value="/positions" />'>Vacancies</a></li>
      <li><a href='<c:url value="/dialogs" />'>Dialogs<span class="new-messages"></span></a></li>
    </ul>
  </header>

  <div class="wrapper">
    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-8 col-xs-offset-2 dialog">
          <div class="interlocutor">
            <gravatar:gravatar user="${user}" size='50'/>
            <a href='<c:url value="/users/${user.username}"/>'>${user.username}</a>
          </div>
          <div class="scroll-pane mCustomScrollbar" data-mcs-theme="minimal-dark">
          	<div class="cssload-container">
			  <div class="cssload-double-torus"></div>
			</div>
          </div>
          <div class="send-area">
            <textarea placeholder="Type message..."></textarea>
            <span class="send" onclick="send()"></span>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/base.js" />'></script>
  <script src='<c:url value="/js/dialog.js" />'></script>
  <script src='<c:url value="/js/jquery.mCustomScrollbar.concat.min.js" />'></script>

</body>
</html>
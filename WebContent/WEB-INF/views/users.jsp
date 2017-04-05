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
  <title>Users</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/users.css" />'>
</head>
<body>
  <header>
    <a class="logo" href='<c:url value="/" />'></a>
    <ul>
      <li><a class="logout" href='<c:url value="/logout" />'></a></li>
      <li class="active"><a href='<c:url value="/users" />'>Users</a></li>
      <li><a href='<c:url value="/projects" />'>Projects</a></li>
      <li><a href='<c:url value="/positions" />'>Vacancies</a></li>
      <li><a href='<c:url value="/dialogs" />'>Dialogs<span class="new-messages"></span></a></li>
    </ul>
  </header>

  <div class="wrapper">
    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-6 users">
          <c:forEach items="${users}" var="user">
          	<div class="user">
              <gravatar:gravatar user="${user}" size="80"/>
              <a href='<c:url value="/users/${user.username}" />'>${user.username}</a>
            </div>
          </c:forEach>
        </div>
        <div class="col-xs-5 col-xs-offset-1 search">
          <div class="panel-title-big">Search</div>
          <div class="panel-body">
            <input type="text" name="search" placeholder="Username...">
            <input type="text" name="tagName" placeholder="Tag...">            
            <button class="search-button" onclick="search()">Search</button>
            <div class="similar-tags"></div>
            <div class="added-tags"></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/base.js" />'></script>
  <script src='<c:url value="/js/users.js" />'></script>

</body>
</html>
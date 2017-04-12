<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Vacancies</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/positions.css" />'>
</head>
<body>
  <header>
    <a class="logo" href='<c:url value="/" />'></a>
    <ul>
      <li><a class="logout" href='<c:url value="/logout" />'></a></li>
      <li><a href='<c:url value="/users" />'>Users</a></li>
      <li><a href='<c:url value="/projects" />'>Projects</a></li>
      <li class="active"><a href='<c:url value="/positions" />'>Vacancies</a></li>
      <li><a href='<c:url value="/dialogs" />'>Dialogs<span class="new-messages"></span></a></li>
    </ul>
  </header>

  <div class="wrapper">
    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-6 vacancies">
          <c:forEach items="${vacancies}" var="position">
          	<div class="vacancy">
              <h2>
                <a href='<c:url value="/positions/${position.id}" />'>${position.name}</a>
              </h2>
              <a class="project-link" href='<c:url value="/projects/${position.projectId}" />'>${position.projectName}</a>
              <p>${position.description}</p>
          </div>
          </c:forEach>
        </div>
        <div class="col-xs-5 col-xs-offset-1 search">
          <div class="panel">
            <div class="panel-title-big">Search</div>
            <div class="panel-body">
              <input type="text" name="search" placeholder="Position name">
              <input type="text" name="tagName" placeholder="Tag">
              <div class="similar-tags"></div>
              <div class="added-tags"></div>
              <p class="button-wrapper">
                <button onclick="search()">Search</button>            
              </p>
          </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script src='<c:url value="/js/jquery.js" />'></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="js/base.js" />'></script>
  <script src='<c:url value="js/positions.js" />'></script>

</body>
</html>
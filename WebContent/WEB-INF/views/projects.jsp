<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Projects</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/projects.css" />'>
</head>
<body>
  <header>
    <a class="logo" href='<c:url value="/" />'></a>
    <ul>
      <li><a class="logout" href='<c:url value="/logout" />'></a></li>
      <li><a href='<c:url value="/users" />'>Users</a></li>
      <li class="active"><a href='<c:url value="/projects" />'>Projects</a></li>
      <li><a href='<c:url value="/positions" />'>Vacancies</a></li>
      <li><a href='<c:url value="/dialogs" />'>Dialogs<span class="new-messages"></span></a></li>
    </ul>
  </header>

  <div class="wrapper">
    <div class="container-fluid">
      <div class="row">
        <div class="col-xs-6 projects">
          <c:forEach items="${projects}" var="project">
            <div class="project">
              <h2>
                <a href='<c:url value="/projects/${project.id}" />'>${project.name}</a>
              </h2>
              <h3>${project.vacanciesAmount} vacancies</h3>
              <p>${project.description}</p>
          	</div>
          </c:forEach>
        </div>
        <div class="col-xs-5 col-xs-offset-1 search">
          <div class="panel">
          	<div class="panel-title-big">Search</div>
          	<div class="panel-body">
              <input type="text" name="search" placeholder="Project name">
              <p class="button-wrapper">
                <button class="search-button" onclick="search()">Search</button>
              </p>
          	</div>
          </div>
          <c:if test="${securityUtil.isLoggedIn()}">
          	<span class="create-project" onclick="openPanel('.create-panel')" title="Create project"></span>
          </c:if>
        </div>
      </div>
    </div>
  </div>

  <div class="create-panel panel openable">
    <div class="panel-title">Create your project</div>
    <div class="panel-body">
   	  <form method="post">
        <input type="text" name="name" placeholder="Project name...">
        <input type="text" name="position" placeholder="Your position...">
        <textarea name="description" placeholder="Description..."></textarea>
        <p class="status"></p>
        <p class="button-wrapper">
          <button type="submit" onclick="create()" disabled>Create</button>
        </p>        
      </form>
    </div>
  </div>

  <div class="bg_layer"></div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/jquery.cookie.js" />'></script>  
  <script src='<c:url value="js/base.js" />'></script>
  <script src='<c:url value="js/projects.js" />'></script>

</body>
</html>
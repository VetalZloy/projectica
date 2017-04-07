<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="gravatar" tagdir="/WEB-INF/tags/gravatar" %>
<%@ taglib prefix="date" tagdir="/WEB-INF/tags/date" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>${user.username}</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/user.css" />'>
</head>
<body>
  <a class="github" href="https://github.com/VetalZloy/projectica"></a>  
  <a class="about" href='<c:url value="/about" />'></a>
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
        <div class="avatar col-xs-3">
          <gravatar:gravatar user="${user}" currentUser="${currentUser}"/>
        </div>
        
        <div class="info col-xs-5">
          <h1>
            ${user.username}
            <c:if test='${currentUser}'>
              <span class="edit" onclick="openEditPanel()"></span>
            </c:if>
            <c:if test='${!currentUser && securityUtil.isLoggedIn()}'>
              <a href='<c:url value="/dialogs/${user.username}" />' class="send-message"></a>
            </c:if>
          </h1>
          <p class="email">${user.email}</p>
          <p class="full-name">${user.name} ${user.surname}</p>
          <c:if test='${user.cvLink != null}'>
          	<a href='<c:url value="${user.cvLink}" />'>Download CV</a>
          </c:if>
        </div>
        <div class="right-side col-xs-4">
          <div class="tags">
            <div class="list-title">
          	  Tags <c:if test="${currentUser}"><span class="add" onclick="openTagAddPanel()"></span></c:if>
            </div>
            <div class="panel-body tag-list">
          	  <c:forEach items="${tags}" var="tag">
          	    <div>
          	  	  ${tag.tag} <c:if test="${currentUser}"><span class="close"></span></c:if>
          	    </div>
          	  </c:forEach>
            </div>
          </div>
          <c:if test="${currentProjects.size() > 0}">
          <div class="projects">
            <div class="list-title">Current projects</div>
            <div class="panel-body">
          	  <c:forEach items="${currentProjects}" var="project">
          	    <a href='<c:url value="/projects/${project.id}"/>'>${project.name}</a>
          	  </c:forEach>
            </div>
          </div>      
        </c:if>
        </div>
      </div>
      <div class="row">
      	<div class="comments col-xs-7">
          <c:forEach items="${closedPositions}" var="position">
          	<div class="comment">
              <div class='list-title ${position.estimation ? "good" : "bad" }'>
              	<a href='<c:url value="/positions/${position.id}"/>'>${position.name}</a>
              	<span class="date">
              	  <date:dateFormatter date="${position.firingDate}" />
              	</span>
              </div>
              <div class="panel-body">${position.comment}</div>
            </div>
		  </c:forEach>
        </div>
        <!--<div class="comments col-xs-7">
          <c:forEach items="${comments}" var="comment">
          	<div class="comment">
              <div class='list-title ${comment.estimation ? "good" : "bad" }'>
              	<a href='<c:url value="/projects/${comment.project.id}"/>'>${comment.project.name}</a>
              	<span class="date">${comment.date }</span>
              </div>
              <div class="panel-body">${comment.text}</div>
            </div>
		  </c:forEach>
        </div>  -->
      </div>
    </div>
    <div class="edit-panel">
      <div class="panel-title">
        <span class="title" onclick="update()">UPDATE</span>
        <span class="close" onclick="closeEditPanel()"></span>
      </div>
      <div class="panel-body edit-body">
        <input type="text" name="username" disabled="disabled" value="${user.username}"><span class="valid"></span><br>
        <input type="text" name="email" disabled="disabled" value="${user.email}"><span class="valid"></span><br>
        <input type="text" name="name" placeholder="Name" value="${user.name}"><span class="valid"></span><br>
        <input type="text" name="surname" placeholder="Surname" value="${user.surname}"><span class="valid"></span><br>
        <input type="text" name="cv-link" placeholder="CV link" value="${user.cvLink}"><span class="valid"></span><br>
        <button class="update-button" onclick="update()">Update</button>
      </div>
    </div>
    <div class="tag-add-panel">
      <div class="panel-title">
        <span class="title" onclick="addTagWrapper()">Add tag</span><span class="close" onclick="closeTagAddPanel()"></span>
      </div>
      <div class="panel-body add-body">
        <input type="text" name="tagName" autofocus>
        <button class="add-button" onclick="addTagWrapper()">Add</button>
      </div>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/base.js" />'></script>
  <script src='<c:url value="/js/user.js" />'></script>

</body>
</html>

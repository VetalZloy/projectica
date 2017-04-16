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
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
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
        <div class="info col-xs-6">
          <div class="avatar">
            <gravatar:gravatar user="${user}" currentUser="${currentUser}"/>
          </div>
          <div class="text">
            <h1>
              ${user.username}              
              <c:if test='${currentUser}'>
                <span class="edit" onclick="openPanel('.edit-panel')"></span>
              </c:if>
              <c:if test='${!currentUser && securityUtil.isLoggedIn()}'>
              	<a href='<c:url value="/dialogs/${user.username}" />' class="send-message"></a>
              </c:if>
            </h1>
            <p class="email">${user.email}</p>
			<p class="full-name">${user.name} ${user.surname}</p>
			<c:if test="${user.cvLink != null}">
			  <a href="${user.cvLink}" class="cv-link">${user.username}'s CV</a>
			</c:if> 
            <div class="tags">
          	  <c:forEach items="${tags}" var="tag">
          	    <div>
          	  	  ${tag.tag} <c:if test="${currentUser}"><span class="close"></span></c:if>
          	    </div>
          	  </c:forEach>
          	  <c:if test='${currentUser}'>
          	    <span class="add" onclick="openPanel('.tag-add-panel')"></span>
          	  </c:if>
            </div>
          </div>
        </div>
        <div class="right-side col-xs-4 col-xs-offset-2">
          <c:if test="${currentProjects.size() > 0}">
            <div class="projects">
              <span class="title">Current projects</span>
          	  <c:forEach items="${currentProjects}" var="project">
          	    <a href='<c:url value="/projects/${project.id}"/>'>${project.name}</a>
          	  </c:forEach>
            </div>      
          </c:if>
        </div>
      </div>
      <c:if test="${closedPositions.size() > 0}">
        <span class="title">Feedback</span>
        <div class="comments">
          <c:forEach items="${closedPositions}" var="position">
            <div class="comment">
              <a href='<c:url value="/positions/${position.id}"/>'>${position.name}</a>
              <span class='${position.estimation ? "good" : "bad" }'></span>
          	  <span class="date">
           	    <date:dateFormatter date="${position.firingDate}" />
           	  </span>           	    
              <p>${position.comment}</p>
            </div>
		  </c:forEach>
         </div>
      </c:if>
    </div>
    
    <div class="edit-panel panel openable">
      <div class="panel-title">UPDATE</div>
      <div class="panel-body edit-body">
        <input type="text" name="username" disabled="disabled" value="${user.username}"><span class="valid"></span><br>
        <input type="text" name="email" disabled="disabled" value="${user.email}"><span class="valid"></span><br>
        <input type="text" name="name" placeholder="Name" value="${user.name}"><span class="valid"></span><br>
        <input type="text" name="surname" placeholder="Surname" value="${user.surname}"><span class="valid"></span><br>
        <input type="text" name="cv-link" placeholder="CV link" value="${user.cvLink}"><span class="valid"></span><br>
        <p class="button-wrapper">
          <button class="update-button" onclick="update()">Update</button>
        </p>
      </div>
    </div>
    
    <div class="tag-add-panel panel openable">
      <div class="panel-title">Add tag</div>
      <div class="panel-body">
        <input type="text" name="tagName" placeholder="Tag" autofocus>
        <div class="similar-tags"></div>
        <p class="button-wrapper">
          <button class="add-button" onclick="addTagWrapper()">Add</button>
        </p>
      </div>
    </div>
  </div>
  
  <div class="bg_layer"></div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/base.js" />'></script>
  <script src='<c:url value="/js/user.js" />'></script>

</body>
</html>

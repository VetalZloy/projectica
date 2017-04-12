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
  <title>${position.name}</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/position.css" />'>
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
        <div class="info col-xs-6">
          <h1>${position.name} 
          	<span class="status ${status.toLowerCase()}">${status}</span>
          	<c:if test='${status eq "Free" && creator}'>
          	  <span class="edit" onclick="openEditPanel()"></span>
          	</c:if>
          </h1>
          <p class="project">
          	Project: 
          	<a href='<c:url value="/projects/${position.project.id}"/>'>${position.project.name}</a>
          </p>
          <c:if test='${status eq "Active" || status eq "Closed"}'>
            <div class="participant">
	          <gravatar:gravatar user="${position.user}"/>
              <a href='<c:url value="/users/${position.user.username}"/>'>${position.user.username}</a>
            </div>
            <p>Hiring date: ${hiringDate}</p>
          </c:if>
          <c:if test='${status eq "Closed"}'>
          	<p>Firing date: ${firingDate}</p>
          </c:if>
          <c:if test='${status eq "Free" || status eq "Requested"}'>
            <div class="description panel">
              <div class="panel-title-big">Description</div>
              <div class="panel-body">
                <p class="description">${position.description}</p>
              </div>
            </div>
          </c:if>
        </div>
        <div class="col-xs-4 col-xs-offset-2">
          <div class="tags panel">
            <div class="panel-title-big">
              Tags 
              <c:if test="${creator}">
                <span class="add" onclick="openAddTagPanel()"></span>
              </c:if>
            </div>
            <div class="panel-body">
              <c:forEach items="${position.tags}" var="tag">
                <div>
                  ${tag.tag} 
                  <c:if test="${creator}">
                    <span class="close"></span>
                  </c:if>
                </div>
              </c:forEach>
            </div>
          </div>
          <c:if test='${status eq "Closed"}'>
	          <div class="comment panel">
	            <div class='panel-title-big ${position.estimation ? "good" : "bad"}'>
	              Comment
	            </div>
	            <div class="panel-body">
	              <p>${position.comment}</p>
	            </div>
	          </div>
          </c:if>
          <c:if test='${status eq "Free" && !creator}'>
            <div class="request panel">
              <div class="panel-title-big">Create request</div>
              <div class="panel-body">
                <textarea placeholder="Your additions..."></textarea>
                <button type="button" name="button" onclick="createRequest()">Create</button>
              </div>
            </div>
          </c:if>
          <c:if test='${status eq "Active" && creator && !creatorPosition}'>
            <div class="close-position panel">
              <div class="panel-title-big">Close position</div>
              <div class="panel-body">
                <textarea name="comment" placeholder="Comment"></textarea>
                <button class="like" onclick="closePosition('true')">Close</button>
                <button class="dislike" onclick="closePosition('false')">Close</button>
              </div>
            </div>
          </c:if>
          <c:if test='${status eq "Free" && creator}'>
            <div class="applicants panel">
              <div class="panel-title-big">Applicants</div>
              <div class="panel-body">              	
          		<c:if test="${position.requests.size() == 0}">
          		  <p>There is not any requests, now.</p>
          		</c:if>
              	<c:forEach items="${position.requests}" var="request">
                  <div class="applicant">
                    <p class="user">
                      <gravatar:gravatar user="${request.user}"/>
                      <a href='<c:url value="/users/${request.user.username}"/>'>${request.user.username}</a>
                      <a href='<c:url value="/dialogs/${request.user.username}"/>'><span class="send-message"></span></a>
                      <button onclick="join('${request.user.username}')">Join</button>
                    </p>
                    <p class="user-additions">${request.userAdditions}</p>
                  </div>
                </c:forEach>
              </div>
            </div>
          </c:if>
        </div>
      </div>
    </div>
  </div>

  <div class="edit-panel panel">
    <div class="panel-title">
      Edit
      <span class="close" onclick="closeEditPanel()"></span>
    </div>
    <div class="panel-body">
      <input type="text" name="name" value="${position.name}" placeholder="Posiiton name"/>
      <textarea name="description" placeholder="Description">${position.description}</textarea>
      <p class="error"></p>
      <p class="button-wrapper">
        <button type="button" name="button" onclick="edit()">Edit</button>
      </p>      
    </div>
  </div>

  <div class="tag-add-panel panel">
    <div class="panel-title">
      Add tag
	  <span class="close" onclick="closeTagAddPanel()"></span>
    </div>
    <div class="panel-body">
      <input type="text" name="tagName" placeholder="Tag" autofocus>
      <div class="similar-tags"></div>
      <p class="button-wrapper">
        <button class="add-button" onclick="addTagWrapper()">Add</button>
      </p>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/base.js" />'></script>
  <script src='<c:url value="/js/position.js" />'></script>

</body>
</html>

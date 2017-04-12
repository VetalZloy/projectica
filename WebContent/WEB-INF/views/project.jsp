<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>${project.name}</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/project.css" />'>
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
          <h1>
            ${project.name}
            <c:if test='${creator}'>
          	  <span class="edit" onclick="openEditPanel()"></span>
          	</c:if>
          </h1>
          <p>${project.description}</p>
          <p class="creator">
            Creator: 
            <a href='<c:url value="/users/${project.creator.username}"/>'>${project.creator.username}</a>
          </p>
          <div class="participants panel">
            <div class="panel-title-big">Participants</div>
            <div class="panel-body">          	
          	  <c:forEach items="${participants}" var="entry">
          	    <div class="user">
          	      <p>
          	        <a href='<c:url value="/users/${entry.key.username}" />'>${entry.key.username}</a>
          	        <span class="reveal"></span>
          	  	  </p>
          	  	  <div class="user-positions">
          	        <c:forEach items="${entry.value}" var="position">
          	  	      <a href='<c:url value="/positions/${position.id}" />'>${position.name}</a>
          	        </c:forEach>
          	      </div>
          	    </div>
          	  </c:forEach>
            </div>
          </div>
        </div>
        <div class="positions col-xs-5 col-xs-offset-1">
          <c:if test="${creator || participant}">
            <div class="chatrooms panel">
              <div class="panel-title-big">
                Chatrooms 
                <c:if test="${creator}">
                  <span class="add" onclick="openAddChatroomPanel()"></span>
                </c:if>
              </div>
              <div class="panel-body">          	
          	    <c:forEach items="${project.chatRooms}" var="chatRoom">
          	      <a href='<c:url value="/chatrooms/${chatRoom.id}" />'>${chatRoom.name}</a>
			    </c:forEach>
              </div>
            </div>
          </c:if>
          <div class="free-positions panel">
            <div class="panel-title-big">
              Free positions <c:if test="${creator}"><span class="add" onclick="openCreatePositionPanel()"></span></c:if>
            </div>
            <div class="panel-body">
              <c:forEach items="${openPositions}" var="position">
                <a href='<c:url value="/positions/${position.id}"/>'>${position.name}</a>
		 	  </c:forEach>
            </div>
          </div>
          <div class="closed-positions panel">
            <div class="panel-title-big">
              Closed positions
            </div>
            <div class="panel-body">
              <c:forEach items="${closedPositions}" var="position">
                <a href='<c:url value="/positions/${position.id}"/>'>${position.name}</a>
		 	  </c:forEach>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="add-chatroom panel">
    <div class="panel-title">
      Add chatroom
      <span class="close" onclick="closeAddChatroomPanel()"></span>
    </div>
    <div class="panel-body">
      <input type="text" name="chatroomName" placeholder="New chatroom">
      <p class="button-wrapper">
        <button type="button" name="button" onclick="addChatroom()">Add</button>
      </p>      
    </div>
  </div>

  <div class="create-position panel">
    <div class="panel-title">
      Add position
      <span class="close" onclick="closeCreatePositionPanel()"></span>
    </div>
    <div class="panel-body">
      <input type="text" name="positionName" placeholder="Position">
      <textarea name="position-description" placeholder="Description"></textarea>
      <div class="tag-block">
        <input class="tag-input" type="text" name="tagName" placeholder="Tag">
        <button class="add-button" type="button" name="button" onclick="addTagWrapper()">Add</button>
        <div class="similar-tags"></div>
      </div>
      <div class="added-tags"></div>
      <p class="button-wrapper">
        <button type="button" name="button" onclick="createPosition()">Create</button>
      </p>      
    </div>
  </div>
  
  <div class="edit-panel panel">
    <div class="panel-title">
      Edit
      <span class="close" onclick="closeEditPanel()"></span>
    </div>
    <div class="panel-body">
      <input type="text" name="name" value="${project.name}" placeholder="Posiiton name..."/>
      <textarea name="description" placeholder="Description...">${project.description}</textarea>
      <p class="error"></p>
      <p class="button-wrapper">
        <button type="button" name="button" onclick="edit()">Edit</button>
      </p>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/base.js" />'></script>
  <script src='<c:url value="/js/project.js" />'></script>

</body>
</html>

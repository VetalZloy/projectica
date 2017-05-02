<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>${title}</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/info.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/registrationConfirm.css" />'>
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
          <p class="topic">Registration is completed</p>
          <p class="additions"></p>
        </div>
      </div>      
    </div>
  </div>
  
  <div class="blocking-div"></div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="js/base.js" />'></script>
  <script src='<c:url value="js/registrationConfirm.js" />'></script>

</body>
</html>
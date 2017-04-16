<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>About</title>

  <link rel="stylesheet" href='<c:url value="/css/bootstrap.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/base.css" />'>
  <link rel="stylesheet" href='<c:url value="/css/about.css" />'>
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
        <div class="col-xs-10 col-xs-offset-1">

          <h1>Projectica</h1>
          Projectica is a social network, which helps young developers to find like-minded ones. Also Projectica gives you abilities to create projects(startups) by your wish and hand-pick team for such project.

          <h2>First steps</h2>
          <ol>
            <li>Register</li>
            <li>Fill out Tag container in your homepage. Tag container should contain set of your skills. It helps other users to find you</li>
            <li>Visit <a href='<c:url value="/users" />'>Users</a> page to find like-minded and communicate with them</li>
            <li>Visit <a href='<c:url value="/positions" />'>Vacancies</a> page to select a vacancy by your wish and skills and make request to get it</li>
            <li>Visit <a href='<c:url value="/projects" />'>Projects</a> page to familiarize with current projects or create your one</li>
          </ol>
          <div class="note">Note! Projectica has test project – <a href='<c:url value="/projects/36" />'>Matrix</a>. It was created to show you how the system works. All positions and participants in Matrix are inactive, so don’t try to communicate with them. In time it will be removed</div>

          <h2>Creating and managing projects</h2>
          <ol>
            <li>Visit <a href='<c:url value="/projects" />'>Projects</a> page and click “plus” button<span class="plus-button"></span></li>
            <li>Fill out appeared form. IMPORTANT, if you create a project, you should possess a position(role) in it.</li>
            <li>If the form was filled out correctly, you will be redirected to your project page. Here you will manage project</li>
            <li>Create positions necessary for your project. And periodically check them for new requests. If you find the perfect applicant for your position and there is request created by him, you can join him to project</li>
            <li>
              Also you can close position if it’s no longer needed or position owner doesn’t match with it. You should leave a comment about the position owner and evaluate his job via a positive or negative button
              <span class="button good">Close</span>
              <span class="button bad">Close</span>
            </li>
          </ol>
          <div class="space"></div>
          Please be patient. Projectica is not completed now, so few bugs might have a place. You always can communicate with me.<br>
          Here’s my contacts:
          <ul>
            <li><a href="https://github.com/VetalZloy/projectica" class="my-github"></a></li>
            <li><a href="https://vk.com/vetalzloy" class="vk"></a></li>
            <li>vetalzloy@gmail.com</li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/base.js" />'></script>

</body>
</html>
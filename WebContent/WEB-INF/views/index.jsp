<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Projectica</title>
  <link rel="stylesheet" href='<c:url value="/css/index.css" />'>
</head>
<body>
  <div class="wrapper">
    <div class="left">
      <div class="login" onclick="openPanel('.log-in')">
        <p>Log in</p>
      </div>
      <div class="signup" onclick="openPanel('.sign-up')">
        <p>Sign up</p>
      </div>
    </div>
    <div class="right">
      <div class="projects" onclick="location.href = '<c:url value="/projects"/>'">
        <p>Projects</p>
      </div>
      <div class="vacancies" onclick="location.href = '<c:url value="/positions"/>'">
        <p>Vacancies</p>
      </div>
    </div>
    <img class="logo" src="<c:url value="/img/logo-main.png"/>" alt="Projectica">
    <a class="github" href="https://github.com/VetalZloy/projectica"></a>
    <a class="about" href='<c:url value="/about" />'></a>
  </div>

  <div class="log-in panel openable">
    <div class="panel-title-big">Log in</div>
    <div class="panel-body">
      <p></p>
    	<!-- <c:if test="${param.logout != null}">YOU'VE BEEN LOGOUT SUCCESSFULLY</c:if> -->
      <form action='<c:url value="/login" />' method="POST">
    	<input type="text" name="username" placeholder="Username or email"/><br/>
    	<input type="password" name="password" placeholder="Password"/><br/>
    	<input id="remember-me" class="css-checkbox" type="checkbox" name="remember-me" checked/>
        <label for="remember-me" class="css-label">Remember me</label><br/>
    	  <!-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>-->
    	<input type="submit" value="Log in"/>
        <a onclick="openResetPasswordPanel()">Forgot password</a>
      </form>
    </div>    
  </div>

  <div class="sign-up panel openable">
    <div class="panel-title-big">Sign up</div>
    <div class="panel-body">
      <form:form method="POST" commandName="registrationForm">
		<form:input path="username" placeholder="Username" /><span class="invalid"></span>	
		<form:input path="email" placeholder="Email" /><span class="invalid"></span>
		<form:password path="password" placeholder="Password" /><span class="invalid"></span>		
		<form:password path="passwordConf" placeholder="Confirmation" /><span class="invalid"></span>		
		<form:button disabled="">Sign up</form:button><br>
		<p><form:errors path="username" /></p>
        <p><form:errors path="email" /></p>
        <p><form:errors path="password" /></p>
        <p><form:errors path="passwordConf" /></p>
	  </form:form>
    </div>
  </div>

  <div class="reset-password panel openable">
    <div class="panel-title-big">Reset password</div>
    <div class="panel-body">
      <form action='<c:url value="/reset-password" />' method="post">
        <input type="text" name="username" placeholder="Username ...">
        <p><input type="submit" value="Reset"></p>
      </form>
    </div>
  </div>

  <div class="bg_layer"></div>

  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src='<c:url value="/js/index.js" />'></script>

</body>
</html>

<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag import="com.vetalzloy.projectica.util.GravatarUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ tag body-content="empty" %>
<%@ attribute name="user" required="true" type="com.vetalzloy.projectica.model.User"%>
<%@ attribute name="currentUser" required="false" type="java.lang.Boolean"%>
<%@ attribute name="size" required="false" type="java.lang.Integer"%>
<c:choose>
  <c:when test="${empty size}">
    <c:set var="url"><%= GravatarUtil.getGravatarUrl(user.getEmail())%></c:set>
  </c:when>
  <c:otherwise>
    <c:set var="url"><%= GravatarUtil.getGravatarUrl(user.getEmail(), size)%></c:set>
  </c:otherwise>
</c:choose>
<img src="${url}" />
<c:if test="${!currentUser && onlineUtil.isOnline(user.username)}">
  <span class="online"></span>
</c:if>

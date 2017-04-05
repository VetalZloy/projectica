<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag import="java.time.format.DateTimeFormatter"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ tag body-content="empty" %>
<%@ attribute name="date" required="true" type="java.time.LocalDateTime"%>
<%@ attribute name="format" required="false" type="java.lang.String"%>
<c:if test="${empty format}" >
	<c:set var="format" value="dd.MM.yyyy" />
</c:if>
<% 
	String format = (String) jspContext.getAttribute("format");
	out.print(date.format(DateTimeFormatter.ofPattern(format)));
%>

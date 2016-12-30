<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="java.util.Date"%>
<%
	long serverTime = new Date().getTime();
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="serverTime" value="<%=serverTime%>" />

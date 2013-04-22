<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8"%>
<%
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache");
response.setHeader("Expires","0");
%>

<%@ taglib prefix="c"        uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"      uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"       uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form"     uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tiles"    uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sec"      uri="http://www.springframework.org/security/tags" %>

<c:set var="ctx" value="${pageContext['request'].contextPath}"/>
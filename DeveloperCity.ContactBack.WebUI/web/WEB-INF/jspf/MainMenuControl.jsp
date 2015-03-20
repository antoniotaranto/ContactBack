<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page isELIgnored="false"%>
                    <div id="left" class="sidebar first">
                        <ul class="menu">
                            <li class="leaf first<c:if test="${currentAction == activeModule}"> active active-trail</c:if>"><a href="${rootPath}${activeModule}" title="Página inicial" class="active">PÁGINA INICIAL</a></li>
                            <c:forEach var="module" items="${modules}">

<c:choose>
	<c:when test="${fn:startsWith(module.servlet, 'http') }">
                            <li class="leaf<c:if test="${currentAction == module.servlet}"> active active-trail</c:if>"><a href="${module.servlet}" title="${module.description}">${module.description}</a></li>
	</c:when>
	<c:otherwise>
                            <li class="leaf<c:if test="${currentAction == module.servlet}"> active active-trail</c:if>"><a href="${rootPath}${module.servlet}" title="${module.description}">${module.description}</a></li>
	</c:otherwise>
</c:choose>
                            </c:forEach>
                        </ul>
                    </div>

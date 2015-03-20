<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="javax.servlet.jsp.jstl.core.Config, java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>
<c:if test="${!empty currentWorkTime}">
    <table class="grid" id="agentBreakList" border="1">
        <caption>Lista de pausas de hoje</caption>
        <thead>
            <tr>
                <th>Tipo<span></span></th>
                <th>Início<span></span></th>
                <th>Fim<span></span></th>
                <th>Duração<span></span></th>
                <th>Ação<span></span></th>
            </tr>
        </thead>
        <tfoot class="pager">
            <tr class="control">
                <td colspan="5">
                    <table>
                        <tr>
                            <td><strong>Login em:</strong> <c:choose><c:when test="${currentWorkTime.loginToday}"><fmt:formatDate value="${currentWorkTime.loginTime}" pattern="HH:mm:ss"/></c:when><c:otherwise><fmt:formatDate value="${currentWorkTime.loginTime}" pattern="dd/MM/yyyy HH:mm:ss"/></c:otherwise></c:choose></td>
                            <c:if test="${!empty currentWorkTime.logoutTime}">
                            <td><strong>Logout em:</strong> <c:choose><c:when test="${currentWorkTime.logoutToday}"><fmt:formatDate value="${currentWorkTime.logoutTime}" pattern="HH:mm:ss"/></c:when><c:otherwise><fmt:formatDate value="${currentWorkTime.logoutTime}" pattern="dd/MM/yyyy HH:mm:ss"/></c:otherwise></c:choose></td>
                            </c:if>
                            <td><strong>Tempo em sessão:</strong> ${currentWorkTime.sessionDurationString}</td>
                            <td><strong>Quantidade de Pausas:</strong> ${rowCount}</td>
                            <td><strong>Tempo em pausa:</strong> ${currentWorkTime.breakDurationString}</td>
                        </tr>
                    </table>
                </td>
            </tr>
        </tfoot>
        <tbody>
            <c:set var="rowNumber" value="0" />
            <c:forEach var="break" items="${currentWorkTime.breaks}">
                <c:set var="rowNumber" value="${rowNumber + 1}" />
                <c:set var="rowClass" value="" />
                <c:choose>
                    <c:when test="${ rowNumber % 2 == 0 }">
                        <c:set var="rowClass" value="odd" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="rowClass" value="even" />
                    </c:otherwise>
                </c:choose>
                <tr class="${rowClass}">
                    <td style="width: 32%;">${break.breakType}</td>
                    <td style="text-align: right; width: 23%;">
                <c:choose>
                    <c:when test="${break.breakStartToday}">
                        <fmt:formatDate value="${break.breakStart}" pattern="HH:mm:ss"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:formatDate value="${break.breakStart}" pattern="dd/MM/yyyy HH:mm:ss"/>
                    </c:otherwise>
                </c:choose>
                    </td>
                    <td style="text-align: right; width: 23%;">
                <c:choose>
                    <c:when test="${break.breakEndToday}">
                        <fmt:formatDate value="${break.breakEnd}" pattern="HH:mm:ss"/>
                    </c:when>
                    <c:otherwise>
                        <fmt:formatDate value="${break.breakEnd}" pattern="dd/MM/yyyy HH:mm:ss"/>
                    </c:otherwise>
                </c:choose>
                    </td>
                    <td style="text-align: right; width: 12%;">${break.breakDurationString}</td>
                    <c:choose>
                        <c:when test="${ !break.systemBreak && break.breakEnd == null}">
                            <td style="width: 10%;"><input type="button" onclick="endBreak(${break.breakID});" value="Finalizar" /></td>
                        </c:when>
                        <c:otherwise>
                            <td style="width: 10%;"></td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </c:forEach>

        </tbody>
    </table>
</c:if>

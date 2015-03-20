<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="javax.servlet.jsp.jstl.core.Config, java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <meta http-equiv="Expires" content="0" />
        <meta http-equiv="Last-Modified" content="0" />
        <meta http-equiv="Cache-Control" content="no-cache,must-revalidate,max-age=0" />
        <link href="/CAC/resources/CSS/mobile.css" rel="stylesheet" type="text/css" />
        <meta http-equiv="Pragma" content="no-cache" />
        <title>ContactBack - CAC Danone</title>
    </head>
    <body>
        <div>
            <h1>Danone CAC</h1>
            <h2>Agendamento de Contato</h2>
    <c:choose>
        <c:when test="${webQueue != null}">
            <ul id="details">
                <li>Agendado por: <span id="lblUsername">${webQueue.customer.name}</span></li>
                <li>Agendado em: <span id="lblScheduleTime"><fmt:formatDate value="${webQueue.scheduleTime}" pattern="dd/MM/yyyy HH:mm:ss"/></span></li>
                <li>Número de retorno: <span id="lblCallbackNumber">${webQueue.callBackNumber}</span></li>
                <li>Posição de ingresso: <span id="lblEntryPosition">${webQueue.entryPosition + 1}&ordf;</span></li>
                <li>Previsão para retorno: <span id="lblEstimatedTimeToAttend"><fmt:formatDate value="${webQueue.estimatedTimeToAttend}" pattern="dd/MM/yyyy HH:mm:ss"/></span></li>
                <li>Tentativas de retorno realizadas: <span id="lblAttendCount">${webQueue.attendCount}</span></li>
                <li>Status atual: <span id="lblCurrentStatus">${webQueue.queueStatus == "InQueue" ? "Na fila" : "Desconhecido"}</span></li>
                <li>Congelado até: <span id="lblDontCallBefore"><fmt:formatDate value="${webQueue.dontCallBefore}" pattern="dd/MM/yyyy HH:mm:ss"/></span></li>
            </ul>
        </c:when>
        <c:otherwise>
            <form id="formWebQueue" action="/CAC/CockPit/WebQueueMobile" method="post">
                <fieldset class="formList">
                    <legend>Dados para Agendamento</legend>
                    <span id="errorLabel" class="error">
                        <c:if test="${!empty errorMessage}">
                            ${errorMessage}
                        </c:if>
                    </span>
                    <ul>
                        <li><label>Usuário:</label><input id="txtUsername" maxlength="100" type="text" readonly="readonly" value="${sessionUser}" disabled="disabled" /></li>
                        <li><label>Número de Retorno:</label><input id="txtCallbackNumber" name="txtCallbackNumber" maxlength="10" type="text" value="${sessionMobilePhone}" /></li>
                        <li id="btnSubmit"><input type="submit" value="Salvar"></input></li>
                    </ul>
                </fieldset>
            </form>
        </c:otherwise>
    </c:choose>
        </div>
    </body>
</html>
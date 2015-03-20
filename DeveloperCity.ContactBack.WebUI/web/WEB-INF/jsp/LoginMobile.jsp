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
            <h2>Login</h2>
            <form id="formLogin" action="/CAC/Mobile" method="post">
                <fieldset class="formList">
                    <legend>Dados para Login</legend>
                    <span id="errorLabel" class="error">
                        <c:if test="${!empty errorMessage}">
                            ${errorMessage}
                        </c:if>
                    </span>
                    <ul>
                        <li><label>Usuário:</label><input id="txtLogin" name="txtLogin" maxlength="20" type="text" value="${lastUser}" /></li>
                        <li><label>Senha:</label><input id="txtPassword" name="txtPassword" maxlength="20" type="password" value="" /></li>
                        <li id="btnSubmit"><input type="submit" value="Entrar"></input></li>
                    </ul>
                </fieldset>
            </form>
        </div>
    </body>
</html>
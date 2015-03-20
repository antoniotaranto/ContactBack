<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <meta http-equiv="Expires" content="0" />
        <meta http-equiv="Last-Modified" content="0" />
        <meta http-equiv="Cache-Control" content="no-cache,must-revalidate,max-age=0" />
        <meta http-equiv="Pragma" content="no-cache" />
        <title>ContactBack - CAC Danone</title>
        <link href="/CAC/resources/CSS/main.css" rel="stylesheet" type="text/css" />
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/jquery.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/jquery.metadata.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/jquery.blockui.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/main.js"></script>

        <!--[if IE 6]>
                <script type="text/javascript" src="/CAC/resources/SCRIPT/DD_belatedPNG_0.0.7a.js"></script>
                <link href="/CAC/resources/CSS/main-ie6.css" rel="stylesheet" type="text/css" />
        <![endif]-->

        <!--[if IE 7]>
                <link href="/CAC/resources/CSS/main-ie7.css" rel="stylesheet" type="text/css" />
        <![endif]-->
    </head>
    <body class="home">
        <div class="outer">
            <div class="limit">
                <div class="header">
                    <p>SEJA BEM-VINDO AO PORTAL DO IP CallBack</p>
                </div>
                <div class="inside">
                    <h1>Danone</h1>
                    <div class="content">
                        <form action="Login" method="POST">
                            <fieldset>
                                <legend><span class="fixpng"><b>ENTRE COM SEUS DADOS DE ACESSO</b></span></legend>
                                <ul class="login fixpng">
                                    <li>
                                        <label>LOGIN</label>
                                        <input type="text" name="txtLogin" />
                                    </li>
                                    <li>
                                        <label>SENHA</label>
                                        <input type="password" name="txtPassword" />
                                    </li>
                                </ul>
                            </fieldset>
                            <c:if test="${!empty errorMessage}">
                            <span class="error">${errorMessage}</span>
                            </c:if>
                            <fieldset class="buttons">
                                <ul>
                                    <li><input type="submit" value="entrar" class="entrar" /></li>
                                </ul>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
            <!-- FOOTER -->
            <c:import url="/Controls/FooterControl"></c:import>
        </div>
    </body>
</html>
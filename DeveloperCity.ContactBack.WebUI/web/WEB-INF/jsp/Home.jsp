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
                        <ul class="welcome">
                            <c:if test="${hasBackOfficeAccess}">
                            <li class="fixpng">
                                <strong>BackOffice</strong>
                                <p class="fixpng">
                                    Administra��o do servi�o, parametriza��o das prefer�ncias, cria��o e modifica��o de usu�rios e agentes, visualiza��o de status de servi�os.
                                    <span class="entrar button"><a href="${rootPath}BackOffice">ENTRAR</a></span>
                                </p>
                            </li>
                            </c:if>
                            <c:if test="${hasCockPitAccess}">
                            <li class="fixpng">
                                <strong>CockPit</strong>
                                <p class="fixpng">
                                    Painel de controle do gerente e supervisor do CAC, com gr�ficos e relat�rios, ferramenta para consulta e manipula��o da lista de espera por retorno e dos agentes da central de atendimento.
                                    <span class="entrar button"><a href="${rootPath}CockPit">ENTRAR</a></span>
                                </p>
                            </li>
                            </c:if>
                            <c:if test="${hasAgentCockPitAccess}">
                            <li class="fixpng">
                                <strong>Agent CockPit</strong>
                                <p class="fixpng">
                                    Visualiza��o das minhas estat�sticas como agente, manipula��o de pausas e sess�es (login/logoff), visualiza��o da lista de espera por retorno.
                                    <span class="entrar button"><a href="${rootPath}Agent">ENTRAR</a></span>
                                </p>
                            </li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- FOOTER -->
            <c:import url="/Controls/FooterControl"></c:import>
        </div>
    </body>
</html>
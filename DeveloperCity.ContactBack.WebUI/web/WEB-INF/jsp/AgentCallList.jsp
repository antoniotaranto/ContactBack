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
        <script language="javascript" type="text/javascript">
            var rootPath = '';
            var msgConfirmation = "Você tem certeza que deseja alterar essa chamada para caixa postal?";
            jQuery(document).ready(function() {
                rootPath = '${rootPath}';
                pageLoad();
            });
        </script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/AgentCallList.js"></script>
    </head>
    <body>
        <div class="outer">
            <div class="limit">
                <c:import url="/Controls/HeaderControl/Agent"></c:import>

                <div class="inside two-column-left">
                    <div class="breadcrumb">
                        <h3 class="fixpng">Menu</h3>
                        <strong>Você está aqui:</strong>

                        <ul>
                            <li><a href="${rootPath}">Início</a></li>
                            <li><a href="${rootPath}Agent" class="active">Agent CockPit</a></li>
                            <li class="active last"><a href="${rootPath}Agent/CallList" class="active">Lista de Chamadas</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/Agent/CallList"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>LISTA DE CHAMADAS</h2>
                        <form method="POST" action="CallList">
                            <span id="errorLabel" class="error">
                                <c:if test="${!empty errorMessage}">
                                    ${errorMessage}
                                </c:if>
                            </span>
                            <table id="callList">
                                <thead>
                                    <tr>
                                        <th style="width:13%;">Número</th>
                                        <th style="width:13%;">Discagem</th>
                                        <th style="width:13%;">Atendimento</th>
                                        <th style="width:13%;">Fim</th>
                                        <th style="width:17%;">Status</th>
                                        <th style="width:17%;">Tempo</th>
                                        <th style="width:14%;">Ação</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
            <div id="BackgroundWork" style="position: absolute; text-align: center; vertical-align: middle; background: red; width: 100px; height: 20px; line-height: 20px; right: 1px; top: 1px; font-weight: bold; color: white;">Carregando...</div>
            <!-- FOOTER -->
            <c:import url="/Controls/FooterControl"></c:import>

        </div>
    </body>
</html>
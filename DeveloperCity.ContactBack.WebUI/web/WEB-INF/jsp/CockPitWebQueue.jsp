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
        <link href="/CAC/resources/CSS/datepicker/ui.all.css" rel="stylesheet" type="text/css" />
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/jquery.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/ui.core.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/jquery.metadata.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/jquery.blockui.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/main.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/jquery-ui-1.7.2.custom.min.js"></script>
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/ui.datepicker-pt-BR.js"></script>
        <!--[if IE 6]>
            <script type="text/javascript" src="/CAC/resources/SCRIPT/DD_belatedPNG_0.0.7a.js"></script>
            <link href="/CAC/resources/CSS/main-ie6.css" rel="stylesheet" type="text/css" />
        <![endif]-->
        <!--[if IE 7]>
            <link href="/CAC/resources/CSS/main-ie7.css" rel="stylesheet" type="text/css" />
        <![endif]-->
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/CockPitWebQueue.js"></script>
        <script language="javascript" type="text/javascript">
            var rootPath = '';
            var sessionUser = '${sessionUser}';
            var sessionMobilePhone = '${sessionMobilePhone}';
            jQuery(document).ready(function() {
                rootPath = '${rootPath}';
                loadWebQueue();
            });
        </script>
    </head>
    <body>
        <div class="outer">
            <div class="limit">

                <c:import url="/Controls/HeaderControl/CockPit"></c:import>
                <div class="inside two-column-left">
                    <div class="breadcrumb">
                        <h3 class="fixpng">Menu</h3>
                        <strong>Você está aqui:</strong>
                        <ul>
                            <li><a href="${rootPath}">Início</a></li>
                            <li><a href="${rootPath}CockPit" class="active">CockPit</a></li>
                            <li class="active last"><a href="${rootPath}CockPit/WebQueue" class="active">Agendamento Web</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/CockPit/WebQueue"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>Agendamento Web</h2>

                        <fieldset class="buttons">
                            <legend></legend>
                            <ul>
                                <li id="btnAddQueue"><a href="javascript:addQueue();">Incluir Agendamento</a></li>
                                <li id="btnSaveQueue"><a href="javascript:saveQueue();">Salvar Agendamento</a></li>
                                <li id="btnCancelQueue"><a href="javascript:cancelQueue();">Cancelar Inclusão</a></li>
                            </ul>
                        </fieldset>
                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>

                        <form id="formWebQueue" action="">
                            <fieldset class="formList">
                                <legend>Dados para Agendamento</legend>
                                <ul>
                                    <li><label>Usuário:</label><input id="txtUsername" maxlength="100" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Número de Retorno:</label><input id="txtCallbackNumber" maxlength="10" type="text" readonly="readonly" disabled="disabled" /></li>
                                </ul>
                            </fieldset>
                        </form>
                        <ul id="scheduleDetails">
                            <li>Agendado por: <span id="lblUsername"></span></li>
                            <li>Agendado em: <span id="lblScheduleTime"></span></li>
                            <li>Número de retorno: <span id="lblCallBackNumber"></span></li>
                            <li>Posição de ingresso: <span id="lblEntryPosition"></span></li>
                            <li>Previsão para retorno: <span id="lblEstimatedTimeToAttend"></span></li>
                            <li>Tentativas de retorno realizadas: <span id="lblAttendCount"></span></li>
                            <li>Status atual: <span id="lblCurrentStatus"></span></li>
                            <li>Congelado até: <span id="lblDontCallBefore"></span></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="BackgroundWork" style="position: absolute; text-align: center; vertical-align: middle; background: red; width: 100px; height: 20px; line-height: 20px; right: 1px; top: 1px; font-weight: bold; color: white;">Carregando...</div>
            <!-- FOOTER -->

            <c:import url="/Controls/FooterControl"></c:import>

        </div>
    </body>
</html>
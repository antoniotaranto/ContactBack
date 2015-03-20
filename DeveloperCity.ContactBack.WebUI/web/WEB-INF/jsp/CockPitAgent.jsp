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

        <script language="javascript" type="text/javascript">
            var itemDetails = 0;
            function backgroundWorkStart() {
                jQuery("#BackgroundWork").show();
            }
            function backgroundWorkEnd() {
                jQuery("#BackgroundWork").hide();
            }
            jQuery(document).ready(function() {
                defineEscPress();
                doLoop();
            });
            function doLoop() {
                if (itemDetails == 0) {
                    refreshAgent();
                } else {
                    agentItemSelected(itemDetails);
                }
                t=setTimeout("doLoop()",15000);
            }
            function openItemDetails(agentID) {
                jQuery("#agentItemDetails").html("");
                agentItemSelected(agentID);
                itemDetails = agentID;
                jQuery.blockUI({
                    message: jQuery('#agentItemDetails'),
                    css: {
                        top:  (jQuery(window).height() - 350) /2 + 'px',
                        left: (jQuery(window).width() - 500) /2 + 'px',
                        width: '500px',
                        height: '350px'
                    }
                });
            }
            function closeItemDetails() {
                refreshAgent();
                jQuery.unblockUI();
                itemDetails = 0;
            }
            function agentItemSelected(agentID) {
                backgroundWorkStart();
                jQuery.ajax({
                    url: '${rootPath}CockPit/Agent/' + agentID,
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            var birthday = data.birthday == null ? null : new Date(data.birthday);

                            jQuery("#agentItemDetails").html("");
                            jQuery("#agentItemDetails").append("<a id=\"closeAgentItemDetails\" href=\"javascript:closeItemDetails();\">Fechar [ X ]</a>");

                            jQuery("#agentItemDetails").append("<h3>Detalhes</h3>");
                            jQuery("#agentItemDetails").append("<ul id='agentItemBasic'></ul>");
                            jQuery("#agentItemBasic").append("<li><label>Nome:</label><input type=\"text\" readonly=\"readonly\" value=\"" + data.name + "\"></input></li>");
                            jQuery("#agentItemBasic").append("<li><label>Ramal:</label><input type=\"text\" readonly=\"readonly\" value=\"" + data.directoryNumber + "\"></input></li>");
                            jQuery("#agentItemBasic").append("<li><label>Terminal:</label><input type=\"text\" readonly=\"readonly\" value=\"" + data.terminal + "\"></input></li>");
                            jQuery("#agentItemBasic").append("<li><label>Nascimento:</label><input type=\"text\" readonly=\"readonly\" value=\"" + (birthday == null ? "" : (birthday.getDate() + "/" + (birthday.getMonth() + 1) + "/" + birthday.getFullYear()    )) + "\"></input></li>");
                            jQuery("#agentItemBasic").append("<li><label>E-mail:</label><input type=\"text\" readonly=\"readonly\" value=\"" + data.email + "\"></input></li>");
                            jQuery("#agentItemBasic").append("<li><label>Usuário:</label><input type=\"text\" readonly=\"readonly\" value=\"" + data.username + "\"></input></li>");

                            jQuery("#agentItemDetails").append("<h3>Último registro de trabalho</h3>");
                            jQuery("#agentItemDetails").append("<table id='agentItemHistory'></table>");

                            var lastWorkTime = data.workTimes[0];
                            var workTimeLogin = lastWorkTime.loginTime == null ? null : new Date(lastWorkTime.loginTime);
                            var workTimeLogout = lastWorkTime.logoutTime == null ? null : new Date(lastWorkTime.logoutTime);
                            jQuery("#agentItemHistory").append("<thead><tr><th colspan=\"3\">Login entre " + (workTimeLogin == null ? "" : (workTimeLogin.getDate() + "/" + (workTimeLogin.getMonth() + 1) + "/" + workTimeLogin.getFullYear() + " " + workTimeLogin.toLocaleTimeString())) + " e " + (workTimeLogout == null ? "agora" : (workTimeLogout.getDate() + "/" + (workTimeLogout.getMonth() + 1) + "/" + workTimeLogout.getFullYear() + " " + workTimeLogout.toLocaleTimeString())) + "</th></tr><tr style=\"width: 100%;\"><th style=\"width: auto;\">Pausa</th><th style=\"width: 50px;\">Início</th><th style=\"width: 50px;\">Fim</th></tr></thead>");

                            for (var i = 0; i < lastWorkTime.breaks.length; i++) {
                                var currentBreak = lastWorkTime.breaks[i];
                                var currentBreakStart = currentBreak.breakStart == null ? null : new Date(currentBreak.breakStart);
                                var currentBreakEnd = currentBreak.breakEnd == null ? null : new Date(currentBreak.breakEnd);
                                var currentBreakDescription = currentBreak.breakType.description;

                                var newHistoryLine = "<tr>";
                                newHistoryLine += "   <td style=\"text-align: left;\">" + currentBreakDescription + "</td>";
                                newHistoryLine += "   <td>" + (currentBreakStart == null ? "" : (currentBreakStart.getDate() + "/" + (currentBreakStart.getMonth() + 1) + "/" + currentBreakStart.getFullYear() + " " + currentBreakStart.toLocaleTimeString())) + "</td>";
                                newHistoryLine += "   <td>" + (currentBreakEnd == null ? "agora" : (currentBreakEnd.getDate() + "/" + (currentBreakEnd.getMonth() + 1) + "/" + currentBreakEnd.getFullYear() + " " + currentBreakEnd.toLocaleTimeString())) + "</td>";
                                newHistoryLine += "</tr>";
                                jQuery("#agentItemHistory").append(newHistoryLine);
                            }
                        }
                    }
                });
                backgroundWorkEnd();
            }
            function refreshAgent() {
                backgroundWorkStart();
                jQuery.ajax({
                    url: '${rootPath}CockPit/Agent/List',
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            jQuery("#agentContainer").html("");
                            jQuery("#agentContainer").append("<table id='agentTable'></table>");

                            var header = "<thead>";
                            header += "   <tr>";
                            header += "      <th>Agente</th>";
                            header += "      <th>Status</th>";
                            header += "      <th>Chamadas</th>";
                            header += "      <th>Última chamada</th>";
                            header += "   </tr>";
                            header += "</thead>";

                            jQuery("#agentTable").append(header);

                            var footer = "<tfoot>";
                            footer += "   <tr>";
                            footer += "      <td colspan='4'>Agentes: " + data.length + "</td>";
                            footer += "   </tr>";
                            footer += "</tfoot>";

                            jQuery("#agentTable").append(footer);

                            for (var i = 0; i < data.length; i++) {
                                var countCalls = ((data[i].callManagerCallIDs == null) ? 0 : data[i].callManagerCallIDs.length);
                                var styleRow = "offline";
                                var dLastCallTime = data[i].lastCallTime != null ? new Date(data[i].lastCallTime) : null;
                                var statusAgent = "";
                                if (data[i].agentStatus == "NotLogged") {
                                    statusAgent = "Deslogado";
                                    styleRow = "offline";
                                } else if (data[i].agentStatus == "Break" || data[i].agentStatus == "NotReady") {
                                    statusAgent = "Pausa";
                                    styleRow = "break";
                                } else if (countCalls > 0) {
                                    statusAgent = "Em ligação";
                                    styleRow = "talking";
                                } else {
                                    statusAgent = "Disponível";
                                    styleRow = "waiting";
                                }

                                if (data[i].requestingBreak != 0) { statusAgent += " (solicitando pausa)"; }
                                else if (data[i].requestingLogoff) { statusAgent += " (solicitando logoff)"; }
                                var listItem = "<tr class='" + styleRow + "' >";
                                listItem += "   <td><a href=\"javascript:openItemDetails(" + data[i].userID + ");\">" + (data[i].name) + "</a></td>";
                                listItem += "   <td>" + statusAgent + "</td>";
                                listItem += "   <td>" + countCalls + "</td>";
                                listItem += "   <td>" + (dLastCallTime != null ? dLastCallTime.getDate() + "/" + (dLastCallTime.getMonth() + 1) + "/" + dLastCallTime.getFullYear() + " " + dLastCallTime.toLocaleTimeString() : "") + "</td>";
                                listItem += "</tr>";
                                jQuery("#agentTable").append(listItem);
                            }
                        }
                        var now = new Date();
                        jQuery("#lastUpdate").text( now.getDate() + "/" + (now.getMonth() + 1) + "/" + now.getFullYear() + " " + now.toLocaleTimeString() );
                    }
                });
                backgroundWorkEnd();
            }
            function defineEscPress() {
                if (jQuery.browser.mozilla) {
                    jQuery(document).keypress(onKeyPress);
                } else {
                    jQuery(document).keydown(onKeyPress);
                }
            }
            function onKeyPress(e) {
                if (e.keyCode == '27') {
                    e.preventDefault();
                    if (itemDetails != 0) {
                        closeItemDetails();
                    }
                }
            }
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
                            <li class="active last"><a href="${rootPath}CockPit/Agent" class="active">Estado de Agentes</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/CockPit/Agent"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>ESTADO DE AGENTES</h2>

                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>

                        <div id="agentContainer">

                        </div>
                        <p id="lastUpdate" style="text-align: right;"></p>

                        <div id="agentItemDetails" style="display:none; cursor: default">

                        </div>
                    </div>
                </div>
            </div>
            <div id="BackgroundWork" style="position: absolute; text-align: center; vertical-align: middle; background: red; width: 100px; height: 20px; line-height: 20px; right: 1px; top: 1px; font-weight: bold; color: white;">Carregando...</div>
            <!-- FOOTER -->

            <c:import url="/Controls/FooterControl"></c:import>

        </div>
    </body>
</html>
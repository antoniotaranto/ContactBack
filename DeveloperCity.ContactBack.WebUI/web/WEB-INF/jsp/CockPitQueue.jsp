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
                    refreshQueue();
                } else {
                    queueItemSelected(itemDetails);
                }
                t=setTimeout("doLoop()",15000);
            }
            function openItemDetails(queueID) {
                jQuery("#queueItemDetails").html("");
                queueItemSelected(queueID);
                itemDetails = queueID;
                jQuery.blockUI({
                    message: jQuery('#queueItemDetails'),
                    css: {
                        top:  (jQuery(window).height() - 350) /2 + 'px',
                        left: (jQuery(window).width() - 500) /2 + 'px',
                        width: '500px',
                        height: '350px'
                    }
                });
            }
            function closeItemDetails() {
                refreshQueue();
                jQuery.unblockUI();
                itemDetails = 0;
            }
            function queueItemSelected(queueID) {
                backgroundWorkStart();
                jQuery.ajax({
                    url: '${rootPath}CockPit/Queue/' + queueID,
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            var callTime = data.callTime == null ? null : new Date(data.callTime);
                            var dontCallBefore = data.dontCallBefore == null ? null : new Date(data.dontCallBefore);
                            var scheduleTime = data.scheduleTime == null ? null : new Date(data.scheduleTime);
                            var attendCallID = (data.attendCall == null || data.attendCall.callID == null) ? 0 : data.attendCall.callID;
                            var binaNumber = data.binaNumber.toString().trim();
                            if (binaNumber.length == 10) {
                                binaNumber = "(" + binaNumber.substring(0, 2) + ") " + binaNumber.substring(2, 6) + "-" + binaNumber.substring(6, 10);
                            } else if (binaNumber.length == 10) {
                                binaNumber = "(" + binaNumber.substring(0, 2) + ") " + binaNumber.substring(2, 7) + "-" + binaNumber.substring(7, 11);
                            }
                            
                            var callBackNumber = data.callBackNumber.toString();
                            if (callBackNumber.length == 10) {
                                callBackNumber = "(" + callBackNumber.substring(0, 2) + ") " + callBackNumber.substring(2, 6) + "-" + callBackNumber.substring(6, 10);
                            } else if (callBackNumber.length == 11) {
                                callBackNumber = "(" + callBackNumber.substring(0, 2) + ") " + callBackNumber.substring(2, 7) + "-" + callBackNumber.substring(7, 11);
                            }


                            jQuery("#queueItemDetails").html("");
                            jQuery("#queueItemDetails").append("<a id=\"closeQueueItemDetails\" href=\"javascript:closeItemDetails();\">Fechar [ X ]</a>");

                            jQuery("#queueItemDetails").append("<h3>Detalhes</h3>");
                            jQuery("#queueItemDetails").append("<ul id='queueItemBasic'></ul>");
                            jQuery("#queueItemBasic").append("<li><label>Número do Bina:</label><input type=\"text\" readonly=\"readonly\" value=\"" + binaNumber + "\"></input></li>");
                            jQuery("#queueItemBasic").append("<li><label>Número para retorno:</label><input type=\"text\" readonly=\"readonly\" value=\"" + callBackNumber + "\"></input></li>");
                            jQuery("#queueItemBasic").append("<li><label>Tentativas:</label><input type=\"text\" readonly=\"readonly\" value=\"" + data.attendCount + "\"></input></li>");
                            jQuery("#queueItemBasic").append("<li><label>Horário da chamada:</label><input type=\"text\" readonly=\"readonly\" value=\"" + (callTime == null ? "" : (callTime.getDate() + "/" + (callTime.getMonth() + 1) + "/" + callTime.getFullYear() + " " + callTime.toLocaleTimeString())) + "\"></input></li>");
                            jQuery("#queueItemBasic").append("<li><label>Não retornar antes de:</label><input type=\"text\" readonly=\"readonly\" value=\"" + (dontCallBefore == null ? "" : (dontCallBefore.getDate() + "/" + (dontCallBefore.getMonth() + 1) + "/" + dontCallBefore.getFullYear() + " " + dontCallBefore.toLocaleTimeString())) + (data.frozen ? " (congelado)": "") + "\"></input></li>");
                            jQuery("#queueItemBasic").append("<li><label>Horário do agendamento:</label><input type=\"text\" readonly=\"readonly\" value=\"" + (scheduleTime == null ? "" : (scheduleTime.getDate() + "/" + (scheduleTime.getMonth() + 1) + "/" + scheduleTime.getFullYear() + " " + scheduleTime.toLocaleTimeString())) + "\"></input></li>");
                            jQuery("#queueItemBasic").append("<li><label>Prioridade atual:</label><input type=\"text\" readonly=\"readonly\" value=\"" + (data.priorityValue > 0 ? ("+" + data.priorityValue) : data.priorityValue) + "\"></input></li>");

                            jQuery("#queueItemDetails").append("<h3>Ligações do cliente ao 0800</h3>");
                            jQuery("#queueItemDetails").append("<table id='queueItemHistory'></table>");
                            jQuery("#queueItemHistory").append("<thead><tr><th>Horário</th><th>Resultado</th></tr></thead>");
                            for (var i = 0; i < data.history.length; i++) {
                                var historyDate = data.history[i].callTime == null ? null : new Date(data.history[i].callTime);
                                var historyQueue = (data.callTime != null && data.callTime == data.history[i].callTime);
                                var newHistoryLine = "<tr class=\"" + (historyQueue ? "validHistory" : "invalidHistory") + "\">";
                                newHistoryLine += "   <td>" + (historyDate == null ? "" : (historyDate.getDate() + "/" + (historyDate.getMonth() + 1) + "/" + historyDate.getFullYear() + " " + historyDate.toLocaleTimeString())) + "</td>";
                                newHistoryLine += "   <td>" + (historyQueue ? "Retorno agendado" : "Insistência") + "</td>";
                                newHistoryLine += "</tr>";
                                jQuery("#queueItemHistory").append(newHistoryLine);
                            }

                            jQuery("#queueItemDetails").append("<h3>Ligações de retorno de agentes</h3>");
                            jQuery("#queueItemDetails").append("<table id='queueItemCallBacks'></table>");
                            jQuery("#queueItemCallBacks").append("<thead><tr><th style=\"width: 115px;\">Início</th><th style=\"width: 80px;\">Atendimento</th><th style=\"width: 80px;\">Finalização</th><th>Agente</th></tr></thead>");
                            for (var i = 0; i < data.calls.length; i++) {
                                var callStartTime = data.calls[i].startTime == null ? null : new Date(data.calls[i].startTime);
                                var callAnswerTime = data.calls[i].answerTime == null ? null : new Date(data.calls[i].answerTime);
                                var callEndTime = data.calls[i].endTime == null ? null : new Date(data.calls[i].endTime);
                                var successCallBack = (attendCallID == data.calls[i].callID);

                                var newCallLine = "<tr class=\"" + (successCallBack ? "validCallBack" : "invalidCallBack") + "\">";
                                newCallLine += "   <td>" + (callStartTime == null ? "" : (callStartTime.getDate() + "/" + (callStartTime.getMonth() + 1) + "/" + callStartTime.getFullYear() + " " + callStartTime.toLocaleTimeString())) + "</td>";
                                newCallLine += "   <td>" + (callAnswerTime == null ? "" : (callAnswerTime.toLocaleTimeString())) + "</td>";
                                newCallLine += "   <td>" + (callEndTime == null ? "" : (callEndTime.toLocaleTimeString())) + "</td>";
                                newCallLine += "   <td>" + data.calls[i].agent.username + "</td>";
                                newCallLine += "</tr>";
                                jQuery("#queueItemCallBacks").append(newCallLine);
                            }
                        }
                    }
                });
                backgroundWorkEnd();
            }
            function refreshQueue() {
                backgroundWorkStart();
                jQuery.ajax({
                    url: '${rootPath}CockPit/Queue/List',
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            jQuery("#queueContainer").html("");
                            jQuery("#queueContainer").append("<table id='queueTable'></table>");

                            var header = "<thead>";
                            header += "   <tr>";
                            header += "      <th style=\"width: 13%;\">Cliente</th>";
                            header += "      <th style=\"width: 19%;\">Contato em</th>";
                            header += "      <th style=\"width: 19%;\">Agendado para</th>";
                            header += "      <th style=\"width: 19%;\">Congelado até</th>";
                            header += "      <th style=\"width: 11%;\">Status</th>";
                            header += "      <th style=\"width: 7%;\">Retorno</th>";
                            header += "      <th style=\"width: 7%;\">Atend.</th>";
                            header += "      <th style=\"width: 5%;\">P</th>";
                            header += "   </tr>";
                            header += "</thead>";

                            jQuery("#queueTable").append(header);

                            var footer = "<tfoot>";
                            footer += "   <tr>";
                            footer += "      <td colspan='8'>Lista de espera: " + data.length + " números</td>";
                            footer += "   </tr>";
                            footer += "</tfoot>";

                            jQuery("#queueTable").append(footer);

                            for (var i = 0; i < data.length; i++) {
                                var dCallTime = data[i].callTime != null ? new Date(data[i].callTime) : null;
                                var dScheduleTime = data[i].scheduleTime != null ? new Date(data[i].scheduleTime) : null;
                                var dDontCallBefore = data[i].dontCallBefore != null ? new Date(data[i].dontCallBefore) : null;
                                var callback = "";
                                var styleRow = "queue";
                                var queueStatus = data[i].queueStatus == "InQueue" ? "Fila" : "";

                                if (data[i].frozen) {
                                        styleRow = "frozen";
                                } else if (data[i].attendCall != null && data[i].attendCall.agent != null) {
                                    var infoCallback = "";
                                    if (data[i].attendCall.answerTime != null) {
                                        infoCallback = "Atendido em " + new Date(data[i].attendCall.answerTime).toLocaleTimeString();
                                        styleRow = "talking";
                                    } else {
                                        infoCallback = "Discando em " + new Date(data[i].attendCall.startTime).toLocaleTimeString();
                                        styleRow = "dialing";
                                    }
                                    callback = data[i].attendCall.agent.username + " (" + infoCallback + ")";
                                }

                                var callBackNumber = data[i].callBackNumber.toString();
                                if (callBackNumber.length == 10) {
                                    callBackNumber = "(" + callBackNumber.substring(0, 2) + ") " + callBackNumber.substring(2, 6) + "-" + callBackNumber.substring(6, 10);
                                } else if (callBackNumber.length == 11) {
                                    callBackNumber = "(" + callBackNumber.substring(0, 2) + ") " + callBackNumber.substring(2, 7) + "-" + callBackNumber.substring(7, 11);
                                }

                                var listItem = "<tr class='" + styleRow + "' >";
                                listItem += "   <td><a href=\"javascript:openItemDetails(" + data[i].queueID + ");\">" + callBackNumber + "</a></td>";
                                listItem += "   <td>" + (dCallTime != null ? dCallTime.getDate() + "/" + (dCallTime.getMonth() + 1) + "/" + dCallTime.getFullYear() + " " + dCallTime.toLocaleTimeString() : "") + "</td>";
                                listItem += "   <td>" + (dScheduleTime != null ? dScheduleTime.getDate() + "/" + (dScheduleTime.getMonth() + 1) + "/" + dScheduleTime.getFullYear() + " " + dScheduleTime.toLocaleTimeString() : "") + "</td>";
                                listItem += "   <td>" + (dDontCallBefore != null ? dDontCallBefore.getDate() + "/" + (dDontCallBefore.getMonth() + 1) + "/" + dDontCallBefore.getFullYear() + " " + dDontCallBefore.toLocaleTimeString() : "") + "</td>";
                                listItem += "   <td class=\"center\">" + queueStatus + "</td>";
                                listItem += "   <td class=\"center\">" + (data[i].attendCount) + "</td>";
                                listItem += "   <td class=\"center\">" + callback + "</td>";
                                listItem += "   <td class=\"center\">" + data[i].priorityValue + "</td>";
                                listItem += "</tr>";
                                jQuery("#queueTable").append(listItem);
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
                            <li class="active last"><a href="${rootPath}CockPit/Queue" class="active">Fila de espera</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/CockPit/Queue"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>FILA DE ESPERA</h2>

                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>

                        <div id="queueContainer">

                        </div>
                        <p id="lastUpdate" style="text-align: right;"></p>

                        <div id="queueItemDetails" style="display:none; cursor: default">

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
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
            var t;
            var working = false;
            var currentStatus = 0;
            var requestingChange = false;
            jQuery(document).ready(function() {
                doLoop();
//		refreshStatus();
            });

            function refreshStatus() {
                if (requestingChange) {
                    refreshData();
                    t=setTimeout("doLoop()",5000);
                } else {
                    refreshData();
//                    var data = jQuery.ajax({
//                        url: 'TimeSheet',
//                        processData: false,
//                        data: 'command=keepAlive',
//                        type: 'POST',
//                        dataType: 'html',
//                        async: false
//                    }).responseXML;
                    t=setTimeout("doLoop()",60000);
                }
            }

            function doLoop() {
                refreshStatus();
            }

            function startBreak(breakTypeID) {
                if (working || requestingChange) {
                    return;
                }
                working = true;
                backgroundWorkStart();
                var data = jQuery.ajax({
                    url: 'TimeSheet',
                    processData: false,
                    data: 'command=startBreak&breakTypeID=' + breakTypeID,
                    type: 'POST',
                    dataType: 'html',
                    async: false
                }).responseXML;
                refreshData();
                if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
                    jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
                } else {
                    jQuery("#errorLabel").html("");
                }
                backgroundWorkEnd();
                working = false;
            }
            function endBreak(breakID) {
                if (working || requestingChange) {
                    return;
                }
                working = true;
                backgroundWorkStart();
                var data = jQuery.ajax({
                    url: 'TimeSheet',
                    processData: false,
                    data: 'command=endBreak&breakID=' + breakID,
                    type: 'POST',
                    dataType: 'html',
                    async: false
                }).responseXML;
                refreshData();
                if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
                    jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
                } else {
                    jQuery("#errorLabel").html("");
                }
                backgroundWorkEnd();
                working = false;
            }
            function startSession() {
                if (working || requestingChange) {
                    return;
                }
                working = true;
                backgroundWorkStart();
                var data = jQuery.ajax({
                    url: 'TimeSheet',
                    processData: false,
                    data: 'command=startSession',
                    type: 'POST',
                    dataType: 'xml',
                    async: false
                }).responseXML;
                refreshData();
                if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
                    jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
                } else {
                    jQuery("#errorLabel").html("");
                }
                backgroundWorkEnd();
                working = false;
            }
            function restartSession(breakTypeID) {
                if (working || requestingChange) {
                    return;
                }
                working = true;
                backgroundWorkStart();
                var data = jQuery.ajax({
                    url: 'TimeSheet',
                    processData: false,
                    data: 'command=restartSession&breakTypeID=' + breakTypeID,
                    type: 'POST',
                    dataType: 'xml',
                    async: false
                }).responseXML;
                refreshData();
                if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
                    jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
                } else {
                    jQuery("#errorLabel").html("");
                }
                backgroundWorkEnd();
                working = false;
            }
            function endSession() {
                if (working || requestingChange) {
                    return;
                }
                if (!confirm('Você tem certeza que deseja encerrar o atendimento por hoje?'))
                    return;
                working = true;
                backgroundWorkStart();
                var data = jQuery.ajax({
                    url: 'TimeSheet',
                    processData: false,
                    data: 'command=endSession',
                    type: 'POST',
                    dataType: 'html',
                    async: false
                }).responseXML;
                refreshData();
                if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
                    jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
                } else {
                    jQuery("#errorLabel").html("");
                }
                working = false;
            }
            var tableComplete = false;
            var menuComplete = false;
            function refreshData() {
                tableComplete = false;
                menuComplete = false;
                backgroundWorkStart();
                jQuery("#errorLabel").html("");
                refreshTable();
                refreshMenu();
            }
            function refreshTable() {
                jQuery.get('${rootPath}Controls/AgentBreakTableControl', null, function(tableControl) {
                    jQuery('#agentBreakContainer').children().remove();
                    jQuery('#agentBreakContainer').append(tableControl);
                    tableComplete = true;
                    if (tableComplete && menuComplete) {
                        backgroundWorkEnd();
                    }
                });
            }
            function refreshMenu() {
                jQuery.get('${rootPath}Controls/AgentTimeSheetMenuControl', null, function(menuControl, textStatus) {
                    jQuery('#breakMenuContainer').children().remove();
                    jQuery('#breakMenuContainer').append(menuControl);
                    menuComplete = true;
                    if (tableComplete && menuComplete) {
                        backgroundWorkEnd();
                    }
                    if (jQuery("#lblAgentStatus").text().indexOf("(") > 0) {
                        requestingChange = true;
                    } else {
                        requestingChange = false;
                    }
                });
            }
            function backgroundWorkStart() {
                jQuery("#BackgroundWork").show();
            }
            function backgroundWorkEnd() {
                jQuery("#BackgroundWork").hide();
            }
        </script>
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
                            <li class="active last"><a href="${rootPath}Agent/TimeSheet" class="active">Sessão e Pausas</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/Agent/TimeSheet"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>SESSÃO E PAUSAS</h2>
                        <form method="POST" action="TimeSheet">
                            <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                            </span>

                            <div id ="breakMenuContainer"></div>
                                    
                            <div id="agentBreakContainer"></div>
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
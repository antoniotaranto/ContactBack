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
        <script type="text/javascript" src="/CAC/resources/SCRIPT/swfobject.js"></script>
        <!--[if IE 6]>
            <script type="text/javascript" src="/CAC/resources/SCRIPT/DD_belatedPNG_0.0.7a.js"></script>
            <link href="/CAC/resources/CSS/main-ie6.css" rel="stylesheet" type="text/css" />
        <![endif]-->
        <!--[if IE 7]>
            <link href="/CAC/resources/CSS/main-ie7.css" rel="stylesheet" type="text/css" />
        <![endif]-->

        <script language="javascript" type="text/javascript">
            var t;
            var currentStatus = 0;
            var showingChart = true;

            jQuery(document).ready(function() {
                noChartSelected();
                defineEscPress();
            });

            function noChartSelected() {
                jQuery("#errorLabel").html("");
                backgroundWorkEnd();
            }
            function chartSelected(chartName) {
                backgroundWorkStart();
                jQuery.get('${rootPath}Controls/' + chartName + 'Control', null, function(responseHTML) {
                    jQuery('#filterChart').children().remove();
                    jQuery('#filterChart').append(responseHTML);
                    backgroundWorkEnd();
                });
                jQuery("#errorLabel").html("");
            }
            function filterComplete(chartName, parameters){
                backgroundWorkStart();
                showingChart = true;

                swfobject.embedSWF(
                    "/CAC/resources/SCRIPT/open-flash-chart.swf",
                    "theChart",
                    jQuery(window).width() - 106,
                    jQuery(window).height() - 86,
                    "9.0.0",
                    "expressInstall.swf",
                    { "data-file": "${rootPath}Controls/" + chartName + "/JSON?" + parameters }
                );
                jQuery.blockUI({
                    message: jQuery('#theChart'),
                    css: {
                        top:  40 + 'px',
                        left: 50 + 'px',
                        width: jQuery(window).width() - 100,
                        height: jQuery(window).height() - 80
                    }
                });
                backgroundWorkEnd();
                jQuery("#errorLabel").html("");
            }
            function closeChart() {
                jQuery.unblockUI();
                jQuery("#theChart").hide();
                jQuery("#theChart").attr("visibility", "none");
                showingChart = false;
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
                    if (showingChart) {
                        closeChart();
                    }
                }
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

                <c:import url="/Controls/HeaderControl/CockPit"></c:import>

                <div class="inside two-column-left">
                    <div class="breadcrumb">
                        <h3 class="fixpng">Menu</h3>
                        <strong>Você está aqui:</strong>

                        <ul>
                            <li><a href="${rootPath}">Início</a></li>
                            <li><a href="${rootPath}CockPit" class="active">CockPit</a></li>
                            <li class="active last"><a href="${rootPath}CockPit/Chart" class="active">Gráficos</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/CockPit/Chart"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>GRÁFICOS</h2>
                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>
                        <ul id="ReportList">

                        <c:forEach var="chartPermission" items="${charts}">
                            <li><a href="javascript:chartSelected('${chartPermission.chart.chartFile}')">${chartPermission.chart.chartDescription}</a></li>
                        </c:forEach>

                        </ul>

                        <div id="filterChart">
                        </div>

                        <div id="theChart" style="display:none; cursor: default">
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
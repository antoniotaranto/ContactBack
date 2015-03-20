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
                doLoop();
            });
            function doLoop() {
                refreshLogs();
                t=setTimeout("doLoop()",60000);
            }
            function refreshLogs() {
                backgroundWorkStart();
                jQuery.ajax({
                    url: '${rootPath}BackOffice/Logs/List',
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            jQuery("#logContainer").html("");
                            jQuery("#logContainer").append("<table id='logTable'></table>");

                            var header = "<thead>";
                            header += "   <tr>";
                            header += "      <th>Arquivo</th>";
                            header += "      <th>Tamanho</th>";
                            header += "      <th>Data</th>";
                            header += "   </tr>";
                            header += "</thead>";

                            jQuery("#logTable").append(header);

                            var footer = "<tfoot>";
                            footer += "   <tr>";
                            footer += "      <td colspan='3'>Arquivos: " + data.length + "</td>";
                            footer += "   </tr>";
                            footer += "</tfoot>";

                            jQuery("#logTable").append(footer);

                            for (var i = 0; i < data.length; i++) {
                                var dLastModified = data[i].lastModified != null ? new Date(data[i].lastModified) : null;
                                var listItem = "<tr>";
                                listItem += "   <td><a href=\"${rootPath}BackOffice/Logs/Get/" + data[i].fileName + "\">" + (data[i].fileName) + "</a></td>";
                                listItem += "   <td>" + (data[i].size / (1024 * 1024)).toFixed(3) + " MB</td>";
                                listItem += "   <td>" + (dLastModified != null ? dLastModified.getDate() + "/" + (dLastModified.getMonth() + 1) + "/" + dLastModified.getFullYear() + " " + dLastModified.toLocaleTimeString() : "") + "</td>";
                                listItem += "</tr>";
                                jQuery("#logTable").append(listItem);
                            }
                        }
                        var now = new Date();
                        jQuery("#lastUpdate").text( now.getDate() + "/" + (now.getMonth() + 1) + "/" + now.getFullYear() + " " + now.toLocaleTimeString() );
                    }
                });
                backgroundWorkEnd();
            }
        </script>
    </head>
    <body>
        <div class="outer">
            <div class="limit">

                <c:import url="/Controls/HeaderControl/BackOffice"></c:import>
                <div class="inside two-column-left">
                    <div class="breadcrumb">
                        <h3 class="fixpng">Menu</h3>
                        <strong>Você está aqui:</strong>

                        <ul>
                            <li><a href="${rootPath}">Início</a></li>
                            <li><a href="${rootPath}BackOffice">BackOffice</a></li>
                            <li class="active last"><a href="${rootPath}BackOffice/Logs" class="active">Logs</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/BackOffice/Logs"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>LOGS</h2>
                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>
                        <div id="logContainer">

                        </div>
                        <p id="lastUpdate" style="text-align: right;"></p>
                    </div>
                </div>
            </div>
            <div id="BackgroundWork" style="position: absolute; text-align: center; vertical-align: middle; background: red; width: 100px; height: 20px; line-height: 20px; right: 1px; top: 1px; font-weight: bold; color: white;">Carregando...</div>
            <!-- FOOTER -->

            <c:import url="/Controls/FooterControl"></c:import>

        </div>
    </body>
</html>
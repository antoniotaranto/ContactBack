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
        <style type="text/css">
            fieldset.formList ul li input { margin-left: 20px; width: 550px; }
            div#Ping { width: 60px; height: 16px; display: inline-block; border: 1px dotted #dde; margin-left: 20px; }
        </style>
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
            function startApplication() {
                jQuery.ajax({
                    url: '${rootPath}BackOffice/Diagnostics/Start',
                    type: 'POST',
                    dataType: 'text',
                    success: function(data, status) {
                        alert(data);
                    }
                });
            }
            function stopApplication() {
                jQuery.ajax({
                    url: '${rootPath}BackOffice/Diagnostics/Stop',
                    type: 'POST',
                    dataType: 'text',
                    success: function(data, status) {
                        alert(data);
                    }
                });
            }
            jQuery(document).ready(function() {
                doLoop();
            });
            function doLoop() {
                refreshDiagnostics();
                t=setTimeout("doLoop()",15000);
            }
            function refreshDiagnostics() {
                backgroundWorkStart();
                var now = new Date();
                jQuery("#lastUpdate").text( now.getDate() + "/" + (now.getMonth() + 1) + "/" + now.getFullYear() + " " + now.toLocaleTimeString() );
                jQuery.ajax({
                    url: '${rootPath}BackOffice/Diagnostics/IsAlive',
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            if (data.isAlive) {
                                jQuery("#Ping").css("background-color", "green");
                            } else {
                                jQuery("#Ping").css("background-color", "red");
                            }
                        } else {
                            jQuery("#Ping").css("background-color", "red");
                        }
                    },
                    error: function() {
                        jQuery("#Ping").css("background-color", "red")
                    }
                });
                jQuery.ajax({
                    url: '${rootPath}BackOffice/Diagnostics',
                    type: 'POST',
                    dataType: 'json',
                    error: function() {
                            jQuery("#txtApplication").val("");
                            jQuery("#txtBuild").val("");
                            jQuery("#txtStartTime").val("");
                            jQuery("#txtUser").val("");
                            jQuery("#txtCurrentDirectory").val("");
                            jQuery("#txtOS").val("");
                            jQuery("#txtCurrentTimeZone").val("");
                            jQuery("#txtCurrentOffset").val("");
                            jQuery("#txtDaylightSavingsOffset").val("");
                            jQuery("#txtJava").val("");
                            jQuery("#txtAppMemoryHeap").val("");
                            jQuery("#txtAppMemoryNonHeap").val("");
                            jQuery("#txtVMMemory").val("");
                            jQuery("#txtProcessMemory").val("");
                            jQuery("#txtObjectPendingFinalizationCount").val("");
                            jQuery("#txtProcessors").val("");
                    },
                    success: function(data, status) {
                        if (status == 'success' && data != null && data.startTime != null) {
                            var dStartTime = data.startTime != null ? new Date(data.startTime) : null;
                            var tStartTime = dStartTime == null ? null : dStartTime.getDate() + "/" + (dStartTime.getMonth() + 1) + "/" + dStartTime.getFullYear() + " " + dStartTime.toLocaleTimeString();
                            var heapMax = (data.appMemoryHeapMax / (1024 * 1024 )).toFixed(2);
                            var heapInit = (data.appMemoryHeapInit / (1024 * 1024 )).toFixed(2);
                            var heapCommited = (data.appMemoryHeapCommitted  / (1024 * 1024 )).toFixed(2);
                            var heapUsed = (data.appMemoryHeapUsed / (1024 * 1024 )).toFixed(2);
                            var nonHeapMax = (data.appMemoryNonHeapMax / (1024 * 1024 )).toFixed(2);
                            var nonHeapInit = (data.appMemoryNonHeapInit / (1024 * 1024 )).toFixed(2);
                            var nonHeapCommited = (data.appMemoryNonHeapCommitted / (1024 * 1024 )).toFixed(2);
                            var nonHeapUsed = (data.appMemoryNonHeapUsed / (1024 * 1024 )).toFixed(2);
                            var vmFree = (data.VMMemoryFree / (1024 * 1024)).toFixed(2);
                            var vmMax = (data.VMMemoryMax / (1024 * 1024)).toFixed(2);
                            var vmTotal = (data.VMMemoryTotal / (1024 * 1024)).toFixed(2);
                            var vmUsed = ( (data.VMMemoryTotal - data.VMMemoryFree) / (1024 * 1024)).toFixed(2);
                            var processUsed = ((data.appMemoryHeapMax + data.appMemoryNonHeapCommitted) / (1024 * 1024) ).toFixed(2);
                            var processFree = ((data.appMemoryHeapCommitted - data.appMemoryHeapUsed + data.appMemoryNonHeapCommitted - data.appMemoryNonHeapUsed) / (1024 * 1024) ).toFixed(2);
                            var processCommited = ( (data.appMemoryHeapCommitted + data.appMemoryNonHeapCommitted) / (1024 * 1024) ).toFixed(2);
                            var processMax = ( (data.appMemoryHeapMax + data.appMemoryNonHeapMax) / (1024 * 1024) ).toFixed(2);
                            jQuery("#txtApplication").val(data.implementationVendor + " " + data.implementationTitle + " " + data.implementationVersion);
                            jQuery("#txtBuild").val(data.builtDate + " por " + data.builtBy);
                            jQuery("#txtStartTime").val(tStartTime);
                            jQuery("#txtUser").val(data.userName + " (" + data.userHome + ")");
                            jQuery("#txtCurrentDirectory").val(data.currentDirectory);
                            jQuery("#txtOS").val(data.OSName + " (" + data.OSArch + ") " + data.OSVersion);
                            jQuery("#txtCurrentTimeZone").val(data.currentTimeZone);
                            jQuery("#txtCurrentOffset").val( (data.currentOffset / (1000 * 60 * 60)).toFixed(1) + " hora(s)");
                            jQuery("#txtDaylightSavingsOffset").val( (data.daylightSavingsOffset / (1000 * 60 * 60)).toFixed(1) + " hora(s)");
                            jQuery("#txtJava").val(data.javaVersion + " por " + data.javaVendor + " (" + data.javaHome + ")");
                            jQuery("#txtAppMemoryHeap").val("Usada: " + heapUsed + "MB  \t\tInicial: " + heapInit + "MB  \t\tAlocada: " + heapCommited + "MB\t\tMáxima: " + heapMax + "MB");
                            jQuery("#txtAppMemoryNonHeap").val("Usada: " + nonHeapUsed + "MB  \t\tInicial: " + nonHeapInit + "MB  \t\tAlocada: " + nonHeapCommited + "MB\t\tMáxima: " + nonHeapMax + "MB");
                            jQuery("#txtVMMemory").val("Usada: " + vmUsed + "MB  \t\tLivre: " + vmFree + "MB  \t\tAlocada: " + vmTotal + "MB  \t\tMáxima: " + vmMax + "MB");
                            jQuery("#txtProcessMemory").val("Usada: " + processUsed + "MB  \t\tLivre: " + processFree + "MB  \t\tTotal: " + processCommited + "MB  \t\tMáxima: " + processMax + "MB");
                            jQuery("#txtObjectPendingFinalizationCount").val(data.objectPendingFinalizationCount);
                            jQuery("#txtProcessors").val(data.processors);
                        } else {
                            jQuery("#txtApplication").val("");
                            jQuery("#txtBuild").val("");
                            jQuery("#txtStartTime").val("");
                            jQuery("#txtUser").val("");
                            jQuery("#txtCurrentDirectory").val("");
                            jQuery("#txtOS").val("");
                            jQuery("#txtCurrentTimeZone").val("");
                            jQuery("#txtCurrentOffset").val("");
                            jQuery("#txtDaylightSavingsOffset").val("");
                            jQuery("#txtJava").val("");
                            jQuery("#txtAppMemoryHeap").val("");
                            jQuery("#txtAppMemoryNonHeap").val("");
                            jQuery("#txtVMMemory").val("");
                            jQuery("#txtProcessMemory").val("");
                            jQuery("#txtObjectPendingFinalizationCount").val("");
                            jQuery("#txtProcessors").val("");
                        }
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
                            <li class="active last"><a href="${rootPath}BackOffice/Diagnostics" class="active">Diagnósticos</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/BackOffice/Diagnostics"></c:import>

                    <div id="right" class="sidebar last">

                    </div>


                    <div id="diagnostics" class="content">
                        <h2>DIAGNÓSTICOS</h2>
                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>
                        
                        <fieldset class="buttons">
                            <legend></legend>
                            <ul>
                                <li><a href="javascript:startApplication();">Iniciar aplicação</a></li>
                                <li><a href="javascript:stopApplication();">Parar aplicação</a></li>
                            </ul>
                        </fieldset>
                        <fieldset class="formList">
                            <legend><span class="fixpng"><b>Aplicação Telefônica</b></span></legend>
                            <ul>
                                <li>
                                    <label>Status:</label>
                                    <div id="Ping"></div>
                                </li>
                                <li>
                                    <label>Aplicação:</label>
                                    <input type="text" id="txtApplication" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Compilação:</label>
                                    <input type="text" id="txtBuild" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Execução:</label>
                                    <input type="text" id="txtStartTime" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Usuário de Execução:</label>
                                    <input type="text" id="txtUser" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Diretório de Execução:</label>
                                    <input type="text" id="txtCurrentDirectory" readonly="readonly" value=""></input>
                                </li>
                            </ul>
                        </fieldset>

                        <fieldset class="formList">
                            <legend><span class="fixpng"><b>Aplicação Portal Web</b></span></legend>
                            <ul>
                                <li>
                                    <label>Aplicação:</label>
                                    <input type="text" id="txtWebApplication" readonly="readonly" value="${webApplication}"></input>
                                </li>
                                <li>
                                    <label>Compilação:</label>
                                    <input type="text" id="txtWebBuild" readonly="readonly" value="${webBuild}"></input>
                                </li>
                                <li>
                                    <label>Usuário de Execução:</label>
                                    <input type="text" id="txtWebUser" readonly="readonly" value="${webUser}"></input>
                                </li>
                                <li>
                                    <label>Diretório de Execução:</label>
                                    <input type="text" id="txtWebCurrentDirectory" readonly="readonly" value="${webCurrentDirectory}"></input>
                                </li>
                            </ul>
                        </fieldset>

                        <fieldset class="formList">
                            <legend><span class="fixpng"><b>Ambiente</b></span></legend>
                            <ul>
                                <li>
                                    <label>Sistema Operacional:</label>
                                    <input type="text" id="txtOS" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Região:</label>
                                    <input type="text" id="txtCurrentTimeZone" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Fuso Horário:</label>
                                    <input type="text" id="txtCurrentOffset" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Horário de Verão::</label>
                                    <input type="text" id="txtDaylightSavingsOffset" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Java:</label>
                                    <input type="text" id="txtJava" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Memória da Aplicação (Heap):</label>
                                    <input type="text" id="txtAppMemoryHeap" readonly="readonly" value=""></input>
                                </li>

                                <li>
                                    <label>Memória da Aplicação (Stack):</label>
                                    <input type="text" id="txtAppMemoryNonHeap" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Memória da VM:</label>
                                    <input type="text" id="txtVMMemory" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Memória do Processo:</label>
                                    <input type="text" id="txtProcessMemory" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Objetos Pendentes:</label>
                                    <input type="text" id="txtObjectPendingFinalizationCount" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Processadores:</label>
                                    <input type="text" id="txtProcessors" readonly="readonly" value=""></input>
                                </li>
                            </ul>
                        </fieldset>
                        
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
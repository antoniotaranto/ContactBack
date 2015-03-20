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
            jQuery(document).ready(function() {
                doLoop();
            });
            function doLoop() {
                getRealTimeData();
                t=setTimeout("doLoop()",20000);
            }
            function getRealTimeData() {
                backgroundWorkStart();
                jQuery.ajax({
                    url: '${rootPath}Agent/Stat/RealTime',
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            var birthday = data.birthday == null ? null : new Date(data.birthday);
                            var lastCallTime = data.lastCallTime == null ? null : new Date(data.lastCallTime);

                            jQuery("#txtName").val(data.name);
                            jQuery("#txtBirthday").val((birthday == null ? "" : (birthday.getDate() + "/" + (birthday.getMonth() + 1) + "/" + birthday.getFullYear())));
                            jQuery("#txtLogin").val(data.username);
                            jQuery("#txtDN").val(data.directoryNumber);
                            jQuery("#txtDevice").val(data.terminal);
                            jQuery("#txtEmail").val(data.email);
                            jQuery("#txtLastCall").val((lastCallTime == null ? "" : (lastCallTime.getDate() + "/" + (lastCallTime.getMonth() + 1) + "/" + lastCallTime.getFullYear() + " " + lastCallTime.toLocaleTimeString())));
                            jQuery("#txtStatus").val(data.agentStatusDescription);
                            //jQuery("#txtCurrentCall").val(data.currentCall);
                            jQuery("#txtCalls").val(data.callManagerCallIDs.length);
                            jQuery("#txtRequestingBreak").val(data.requestingBreak > 0 ? "Sim" : "Não");
                            jQuery("#txtRequestingLogout").val(data.requestingLogoff ? "Sim" : "Não");
                        }
                    }
                });
                backgroundWorkEnd();
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
                            <li class="active last"><a href="${rootPath}Agent/Stat" class="active">Estatísticas</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/Agent/Stat"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>ESTATÍSTICAS</h2>
                        <form method="POST" action="TimeSheet">
                            <span id="errorLabel" class="error">
                                <c:if test="${!empty errorMessage}">
                                    ${errorMessage}
                                </c:if>
                            </span>

                            <div id="agents">
                                <fieldset class="formList">
                                    <legend><span class="fixpng"><b>ESTATÍSTICAS DO AGENTE</b></span></legend>
                                    <ul>
                                        <li>
                                            <label>Nome:</label>
                                            <input type="text" id="txtName" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Nascimento:</label>
                                            <input type="text" id="txtBirthday" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Login:</label>
                                            <input type="text" id="txtLogin" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Ramal:</label>
                                            <input type="text" id="txtDN" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Terminal:</label>
                                            <input type="text" id="txtDevice" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>E-mail:</label>
                                            <input type="text" id="txtEmail" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Última chamada:</label>
                                            <input type="text" id="txtLastCall" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Status:</label>
                                            <input type="text" id="txtStatus" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Chamadas:</label>
                                            <input type="text" id="txtCalls" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Solicitando pausa:</label>
                                            <input type="text" id="txtRequestingBreak" readonly="readonly" value=""></input>
                                        </li>
                                        <li>
                                            <label>Solicitando logout:</label>
                                            <input type="text" id="txtRequestingLogout" readonly="readonly" value=""></input>
                                        </li>
                                    </ul>
                                </fieldset>
                            </div>
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
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
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/BackOfficeCallManager.js"></script>
        <script language="javascript" type="text/javascript">
            var rootPath = '';
            jQuery(document).ready(function() {
                rootPath = '${rootPath}';
                jQuery("#btnSaveCallManager").hide();
                jQuery("#btnCancelCallManager").hide();
                loadCallManager();
            });
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
                            <li><a href="${rootPath}BackOffice" class="active">BackOffice</a></li>
                            <li class="active last"><a href="${rootPath}BackOffice/CallManager" class="active">CallManager</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/BackOffice/CallManager"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>CallManager</h2>

                        <fieldset class="buttons">
                            <legend></legend>
                            <ul>
                                <li id="btnEditCallManager"><a href="javascript:startEdit();">Editar CallManager</a></li>
                                <li id="btnSaveCallManager"><a href="javascript:saveEdit();">Salvar CallManager</a></li>
                                <li id="btnCancelCallManager"><a href="javascript:cancelEdit();">Cancelar alterações</a></li>
                            </ul>
                        </fieldset>
                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>

                        <form id="formSetup" action="/">
                            <fieldset id="pbxFields" class="formList">
                                <legend>Telefonia</legend>
                                <ul>
                                    <li><label>IP:</label><input id="txtIP" maxlength="31" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Usuário:</label><input id="txtJtapiUsername" maxlength="20" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Senha:</label><input id="txtJtapiPassword" maxlength="128" type="password" readonly="readonly" disabled="disabled" /></li>
                                </ul>
                            </fieldset>
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
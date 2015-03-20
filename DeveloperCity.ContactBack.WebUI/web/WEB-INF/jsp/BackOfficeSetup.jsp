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
        <script language="javascript" type="text/javascript" src="/CAC/resources/SCRIPT/BackOfficeSetup.js"></script>
        <script language="javascript" type="text/javascript">
            var rootPath = '';
            jQuery(document).ready(function() {
                rootPath = '${rootPath}';
                jQuery("#btnSaveSetup").hide();
                jQuery("#btnCancelSetup").hide();
                loadSetup();
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
                            <li class="active last"><a href="${rootPath}BackOffice/Setup" class="active">Configurações</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/BackOffice/Setup"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>CONFIGURAÇÕES</h2>

                        <fieldset class="buttons">
                            <legend></legend>
                            <ul>
                                <li id="btnEditSetup"><a href="javascript:startEdit();">Editar configurações</a></li>
                                <li id="btnSaveSetup"><a href="javascript:saveEdit();">Salvar configurações</a></li>
                                <li id="btnCancelSetup"><a href="javascript:cancelEdit();">Cancelar alterações</a></li>
                                <li id="btnCommitSetup"><a href="javascript:commitEdit();">Efetivar configurações</a></li>
                            </ul>
                        </fieldset>
                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>

                        <form id="formSetup" action="/">
                            <fieldset id="queueFields" class="formList">
                                <legend>Fila</legend>
                                <ul>
                                    <li><span>Eliminar após </span><input id="txtMaxCallBacks" maxlength="2" type="text" readonly="readonly" disabled="disabled" /><span> tentativas.</span></li>
                                    <li><span>Fim da fila a cada </span><input id="txtEndOfQueue" maxlength="2" type="text" readonly="readonly" disabled="disabled" /><span> tentativas.</span></li>
                                    <li><span>Após ocupado, congelar por </span><input id="txtTimeBetweenCallBacks" maxlength="4" type="text" readonly="readonly" disabled="disabled" /><span> segundos.</span></li>
                                    <li><span>A cada </span><input id="txtLateCallBackAfter" maxlength="2" type="text" readonly="readonly" disabled="disabled" /><span> tentativas, congelar por </span><input id="txtLateCallBackTime" maxlength="4" type="text" readonly="readonly" disabled="disabled" /><span> segundos.</span></li>
                                </ul>
                            </fieldset>
                            <fieldset id="pbxFields" class="formList">
                                <legend>Telefonia</legend>
                                <ul>
                                    <li><label>Dispositivo de entrada:</label><input id="txtCTI_DeviceName" maxlength="30" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Dispositivo de URA:</label><input id="txtIVR_DeviceName" maxlength="30" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Pasta com gravações da URA:</label><input id="txtVoiceFolder" maxlength="255" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>CTI para sucesso no enfileiramento:</label><input id="txtQueueSuccessDevice" maxlength="30" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>CTI para ligação de insistência:</label><input id="txtQueueAlreadyDevice" maxlength="30" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>CTI para fora de expediente:</label><input id="txtQueueNotInShiftTimeDevice" maxlength="30" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>CTI para número inválido:</label><input id="txtQueueInvalidNumberDevice" maxlength="30" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Prefixo para CallBack:</label><input id="txtPrefixDial" maxlength="10" type="text" readonly="readonly" disabled="disabled" /></li>
                                </ul>
                            </fieldset>
                            <fieldset id="shiftFields" class="formList">
                                <legend>Turno</legend>
                                <ul>
                                    <li><label>Início semanal:</label><input id="txtShiftWeekdayStart" maxlength="5" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Encerramento semanal:</label><input id="txtShiftWeekdayEnd" maxlength="5" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Início aos sábados:</label><input id="txtShiftSaturdayStart" maxlength="5" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Encerramento aos sábados:</label><input id="txtShiftSaturdayEnd" maxlength="5" type="text" readonly="readonly" disabled="disabled" /></li>
                                </ul>
                            </fieldset>
                            <fieldset id="workflowFields" class="formList">
                                <legend>Fluxo</legend>
                                <ul>
                                    <li><label>Ramal interno:</label><select id="ddlInternalExtensionDestination" disabled="disabled"><option value="DropCall">Desligar sem enfileirar</option><option value="Enqueue">Enfileirar o número identificado</option><option value="OptionalAnotherPhone">Escolha do número de retorno opcional</option><option value="ForceAnotherPhone">Forçar outro número de retorno</option></select></li>
                                    <li><label>Número de celular:</label><select id="ddlMobilePhoneDestination" disabled="disabled"><option value="DropCall">Desligar sem enfileirar</option><option value="Enqueue">Enfileirar o número identificado</option><option value="OptionalAnotherPhone">Escolha do número de retorno opcional</option><option value="ForceAnotherPhone">Forçar outro número de retorno</option></select></li>
                                    <li><label>Número fixo:</label><select id="ddlLandLineDestination" disabled="disabled"><option value="DropCall">Desligar sem enfileirar</option><option value="Enqueue">Enfileirar o número identificado</option><option value="OptionalAnotherPhone">Escolha do número de retorno opcional</option><option value="ForceAnotherPhone">Forçar outro número de retorno</option></select></li>
                                    <li><label>Número oculto/inválido:</label><select id="ddlInvalidNumberDestination" disabled="disabled"><option value="DropCall">Desligar sem enfileirar</option><option value="ForceAnotherPhone">Forçar outro número de retorno</option></select></li>
                                </ul>
                            </fieldset>
                            <fieldset id="moreFields" class="formList">
                                <legend>Mais</legend>
                                <ul>
                                    <li><label>URL SMS:</label><input id="txtSMSUrl" maxlength="255" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>SMS de exclusão:</label><input id="txtSMSMessage" maxlength="150" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Conta:</label><input id="txtSMSAccount" maxlength="50" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Código:</label><input id="txtSMSCode" maxlength="50" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Remetente:</label><input id="txtSMSFrom" maxlength="15" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Senha padrão de usuários:</label><input id="txtDefaultPassword" maxlength="50" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Proxy IP:</label><input id="txtProxyIP" maxlength="15" type="text" readonly="readonly" disabled="disabled" /></li>
                                    <li><label>Proxy Porta:</label><input id="txtProxyPort" maxlength="5" type="text" readonly="readonly" disabled="disabled" /></li>
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
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <script type="text/javascript" language="Javascript">
            jQuery(document).ready(function() {
            });
            function cancelChange() {
                jQuery("#formChange").attr("action", "${rootPath}");
                jQuery("#formChange").submit();
                return true;
            }
        </script>
    </head>
    <body>
        <div class="outer">
            <div class="limit">
                <c:import url="/Controls/HeaderControl/ChangePassword"></c:import>

                <div class="inside two-column-left">
                    <div class="breadcrumb">
                        <h3 class="fixpng">Menu</h3>
                        <strong>Você está aqui:</strong>

                        <ul>
                            <li><a href="${rootPath}">Início</a></li>
                            <li class="active last"><a href="${rootPath}ChangePassword" class="active">Alterar senha</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/ChangePassword"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>ALTERAÇÃO DE SENHA</h2>
                        <form method="POST" action="ChangePassword" id="formChange">
                            <fieldset class="formList">
                                <legend><span class="fixpng"><b>ENTRE COM OS DADOS PARA ALTERAÇÃO DA SENHA</b></span></legend>
                                <ul>
                                    <li>
                                        <label>SENHA ATUAL</label>
                                        <input type="password" class="extra-large" name="txtCurrentPassword" />
                                    </li>
                                    <li>
                                        <label>NOVA SENHA</label>
                                        <input type="password" class="extra-large" name="txtNewPassword" />
                                    </li>
                                    <li>
                                        <label>CONFIRMAR SENHA</label>
                                        <input type="password" class="extra-large" name="txtConfirmPassword" />
                                    </li>
                                </ul>
                            </fieldset>
                            <c:if test="${!empty errorMessage}">
                            <span class="error">${errorMessage}</span>
                            </c:if>
                            <fieldset class="buttons">
                                <ul>
                                    <li><input type="submit" value="Alterar" class="cancelar" /></li>
                                    <li><input type="button" value="Cancelar" class="cancelar" onclick="cancelChange();" /></li>
                                </ul>
                            </fieldset>
                            <input type="hidden" name="hdnLastPage" value="${lastPage}" ></input>
                        </form>
                    </div>

                </div>
            </div>

            <!-- FOOTER -->
            <c:import url="/Controls/FooterControl"></c:import>

        </div>
    </body>
</html>
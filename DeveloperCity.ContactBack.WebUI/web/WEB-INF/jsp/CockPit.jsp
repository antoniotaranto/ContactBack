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
        <script language="javascript" type="text/javascript">
            jQuery(document).ready(function() {
                doLoop();
            });
            function doLoop() {
                refreshCountQueue();
                t=setTimeout("doLoop()",20000);
            }
            function refreshCountQueue() {
                jQuery.ajax({
                    url: '${rootPath}CockPit/QueueCount',
                    type: 'POST',
                    dataType: 'json',
                    success: function(data, status) {
                        if (status == 'success') {
                            jQuery("#txtInQueue").val(data.inQueue);
                            jQuery("#txtInQueueFrozen").val(data.frozen);
                            jQuery("#txtInCall").val(data.inCall);
                            jQuery("#txtTotal").val(data.total - data.others);
                        }
                    }
                });
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
                            <li class="active last"><a href="${rootPath}CockPit" class="active">CockPit</a></li>
                        </ul>

                    </div>

                    <c:import url="/Controls/MainMenuControl/CockPit"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>COCKPIT INICIAL</h2>
                        <fieldset class="formList">
                            <legend><span class="fixpng"><b>DADOS ATUAIS</b></span></legend>
                            <ul>
                                <li>
                                    <label>Na fila (espera):</label>
                                    <input type="text" id="txtInQueue" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Na fila (congelados):</label>
                                    <input type="text" id="txtInQueueFrozen" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Em ligação:</label>
                                    <input type="text" id="txtInCall" readonly="readonly" value=""></input>
                                </li>
                                <li>
                                    <label>Total:</label>
                                    <input type="text" id="txtTotal" readonly="readonly" value=""></input>
                                </li>
                            </ul>
                        </fieldset>
                    </div>
                </div>
            </div>

            <!-- FOOTER -->
            <c:import url="/Controls/FooterControl"></c:import>
        </div>
    </body>
</html>
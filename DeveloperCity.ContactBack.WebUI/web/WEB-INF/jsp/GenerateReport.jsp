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
            jQuery(document).ready(function() {
                jQuery("#btnExportMenu").click(function() {
                    jQuery("#ExportMenu").toggle("slow");
                });
            });
            function formatExport(formatToExport) {
                jQuery("#txtFormat").val(formatToExport);
                jQuery("#formExport").submit();
            }
        </script>
        <style type="text/css">
            div#ExportMenu { border: solid 1px #87b; background-color: #bbe; display: none; width: 100%; margin: 5px 0; height: 95px; padding: 5px; }
            div#ExportMenu ul { background-color: #bbe; }
            div#ExportMenu ul li { background-color: #bbe; text-align: center; vertical-align: middle; margin: 2px; display: block; float: left; width: 85px; height: 80px; }
            div#ExportMenu ul li input { background-color: #bbe; border: 0; display: block; margin: auto; }
            div#ExportMenu ul li label { border: 0; display: block; font-weight: normal; font-size: -1em; margin: auto; cursor: pointer; }
        </style>
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
                            <li class="active last"><a href="${rootPath}CockPit/Report" class="active">Relatórios</a></li>
                        </ul>
                    </div>

                    <c:import url="/Controls/MainMenuControl/CockPit/Report"></c:import>

                    <div id="right" class="sidebar last">

                    </div>

                    <div class="content">
                        <h2>RELATÓRIOS</h2>

                        <span id="errorLabel" class="error">
                            <c:if test="${!empty errorMessage}">
                                ${errorMessage}
                            </c:if>
                        </span>
                        
                        <form method="POST" target="_blank" id="formExport" action="GenerateReport">
                        <c:forEach begin="0" end="${paramsCount - 1}" var="i" step="1" >
                            <input type="hidden" id="txt${params[i][0]}" name="${params[i][0]}" value="${params[i][1]}" />
                        </c:forEach>

                            <input type="button" id="btnExportMenu" value="Exportar" style="clear: both; font-weight: bold; " /><br />
                            <div id="ExportMenu">
                                <ul>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_pdf.png" style="height:48px;width:48px;" alt="Adobe Acrobat (PDF)" value="Adobe Acrobat (PDF)" onclick="javascript:formatExport('pdf');" id="btnPDF" name="btnPDF" /><label for="btnPDF">Adobe Acrobat (PDF)</label></li>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_xls.png" style="height:48px;width:48px;" alt="MS Excel 2000 (XLS)" value="MS Excel 2000 (XLS)" onclick="javascript:formatExport('xls');" id="btnXLS" name="btnXLS" /><label for="btnXLS">MS Excel 2000 (XLS)</label></li>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_xlsx.png" style="height:48px;width:48px;" alt="MS Excel 2007 (XLSX)" value="MS Excel 2007 (XLSX)" onclick="javascript:formatExport('xlsx');" id="btnXLSX" name="btnXLSX" /><label for="btnXLSX">MS Excel 2007 (XLSX)</label></li>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_docx.png" style="height:48px;width:48px;" alt="MS Word 2007 (DOCX)" value="MS Word 2007 (DOCX)" onclick="javascript:formatExport('docx');" id="btnDOCX" name="btnDOCX" /><label for="btnDOCX">MS Word 2007 (DOCX)</label></li>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_rtf.png" style="height:48px;width:48px;" alt="Rich Text (RTF)" value="Rich Text (RTF)" onclick="javascript:formatExport('rtf');" id="btnRTF" name="btnRTF" /><label for="btnRTF">Rich Text (RTF)</label></li>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_txt.png" style="height:48px;width:48px;" alt="Texto plano (TXT)" value="Texto plano (TXT)" onclick="javascript:formatExport('txt');" id="btnTXT" name="btnTXT" /><label for="btnTXT">Texto plano (TXT)</label></li>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_xml.png" style="height:48px;width:48px;" alt="XML" value="XML" onclick="javascript:formatExport('xml');" id="btnXML" name="btnXML" /><label for="btnXML">XML</label></li>
                                    <li><input type="image" src="/CAC/resources/IMAGES/icon_csv.png" style="height:48px;width:48px;" alt="CSV" value="CSV" onclick="javascript:formatExport('csv');" id="btnCSV" name="btnCSV" /><label for="btnCSV">CSV</label></li>
                                </ul>
                            </div>
                        </form>

                        <iframe src="GenerateReport?Format=pdf${parString}" style="margin: 15px 0 0 0; border: #aaa solid 1px; width: 100%; height: 700px; z-index: -1; "></iframe>

                    </div>

                </div>
            </div>
            <!-- FOOTER -->
            <c:import url="/Controls/FooterControl"></c:import>
        </div>
    </body>
</html>

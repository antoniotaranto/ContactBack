<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
                <div style="clear:both;"></div>
                <div id="ExportMenu">
                    <ul>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_html.png" style="height:48px;width:48px;" alt="Visualização na tela" value="Visualização na tela" onclick="return formatExport('html');" id="btnHTML" name="btnHTML" /><label for="btnHTML">Visualização na tela</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_pdf.png" style="height:48px;width:48px;" alt="Adobe Acrobat (PDF)" value="Adobe Acrobat (PDF)" onclick="return formatExport('pdf');" id="btnPDF" name="btnPDF" /><label for="btnPDF">Adobe Acrobat (PDF)</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_xls.png" style="height:48px;width:48px;" alt="MS Excel 2000 (XLS)" value="MS Excel 2000 (XLS)" onclick="return formatExport('xls');" id="btnXLS" name="btnXLS" /><label for="btnXLS">MS Excel 2000 (XLS)</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_xlsx.png" style="height:48px;width:48px;" alt="MS Excel 2007 (XLSX)" value="MS Excel 2007 (XLSX)" onclick="return formatExport('xlsx');" id="btnXLSX" name="btnXLSX" /><label for="btnXLSX">MS Excel 2007 (XLSX)</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_docx.png" style="height:48px;width:48px;" alt="MS Word 2007 (DOCX)" value="MS Word 2007 (DOCX)" onclick="return formatExport('docx');" id="btnDOCX" name="btnDOCX" /><label for="btnDOCX">MS Word 2007 (DOCX)</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_rtf.png" style="height:48px;width:48px;" alt="Rich Text (RTF)" value="Rich Text (RTF)" onclick="return formatExport('rtf');" id="btnRTF" name="btnRTF" /><label for="btnRTF">Rich Text (RTF)</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_txt.png" style="height:48px;width:48px;" alt="Texto plano (TXT)" value="Texto plano (TXT)" onclick="return formatExport('txt');" id="btnTXT" name="btnTXT" /><label for="btnTXT">Texto plano (TXT)</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_xml.png" style="height:48px;width:48px;" alt="XML" value="XML" onclick="return formatExport('xml');" id="btnXML" name="btnXML" /><label for="btnXML">XML</label></li>
                        <li><input type="image" src="/CAC/resources/IMAGES/icon_csv.png" style="height:48px;width:48px;" alt="CSV" value="CSV" onclick="return formatExport('csv');" id="btnCSV" name="btnCSV" /><label for="btnCSV">CSV</label></li>
                    </ul>
                </div>
                <div style="clear:both;"></div>

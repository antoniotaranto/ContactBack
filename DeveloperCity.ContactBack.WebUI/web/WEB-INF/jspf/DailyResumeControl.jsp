<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<form method="POST" id="formExport" action="GenerateReport">
    <script type="text/javascript">
        function customRange(input) {
            return {
                minDate: (input.id == "txtEndDate" ? jQuery("#txtStartDate").datepicker("getDate") : null),
                maxDate: (input.id == "txtStartDate" ? jQuery("#txtEndDate").datepicker("getDate") : null)
            };
        }
	jQuery(function() {
            jQuery.datepicker.setDefaults(jQuery.extend({showMonthAfterYear: false}, jQuery.datepicker.regional['pt-BR']));
            jQuery('#txtStartDate,#txtEndDate').datepicker({
                showOn: 'both',
                buttonImage: '/CAC/resources/CSS/datepicker/images/calendar.gif',
                buttonImageOnly: true,
                beforeShow: customRange,
                showStatus: true
            });
	});
        function formatExport(formatToExport) {
            if (jQuery('#txtStartDate').val() == '' || jQuery('#txtEndDate').val() == '') {
                jQuery("#errorLabel").html("As datas de início e fim são obrigatórias.");
                return false;
            }
            if (jQuery('#txtStartDate').val() == '' || jQuery('#txtEndDate').val() == '') {
                return false;
            }
            var data = jQuery.ajax({
                url: '/CAC/Controls/DailyResumeControl',
                processData: false,
                data: 'startDate=' + jQuery('#txtStartDate').val() + '&endDate=' + jQuery('#txtEndDate').val(),
                type: 'POST',
                dataType: 'html',
                async: false
            }).responseXML;

            if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
                jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
                return false;
            } else {
                jQuery("#errorLabel").html("");
                jQuery("#txtFormat").val(formatToExport);
                return true;
            }
        }
    </script>
    <fieldset class="formList">
        <legend><span class="fixpng"><b>FILTRO PARA RELATÓRIO - Resumo Diário</b></span></legend>
        <ul>
            <li>
                <label for="StartDate">De:</label>
                <input type="text" id="txtStartDate" name="StartDate" value="${today}" style="margin-right: 10px;" />
            </li>
            <li>
                <label for="EndDate">Até:</label>
                <input type="text" id="txtEndDate" name="EndDate" value="${today}" style="margin-right: 10px;" />
            </li>
            <li>
                <input type="hidden" name="Format" id="txtFormat" value="html" />
                <input type="hidden" name="Report" value="dailyResume" />
                <c:import url="/Controls/ReportExportMenuControl"></c:import>
            </li>
        </ul>
    </fieldset>
</form>

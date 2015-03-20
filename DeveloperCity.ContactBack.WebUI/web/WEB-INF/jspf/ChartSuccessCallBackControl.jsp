<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<form method="POST" id="formExport" action="ChartSuccessCallBackControl">
    <script type="text/javascript">
        function validate() {
            var sDate = jQuery("#txtStartDate").datepicker("getDate");
            var eDate = jQuery("#txtEndDate").datepicker("getDate");
            var diff = 1 + (eDate.getTime() - sDate.getTime()) / (1000 * 60 * 60 * 24);
            if (diff > 31) {
                jQuery("#errorLabel").html("O prazo máximo deverá ser de 1 mês.");
                return false;
            }
            jQuery("#errorLabel").html("");
            filterComplete('ChartSuccessCallBackControl', 'startDate=' + jQuery('#txtStartDate').val() + '%26endDate=' + jQuery('#txtEndDate').val());
        }
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
    </script>
    <fieldset id="chartFilterForm" class="formList">
        <legend><span class="fixpng"><b>FILTRO PARA GRÁFICO</b></span></legend>
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
                <input type="button" name="btnPrintChart" value="Gerar" onclick="javascript:validate();" />
            </li>
        </ul>
    </fieldset>
</form>

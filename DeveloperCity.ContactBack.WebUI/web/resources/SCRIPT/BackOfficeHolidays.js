var itemDetails = -1;

function backgroundWorkStart() {
    jQuery("#BackgroundWork").show();
}
function backgroundWorkEnd() {
    jQuery("#BackgroundWork").hide();
}
jQuery(function() {
    jQuery.datepicker.setDefaults(jQuery.extend({showMonthAfterYear: false}, jQuery.datepicker.regional['pt-BR']));
});
function doLoop() {
    if (itemDetails == -1) {
        refreshHolidays();
    } else {
        //holidayItemSelected(itemDetails);
    }
    t=setTimeout("doLoop()",15000);
}
function openItemDetails(holidayID) {
    jQuery("#holidayItemDetails").html("");
    holidayItemSelected(holidayID);
    itemDetails = holidayID;
    jQuery.blockUI({
        message: jQuery('#holidayItemDetails'),
        css: {
            top:  (jQuery(window).height() - 350) /2 + 'px',
            left: (jQuery(window).width() - 500) /2 + 'px',
            width: '500px',
            height: '350px'
        }
    });
}
function closeItemDetails() {
    refreshHolidays();
    jQuery.unblockUI();
    itemDetails = -1;
}
function deleteHoliday() {
    backgroundWorkStart();

    if (itemDetails <= 0) { return; }
    var sure = confirm("Deseja excluir realmente este item?");
    if (!sure) { return; }

    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/Holidays/Delete/' + itemDetails,
        processData: false,
        type: 'POST',
        dataType: 'html',
        async: false
    }).responseXML;

    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#detailsErrorLabel").html( jQuery(data).find('Details').text() );
    } else {
        jQuery("#detailsErrorLabel").html("");
        closeItemDetails();
    }
    backgroundWorkEnd();
}
function saveItemDetails() {
    backgroundWorkStart();

    var holidayName= jQuery("#txtHolidayName").val();
    if (holidayName == null || holidayName.length == 0) {
        jQuery("#detailsErrorLabel").html("Nome de feriado inv&aacute;lido");
        return false;
    }
    var day = jQuery("#txtDay").val().toString().length > 0 ? jQuery("#txtDay").datepicker("getDate") : null;
    if (day == null) {
        jQuery("#detailsErrorLabel").html("Data inv&aacute;lida");
        return false;
    }
    var formData =
        'holidayName=' + holidayName +
        '&day=' + (day.getDate() + "/" + (day.getMonth() + 1) + "/" + day.getFullYear());
    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/Holidays/' + itemDetails,
        contentType: "application/x-www-form-urlencoded;charset=ISO-8859-1",
        processData: false,
        data: formData,
        type: 'POST',
        dataType: 'html',
        async: false
    }).responseXML;

    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#detailsErrorLabel").html( jQuery(data).find('Details').text() );
    } else {
        jQuery("#detailsErrorLabel").html("");
        closeItemDetails();
    }
    backgroundWorkEnd();
    return true;
}
function holidayItemSelected(holidayID) {
    backgroundWorkStart();
    if (holidayID > 0) {
        jQuery.ajax({
            url: rootPath + 'BackOffice/Holidays/' + holidayID,
            type: 'GET',
            dataType: 'json',
            success: function(data, status) {
                if (status == 'success') {
                    var holidayName = data.holidayName;
                    var day = data.day != null ? new Date(data.day) : null;
                    holidayDetailsControl();
                    jQuery("#txtHolidayName").val( holidayName );
                    jQuery("#txtDay").val( (day.getDate() + "/" + (day.getMonth() + 1) + "/" + day.getFullYear()) );
                    jQuery("#EditionTitle").html("Altera&ccedil;&atilde;o");
                    jQuery("#deleteHolidayItemDetails").show();
                }
            }
        });
    } else if (holidayID == 0) {
        holidayDetailsControl();
        jQuery("#EditionTitle").html("Inclus&atilde;o");
        jQuery("#deleteHolidayItemDetails").hide();
    }
    backgroundWorkEnd();
}
function holidayDetailsControl() {
    jQuery("#holidayItemDetails").html("");
    jQuery("#holidayItemDetails").append("<a id=\"saveHolidayItemDetails\" href=\"javascript:saveItemDetails();\">Salvar</a>");
    jQuery("#holidayItemDetails").append("<a id=\"deleteHolidayItemDetails\" href=\"javascript:deleteHoliday();\">Excluir</a>");
    jQuery("#holidayItemDetails").append("<a id=\"closeHolidayItemDetails\" href=\"javascript:closeItemDetails();\">Fechar [ X ]</a>");
    jQuery("#holidayItemDetails").append("<h3 id=\"EditionTitle\"></h3>");
    jQuery("#holidayItemDetails").append("<span id=\"detailsErrorLabel\" class=\"error\"></span>");
    jQuery("#holidayItemDetails").append("<ul id='holidayItemBasic'></ul>");
    jQuery("#holidayItemBasic").append("<li><label>Nome do Feriado:</label><input type=\"text\" maxlength=\"100\" id=\"txtHolidayName\" name=\"txtHolidayName\" value=\"\"></input></li>");
    jQuery("#holidayItemBasic").append("<li><label>Data:</label><input type=\"text\" maxlength=\"10\" id=\"txtDay\" name=\"txtDay\" value=\"\"></input></li>");
    jQuery(function() {
        jQuery('#txtDay').datepicker();
    });
}
function refreshHolidays() {
    backgroundWorkStart();
    jQuery.ajax({
        url: rootPath + 'BackOffice/Holidays/List',
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                jQuery("#holidaysContainer").html("");
                jQuery("#holidaysContainer").append("<table id='holidaysTable'></table>");

                var header = "<thead>";
                header += "   <tr>";
                header += "      <th style=\"width: 20%;\">Data</th>";
                header += "      <th style=\"width: 80%;\">Feriado</th>";
                header += "   </tr>";
                header += "</thead>";

                jQuery("#holidaysTable").append(header);

                var footer = "<tfoot>";
                footer += "   <tr>";
                footer += "      <td colspan='2'>Total de feriados: " + data.length + "</td>";
                footer += "   </tr>";
                footer += "</tfoot>";

                jQuery("#holidaysTable").append(footer);

                for (var i = 0; i < data.length; i++) {
                    var day = data[i].day != null ? new Date(data[i].day) : null;

                    var listItem = "<tr onClick=\"openItemDetails(" + data[i].holidayID + ");\">";
                    listItem += "   <td>" + (day != null ? (day.getDate() + "/" + (day.getMonth() + 1) + "/" + day.getFullYear()) : "") + "</td>";
                    listItem += "   <td>" + data[i].holidayName + "</td>";
                    listItem += "</tr>";
                    jQuery("#holidaysTable").append(listItem);
                }
            }
            var now = new Date();
            jQuery("#lastUpdate").text( now.getDate() + "/" + (now.getMonth() + 1) + "/" + now.getFullYear() + " " + now.toLocaleTimeString() );
        }
    });
    backgroundWorkEnd();
}
function defineEscPress() {
    if (jQuery.browser.mozilla) {
        jQuery(document).keypress(onKeyPress);
    } else {
        jQuery(document).keydown(onKeyPress);
    }
}
function onKeyPress(e) {
    if (e.keyCode == '27') {
        e.preventDefault();
        if (itemDetails != -1) {
            closeItemDetails();
        }
    }
}
function commitHolidays() {
    backgroundWorkStart();
    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/Holidays/List',
        processData: false,
        type: 'POST',
        dataType: 'html',
        async: false
    }).responseXML;
    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
    } else {
        jQuery("#errorLabel").html("");
        alert('Efetivado com sucesso no software de telefonia!');
    }
    backgroundWorkEnd();
}

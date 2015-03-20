var itemDetails = -1;

function backgroundWorkStart() {
    jQuery("#BackgroundWork").show();
}
function backgroundWorkEnd() {
    jQuery("#BackgroundWork").hide();
}
function doLoop() {
    if (itemDetails == -1) {
        refreshBlackList();
    } else {
        //blackListItemSelected(itemDetails);
    }
    t=setTimeout("doLoop()",15000);
}
function openItemDetails(blackListID) {
    jQuery("#blackListItemDetails").html("");
    blackListItemSelected(blackListID);
    itemDetails = blackListID;
    jQuery.blockUI({
        message: jQuery('#blackListItemDetails'),
        css: {
            top:  (jQuery(window).height() - 350) /2 + 'px',
            left: (jQuery(window).width() - 500) /2 + 'px',
            width: '500px',
            height: '350px'
        }
    });
}
function closeItemDetails() {
    refreshBlackList();
    jQuery.unblockUI();
    itemDetails = -1;
}
function deleteBlackList() {
    backgroundWorkStart();

    if (itemDetails <= 0) { return; }
    var sure = confirm("Deseja excluir realmente este item?");
    if (!sure) { return; }

    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/BlackList/Delete/' + itemDetails,
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

    var dStartTime = jQuery("#startTimeHour").val() + ":" + jQuery("#startTimeMinute").val() + ":00";
    var dEndTime = jQuery("#endTimeHour").val() + ":" + jQuery("#endTimeMinute").val() + ":00";
    var blackListDestination = jQuery("#ddlBlackListDestination").val();
    var matchMode = jQuery("#ddlMatchMode").val();
    var number = jQuery("#txtNumber").val();
    if (number == null || number.length == 0 || !number.match(/[0-9]/) || number.match(/[^0-9]/)) { jQuery("#detailsErrorLabel").html("N&uacute;mero incorreto!"); backgroundWorkEnd(); return false; }
    var sunday = jQuery("#chkSunday").attr("checked");
    var monday = jQuery("#chkMonday").attr("checked");
    var tuesday = jQuery("#chkTuesday").attr("checked");
    var wednesday = jQuery("#chkWednesday").attr("checked");
    var thurday = jQuery("#chkThurday").attr("checked");
    var friday = jQuery("#chkFriday").attr("checked");
    var saturday = jQuery("#chkSaturday").attr("checked");
    var formData =
        'startTime=' + dStartTime +
        '&endTime=' + dEndTime +
        '&blackListDestination=' + blackListDestination +
        '&matchMode=' + matchMode +
        '&number=' + number +
        '&sunday=' + sunday +
        '&monday=' + monday +
        '&tuesday=' + tuesday +
        '&wednesday=' + wednesday +
        '&thurday=' + thurday +
        '&friday=' + friday +
        '&saturday=' + saturday;
    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/BlackList/' + itemDetails,
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
function blackListItemSelected(blackListID) {
    backgroundWorkStart();
    if (blackListID > 0) {
        jQuery.ajax({
            url: rootPath + 'BackOffice/BlackList/' + blackListID,
            type: 'GET',
            dataType: 'json',
            success: function(data, status) {
                if (status == 'success') {
                    var dStartTime = data.startTime != null ? new Date(data.startTime) : null;
                    var dEndTime = data.endTime != null ? new Date(data.endTime) : null;
                    var number = data.number;
                    var matchMode = data.matchMode;
                    var blackListDestination = data.blackListDestination;
                    blackListDetailsControl();
                    var startH = dStartTime.getHours().toString(); startH = "00".substr(0, 2 - startH.length) + startH;
                    var startM = dStartTime.getMinutes().toString(); startM = "00".substr(0, 2 - startM.length) + startM;
                    var endH = dEndTime.getHours().toString(); endH = "00".substr(0, 2 - endH.length) + endH;
                    var endM = dEndTime.getMinutes().toString(); endM = "00".substr(0, 2 - endM.length) + endM;
                    jQuery("#startTimeHour").val( startH );
                    jQuery("#startTimeMinute").val( startM );
                    jQuery("#endTimeHour").val( endH );
                    jQuery("#endTimeMinute").val( endM );
                    jQuery("#ddlBlackListDestination").val(blackListDestination);
                    jQuery("#ddlMatchMode").val(matchMode);
                    jQuery("#txtNumber").val(number);
                    jQuery("#EditionTitle").html("Altera&ccedil;&atilde;o");
                    jQuery("#deleteBlackListItemDetails").show();
                    for(var i = 0; i < data.weekdays.length; i++) {
                        if (data.weekdays[i] == "Sunday") { jQuery("#chkSunday").attr("checked", "checked"); }
                        else if (data.weekdays[i] == "Monday") { jQuery("#chkMonday").attr("checked", "checked"); }
                        else if (data.weekdays[i] == "Tuesday") { jQuery("#chkTuesday").attr("checked", "checked"); }
                        else if (data.weekdays[i] == "Wednesday") { jQuery("#chkWednesday").attr("checked", "checked"); }
                        else if (data.weekdays[i] == "Thurday") { jQuery("#chkThurday").attr("checked", "checked"); }
                        else if (data.weekdays[i] == "Friday") { jQuery("#chkFriday").attr("checked", "checked"); }
                        else if (data.weekdays[i] == "Saturday") { jQuery("#chkSaturday").attr("checked", "checked"); }
                    }

                }
            }
        });
    } else if (blackListID == 0) {
        blackListDetailsControl();
        jQuery("#EditionTitle").html("Inclus&atilde;o");
        jQuery("#deleteBlackListItemDetails").hide();
    }
    backgroundWorkEnd();
}
function timePicker(componentID) {
    var hourCombo = "<select id=\"" + componentID + "Hour\">\r\n";
    var printNumber = "";
    for (var i = 0; i < 24; i++) {
        printNumber = i.toString();
        if (printNumber.length < 2) {
            printNumber = "00".substr(0, 2 - printNumber.length) + printNumber;
        }
        hourCombo += "   <option value=\""+ printNumber + "\">" + printNumber + "</option>\r\n";
    }
    hourCombo += "</select>";

    var minuteCombo = "<select id=\"" + componentID + "Minute\">\r\n";
    for (var j = 0; j < 60; j++) {
        printNumber = j.toString();
        if (printNumber.length < 2) {
            printNumber = "00".substr(0, 2 - printNumber.length) + printNumber;
        }
        minuteCombo += "   <option value=\"" + printNumber + "\">" + printNumber + "</option>\r\n";
    }
    minuteCombo += "</select>";

    return hourCombo + " : " + minuteCombo;
}
function blackListDetailsControl() {
    jQuery("#blackListItemDetails").html("");
    jQuery("#blackListItemDetails").append("<a id=\"saveBlackListItemDetails\" href=\"javascript:saveItemDetails();\">Salvar</a>");
    jQuery("#blackListItemDetails").append("<a id=\"deleteBlackListItemDetails\" href=\"javascript:deleteBlackList();\">Excluir</a>");
    jQuery("#blackListItemDetails").append("<a id=\"closeBlackListItemDetails\" href=\"javascript:closeItemDetails();\">Fechar [ X ]</a>");
    jQuery("#blackListItemDetails").append("<h3 id=\"EditionTitle\"></h3>");
    jQuery("#blackListItemDetails").append("<span id=\"detailsErrorLabel\" class=\"error\"></span>");
    jQuery("#blackListItemDetails").append("<ul id='blackListItemBasic'></ul>");
    jQuery("#blackListItemBasic").append("<li><label>In&iacute;cio:</label>" + timePicker("startTime") + "</li>");
    jQuery("#blackListItemBasic").append("<li><label>T&eacute;rmino:</label>" + timePicker("endTime") + "</li>");
    jQuery("#blackListItemBasic").append("<li><label>Destino:</label><select id=\"ddlBlackListDestination\" name=\"ddlBlackListDestination\">\r\n" +
        "<option value=\"DropCall\">Desligar sem enfileirar</option>\r\n" +
        "<option value=\"OptionalAnotherPhone\">Escolha do n&uacute;mero de retorno opcional</option>\r\n" +
        "<option value=\"ForceAnotherPhone\">For&ccedil;ar outro n&uacute;mero de retorno</option></select></li>");
    jQuery("#blackListItemBasic").append("<li><label>Padr&atilde;o:</label><select id=\"ddlMatchMode\" name=\"ddlMatchMode\"><option value=\"Exact\">Exatamente</option><option value=\"StartsWith\">Inicia com</option><option value=\"EndsWith\">Termina com</option><option value=\"Contains\">Cont&eacute;m</option></select></li>");
    jQuery("#blackListItemBasic").append("<li><label>N&uacute;mero:</label><input type=\"text\" maxlength=\"10\" id=\"txtNumber\" name=\"txtNumber\" value=\"\"></input></li>");
    jQuery("#blackListItemBasic").append("<li><label>Dias:</label>\r\n" +
            "<ul class=\"CheckBoxList\">\r\n" +
                "<li><input type=\"checkbox\" id=\"chkSunday\" name=\"chkSunday\" /><span> Domingo</span></li>\r\n" +
                "<li><input type=\"checkbox\" id=\"chkMonday\" name=\"chkMonday\" /><span> Segunda-feira</span></li>\r\n" +
                "<li><input type=\"checkbox\" id=\"chkTuesday\" name=\"chkTuesday\" /><span> Ter&ccedil;a-feira</span></li>\r\n" +
                "<li><input type=\"checkbox\" id=\"chkWednesday\" name=\"chkWednesday\" /><span> Quarta-feira</span></li>\r\n" +
                "<li><input type=\"checkbox\" id=\"chkThurday\" name=\"chkThurday\" /><span> Quinta-feira</span></li>\r\n" +
                "<li><input type=\"checkbox\" id=\"chkFriday\" name=\"chkFriday\" /><span> Sexta-feira</span></li>\r\n" +
                "<li><input type=\"checkbox\" id=\"chkSaturday\" name=\"chkSaturday\" /><span> S&aacute;bado</span></li>\r\n" +
            "</ul></li>");
}
function refreshBlackList() {
    backgroundWorkStart();
    jQuery.ajax({
        url: rootPath + 'BackOffice/BlackList/List',
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                jQuery("#blackListContainer").html("");
                jQuery("#blackListContainer").append("<table id='blackListTable'></table>");

                var header = "<thead>";
                header += "   <tr>";
                header += "      <th style=\"width: 8%;\">In&iacute;cio</th>";
                header += "      <th style=\"width: 8%;\">T&eacute;rmino</th>";
                header += "      <th style=\"width: 30%;\">Dias da semana</th>";
                header += "      <th style=\"width: 23%;\">Destino</th>";
                header += "      <th style=\"width: 16%;\">Padr&atilde;o</th>";
                header += "      <th style=\"width: 15%;\">N&uacute;mero</th>";
                header += "   </tr>";
                header += "</thead>";

                jQuery("#blackListTable").append(header);

                var footer = "<tfoot>";
                footer += "   <tr>";
                footer += "      <td colspan='6'>Total de pol&iacute;ticas: " + data.length + "</td>";
                footer += "   </tr>";
                footer += "</tfoot>";

                jQuery("#blackListTable").append(footer);

                for (var i = 0; i < data.length; i++) {
                    var dStartTime = data[i].startTime != null ? new Date(data[i].startTime) : null;
                    var dEndTime = data[i].endTime != null ? new Date(data[i].endTime) : null;
                    var matchMode = "";
                    if (data[i].matchMode == "Exact") { matchMode = "Exatamente" }
                    else if (data[i].matchMode == "StartsWith") { matchMode = "Inicia com" }
                    else if (data[i].matchMode == "EndsWith") { matchMode = "Termina com" }
                    else if (data[i].matchMode == "Contains") { matchMode = "Cont&eacute;m" }
                    var wdays = "";

                    for (var j = 0; j < data[i].weekdays.length; j++) {
                        var engDay = data[i].weekdays[j];
                        if (engDay == "Sunday") { wdays += "DOM; "; }
                        if (engDay == "Monday") { wdays += "SEG; "; }
                        if (engDay == "Tuesday") { wdays += "TER; "; }
                        if (engDay == "Wednesday") { wdays += "QUA; "; }
                        if (engDay == "Thurday") { wdays += "QUI; "; }
                        if (engDay == "Friday") { wdays += "SEX; "; }
                        if (engDay == "Saturday") { wdays += "SAB; "; }
                    }

                    var listItem = "<tr onClick=\"openItemDetails(" + data[i].blackListID + ");\">";
                    var blackListDestination = "";
                    if (data[i].blackListDestination == "DropCall") {
                        blackListDestination = "Desligar";
                    } else if (data[i].blackListDestination == "OptionalAnotherPhone") {
                        blackListDestination = "Oferecer n&uacute;mero alternativo";
                    } else if (data[i].blackListDestination == "ForceAnotherPhone") {
                        blackListDestination = "Obrigar n&uacute;mero alternativo";
                    }
                    listItem += "   <td>" + (dStartTime != null ? dStartTime.toLocaleTimeString() : "") + "</td>";
                    listItem += "   <td>" + (dEndTime != null ? dEndTime.toLocaleTimeString() : "") + "</td>";
                    listItem += "   <td>" + wdays + "</td>";
                    listItem += "   <td>" + blackListDestination + "</td>";
                    listItem += "   <td>" + matchMode + "</td>";
                    listItem += "   <td>" + data[i].number + "</td>";
                    listItem += "</tr>";
                    jQuery("#blackListTable").append(listItem);
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
function refreshPolicy() {
    backgroundWorkStart();
    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/BlackList/List',
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

var itemDetails = -1;
var permissionsLoaded = false;
var permissionsReportLoaded = false;
var permissionsChartLoaded = false;
var saving = false;

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
        refreshUsers();
    } else {
        //agentItemSelected(itemDetails);
    }
    t=setTimeout("doLoop()",60000);
}
function openItemDetails(userID) {
    permissionsLoaded = false;
    permissionsReportLoaded = false;
    permissionsChartLoaded = false;
    jQuery("#agentItemDetails").html("");
    jQuery("#saveAgentItemDetails").removeAttr("disabled");
    saving = false;
    agentItemSelected(userID);
    itemDetails = userID;
    jQuery.blockUI({
        message: jQuery('#agentItemDetails'),
        css: {
            top:  (jQuery(window).height() - 350) /2 + 'px',
            left: (jQuery(window).width() - 500) /2 + 'px',
            width: '500px',
            height: '350px'
        }
    });
}
function closeItemDetails() {
    refreshUsers();
    jQuery.unblockUI();
    itemDetails = -1;
}
function saveItemDetails() {
    if (saving) { return; }
    saving = true;
    backgroundWorkStart();
    jQuery("#saveAgentItemDetails").attr("disabled", "disabled");
    var username = jQuery("#txtUsername").val();
    if (username == null || username.length < 4) {jQuery("#detailsErrorLabel").html("Nome-de-usu&aacute;rio incorreto!");backgroundWorkEnd();return;}
    var name = jQuery("#txtName").val();
    if (name == null || name.length < 4) {jQuery("#detailsErrorLabel").html("Nome incorreto!");backgroundWorkEnd();return;}
    var email = jQuery("#txtEmail").val();
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (email == null || email.length == 0 || !email.match(re)) {jQuery("#detailsErrorLabel").html("Endere&ccedil;o de e-mail incorreto!");backgroundWorkEnd();return;}
    var birthday = jQuery("#txtBirthday").val().toString().length > 0 ? jQuery("#txtBirthday").datepicker("getDate") : null;
    var userStatus = jQuery("#ddlUserStatus").val();
    var isAgent = jQuery("#ddlUserClass").val() == 'ClassAgent';
    var directoryNumber = isAgent ? jQuery("#txtDirectoryNumber").val() : "";
    var terminal = isAgent ? jQuery("#txtTerminal").val() : "";
    if (isAgent) {
        var rDN = /^([0-9]+)$/;
        if (directoryNumber == null || directoryNumber.length == 0 || !directoryNumber.match(rDN)) {jQuery("#detailsErrorLabel").html("Ramal incorreto!");backgroundWorkEnd();return;}
        var rSEP = /^SEP([0-9A-F]{12})$/;
        if (terminal == null || terminal.length == 0 || !terminal.match(rSEP)) {jQuery("#detailsErrorLabel").html("Terminal incorreto!");backgroundWorkEnd();return;}
    }

    var formData =
        'username=' + username +
        '&name=' + name +
        '&email=' + email +
        '&birthday=' + ((birthday == null) ? "" : (birthday.getDate() + "/" + (birthday.getMonth() + 1) + "/" + birthday.getFullYear())) +
        '&userStatus=' + userStatus +
        '&isAgent=' + isAgent +
        '&directoryNumber=' + directoryNumber +
        '&terminal=' + terminal;

    if (permissionsLoaded) {
        var selectedModules = "&selectedModules=0";
        jQuery(".ModuleCheckbox:checked").each(function() {
            selectedModules += "|" + this.id.substr("chkModule_".length);
        });
        formData += selectedModules;
    }
    if (permissionsReportLoaded) {
        var selectedReports = "&selectedReports=0";
        jQuery(".ReportCheckbox:checked").each(function() {
            selectedReports += "|" + this.id.substr("chkReport_".length);
        });
        formData += selectedReports;
    }
    if (permissionsChartLoaded) {
        var selectedCharts = "&selectedCharts=0";
        jQuery(".ChartCheckbox:checked").each(function() {
            selectedCharts += "|" + this.id.substr("chkChart_".length);
        });
        formData += selectedCharts;
    }

    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/Users/' + itemDetails,
        processData: false,
        contentType: "application/x-www-form-urlencoded;charset=ISO-8859-1",
        data: formData,
        type: 'POST',
        dataType: 'html',
        async: false
    }).responseXML;

    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#detailsErrorLabel").html( jQuery(data).find('Details').text() );
        if (itemDetails > 0) {
            jQuery("#saveAgentItemDetails").removeAttr("disabled");
            saving = false;
        } else { alert('Erro durante inclus&atilde;o. Favor feche essa janela e verifique se o item foi inserido. Persistindo o problema, favor consultar a equipe t&eacute;cnica.'); }
    } else {
        jQuery("#detailsErrorLabel").html("");
        closeItemDetails();
    }
    backgroundWorkEnd();
}
function resetPassword(userID) {
    var response = resetPasswordText();
    if (response) {
        backgroundWorkStart();

        var data = jQuery.ajax({
            url: rootPath + 'BackOffice/Users/ResetPassword/' + userID,
            processData: false,
            type: 'POST',
            dataType: 'html',
            async: false
        }).responseXML;

        if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
            jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
        } else {
            jQuery("#errorLabel").html("");
            closeItemDetails();
        }
        backgroundWorkEnd();
    }
}
function agentItemSelected(userID) {
    backgroundWorkStart();
    if (userID > 0) {
        jQuery.ajax({
            url: rootPath + 'BackOffice/Users/' + userID,
            type: 'GET',
            dataType: 'json',
            success: function(data, status) {
                if (status == 'success') {
                    var username = data.username == null ? "" : data.username;
                    var name = data.name == null ? "" : data.name;
                    var email = data.email == null ? "" : data.email;
                    var birthday = data.birthday == null ? null : new Date(data.birthday);
                    var userStatus = data.userStatus == null ? "Deleted" : data.userStatus;
                    var isAgent = (data.type == "DeveloperCity.ContactBack.DomainModel.Agent");
                    var directoryNumber = isAgent && data.directoryNumber != null ? data.directoryNumber : "";
                    var terminal = isAgent && data.terminal != null ? data.terminal : "";
                    var agentStatus = isAgent && data.agentStatus != null ? data.agentStatus : "";
                    userDetailsControl();
                    jQuery("#EditionTitle").html("Altera&ccedil;&atilde;o");
                    jQuery("#ddlUserClass").attr("readonly", "readonly");
                    jQuery("#ddlUserClass").attr("disabled", "disabled");
                    jQuery("#txtUsername").val( username );
                    jQuery("#txtName").val( name );
                    jQuery("#txtEmail").val( email );
                    jQuery("#txtBirthday").val( (birthday == null ? "" : (birthday.getDate() + "/" + (birthday.getMonth() + 1) + "/" + birthday.getFullYear()) ) );
                    jQuery("#ddlUserStatus").val( userStatus );
                    jQuery("#ddlUserClass").val( (isAgent ? "ClassAgent" : "ClassUser" ) );
                    jQuery("#txtDirectoryNumber").val( directoryNumber );
                    jQuery("#txtTerminal").val( terminal );
                    jQuery("#ddlAgentStatus").val( agentStatus );
                    if(!isAgent) {
                        // User
                        jQuery("#txtDirectoryNumber").attr("readonly", "readonly");
                        jQuery("#txtDirectoryNumber").attr("disabled", "disabled");
                        jQuery("#txtDirectoryNumber").val("");
                        jQuery("#txtTerminal").attr("readonly", "readonly");
                        jQuery("#txtTerminal").attr("disabled", "disabled");
                        jQuery("#txtTerminal").val("");
                    }
                }
            }
        });
    } else if (userID == 0) {
        userDetailsControl();
        jQuery("#EditionTitle").html("Inclus&atilde;o");
        jQuery("#ddlUserClass").removeAttr("readonly");
        jQuery("#ddlUserClass").removeAttr("disabled");
        jQuery("#ddlUserClass").change(function() {
            if(jQuery("#ddlUserClass").val() == 'ClassUser') {
                // User
                jQuery("#txtDirectoryNumber").attr("readonly", "readonly");
                jQuery("#txtDirectoryNumber").attr("disabled", "disabled");
                jQuery("#txtDirectoryNumber").val("");
                jQuery("#txtTerminal").attr("readonly", "readonly");
                jQuery("#txtTerminal").attr("disabled", "disabled");
                jQuery("#txtTerminal").val("");
            } else if (jQuery("#ddlUserClass").val() == 'ClassAgent') {
                // Agent
                jQuery("#txtDirectoryNumber").removeAttr("readonly");
                jQuery("#txtDirectoryNumber").removeAttr("disabled");
                jQuery("#txtTerminal").removeAttr("readonly");
                jQuery("#txtTerminal").removeAttr("disabled");
            }
        })
        jQuery("#ddlUserClass").val("ClassUser");
        jQuery("#txtDirectoryNumber").attr("readonly", "readonly");
        jQuery("#txtDirectoryNumber").attr("disabled", "disabled");
        jQuery("#txtDirectoryNumber").val("");
        jQuery("#txtTerminal").attr("readonly", "readonly");
        jQuery("#txtTerminal").attr("disabled", "disabled");
        jQuery("#txtTerminal").val("");
        jQuery("#ddlUserStatus").val( "Active" );
    }
    backgroundWorkEnd();
}
function editTab(tabIndex) {
    backgroundWorkStart();
    if (tabIndex == 0) {
        jQuery("#tabUser").addClass("selected");
        jQuery("#tabPermission").removeClass("selected");
        jQuery("#tabReport").removeClass("selected");
        jQuery("#tabChart").removeClass("selected");
        jQuery("#agentItemBasic").show();
        jQuery("#userModulePermissionContainer").hide();
        jQuery("#userReportPermissionContainer").hide();
        jQuery("#userChartPermissionContainer").hide();
    } else if (tabIndex == 1) {
        if (!permissionsLoaded) {
            modulesControl();
            permissionsLoaded = true;
        }
        jQuery("#tabUser").removeClass("selected");
        jQuery("#tabPermission").addClass("selected");
        jQuery("#tabReport").removeClass("selected");
        jQuery("#tabChart").removeClass("selected");
        jQuery("#agentItemBasic").hide();
        jQuery("#userModulePermissionContainer").show();
        jQuery("#userReportPermissionContainer").hide();
        jQuery("#userChartPermissionContainer").hide();
    } else if (tabIndex == 2) {
        if (!permissionsReportLoaded) {
            reportsControl();
            permissionsReportLoaded = true;
        }
        jQuery("#tabUser").removeClass("selected");
        jQuery("#tabPermission").removeClass("selected");
        jQuery("#tabReport").addClass("selected");
        jQuery("#tabChart").removeClass("selected");
        jQuery("#agentItemBasic").hide();
        jQuery("#userModulePermissionContainer").hide();
        jQuery("#userReportPermissionContainer").show();
        jQuery("#userChartPermissionContainer").hide();
    } else if (tabIndex == 3) {
        if (!permissionsChartLoaded) {
            chartsControl();
            permissionsChartLoaded = true;
        }
        jQuery("#tabUser").removeClass("selected");
        jQuery("#tabPermission").removeClass("selected");
        jQuery("#tabReport").removeClass("selected");
        jQuery("#tabChart").addClass("selected");
        jQuery("#agentItemBasic").hide();
        jQuery("#userModulePermissionContainer").hide();
        jQuery("#userReportPermissionContainer").hide();
        jQuery("#userChartPermissionContainer").show();
    }
    backgroundWorkEnd();
}
function modulesControl() {
    backgroundWorkStart();
    if (itemDetails < 0) {return;}
    jQuery.ajax({
        url: rootPath + 'BackOffice/Users/Permissions/' + itemDetails,
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                for (var i = 0; i < data.length; i++) {
                    jQuery("#userModulePermission").append("<li><input type=\"checkbox\" class=\"ModuleCheckbox\" id=\"chkModule_" + data[i].module.moduleID + "\" name=\"chkModule_" + data[i].module.moduleID + "\" " + ( (data[i].readWritePermission == "r" || data[i].readWritePermission == "rw") ? ("checked=\"checked\"") : ("") ) + " /><label>" + data[i].module.portal + ": " + data[i].module.description + "</label></li>");
                }
            }
        }
    });
    backgroundWorkEnd();
}
function reportsControl() {
    backgroundWorkStart();
    if (itemDetails < 0) {return;}
    jQuery.ajax({
        url: rootPath + 'BackOffice/Users/Reports/' + itemDetails,
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                for (var i = 0; i < data.length; i++) {
                    jQuery("#userReportPermission").append("<li><input type=\"checkbox\" class=\"ReportCheckbox\" id=\"chkReport_" + data[i].report.reportID + "\" name=\"chkReport_" + data[i].report.reportID + "\" " + ( data[i].grantAccess ? ("checked=\"checked\"") : ("") ) + " /><label>" + data[i].report.reportDescription + "</label></li>");
                }
            }
        }
    });
    backgroundWorkEnd();
}
function chartsControl() {
    backgroundWorkStart();
    if (itemDetails < 0) {return;}
    jQuery.ajax({
        url: rootPath + 'BackOffice/Users/Charts/' + itemDetails,
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                for (var i = 0; i < data.length; i++) {
                    jQuery("#userChartPermission").append("<li><input type=\"checkbox\" class=\"ChartCheckbox\" id=\"chkChart_" + data[i].chart.chartID + "\" name=\"chkChart_" + data[i].chart.chartID + "\" " + ( data[i].grantAccess ? ("checked=\"checked\"") : ("") ) + " /><label>" + data[i].chart.chartDescription + "</label></li>");
                }
            }
        }
    });
    backgroundWorkEnd();
}
function userDetailsControl() {
    jQuery("#agentItemDetails").html("");
    jQuery("#agentItemDetails").append("<a id=\"saveAgentItemDetails\" href=\"javascript:saveItemDetails();\">Salvar</a>");
    jQuery("#agentItemDetails").append("<a id=\"closeAgentItemDetails\" href=\"javascript:closeItemDetails();\">Fechar [ X ]</a>");
    jQuery("#agentItemDetails").append("<h3 id=\"EditionTitle\">Detalhes</h3>");
    jQuery("#agentItemDetails").append("<span id=\"detailsErrorLabel\" class=\"error\"></span>");
    jQuery("#agentItemDetails").append("<div id=\"tabButtons\"><ul><li id=\"tabUser\" class=\"selected\"><a href=\"javascript:editTab(0);\">Usu&aacute;rio</a></li><li id=\"tabPermission\"><a href=\"javascript:editTab(1);\">Permiss&otilde;es</a></li><li id=\"tabReport\"><a href=\"javascript:editTab(2);\">Relat&oacute;rios</a></li><li id=\"tabChart\"><a href=\"javascript:editTab(3);\">Gr&aacute;ficos</a></li></ul></div>");
    jQuery("#agentItemDetails").append("<fieldset id=\"formUser\"><legend></legend></fieldset>");
    jQuery("#formUser").append("<ul id='agentItemBasic'></ul>");
    jQuery("#agentItemBasic").append("<li><label>Usu&aacute;rio:</label><input type=\"text\" id=\"txtUsername\" name=\"txtUsername\" value=\"\"></input></li>");
    jQuery("#agentItemBasic").append("<li><label>Nome:</label><input type=\"text\" id=\"txtName\" name=\"txtName\" value=\"\"></input></li>");
    jQuery("#agentItemBasic").append("<li><label>E-mail:</label><input type=\"text\" id=\"txtEmail\" name=\"txtEmail\" value=\"\"></input></li>");
    jQuery("#agentItemBasic").append("<li><label>Nascimento:</label><input type=\"text\" id=\"txtBirthday\" name=\"txtBirthday\" value=\"\"></input></li>");
    jQuery("#agentItemBasic").append("<li><label>Status:</label><select id=\"ddlUserStatus\" name=\"ddlUserStatus\">\r\n" +
        "<option value=\"Deleted\">Exclu&iacute;do</option>\r\n" +
        "<option value=\"Active\">Ativo</option>\r\n" +
        "<option value=\"Blocked\">Bloqueado</option></select></li>\r\n");
    jQuery("#agentItemBasic").append("<li><label>Categoria:</label><select id=\"ddlUserClass\" name=\"ddlUserClass\">\r\n" +
        "<option value=\"ClassUser\">Usu&aacute;rio administrativo</option>\r\n" +
        "<option value=\"ClassAgent\">Agente</option></select></li>\r\n");
    jQuery("#agentItemBasic").append("<li><label>Ramal:</label><input type=\"text\" id=\"txtDirectoryNumber\" name=\"txtDirectoryNumber\" value=\"\"></input></li>");
    jQuery("#agentItemBasic").append("<li><label>Terminal:</label><input type=\"text\" id=\"txtTerminal\" name=\"txtTerminal\" value=\"\"></input></li>");
    jQuery("#agentItemBasic").append("<li><label>Situa&ccedil;&atilde;o:</label><select id=\"ddlAgentStatus\" readonly=\"readonly\" disabled=\"disabled\" name=\"ddlAgentStatus\">\r\n" +
        "<option value=\"NotLogged\">Offline</option>\r\n" +
        "<option value=\"Available\">Online</option>\r\n" +
        "<option value=\"NotReady\">N&atilde;o dispon&iacute;vel</option>\r\n" +
        "<option value=\"Break\">Em pausa</option></select></li>\r\n");
    jQuery("#formUser").append("<div id='userModulePermissionContainer'><ul id='userModulePermission'></ul></div>");
    jQuery("#formUser").append("<div id='userReportPermissionContainer'><ul id='userReportPermission'></ul></div>");
    jQuery("#formUser").append("<div id='userChartPermissionContainer'><ul id='userChartPermission'></ul></div>");
    jQuery("#userModulePermissionContainer").hide();
    jQuery("#userReportPermissionContainer").hide();
    jQuery("#userChartPermissionContainer").hide();
    jQuery(function() {
        jQuery('#txtBirthday').datepicker();
    });
}
function refreshUsers() {
    backgroundWorkStart();
    jQuery.ajax({
        url: rootPath + 'BackOffice/Users/List',
        type: 'POST',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                jQuery("#agentContainer").html("");
                jQuery("#agentContainer").append("<table id='agentTable'></table>");

                var header = "<thead>";
                header += "   <tr>";
                header += "      <th style=\"width: 20%;\">Usu&aacute;rio</th>";
                header += "      <th style=\"width: 15%;\">Status</th>";
                header += "      <th style=\"width: 15%;\">Tipo</th>";
                header += "      <th style=\"width: 15%;\">Ramal</th>";
                header += "      <th style=\"width: 15%;\">Terminal</th>";
                header += "      <th style=\"width: 15%;\">Online</th>";
                header += "      <th style=\"width: 5%;\">Senha</th>";
                header += "   </tr>";
                header += "</thead>";

                jQuery("#agentTable").append(header);

                var footer = "<tfoot>";
                footer += "   <tr>";
                footer += "      <td colspan='7'>Usu&aacute;rios: " + data.length + "</td>";
                footer += "   </tr>";
                footer += "</tfoot>";

                jQuery("#agentTable").append(footer);

                for (var i = 0; i < data.length; i++) {
                    var userID = data[i].userID;
                    var username = data[i].username;
                    var userStatus = "";
                    var styleRow = "";
                    if (data[i].userStatus == "Deleted") {userStatus = "Exclu&iacute;do";styleRow="Deleted";}
                    else if (data[i].userStatus == "Active") {userStatus = "Ativo";styleRow="Active";}
                    else if (data[i].userStatus == "Blocked") {userStatus = "Bloqueado";styleRow="Blocked";}
                    var isAgent = data[i].type == "DeveloperCity.ContactBack.DomainModel.Agent";
                    var directoryNumber = isAgent && data[i].directoryNumber != null ? data[i].directoryNumber : "";
                    var terminal = isAgent && data[i].terminal != null ? data[i].terminal : "";
                    var agentStatus = "";
                    if (isAgent && data[i].agentStatus) {
                        if (data[i].agentStatus == "NotLogged") {agentStatus = "Offline";if (styleRow=="Active") {styleRow="Offline";}}
                        else if (data[i].agentStatus == "Available") {agentStatus = "Online";if (styleRow=="Active") {styleRow="Available";}}
                        else if (data[i].agentStatus == "NotReady") {agentStatus = "N&atilde;o dispon&iacute;vel";if (styleRow=="Active") {styleRow="NotReady";}}
                        else if (data[i].agentStatus == "Break") {agentStatus = "Em pausa";if (styleRow=="Active") {styleRow="Break";}}
                    }

                    var listItem = "<tr class='" + styleRow + "' >";
                    listItem += "   <td><a href=\"javascript:openItemDetails(" + userID + ");\">" + username + "</a></td>";
                    listItem += "   <td>" + userStatus + "</td>";
                    listItem += "   <td>" + (isAgent ? "Agente" : "Usu&aacute;rio") + "</td>";
                    listItem += "   <td>" + directoryNumber + "</td>";
                    listItem += "   <td>" + terminal + "</td>";
                    listItem += "   <td>" + agentStatus + "</td>";
                    listItem += "   <td><a href=\"javascript:resetPassword(" + userID + ");\">Limpar</a></td>";
                    listItem += "</tr>";
                    jQuery("#agentTable").append(listItem);
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

var itemDetails = -1;
var permissionsLoaded = false;
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
    if (email == null || email.length == 0 || !email.match(re)) {jQuery("#detailsErrorLabel").html("Endere&ccedil;o de e-mail incorreto!");saving = false;backgroundWorkEnd();return;}
    var birthday = jQuery("#txtBirthday").val().toString().length > 0 ? jQuery("#txtBirthday").datepicker("getDate") : null;
    var userStatus = jQuery("#ddlUserStatus").val();
    var mobilePhone = jQuery("#txtMobilePhone").val();
    re = /^[0-9]{10,10}$/;
    if (mobilePhone == null || mobilePhone.length != 10 || !mobilePhone.match(re)) {jQuery("#detailsErrorLabel").html("Celular incorreto!");saving = false;backgroundWorkEnd();return;}

    var formData =
        'username=' + username +
        '&name=' + name +
        '&email=' + email +
        '&birthday=' + ((birthday == null) ? "" : (birthday.getDate() + "/" + (birthday.getMonth() + 1) + "/" + birthday.getFullYear())) +
        '&userStatus=' + userStatus +
        '&mobilePhone=' + mobilePhone;

    if (permissionsLoaded) {
        var selectedModules = "&selectedModules=0";
        jQuery(".ModuleCheckbox:checked").each(function() {
            selectedModules += "|" + this.id.substr("chkModule_".length);
        });
        formData += selectedModules;
    }

    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/Customers/' + itemDetails,
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
            url: rootPath + 'BackOffice/Customers/ResetPassword/' + userID,
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
            url: rootPath + 'BackOffice/Customers/' + userID,
            type: 'GET',
            dataType: 'json',
            success: function(data, status) {
                if (status == 'success') {
                    var username = data.username == null ? "" : data.username;
                    var name = data.name == null ? "" : data.name;
                    var email = data.email == null ? "" : data.email;
                    var birthday = data.birthday == null ? null : new Date(data.birthday);
                    var userStatus = data.userStatus == null ? "Deleted" : data.userStatus;
                    var mobilePhone = data.mobilePhone != null ? data.mobilePhone : "";
                    userDetailsControl();
                    jQuery("#EditionTitle").html("Altera&ccedil;&atilde;o");
                    jQuery("#txtUsername").val( username );
                    jQuery("#txtName").val( name );
                    jQuery("#txtEmail").val( email );
                    jQuery("#txtBirthday").val( (birthday == null ? "" : (birthday.getDate() + "/" + (birthday.getMonth() + 1) + "/" + birthday.getFullYear()) ) );
                    jQuery("#ddlUserStatus").val( userStatus );
                    jQuery("#txtMobilePhone").val( mobilePhone);
                }
            }
        });
    } else if (userID == 0) {
        userDetailsControl();
    }
    backgroundWorkEnd();
}
function editTab(tabIndex) {
    backgroundWorkStart();
    if (tabIndex == 0) {
        jQuery("#tabUser").addClass("selected");
        jQuery("#tabPermission").removeClass("selected");
        jQuery("#agentItemBasic").show();
        jQuery("#userModulePermissionContainer").hide();
    } else if (tabIndex == 1) {
        if (!permissionsLoaded) {
            modulesControl();
            permissionsLoaded = true;
        }
        jQuery("#tabUser").removeClass("selected");
        jQuery("#tabPermission").addClass("selected");
        jQuery("#agentItemBasic").hide();
        jQuery("#userModulePermissionContainer").show();
    }
    backgroundWorkEnd();
}
function modulesControl() {
    backgroundWorkStart();
    if (itemDetails < 0) {return;}
    jQuery.ajax({
        url: rootPath + 'BackOffice/Customers/Permissions/' + itemDetails,
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
function userDetailsControl() {
    jQuery("#agentItemDetails").html("");
    jQuery("#agentItemDetails").append("<a id=\"saveAgentItemDetails\" href=\"javascript:saveItemDetails();\">Salvar</a>");
    jQuery("#agentItemDetails").append("<a id=\"closeAgentItemDetails\" href=\"javascript:closeItemDetails();\">Fechar [ X ]</a>");
    jQuery("#agentItemDetails").append("<h3 id=\"EditionTitle\">Detalhes</h3>");
    jQuery("#agentItemDetails").append("<span id=\"detailsErrorLabel\" class=\"error\"></span>");
    jQuery("#agentItemDetails").append("<div id=\"tabButtons\"><ul><li id=\"tabUser\" class=\"selected\"><a href=\"javascript:editTab(0);\">Usu&aacute;rio</a></li><li id=\"tabPermission\"><a href=\"javascript:editTab(1);\">Permiss&otilde;es</a></li></ul></div>");
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
    jQuery("#agentItemBasic").append("<li><label>Celular:</label><input type=\"text\" id=\"txtMobilePhone\" maxlength=\"10\" name=\"txtMobilePhone\" value=\"\"></input></li>");
    jQuery("#formUser").append("<div id='userModulePermissionContainer'><ul id='userModulePermission'></ul></div>");
    jQuery("#userModulePermissionContainer").hide();
    jQuery(function() {
        jQuery('#txtBirthday').datepicker();
    });
}
function refreshUsers() {
    backgroundWorkStart();
    jQuery.ajax({
        url: rootPath + 'BackOffice/Customers/List',
        type: 'POST',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                jQuery("#agentContainer").html("");
                jQuery("#agentContainer").append("<table id='agentTable'></table>");

                var header = "<thead>";
                header += "   <tr>";
                header += "      <th style=\"width: 30%;\">Usu&aacute;rio</th>";
                header += "      <th style=\"width: 25%;\">Status</th>";
                header += "      <th style=\"width: 25%;\">Celular</th>";
                header += "      <th style=\"width: 20%;\">Senha</th>";
                header += "   </tr>";
                header += "</thead>";

                jQuery("#agentTable").append(header);

                var footer = "<tfoot>";
                footer += "   <tr>";
                footer += "      <td colspan='4'>Usu&aacute;rios: " + data.length + "</td>";
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
                    var mobilePhone = data[i].mobilePhone;

                    var listItem = "<tr class='" + styleRow + "' >";
                    listItem += "   <td><a href=\"javascript:openItemDetails(" + userID + ");\">" + username + "</a></td>";
                    listItem += "   <td>" + userStatus + "</td>";
                    listItem += "   <td>" + mobilePhone + "</td>";
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

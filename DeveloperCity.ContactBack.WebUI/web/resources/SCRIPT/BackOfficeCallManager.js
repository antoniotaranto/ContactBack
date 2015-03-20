var editing = false;
function backgroundWorkStart() {
    jQuery("#BackgroundWork").show();
}
function backgroundWorkEnd() {
    jQuery("#BackgroundWork").hide();
}
function loadCallManager() {
    if (editing) { return; }
    backgroundWorkStart();
    jQuery.ajax({
        url: rootPath + 'BackOffice/CallManager/Json',
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                var ip = data.IP == null ? "" : data.IP;
                var jtapiUsername = data.jtapiUsername == null ? "" : data.jtapiUsername;
                var jtapiPassword = data.jtapiPassword == null ? "" : data.jtapiPassword;

                jQuery("#txtIP").val( ip );
                jQuery("#txtJtapiUsername").val( jtapiUsername );
                jQuery("#txtJtapiPassword").val( jtapiPassword );
                backgroundWorkEnd();
            }
        }
    });
}
function startEdit() {
    if (editing) { return; }
    editing = true;
    backgroundWorkStart();
    jQuery("#btnSaveCallManager").show();
    jQuery("#btnCancelCallManager").show();
    jQuery("#btnEditCallManager").hide();
    jQuery("#formSetup > .formList input").each(function() {jQuery(this).removeAttr("disabled");jQuery(this).removeAttr("readonly");jQuery(this).focus();});
    jQuery("#formSetup > .formList select").each(function() {jQuery(this).removeAttr("disabled");jQuery(this).removeAttr("readonly");jQuery(this).focus();});
    jQuery("#formSetup > .formList input:first").focus();
    backgroundWorkEnd();
}
function saveEdit() {
    if (!editing) { return; }
    editing = false;

    var ip = jQuery("#txtIP").val(  );
    var jtapiUsername = jQuery("#txtJtapiUsername").val(  );
    var jtapiPassword = jQuery("#txtJtapiPassword").val(  );

    var re = /^((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(,(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))?$/;
    if (!ip.match(re)) { jQuery("#txtIP").focus(); jQuery("#errorLabel").html("Campo IP incorreto: para Publisher e Subscriber separar por v&iacute;rgula, sem espa&ccedil;os (exemplo: 192.168.0.1,192.168.0.2)"); editing = true; return; }

    backgroundWorkStart();

    var formData =
        'ip=' + ip +
        '&jtapiUsername=' + jtapiUsername +
        '&jtapiPassword=' + jtapiPassword;
    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/CallManager/Save',
        contentType: "application/x-www-form-urlencoded;charset=ISO-8859-1",
        processData: false,
        data: formData,
        type: 'POST',
        dataType: 'html',
        async: false
    }).responseXML;

    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
        editing = true;
        backgroundWorkEnd();
        return;
    }

    jQuery("#btnEditCallManager").show();
    jQuery("#btnSaveCallManager").hide();
    jQuery("#btnCancelCallManager").hide();
    jQuery("#formSetup > .formList input").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#formSetup > .formList select").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#errorLabel").html("");
    loadCallManager();
    backgroundWorkEnd();
}
function cancelEdit() {
    if (!editing) { return; }
    editing = false;
    backgroundWorkStart();
    jQuery("#btnEditCallManager").show();
    jQuery("#btnSaveCallManager").hide();
    jQuery("#btnCancelCallManager").hide();
    jQuery("#formSetup > .formList input").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#formSetup > .formList select").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#errorLabel").html("");
    loadCallManager();
    backgroundWorkEnd();
}

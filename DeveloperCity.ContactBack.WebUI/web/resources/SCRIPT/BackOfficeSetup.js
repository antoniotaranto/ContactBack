var editing = false;
function backgroundWorkStart() {
    jQuery("#BackgroundWork").show();
}
function backgroundWorkEnd() {
    jQuery("#BackgroundWork").hide();
}
function loadSetup() {
    if (editing) {return;}
    backgroundWorkStart();
    jQuery.ajax({
        url: rootPath + 'BackOffice/Setup/Json',
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (status == 'success') {
                var timeBetweenCallBacks = data.timeBetweenCallBacks == null ? "" : data.timeBetweenCallBacks;
                var lateCallBackAfter = data.lateCallBackAfter == null ? "" : data.lateCallBackAfter;
                var lateCallBackTime = data.lateCallBackTime == null ? "" : data.lateCallBackTime;
                var endOfQueue = data.endOfQueue == null ? "" : data.endOfQueue;
                var maxCallBacks = data.maxCallBacks == null ? "" : data.maxCallBacks;
                var cti_DeviceName = data.CTI_DeviceName == null ? "" : data.CTI_DeviceName;
                var prefixDial = data.prefixDial == null ? "" : data.prefixDial;
                var shiftWeekdayStartHour = data.shiftWeekdayStartHour == null ? "" : data.shiftWeekdayStartHour;
                var shiftWeekdayEndHour = data.shiftWeekdayEndHour == null ? "" : data.shiftWeekdayEndHour;
                var shiftWeekdayStartMinute = data.shiftWeekdayStartMinute == null ? "" : data.shiftWeekdayStartMinute;
                var shiftWeekdayEndMinute = data.shiftWeekdayEndMinute == null ? "" : data.shiftWeekdayEndMinute;
                var shiftSaturdayStartHour = data.shiftSaturdayStartHour == null ? "" : data.shiftSaturdayStartHour;
                var shiftSaturdayEndHour = data.shiftSaturdayEndHour == null ? "" : data.shiftSaturdayEndHour;
                var shiftSaturdayStartMinute = data.shiftSaturdayStartMinute == null ? "" : data.shiftSaturdayStartMinute;
                var shiftSaturdayEndMinute = data.shiftSaturdayEndMinute == null ? "" : data.shiftSaturdayEndMinute;
                var internalExtensionDestination = data.internalExtensionDestination == null ? "" : data.internalExtensionDestination;
                var mobilePhoneDestination = data.mobilePhoneDestination == null ? "" : data.mobilePhoneDestination;
                var landLineDestination = data.landLineDestination == null ? "" : data.landLineDestination;
                var invalidNumberDestination = data.invalidNumberDestination == null ? "" : data.invalidNumberDestination;
                var queueSuccessDevice = data.queueSuccessDevice == null ? "" : data.queueSuccessDevice;
                var queueAlreadyDevice = data.queueAlreadyDevice == null ? "" : data.queueAlreadyDevice;
                var queueInvalidNumberDevice = data.queueInvalidNumberDevice == null ? "" : data.queueInvalidNumberDevice;
                var queueNotInShiftTimeDevice = data.queueNotInShiftTimeDevice == null ? "" : data.queueNotInShiftTimeDevice;
                var smsUrl = data.SMSUrl == null ? "" : data.SMSUrl;
                var smsMessage = data.SMSMessage == null ? "" : data.SMSMessage;
                var smsAccount = data.SMSAccount == null ? "" : data.SMSAccount;
                var smsCode = data.SMSCode == null ? "" : data.SMSCode;
                var smsFrom = data.SMSFrom == null ? "" : data.SMSFrom;
                var defaultPassword = data.defaultPassword == null ? "" : data.defaultPassword;
                var proxyIP = data.proxyIP == null ? "" : data.proxyIP;
                var proxyPort = data.proxyPort == null ? "" : data.proxyPort;
                var ivr_DeviceName = data.IVR_DeviceName == null ? "" : data.IVR_DeviceName;
                var voiceFolder = data.voiceFolder == null ? "" : data.voiceFolder;

                jQuery("#txtTimeBetweenCallBacks").val( timeBetweenCallBacks );
                jQuery("#txtLateCallBackAfter").val( lateCallBackAfter );
                jQuery("#txtLateCallBackTime").val( lateCallBackTime );
                jQuery("#txtEndOfQueue").val( endOfQueue );
                jQuery("#txtMaxCallBacks").val( maxCallBacks );
                jQuery("#txtCTI_DeviceName").val( cti_DeviceName );
                jQuery("#txtPrefixDial").val( prefixDial );
                jQuery("#txtShiftWeekdayStart").val( shiftWeekdayStartHour.toString() + ":" + ("00".substr(shiftWeekdayStartMinute.toString().length)) + shiftWeekdayStartMinute.toString() );
                jQuery("#txtShiftWeekdayEnd").val( shiftWeekdayEndHour.toString() + ":" + ("00".substr(shiftWeekdayEndMinute.toString().length)) + shiftWeekdayEndMinute.toString() );
                jQuery("#txtShiftSaturdayStart").val( shiftSaturdayStartHour.toString() + ":" + ("00".substr(shiftSaturdayStartMinute.toString().length)) + shiftSaturdayStartMinute.toString() );
                jQuery("#txtShiftSaturdayEnd").val( shiftSaturdayEndHour.toString() + ":" + ("00".substr(shiftSaturdayEndMinute.toString().length)) + shiftSaturdayEndMinute.toString() );
                jQuery("#ddlInternalExtensionDestination").val( internalExtensionDestination );
                jQuery("#ddlMobilePhoneDestination").val( mobilePhoneDestination );
                jQuery("#ddlLandLineDestination").val( landLineDestination );
                jQuery("#ddlInvalidNumberDestination").val( invalidNumberDestination );
                jQuery("#txtQueueSuccessDevice").val( queueSuccessDevice );
                jQuery("#txtQueueAlreadyDevice").val( queueAlreadyDevice );
                jQuery("#txtQueueInvalidNumberDevice").val( queueInvalidNumberDevice );
                jQuery("#txtQueueNotInShiftTimeDevice").val( queueNotInShiftTimeDevice );
                jQuery("#txtSMSUrl").val( smsUrl );
                jQuery("#txtSMSMessage").val( smsMessage );
                jQuery("#txtSMSAccount").val( smsAccount );
                jQuery("#txtSMSCode").val( smsCode );
                jQuery("#txtSMSFrom").val( smsFrom );
                jQuery("#txtDefaultPassword").val( defaultPassword );
                jQuery("#txtProxyIP").val( proxyIP );
                jQuery("#txtProxyPort").val( proxyPort );
                jQuery("#txtIVR_DeviceName").val( ivr_DeviceName );
                jQuery("#txtVoiceFolder").val( voiceFolder );
                backgroundWorkEnd();
            }
        }
    });
}

function startEdit() {
    if (editing) {return;}
    editing = true;
    backgroundWorkStart();
    jQuery("#btnSaveSetup").show();
    jQuery("#btnCancelSetup").show();
    jQuery("#btnEditSetup").hide();
    jQuery("#btnCommitSetup").hide();
    jQuery("#formSetup > .formList input").each(function() {jQuery(this).removeAttr("disabled");jQuery(this).removeAttr("readonly");jQuery(this).focus();});
    jQuery("#formSetup > .formList select").each(function() {jQuery(this).removeAttr("disabled");jQuery(this).removeAttr("readonly");jQuery(this).focus();});
    jQuery("#formSetup > .formList input:first").focus();
    backgroundWorkEnd();
}
function saveEdit() {
    if (!editing) {return;}
    editing = false;

    var timeBetweenCallBacks = jQuery("#txtTimeBetweenCallBacks").val(  );
    var lateCallBackAfter = jQuery("#txtLateCallBackAfter").val(  );
    var lateCallBackTime = jQuery("#txtLateCallBackTime").val(  );
    var endOfQueue = jQuery("#txtEndOfQueue").val(  );
    var maxCallBacks = jQuery("#txtMaxCallBacks").val(  );
    var cti_DeviceName = jQuery("#txtCTI_DeviceName").val(  );
    var prefixDial = jQuery("#txtPrefixDial").val(  );
    var shiftWeekdayStart = jQuery("#txtShiftWeekdayStart").val(  );
    var shiftWeekdayEnd = jQuery("#txtShiftWeekdayEnd").val(  );
    var shiftSaturdayStart = jQuery("#txtShiftSaturdayStart").val(  );
    var shiftSaturdayEnd  = jQuery("#txtShiftSaturdayEnd").val(  );
    var internalExtensionDestination = jQuery("#ddlInternalExtensionDestination").val(  );
    var mobilePhoneDestination = jQuery("#ddlMobilePhoneDestination").val(  );
    var landLineDestination = jQuery("#ddlLandLineDestination").val(  );
    var invalidNumberDestination = jQuery("#ddlInvalidNumberDestination").val(  );
    var queueSuccessDevice = jQuery("#txtQueueSuccessDevice").val(  );
    var queueAlreadyDevice = jQuery("#txtQueueAlreadyDevice").val(  );
    var queueInvalidNumberDevice = jQuery("#txtQueueInvalidNumberDevice").val(  );
    var queueNotInShiftTimeDevice = jQuery("#txtQueueNotInShiftTimeDevice").val(  );
    var smsUrl = jQuery("#txtSMSUrl").val();
    var smsMessage = jQuery("#txtSMSMessage").val();
    var smsAccount = jQuery("#txtSMSAccount").val();
    var smsCode = jQuery("#txtSMSCode").val();
    var smsFrom = jQuery("#txtSMSFrom").val();
    var defaultPassword = jQuery("#txtDefaultPassword").val(  );
    var proxyIP = jQuery("#txtProxyIP").val(  );
    var proxyPort = jQuery("#txtProxyPort").val(  );
    var ivr_DeviceName = jQuery("#txtIVR_DeviceName").val(  );
    var voiceFolder = jQuery("#txtVoiceFolder").val(  );

    var re = /^[0-9]{1,4}$/;
    if (!maxCallBacks.match(re)) {jQuery("#txtMaxCallBacks").focus();jQuery("#errorLabel").html("Campo num&eacute;rico incorreto.");editing = true;return;}
    if (!endOfQueue.match(re)) {jQuery("#txtEndOfQueue").focus();jQuery("#errorLabel").html("Campo num&eacute;rico incorreto.");editing = true;return;}
    if (!timeBetweenCallBacks.match(re)) {jQuery("#txtTimeBetweenCallBacks").focus();jQuery("#errorLabel").html("Campo num&eacute;rico incorreto.");editing = true;return;}
    if (!lateCallBackAfter.match(re)) {jQuery("#txtLateCallBackAfter").focus();jQuery("#errorLabel").html("Campo num&eacute;rico incorreto.");editing = true;return;}
    if (!lateCallBackTime.match(re)) {jQuery("#txtLateCallBackTime").focus();jQuery("#errorLabel").html("Campo num&eacute;rico incorreto.");editing = true;return;}

    re = /^(([0-1]?[0-9])|(2[0-3])):([0-5][0-9])$/;
    if (!shiftWeekdayStart.match(re)) {jQuery("#txtShiftWeekdayStart").focus();jQuery("#errorLabel").html("In&iacute;cio do turno semanal incorreto.");editing = true;return;}
    if (!shiftWeekdayEnd.match(re)) {jQuery("#txtShiftWeekdayEnd").focus();jQuery("#errorLabel").html("Encerramento do turno semanal incorreto.");editing = true;return;}
    if (!shiftSaturdayStart.match(re)) {jQuery("#txtShiftSaturdayStart").focus();jQuery("#errorLabel").html("In&iacute;cio do turno do s&aacute;bado incorreto.");editing = true;return;}
    if (!shiftSaturdayEnd.match(re)) {jQuery("#txtShiftSaturdayEnd").focus();jQuery("#errorLabel").html("Encerramento do turno do s&aacute;bado incorreto.");editing = true;return;}

    backgroundWorkStart();

    var formData =
        'timeBetweenCallBacks=' + timeBetweenCallBacks +
        '&lateCallBackAfter=' + lateCallBackAfter +
        '&lateCallBackTime=' + lateCallBackTime +
        '&endOfQueue=' + endOfQueue +
        '&maxCallBacks=' + maxCallBacks +
        '&cti_DeviceName=' + cti_DeviceName +
        '&prefixDial=' + prefixDial +
        '&shiftWeekdayStart=' + shiftWeekdayStart +
        '&shiftWeekdayEnd=' + shiftWeekdayEnd +
        '&shiftSaturdayStart=' + shiftSaturdayStart +
        '&shiftSaturdayEnd=' + shiftSaturdayEnd +
        '&internalExtensionDestination=' + internalExtensionDestination +
        '&mobilePhoneDestination=' + mobilePhoneDestination +
        '&landLineDestination=' + landLineDestination +
        '&invalidNumberDestination=' + invalidNumberDestination +
        '&queueSuccessDevice=' + queueSuccessDevice +
        '&queueAlreadyDevice=' + queueAlreadyDevice +
        '&queueInvalidNumberDevice=' + queueInvalidNumberDevice +
        '&queueNotInShiftTimeDevice=' + queueNotInShiftTimeDevice +
        '&smsUrl=' + smsUrl +
        '&smsMessage=' + smsMessage +
        '&smsAccount=' + smsAccount +
        '&smsCode=' + smsCode +
        '&smsFrom=' + smsFrom +
        '&defaultPassword=' + defaultPassword +
        '&proxyIP=' + proxyIP +
        '&proxyPort=' + proxyPort +
        '&ivr_DeviceName=' + ivr_DeviceName +
        '&voiceFolder=' + voiceFolder;
    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/Setup/Save',
        processData: false,
        data: formData,
        type: 'POST',
        contentType: "application/x-www-form-urlencoded;charset=ISO-8859-1",
        dataType: 'html',
        async: false
    }).responseXML;

    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
        editing = true;
        backgroundWorkEnd();
        return;
    }
    
    jQuery("#btnEditSetup").show();
    jQuery("#btnCommitSetup").show();
    jQuery("#btnSaveSetup").hide();
    jQuery("#btnCancelSetup").hide();
    jQuery("#formSetup > .formList input").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#formSetup > .formList select").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#errorLabel").html("");
    loadSetup();
    backgroundWorkEnd();
}
function cancelEdit() {
    if (!editing) {return;}
    editing = false;
    backgroundWorkStart();
    jQuery("#btnEditSetup").show();
    jQuery("#btnCommitSetup").show();
    jQuery("#btnSaveSetup").hide();
    jQuery("#btnCancelSetup").hide();
    jQuery("#formSetup > .formList input").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#formSetup > .formList select").each(function() {jQuery(this).attr("disabled", "disabled");jQuery(this).attr("readonly", "readonly");});
    jQuery("#errorLabel").html("");
    loadSetup();
    backgroundWorkEnd();
}
function commitEdit() {
    if (editing) {jQuery("#errorLabel").html("Salve as alterações antes de efetivar.");return;}

    backgroundWorkStart();
    var data = jQuery.ajax({
        url: rootPath + 'BackOffice/Setup/Refresh',
        processData: false,
        type: 'POST',
        dataType: 'html',
        async: false
    }).responseXML;
    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
    } else {
        jQuery("#errorLabel").html("");
    }
    backgroundWorkEnd();

    loadSetup();
}

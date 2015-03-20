var editing = false;
function backgroundWorkStart() {
    jQuery("#BackgroundWork").show();
}
function backgroundWorkEnd() {
    jQuery("#BackgroundWork").hide();
}
function loadWebQueue() {
    if (editing) { return; }
    backgroundWorkStart();
    jQuery("#scheduleDetails").hide();
    jQuery("#formWebQueue").hide();
    jQuery("#btnSaveQueue").hide();
    jQuery("#btnCancelQueue").hide();
    jQuery("#btnAddQueue").hide();
    jQuery("#txtUsername").val("");
    jQuery("#txtCallbackNumber").val("");
    jQuery.ajax({
        url: rootPath + 'CockPit/WebQueue/Json',
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            if (data == null) {
                noScheduleYet();
            } else {
                var callBackNumber = data.callBackNumber == null ? "" : data.callBackNumber;
                var dontCallBefore = data.dontCallBefore == null ? null : new Date(data.dontCallBefore);
                var callTime = data.callTime == null ? null : new Date(data.callTime);
                var scheduleTime = data.scheduleTime == null ? null : new Date(data.scheduleTime);
                var estimatedTimeToAttend = data.estimatedTimeToAttend == null ? null : new Date(data.estimatedTimeToAttend);
                var entryPosition = data.entryPosition == null ? "" : data.entryPosition;
                var currentStatus = "";
                if (data.queueStatus != null) {
                    switch (data.queueStatus) {
                        case "None":
                            currentStatus = "Nenhum";
                            break;
                        case "URA":
                            currentStatus = "URA";
                            break;
                        case "QueueProcessing":
                            currentStatus = "Ingressando na fila";
                            break;
                        case "InQueue":
                            currentStatus = "Na fila";
                            break;
                        case "Planning":
                            currentStatus = "Discando";
                            break;
                        case "CallStatus":
                            currentStatus = "Em ligação";
                            break;
                        case "ManuallyRemoved":
                            currentStatus = "Removido manualmente";
                            break;
                        case "AutoRemoved":
                            currentStatus = "Removido por ocupado";
                            break;
                        case "Complete":
                            currentStatus = "Atendido com sucesso";
                            break;
                    }
                }
                var attendCount = data.attendCount == null ? "" : data.attendCount;
                var userID = data.userID == null ? "" : data.userID;
                var username = data.customer == null || data.customer.name == null ? "" : data.customer.name;

                jQuery("#lblUsername").html( username );
                jQuery("#lblScheduleTime").html( scheduleTime == null ? "" : (scheduleTime.getDate() + "/" + (scheduleTime.getMonth() + 1) + "/" + scheduleTime.getFullYear() + " " + scheduleTime.toLocaleTimeString()) );
                jQuery("#lblCallBackNumber").html( callBackNumber );
                jQuery("#lblEntryPosition").html( (entryPosition + 1) + "&ordf;");
                jQuery("#lblEstimatedTimeToAttend").html( estimatedTimeToAttend == null ? "" : (estimatedTimeToAttend.getDate() + "/" + (estimatedTimeToAttend.getMonth() + 1) + "/" + estimatedTimeToAttend.getFullYear() + " " + estimatedTimeToAttend.toLocaleTimeString()) );
                jQuery("#lblAttendCount").html( attendCount );
                jQuery("#lblCurrentStatus").html( currentStatus );
                jQuery("#lblDontCallBefore").html( dontCallBefore == null ? "" : (dontCallBefore.getDate() + "/" + (dontCallBefore.getMonth() + 1) + "/" + dontCallBefore.getFullYear() + " " + dontCallBefore.toLocaleTimeString()) );
                jQuery("#scheduleDetails").show();
                jQuery("#errorLabel").html( "" );
            }
            backgroundWorkEnd();
        },
        error: function(status) {
            noScheduleYet();
        }
    });
}
function noScheduleYet() {
    jQuery("#formWebQueue").show();
    jQuery("#btnAddQueue").show();
    backgroundWorkEnd();
}
function addQueue() {
    if (editing) { return; }
    editing = true;
    backgroundWorkStart();
    jQuery("#btnSaveQueue").show();
    jQuery("#btnCancelQueue").show();
    jQuery("#btnAddQueue").hide();
    jQuery("#txtUsername").val(sessionUser);
    jQuery("#txtCallbackNumber").val(sessionMobilePhone);
    jQuery("#txtCallbackNumber").removeAttr("disabled");
    jQuery("#txtCallbackNumber").removeAttr("readonly");
    jQuery("#txtCallbackNumber").focus();
    backgroundWorkEnd();
}
function saveQueue() {
    if (!editing) { return; }

    jQuery("#txtUsername").val(sessionUser);
    var callbackNumber = jQuery("#txtCallbackNumber").val(  );

    backgroundWorkStart();

    var formData =
        'callbackNumber=' + callbackNumber;
    var data = jQuery.ajax({
        url: rootPath + 'CockPit/WebQueue',
        contentType: "application/x-www-form-urlencoded;charset=ISO-8859-1",
        processData: false,
        data: formData,
        type: 'POST',
        dataType: 'xml',
        async: false
    }).responseXML;

    if (jQuery(data) == null || jQuery(data).find('Success') == null || jQuery(data).find('Success').text().toString().indexOf('false') >= 0) {
        jQuery("#errorLabel").html( jQuery(data).find('Details').text() );
        editing = true;
        backgroundWorkEnd();
        return;
    }

    jQuery("#errorLabel").html( "" );
    editing = false;
    loadWebQueue();
}
function cancelQueue() {
    if (!editing) { return; }
    editing = false;
    jQuery("#errorLabel").html( "" );
    loadWebQueue();
}

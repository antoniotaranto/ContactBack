var tolerance = 20;
var lastCallItem = 0;
var refreshTime = 15000;
var lastUnfinished = 0;

function pageLoad() {
    refreshCalls();
}
function refreshCalls() {
    backgroundWorkStart();
    var ajaxURL = rootPath + 'Agent/CallList/List/' + lastCallItem;
    var firstAccess = (lastCallItem == 0);

    jQuery.ajax({
        url: ajaxURL,
        type: 'POST',
        dataType: 'json',
        success: function(data, status) {
            if (firstAccess) { jQuery("#callList > tbody > tr").remove(); }
            jQuery("#callList > tbody > tr.unfinished").remove();
            if (data == null || data.length == 0) {
                backgroundWorkEnd();
                return;
            }
            for (var i = (data.length - 1); i >= 0; i--) {
                var rows = "";
                var numberLength = data[i].queue.callBackNumber.length;
                var areaCode = data[i].queue.callBackNumber.substr(0, 2);
                var numberPrefix = numberLength == 11 ? data[i].queue.callBackNumber.substr(2, 5) : data[i].queue.callBackNumber.substr(2, 4);
                var numberSuffix = numberLength == 11 ? data[i].queue.callBackNumber.substr(7, 4) : data[i].queue.callBackNumber.substr(6, 4);
                var number = "(" + areaCode + ") " + numberPrefix + "-" + numberSuffix;
                var startTime = new Date(data[i].startTime);
                var answerTime = data[i].answerTime == null ? null : new Date(data[i].answerTime);
                var endTime = data[i].endTime == null ? null : new Date(data[i].endTime);

                if (endTime != null && data[i].callID > lastCallItem) {lastCallItem = data[i].callID;}
                var staticLine = (endTime != null);

                var callStatus = "";
                switch (data[i].callStatus) {
                    case "Dialing":
                        callStatus = "Discando";
                        break;
                    case "Ringing":
                        callStatus = "Campainha";
                        break;
                    case "Talking":
                        callStatus = "Em conversa";
                        break;
                    case "NoAnswer":
                        callStatus = "Sem resposta";
                        break;
                    case "Complete":
                        callStatus = "Sucesso";
                        break;
                    case "VoiceMachine":
                        callStatus = "Caixa postal";
                        break;
                    case "Error":
                        callStatus = "Erro";
                        break;
                    case "Busy":
                        callStatus = "Ocupado";
                        break;
                    default:
                        callStatus = "Desconhecido";
                        break;
                }

                var action = "";
                if (data[i].duration <= tolerance && data[i].callStatus == "Complete") {
                    action = "<a href=\"javascript:setVoiceMachine(" + data[i].callID + ");\">Caixa Postal</a>";
                }
                var hidden = "";
                var unfinished = "";
                if (!firstAccess && data[i].callID > lastUnfinished) {hidden = " style=\"display: none;\"";}
                if (!staticLine) {unfinished = " class=\"unfinished\"";}
                rows += "<tr" + hidden + unfinished + ">";
                rows += "<td>" + number + "</td>";
                rows += "<td>" + startTime.toLocaleTimeString() + "</td>";
                rows += "<td>" + (answerTime == null ? "" : answerTime.toLocaleTimeString()) + "</td>";
                rows += "<td>" + (endTime == null ? "" : endTime.toLocaleTimeString()) + "</td>";
                rows += "<td>" + callStatus + "</td>";
                rows += "<td>" + data[i].durationString + "</td>";
                rows += "<td>" + action + "</td>";
                rows += "</tr>\r\n";
                var el = jQuery(rows).prependTo("#callList > tbody");
                if (!firstAccess && data[i].callID > lastUnfinished) {jQuery(el).fadeIn(3000);}
            }
            lastUnfinished = data[0].callID;
            backgroundWorkEnd();
        },
        error: function() {
            backgroundWorkEnd();
        }
    });
    t=setTimeout("refreshCalls()",refreshTime);
}
function backgroundWorkStart() {
    jQuery("#BackgroundWork").show();
}
function backgroundWorkEnd() {
    jQuery("#BackgroundWork").hide();
}
function setVoiceMachine(callID) {
    var change = confirm(msgConfirmation);
    if (!change) {
        return;
    }
    backgroundWorkStart();
    var ajaxURL = rootPath + 'Agent/CallList/Save/' + callID;

    jQuery.ajax({
        url: ajaxURL,
        type: 'POST',
        dataType: 'json',
        success: function(data, status) {
            if (data.error == "") {
                jQuery("#errorLabel").html( "" );
                lastCallItem = 0;
                lastUnfinished = 0;
            } else {
                jQuery("#errorLabel").html( data.error );
                backgroundWorkEnd();
            }
        },
        error: function(XMLHttpRequest, errorKind, errorMessage) {
            jQuery("#errorLabel").html( (errorMessage == null || errorMessage == "" ? "Erro de execu&ccedil;&atilde;o, por favor tente novamente." : errorMessage) );
            backgroundWorkEnd();
        }
    });
}
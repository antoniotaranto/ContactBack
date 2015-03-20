var MODE_AGENT = 0;
var MODE_QUEUE = 1;
var MODE_CALL = 2;
transitionTime = 350;
refreshTime = 20;
var currentMode = MODE_CALL;

function pageLoad() {
    activeChanger();
}
function backgroundWorkStart() {
    jQuery("#BackgroundWork").show();
}
function backgroundWorkEnd() {
    jQuery("#BackgroundWork").hide();
}
function activeChanger() {
    if (currentMode == MODE_CALL) {
        currentMode = MODE_AGENT;
    } else {currentMode++;}
    showUserControl();
}
function showUserControl() {
    switch(currentMode) {
        case MODE_AGENT:
            showAgent();
            break;
        case MODE_QUEUE:
            showQueue();
            break;
        case MODE_CALL:
            showCall();
            break;
        default:
            break;
    }
}
function showAgent() {
    backgroundWorkStart();
    var ajaxURL = rootPath + 'CockPit/TV/Agent';
    jQuery.ajax({
        url: ajaxURL,
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            jQuery("#mainLayer").hide(transitionTime, function() {
                jQuery("#mainLayer").html("");
                var control = "<h1>Agentes</h1>\r\n";
                control += "<p>Dispon&iacute;vel: " + data.availablecount + " posi&ccedil;" + (data.availablecount == 1 ? "&atilde;o" : "&otilde;es") + "</p>\r\n";
                control += "<p>Em atendimento: " + data.incallcount + " posi&ccedil;" + (data.incallcount == 1 ? "&atilde;o" : "&otilde;es") + "</p>\r\n";
                control += "<p>Em pausa: " + data.inbreakcount + " posi&ccedil;" + (data.inbreakcount == 1 ? "&atilde;o" : "&otilde;es") + "</p>\r\n";
                control += "<p>Offline: " + data.offlinecount + " posi&ccedil;" + (data.offlinecount == 1 ? "&atilde;o" : "&otilde;es") + "</p>\r\n";
                control += "<p>TMA do dia: " + data.avgtalktime + "</p>\r\n";
                control += "<p>TMR do dia: " + data.avgwaittime + "</p>\r\n";
                jQuery("#mainLayer").html(control);
                t=setTimeout("activeChanger()", refreshTime * 1000);
                backgroundWorkEnd();
                jQuery("#mainLayer").show(transitionTime);
            });
        },
        error: function(status) {
            backgroundWorkEnd();
            activeChanger();
        }
    });
}
function showQueue() {
    backgroundWorkStart();
    var ajaxURL = rootPath + 'CockPit/TV/Queue';
    jQuery.ajax({
        url: ajaxURL,
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            jQuery("#mainLayer").hide(transitionTime, function() {
                jQuery("#mainLayer").html("");
                var control = "<h1>Fila</h1>\r\n";
                var ddd = "";
                if (data.areacode1 == "") { ddd = "Nenhum"; }
                else if (data.areacode2 == "") { ddd = data.areacode1 + " <span class='comment'>(" + data.areacode1count + ")</span>"; }
                else { ddd = data.areacode1 + " <span class='comment'>(" + data.areacode1count + ")</span>, " + data.areacode2 + " <span class='comment'>(" + data.areacode2count + ")</span>"; }
                control += "<p>Na fila: " + data.queuecount + " cliente" + (data.queuecount == 1 ? "" : "s") + "</p>\r\n";
                control += "<p>Na fila, congelados: " + data.frozencount + " cliente" + (data.frozencount == 1 ? "" : "s") + "</p>\r\n";
                control += "<p>Em atendimento: " + data.incallcount + " cliente" + (data.incallcount == 1 ? "" : "s") + "</p>\r\n";
                control += "<p>DDD mais comuns: " + ddd + "</p>\r\n";
                control += "<p>Maior espera: " + data.longestwaittime + "</p>\r\n";
                control += "<p>De: " + data.longestwaitfrom + "</p>\r\n";
                control += "<p>TMR atual: " + data.avgwaittime + "</p>\r\n";
                jQuery("#mainLayer").html(control);
                t=setTimeout("activeChanger()", refreshTime * 1000);
                backgroundWorkEnd();
                jQuery("#mainLayer").show(transitionTime);
            });
        },
        error: function(status) {
            backgroundWorkEnd();
            activeChanger();
        }
    });
}
function showCall() {
    backgroundWorkStart();
    var ajaxURL = rootPath + 'CockPit/TV/Call';
    jQuery.ajax({
        url: ajaxURL,
        type: 'GET',
        dataType: 'json',
        success: function(data, status) {
            jQuery("#mainLayer").hide(transitionTime, function() {
                jQuery("#mainLayer").html("");
                var control = "<h1>Chamadas em andamento</h1>\r\n";
                control += "<p>Em atendimento: " + data.incallcount + " cliente" + (data.incallcount == 1 ? "" : "s") + "</p>\r\n";
                control += "<p>Maior espera: " + data.longestwaittime + "</p>\r\n";
                control += "<p>De: " + data.longestwaitfrom + "</p>\r\n";
                control += "<p>TMA em andamento: " + data.avgtalktime + "</p>\r\n";
                control += "<p>TMR em andamento: " + data.avgwaittime + "</p>\r\n";
                jQuery("#mainLayer").html(control);
                t=setTimeout("activeChanger()", refreshTime * 1000);
                backgroundWorkEnd();
                jQuery("#mainLayer").show(transitionTime);
            });
        },
        error: function(status) {
            backgroundWorkEnd();
            activeChanger();
        }
    });
}
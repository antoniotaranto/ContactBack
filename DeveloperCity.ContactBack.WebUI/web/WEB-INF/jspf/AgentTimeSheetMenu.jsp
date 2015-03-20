<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %>
<script type="text/javascript" language="Javascript">
function doStartSession() {
    startSession();
}
function doEndSession() {
    endSession();
}
function doStartBreak() {
    var breakTypeID = 0;
    try {
        breakTypeID = parseInt(jQuery("#cboBreakType").val(), 10);
    } catch(e) { }
    if (breakTypeID == 0 || isNaN(breakTypeID)) {
        jQuery("#lblError").text("Para iniciar uma pausa, escolha o tipo da pausa no campo abaixo.");
        return;
    }
    startBreak(breakTypeID);
}
function doRestartSession() {
    var breakTypeID = 0;
    try {
        breakTypeID = parseInt(jQuery("#cboBreakType").val(), 10);
    } catch(e) { }
    if (breakTypeID == 0 || isNaN(breakTypeID)) {
        jQuery("#lblError").text("Para reiniciar uma sessão, indique a pausa que esteve realizando no campo abaixo.");
        return;
    }
    restartSession(breakTypeID);
}
</script>
<fieldset class="formList">
    <legend><span class="fixpng"><b>GERENCIAMENTO DE PAUSAS E DE LOGIN</b></span></legend>
    <ul>
<c:choose>
    <c:when test="${Status == 'Break'}">
        <li class="StatusBreak">
            <span id="lblAgentStatus">Pausa</span>
        </li>
    </c:when>
    <c:when test="${Status == 'SystemBreak'}">
        <li class="StatusSystemBreak">
            <span id="lblAgentStatus">Ramal Bloqueado</span>
        </li>
    </c:when>
    <c:when test="${Status == 'Online'}">
        <c:if test="${requestingBreak != '0'}">
            <c:set var="reqBreak" value=" (pausa)" />
        </c:if>
        <c:if test="${requestingLogoff}">
            <c:set var="reqLogoff" value=" (fim)" />
        </c:if>

        <li class="StatusOnline">
            <span id="lblAgentStatus">Online${reqBreak}${reqLogoff}</span>
        </li>
    </c:when>
    <c:otherwise>
        <li class="StatusOffline">
            <span id="lblAgentStatus">Offline</span>
        </li>
    </c:otherwise>
</c:choose>

<c:if test="${!empty message}">
        <li>
            <span>${message}</span>
        </li>
</c:if>
        <li>
            <span id="lblError" class="error"></span>
        </li>

<c:if test="${Status == 'SystemBreak' || Status == 'Online' || Status == 'AlreadyLoggedToday' }">
        <li>
            <label>Tipo de Pausa:</label>
            <c:import url="/Controls/DropDownBreakTypeControl"></c:import>
        </li>
</c:if>
    </ul>
</fieldset>
<fieldset class="buttons">
    <legend></legend>
    <ul>
<c:choose>
    <c:when test="${Status == 'Break'}">
        <li><a href="javascript:doEndSession();">Finalizar Sessão</a></li>
    </c:when>
    <c:when test="${Status == 'SystemBreak'}">
        <li><a href="javascript:doStartBreak();">Iniciar Pausa</a></li>
        <li><a href="javascript:doEndSession();">Finalizar Sessão</a></li>
    </c:when>
    <c:when test="${Status == 'Online'}">
        <li><a href="javascript:doStartBreak();">Iniciar Pausa</a></li>
        <li><a href="javascript:doEndSession();">Finalizar Sessão</a></li>
    </c:when>
    <c:when test="${Status == 'NeverLogged'}">
        <li><a href="javascript:doStartSession();">Iniciar Sessão</a></li>
    </c:when>
    <c:when test="${Status == 'AlreadyLoggedToday'}">
        <li><a href="javascript:doRestartSession();">Reabrir Sessão</a></li>
    </c:when>
    <c:when test="${Status == 'NotLoggedToday'}">
        <li><a href="javascript:doStartSession();">Iniciar Sessão</a></li>
    </c:when>
</c:choose>
        <li><a href="javascript:refreshData();">Atualizar Lista</a></li>
    </ul>
</fieldset>
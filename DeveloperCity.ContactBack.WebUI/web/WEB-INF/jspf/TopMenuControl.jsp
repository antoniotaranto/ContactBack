<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
                    <div class="outras-aplicacoes">
                        <strong>Acesse outros módulos do ContactBack</strong>
                        <ul>
                            <c:if test="${hasBackOfficeAccess}">
                                <li <c:if test="${backOfficeMenuClass}">class="active"</c:if>><a href="${rootPath}BackOffice">BackOffice</a></li>
                            </c:if>
                            <c:if test="${hasCockPitAccess}">
                            <li <c:if test="${cockPitMenuClass}">class="active"</c:if>><a href="${rootPath}CockPit">CockPit</a></li>
                            </c:if>
                            <c:if test="${hasAgentCockPitAccess}">
                            <li <c:if test="${agentMenuClass}">class="active"</c:if>><a href="${rootPath}Agent">Agent CockPit</a></li>
                            </c:if>
                        </ul>
                    </div>

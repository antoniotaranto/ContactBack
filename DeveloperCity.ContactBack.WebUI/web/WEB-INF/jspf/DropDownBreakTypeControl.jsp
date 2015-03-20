<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<select name="cboBreakType" id="cboBreakType">
    <option value="0">[Selecione]</option>
<c:forEach var="b" items="${breakTypeList}">
    <option value="${b.breakTypeID}">${b.description}</option>
</c:forEach>
</select>
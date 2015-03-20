<?xml version="1.0" encoding="ISO-8859-1"?>
<%@page contentType="text/xml" pageEncoding="ISO-8859-1"%>
<!DOCTYPE ajaxResponse PUBLIC '-//Apache Software Foundation//DTD Commons Validator Rules Configuration 1.1.3//EN' 'null'>
<ajaxResponse>
    <Success>${success}</Success>
    <Details>
        <![CDATA[
            ${errorMessage}
        ]]>
    </Details>
    <ResponseData>
        <![CDATA[
        ${responseData}
        ]]>
    </ResponseData>
</ajaxResponse>
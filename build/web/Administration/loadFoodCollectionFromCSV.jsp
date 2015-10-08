<%-- 
    Document   : loadFoodCollectionFromCSV
    Created on : 2015-10-05, 00:01:38
    Author     : oem
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Load data from CSV</title>
    </head>
    <body>
        <form id="form_mapping">
        <input type="hidden" name="submitted" value="true">
        <h1>Load data from CSV</h1>
        <h2><%=session.getAttribute("ObjectName")%></h2>
        
        
        <h3>Headers mapping</h3>
        <span class="clDescription">Fill the row number, for each nuttrition description</span>
        <table>
            <c:forEach var="headers" items="${sessionScope.headersMapping}">
                <tr><td></td><td>${requestScope[headers.key.concat("_val")]}</td></tr>
                <tr>
                    <td>${headers.key}</td>
                    <td><input type="text" name="${headers.key}" 
                               value="${empty param[headers.key] ? headers.value: param[headers.key]}"> </td>
                </tr>
            </c:forEach>
        </table>
        
        
        <h3>Datasource parameters</h3>
        <table>
            <c:forEach var="dsParams" items="${sessionScope.dataSourceParameters}">
            <tr><td></td><td>${requestScope[dsParams.key.concat("_dsval")]}</td></tr>
                <tr>
                    <td>${  dsParams.key}</td>
                    <td><input type="text" name="ds_${dsParams.key}" 
                               value="${empty param["ds_".concat(dsParams.key)] ? dsParams.value: param[dsParams.key]}"> </td>
                </tr>
            </c:forEach>
        </table>
        
        
        <h3>Mapping</h3>
         <span class="clDescription">Fill the column number, where below infromation would be stored</span>
        <table>
            <c:forEach var="mapping" items="${sessionScope.mapping}">
                <tr><td></td><td>${requestScope[mapping.key.concat("_mval")]}</td></tr>
                <tr>
                    <td>${mapping.key}</td>
                    <td><input type="text" name="ma_${mapping.key}" 
                               value="${empty param["ma_".concat(mapping.key)] ? mapping.value: param[mapping.key]}"> </td>
                </tr>
            </c:forEach>
        </table>
            <input type="submit" value="Send">
        </form>
    </body>
</html>

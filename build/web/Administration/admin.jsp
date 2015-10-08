<%-- 
    Document   : admin
    Created on : 2015-10-04, 16:52:54
    Author     : oem
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Settings</title>
    </head>
    <body>
        <h1>Menu Creator - Settings</h1>
        <form method="post" action="LoadData.do" enctype="multipart/form-data">
        <p>
        <h3>Food components</h3>
        <div class="clLabel">Existing components number: </div> <div class="clValue"> <%= request.getAttribute("foodComponentsNumber")%></div>
        </p>
            <input type="file" name="dataFile" id="fileChooser"/><br/><br/>
            <input type="submit" value="loadDataFromCSV" />
        </form>
        
    </body>
</html>

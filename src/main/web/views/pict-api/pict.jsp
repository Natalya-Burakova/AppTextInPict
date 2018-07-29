<%@ page import="json.Analysis" %>
<%@ page import="java.util.UUID" %><%--
  Created by IntelliJ IDEA.
  User: macbooknatalya
  Date: 24.07.18
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>Compare expression</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body class="w3-light-grey">

<div class="w3-card-4">
    <form action=<%="/api/picture/generate/" + UUID.randomUUID().toString()%>  method="post" class="w3-selection w3-light-orange w3-padding">
        <h2>Enter the text that will be converted to the image:</h2>
        <textarea type="text" name="in" cols="40" rows="10" required> </textarea>
        <button type="submit" class="w3-btn w3-blue w3-round-large w3-margin-bottom">send</button>
    </form>
</div>
</body>

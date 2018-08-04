<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>Title</title>
</head>
<body>

<jsp:include page="_header.jsp"/>

<c:choose>
    <c:when test="${not empty errorString}">
        <div id="message-green">
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tbody>
                <tr>
                    <td class="red-left">${errorString}</td>
                    <td class="red-right"><a class="close-green"><img
                            src="images/table/icon_close_red.gif" alt=""></a></td>
                </tr>
                </tbody>
            </table>
        </div>
        <br/>
    </c:when>
</c:choose>
<c:choose>
    <c:when test="${not empty successString}">
        <div id="message-green">
            <table border="0" width="100%" cellpadding="0" cellspacing="0">
                <tbody>
                <tr>
                    <td class="green-left">${successString}</td>
                    <td class="green-right"><a class="close-green"><img
                            src="images/table/icon_close_green.gif" alt=""></a></td>
                </tr>
                </tbody>
            </table>
        </div>
        <br/>
    </c:when>
</c:choose>

</body>
</html>

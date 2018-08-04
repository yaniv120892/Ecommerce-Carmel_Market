<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Carmel Market</title>
    <link rel="stylesheet" href="<c:url value="../../css/screen.css" />" type="text/css" media="screen"
          title="default"/>
    <!--  jquery core -->
    <script src="../../js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>

    <!-- Custom jquery scripts -->
    <script src="../../js/jquery/custom_jquery.js" type="text/javascript"></script>

    <!-- MUST BE THE LAST SCRIPT IN <HEAD></HEAD></HEAD> png fix -->
    <script src="../../js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $(document).pngFix();
        });
    </script>
</head>
<body id="login-bg">

<!-- Start: login-holder -->
<div id="login-holder">


    <div class="clear"></div>


    <!--  start loginbox ................................................................................. -->
    <div id="loginbox">
        <!--  start login-inner -->
        <div id="login-inner">
            <form id="myForm" method="post" action="${pageContext.request.contextPath}/createStore">
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <th>Store Name</th>
                        <td><input type="text" value="${newStoreName}" name="newStoreName" class="login-inp"/></td>
                    </tr>

                    <tr>
                        <th>Store Email</th>
                        <td><input type="text" value="${newStoreEmail}" name="newStoreEmail" class="login-inp"/></td>
                        </td>
                    </tr>

                    <tr>
                        <th></th>
                        <td><input type="submit" name="user" value="Create" class="submit-login"/></td>
                    </tr>
                </table>
            </form>
        </div>
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
        <!--  end login-inner -->
        <div class="clear"></div>
    </div>
    <!--  end loginbox -->


</div>
<!-- End: login-holder -->
</body>
</html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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

    <!-- start logo -->
    <div id="logo-login">
        <a><img src="../../images/shared/logo.png" width="156" height="40" alt=""/></a>
    </div>
    <!-- end logo -->

    <div class="clear"></div>


    <!--  start loginbox ................................................................................. -->
    <div id="loginbox">
        <!--  start login-inner -->
        <div id="login-inner">
            <form id="myForm" method="post" action="${pageContext.request.contextPath}/doRegister">
                <table border="0" cellpadding="0" cellspacing="0">
                    <tr>
                        <th>Email</th>
                        <td><input type="text" value="${email}" name="email" class="login-inp"/></td>
                    </tr>
                    <tr>
                        <th>Password</th>
                        <td><input type="password" value="" name="password" class="login-inp"/>
                        </td>
                    </tr>
                    <tr>
                        <th></th>
                        <td><input type="submit" name="Submit" value="Submit" class="submit-login"/></td>
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
        <a href="${pageContext.request.contextPath}/MainMenu" class="back-login">Go Back</a>
    </div>
    <!--  end loginbox -->
</div>
<!-- End: login-holder -->
</body>
</html>
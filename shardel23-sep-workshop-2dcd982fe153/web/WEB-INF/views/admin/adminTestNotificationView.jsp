<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>

<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Stores</h1>
        </div>
        <!-- end page-heading -->
        <c:choose>
            <c:when test="${not empty errorString}">
                <div id="message-red">
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

        <table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table">
            <tbody>
            <tr>
                <th rowspan="3" class="sized"><img src="images/shared/side_shadowleft.jpg" width="20" height="300"
                                                   alt=""></th>
                <th class="topleft"></th>
                <td id="tbl-border-top">&nbsp;</td>
                <th class="topright"></th>
                <th rowspan="3" class="sized"><img src="images/shared/side_shadowright.jpg" width="20" height="300"
                                                   alt=""></th>
            </tr>
            <tr>
                <td id="tbl-border-left"></td>
                <td>
                    <!--  start content-table-inner ...................................................................... START -->
                    <div id="content-table-inner">

                        <!--  start table-content  -->
                        <div id="table-content">

                            <!--  start message-green -->
                            <!--  start product-table ..................................................................................... -->
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">
                                <tbody>
                                <tr>
                                    <th class="table-header-repeat line-left"><a href="">Email</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Message</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Send</a></th>
                                    <th class="table-header-repeat line-left"></th>
                                </tr>
                                <c:forEach items="${listUsers}" var="user">
                                    <tr>
                                        <form id="sendMsg" method="post" action="${pageContext.request.contextPath}testNotifications?userID=${user.getKey()}">
                                            <td>${user.getKey()}</td>
                                            <td><input type="text" value="" name="msg" class="login-inp"/></td>
                                            <td><input type="submit" name="Send" value="Send"/></td>
                                        </form>
                                    </tr>
                                </c:forEach>
                                <tr>
                                    <form id="sendMsgAll" method="post" action="${pageContext.request.contextPath}testNotifications?toAll=true">
                                        <td>Send To All!</td>
                                        <td><input type="text" value="" name="msg" class="login-inp"/></td>
                                        <td><input type="submit" name="Send" value="Send"/></td>
                                    </form>
                                </tr>
                                </tbody>
                            </table>
                            <!--  end product-table................................... -->
                        </div>
                        <!--  end content-table  -->


                    </div>

                    <!--  end content-table-inner ............................................END  -->
                </td>
                <td id="tbl-border-right"></td>
            </tr>
            <tr>
                <th class="sized bottomleft"></th>
                <td id="tbl-border-bottom">&nbsp;</td>
                <th class="sized bottomright"></th>
            </tr>
            </tbody>
        </table>
        <div class="clear">&nbsp;</div>

    </div>
    <!--  end content -->
    <div class="clear">&nbsp;</div>
</div>
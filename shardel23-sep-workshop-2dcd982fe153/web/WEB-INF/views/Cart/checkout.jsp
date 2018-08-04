<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>







<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Checkout</h1>
        </div>
        <!-- end page-heading -->
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
                            <c:choose>
                                <c:when test="${param.delete=='YES'}">
                                    <div id="message-green">
                                        <table border="0" width="100%" cellpadding="0" cellspacing="0">
                                            <tbody>
                                            <tr>
                                                <td class="green-left">Store deleted sucessfully.</td>
                                                <td class="green-right"><a class="close-green"><img
                                                        src="images/table/icon_close_green.gif" alt=""></a></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <br/>
                                </c:when>
                                <c:when test="${param.edit=='YES'}">
                                    <div id="message-green">
                                        <table border="0" width="100%" cellpadding="0" cellspacing="0">
                                            <tbody>
                                            <tr>
                                                <td class="green-left">Store was edited sucessfully.</td>
                                                <td class="green-right"><a class="close-green"><img
                                                        src="images/table/icon_close_green.gif" alt=""></a></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <br/>
                                </c:when>
                            </c:choose>


                        <form id="myForm" method="post" action="${pageContext.request.contextPath}/checkout">
                            <table border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <th>Credit Number</th>
                                    <td><input type="text" value="${creditNumber}" name="creditNumber" class="login-inp"/></td>
                                </tr>
                                <tr>
                                    <th>Credit Validity</th>
                                    <td>
                                        <select id="yearValidity" name="yearValidity">
                                            <option value="2018">2018</option>
                                            <option value="2019">2019</option>
                                            <option value="2020">2020</option>
                                            <option value="2021">2021</option>
                                            <option value="2022">2022</option>
                                        </select>
                                        <select id="monthValidity" name="monthValidity">
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                            <option value="11">11</option>
                                            <option value="12">12</option>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th>CVV Code</th>
                                    <td><input type="text" value="${cvvCode}" name="cvvCode" class="login-inp"/></td>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Name</th>
                                    <td><input type="text" value="${name}" name="name" class="login-inp"/></td>
                                    </td>
                                </tr>
                                <tr>
                                    <th>ID</th>
                                    <td><input type="text" value="${id}" name="id" class="login-inp"/></td>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Address</th>
                                    <td><input type="text" value="${address}" name="address" class="login-inp"/></td>
                                    </td>
                                </tr>
                                <tr>
                                    <th>Country</th>
                                    <td>
                                        <select id="nationalities" name="nationalities">
                                            <c:forEach items="${nat}" var="op">
                                                <option value="${op}">${op}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th></th>
                                    <td><input type="submit" name="Pay" value="Pay" class="submit-login"/></td>
                                </tr>
                            </table>
                        </form>
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
</body>
</html>












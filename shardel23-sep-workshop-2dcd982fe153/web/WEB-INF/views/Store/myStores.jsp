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

                            <!--  start product-table ..................................................................................... -->
                            <form id="mainform" action="">
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">

                                    <tbody>
                                    <tr>
                                        <th class="table-header-repeat line-left"><a href="allStores?orderBy=1">Name</a>
                                        </th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                        <th class="table-header-repeat line-left"></th>
                                    </tr>
                                    <c:forEach items="${listStores}" var="store">
                                        <tr>
                                            <td>${store.getName()}</td>
                                            <td><a href="allItems?storeName=${store.getName()}">Items</a></td>
                                            <td><a href="saleItems?storeName=${store.getName()}&registered=true">Items
                                                In Sale</a></td>
                                            <td><a href="categoryDiscountsInStore?storeName=${store.getName()}">Category Discounts</a></td>

                                            <td>
                                                <a href="addOwnerOrManager?storeName=${store.getName()}&addOwner=true&addManager=false">Add
                                                    Owner</a></td>
                                            <td>
                                                <a href="addOwnerOrManager?storeName=${store.getName()}&addOwner=false&addManager=true">Add
                                                    Manager</a></td>
                                            <td>
                                                <a href="appointmentsHistory?storeName=${store.getName()}">
                                                    Appointments History
                                                </a></td>
                                            <td><a href="purchaseHistory?storeName=${store.getName()}">Purchase
                                                History</a></td>
                                            <td><a href="storePurchasePolicies?storeName=${store.getName()}">Purchase Policies</a></td>
                                            <td><a href="closeStore?storeName=${store.getName()}">Close Store</a></td>
                                        </tr>
                                    </c:forEach>


                                    </tbody>
                                </table>
                                <!--  end product-table................................... -->
                            </form>
                        </div>
                        <!--  end content-table  -->


                    </div>
                    <c:choose>

                    <c:when test="${userType == 'REGISTERED'}">
                    <div id="actions-box">
                        <a href="createStore" class="action-add"></a>
                        <div class="clear"></div>
                    </div>
                    <!--  end content-table-inner ............................................END  -->
                </td>
                </c:when>
                </c:choose>
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
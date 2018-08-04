<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>







<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>My Cart ${storeName}</h1>
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


                            <div><form id="add-coupon" method="post" action="${pageContext.request.contextPath}/addCouponToCart">
                                <table border="0" cellpadding="0" cellspacing="0" align="left">
                                    <tbody>
                                    <tr>
                                        <td>Coupon</td>
                                        <td><input type="text" value="${couponCode}" name="couponCode" class="login-inp"/></td>
                                        <td><input type="submit" name="Add" value="Add" class="submit-login"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form></div>
                            <div>&nbsp</div>

                            <div><h2>Normal Sale Items</h2>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">
                                    <tr>
                                        <th class="table-header-repeat line-left"><a href="">Normal Sale Item</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Original Price</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Price After Discount</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Amount</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Total</a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                    </tr>

                                    <c:forEach items="${cartItems}" var="tripel">
                                        <c:choose>
                                            <c:when test="${tripel.getSaleType() eq 'NORMAL'}">

                                        <tr>
                                            <c:choose>
                                            <c:when test="${itemNameToEdit == tripel.getItem().getName() && saleTypeToEdit eq 'NORMAL'}">
                                                <form id="editItem" method="post" action="${pageContext.request.contextPath}/editSaleItemInCart?storeName=${tripel.getItem().getStoreName()}&itemName=${tripel.getItem().getName()}&saleType=NORMAL" >
                                                        <td>${tripel.getItem().getName()}</td>
                                                        <td>${tripel.getItem().getOriginalPrice()}</td>
                                                        <td>${tripel.getItem().getPrice(listCoupon)}</td>
                                                        <td><input type="text" value="${tripel.getAmount()}" name="amount" class="login-inp"/></td>
                                                        <td>${tripel.getAmount() * tripel.getItem().getPrice(listCoupon)}</td>
                                                        <td><input type="submit" name="Save" value="Save"/></td>

                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>${tripel.getItem().getName()}</td>
                                                    <td>${tripel.getItem().getOriginalPrice()}</td>
                                                    <td>${tripel.getItem().getPrice(listCoupon)}</td>
                                                    <td>${tripel.getAmount()}</td>
                                                    <td>${tripel.getAmount() * tripel.getItem().getPrice(listCoupon)}</td>
                <td><a href="editSaleItemInCart?itemName=${tripel.getItem().getName()}&saleType=NORMAL">Change Quantity</a></td>
            </c:otherwise>
                                            </c:choose>

                                            <td><a href="removeSaleItemFromCart?saleType=NORMAL&itemName=${tripel.getItem().getName()}&storeName=${tripel.getItem().getStoreName()}">Remove</a></td>

                                            </tr>

                                            </c:when>
                                        </c:choose>
                                    </c:forEach>


                                    </tbody>
                                </table>
                            </div>
                            <div>&nbsp</div>

                            <div><h2>Raffle Sale Items</h2>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">
                                    <tr>
                                        <th class="table-header-repeat line-left"><a href="">Raffle Sale Item</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Original Price</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Price After Discount</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Percent</a></th>
                                        <th class="table-header-repeat line-left"><a href="">My Price</a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                    </tr>

                                    <c:forEach items="${cartItems}" var="tripel">
                                        <c:choose>
                                        <c:when test="${tripel.getSaleType() eq 'RAFFLE'}">
                                        <tr>
                                            <c:choose>
                                                <c:when test="${itemNameToEdit == tripel.getItem().getName() && saleTypeToEdit eq 'RAFFLE'}">
                                                    <form id="editItem" method="post" action="editSaleItemInCart?storeName=${tripel.getItem().getStoreName()}&itemName=${tripel.getItem().getName()}&saleType=RAFFLE" >
                                                        <td>${tripel.getItem().getName()}</td>
                                                        <td>${tripel.getItem().getOriginalPrice()}</td>
                                                        <td>${tripel.getItem().getPrice(listCoupon)}</td>
                                                        <td><input type="text" value="${tripel.getAmount()}" name="percent" class="login-inp"/></td>
                                                        <td>${tripel.getAmount() * tripel.getItem().getPrice(listCoupon) /100}</td>
                                                        <td><input type="submit" name="Save" value="Save"/></td>

                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>${tripel.getItem().getName()}</td>
                                                    <td>${tripel.getItem().getOriginalPrice()}</td>
                                                    <td>${tripel.getItem().getPrice(listCoupon)}</td>
                                                    <td>${tripel.getAmount()}</td>
                                                    <td>${tripel.getAmount() * tripel.getItem().getPrice(listCoupon) /100}</td>
                                                    <td><a href="editSaleItemInCart?itemName=${tripel.getItem().getName()}&saleType=RAFFLE">Change Percent</a></td>
                                                </c:otherwise>
                                            </c:choose>

                                            <td><a href="removeSaleItemFromCart?saleType=RAFFLE&itemName=${tripel.getItem().getName()}&storeName=${tripel.getItem().getStoreName()}">Remove</a></td>

                                        </tr>
                                    </c:when>
                                        </c:choose>
                                    </c:forEach>


                                    </tbody>
                                </table>
                            </div>
                            <div>&nbsp</div>
                            <div><h2>My Coupons</h2>
                                <table border="0" width="30%" cellpadding="0" cellspacing="0" id="coupon-table">
                                    <tr>
                                        <th class="table-header-repeat line-left"><a href="">Coupon</a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                    </tr>
                                    <c:forEach items="${listCoupon}" var="coupon">
                                        <tr>
                                            <td>${coupon}</td>
                                            <td><a href="removeCouponFromCart?couponCode=${coupon}">Remove</a></td>
                                        </tr>
                                    </c:forEach>


                                    </tbody>
                                </table>
                                <!--  end product-table................................... -->
                            </div>
                            <div>&nbsp</div>

                        </div>
                        <!--  end content-table  -->





                    </div>
                    <c:choose>

                    <c:when test="${userType == 'ADMIN'}">
                    <div id="actions-box">
                        <a href="createItem" class="action-add"></a>
                        <div class="clear"></div>
                    </div>
                    <!--  end content-table-inner ............................................END  -->
                </td>
                </c:when>
                </c:choose>
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
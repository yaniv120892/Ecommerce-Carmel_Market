<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../../_header.jsp"></jsp:include>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>${storeName} Discounts for ${itemName}</h1>
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
                            <!--  jquery core -->

                            <!--  start product-table ..................................................................................... -->
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">

                                <tbody>
                                <tr>
                                    <th class="table-header-repeat line-left"><a href="">Start Date</a></th>
                                    <th class="table-header-repeat line-left"><a href="">End Date</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Percent Off</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Discount Type</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Coupon Code</a></th>
                                    <th class="table-header-repeat line-left"><a href=""></a></th>
                                    <th class="table-header-repeat line-left"><a href=""></a></th>
                                </tr>
                                <c:forEach items="${itemDiscounts}" var="discount">
                                    <c:choose>
                                    <c:when test="${startDateEdit != null &&
                                    startDateEdit eq discount.getStartDate() &&
                                    endDateEdit eq discount.getEndDate() &&
                                      percentEdit eq discount.getDiscountPercent()}">
                                        <form id="editDiscountForItem" method="post"
                                              action="${pageContext.request.contextPath}editDiscountForItem?storeName=${storeName}&itemName=${itemName}&oldDiscountType=${discount.getType()}&oldPercent=${discount.getDiscountPercent()}&oldStartDate=${discount.getStartDate()}&oldEndDate=${discount.getEndDate()}"><td>
                                                <select id="startYear" name="startYear">
                                                    <option value="${startYearEdit}">${startYearEdit}</option>
                                                    <c:forEach begin="2018" end="2020" varStatus="loop">
                                                        <option value="${loop.index}">${loop.index}</option>
                                                    </c:forEach>
                                                </select>
                                                <select id="startMonth" name="startMonth">
                                                    <option value="${startMonthEdit}">${startMonthEdit}</option>
                                                    <c:forEach begin="1" end="12" varStatus="loop">
                                                        <option value="${loop.index}">${loop.index}</option>
                                                    </c:forEach>
                                                </select>
                                                <select id="startDay" name="startDay">
                                                    <option value="${startDayEdit}">${startDayEdit}</option>
                                                    <c:forEach begin="1" end="31" varStatus="loop">
                                                        <option value="${loop.index}">${loop.index}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td>
                                                <select id="endYear" name="endYear">
                                                    <option value="${endYearEdit}">${endYearEdit}</option>
                                                    <c:forEach begin="2018" end="2020" varStatus="loop">
                                                        <option value="${loop.index}">${loop.index}</option>
                                                    </c:forEach>
                                                </select>
                                                <select id="endMonth" name="endMonth">
                                                    <option value="${endMonthEdit}">${endMonthEdit}</option>
                                                    <c:forEach begin="1" end="12" varStatus="loop">
                                                        <option value="${loop.index}">${loop.index}</option>
                                                    </c:forEach>
                                                </select>
                                                <select id="endDay" name="endDay">
                                                    <option value="${endDayEdit}">${endDayEdit}</option>
                                                    <c:forEach begin="1" end="31" varStatus="loop">
                                                        <option value="${loop.index}">${loop.index}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td><input type="text" value="${percentEdit}"
                                                       name="percentEdit" class="login-inp"/></td>
                                            <td>
                                                <select id="discountTypeEdit" name="discountTypeEdit">
                                                    <option value="${discountTypeEdit}">${discountTypeEdit}</option>
                                                    <c:forEach items="${listDiscountType}" var="op">
                                                        <option value="${op}">${op}</option>
                                                    </c:forEach>
                                                </select>
                                            </td>
                                            <td>${discount.getCouponsCode()}</td>



                                            <td><input type="submit" name="Edit" value="Save"/></td>
                                            <td>
                                                <a href="discountsForItem?remove=true&storeName=${storeName}&itemName=${itemName}&startDate=${discount.getStartDate()}&endDate=${discount.getEndDate()}&percentOff=${discount.getDiscountPercent()}&Type=${discount.getType()}">Delete</a>
                                            </td>

                                        </form>

                                    </c:when>
                                    <c:otherwise>
                                    <tr>
                                        <td>${discount.getStartDate()}</td>
                                        <td>${discount.getEndDate()}</td>
                                        <td>${discount.getDiscountPercent()}</td>
                                        <td>${discount.getType()}</td>
                                        <td>${discount.getCouponsCode()}</td>
                                        <td>
                                            <a href="editDiscountForItem?storeName=${storeName}&itemName=${itemName}&startDate=${discount.getStartDate()}&endDate=${discount.getEndDate()}&percentOff=${discount.getDiscountPercent()}&Type=${discount.getType()}">Edit</a>
                                        </td>
                                        <td>
                                            <a href="discountsForItem?remove=true&storeName=${storeName}&itemName=${itemName}&startDate=${discount.getStartDate()}&endDate=${discount.getEndDate()}&percentOff=${discount.getDiscountPercent()}&Type=${discount.getType()}">Delete</a>
                                        </td>
                                    </tr>
                                    </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                </tbody>
                            </table>
                            <!--  end product-table................................... -->
                        </div>
                        <!--  end content-table  -->
                        <div id="actions-box">
                            <a href="createDiscountForItem?itemName=${itemName}&storeName=${storeName}" class="action-add"></a>
                            <div class="clear"></div>
                        </div>

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
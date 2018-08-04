<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../../_header.jsp"></jsp:include>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Category Discounts for ${storeName}</h1>
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
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table2">

                                <tbody>
                                <tr>
                                    <th class="table-header-repeat line-left"><a href="">Category</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Start Date</a></th>
                                    <th class="table-header-repeat line-left"><a href="">End Date</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Percent</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Type</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Coupon Code</a></th>
                                    <th class="table-header-repeat line-left"><a href=""></a></th>
                                    <th class="table-header-repeat line-left"><a href=""></a></th>
                                </tr>
                                <c:forEach items="${listCategoryDiscounts}" var="discount">
                                        <c:choose>

                                            <c:when test="${categoryDiscountNameToEdit != null &&
                                    categoryDiscountNameToEdit eq discount.getKey() &&
                                    startDateEdit eq discount.getValue().getStartDate() &&
                                    endDateEdit eq discount.getValue().getEndDate() &&
                                      percentEdit eq discount.getValue().getDiscountPercent()}">
                                                <form id="editCategoryDiscount" method="post"
                                                      action="${pageContext.request.contextPath}editCategoryDiscountInStore?storeName=${storeName}&categoryDiscountName=${categoryDiscountNameToEdit}&oldDiscountType=${discount.getValue().getType()}&oldPercent=${discount.getValue().getDiscountPercent()}&oldStartDate=${discount.getValue().getStartDate()}&oldEndDate=${discount.getValue().getEndDate()}">
                                                    <td>${discount.getKey()}</td>
                                                    <td>
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
                                                    <td>${discount.getValue().getCouponsCode()}</td>

                                                    <td><input type="submit" name="Edit" value="Save"/></td>
                                                    <td><a href="removeCategoryDiscountFromStore?categoryDiscountName=${discount.getKey()}&storeName=${storeName}&startDate=${discount.getValue().getStartDate()}&endDate=${discount.getValue().getEndDate()}">Remove</a></td>

                                                </form>

                                            </c:when>
                                            <c:otherwise>
                                                <tr>
                                                <td>${discount.getKey()}</td>
                                                <td>${discount.getValue().getStartDate()}</td>
                                                <td>${discount.getValue().getEndDate()}</td>
                                                <td>${discount.getValue().getDiscountPercent()}</td>
                                                    <td>${discount.getValue().getType()}</td>
                                                    <td>${discount.getValue().getCouponsCode()}</td>
                                                <td><a href="editCategoryDiscountInStore?categoryDiscountName=${discount.getKey()}&storeName=${storeName}&startDate=${discount.getValue().getStartDate()}&endDate=${discount.getValue().getEndDate()}&percentOff=${discount.getValue().getDiscountPercent()}&discountTypeEdit=${discount.getValue().getType()}">Edit</a>
                                                <td><a href="removeCategoryDiscountFromStore?categoryDiscountName=${discount.getKey()}&storeName=${storeName}&startDate=${discount.getValue().getStartDate()}&endDate=${discount.getValue().getEndDate()}&discountType=${discount.getValue().getType()}">Remove</a></td>
                                                </tr>
                                            </c:otherwise>

                                        </c:choose>

                                    </tr>
                                </c:forEach>

                                </tbody>
                            </table>
                            <!--  end product-table................................... -->
                        </div>

                        <div id="actions-box">
                            <a href="addCategoryDiscount?storeName=${storeName}" class="action-add"></a>
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
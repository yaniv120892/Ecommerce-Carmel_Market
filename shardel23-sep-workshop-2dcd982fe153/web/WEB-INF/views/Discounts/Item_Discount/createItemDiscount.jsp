<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../../_header.jsp"></jsp:include>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Add Item Discount</h1>
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
                            <form id="myForm" method="post"
                                  action="${pageContext.request.contextPath}/createDiscountForItem?storeName=${storeName}&itemName=${itemName}">
                                <table border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <th>Discount Type</th>
                                        <td>
                                            <select id="discountType" name="discountType">
                                                <c:forEach items="${listDiscountType}" var="op">
                                                    <option value="${op}">${op}</option>
                                                </c:forEach>
                                            </select>
                                        </td>

                                    </tr>

                                    <tr>
                                        <th>Start Date</th>
                                        <td>
                                            <select id="startYear" name="startYear">
                                                <c:forEach begin="2018" end="2020" varStatus="loop">
                                                    <option value="${loop.index}">${loop.index}</option>
                                                </c:forEach>
                                            </select>
                                            <select id="startMonth" name="startMonth">
                                                <c:forEach begin="1" end="12" varStatus="loop">
                                                    <option value="${loop.index}">${loop.index}</option>
                                                </c:forEach>
                                            </select>
                                            <select id="startDay" name="startDay">
                                                <c:forEach begin="1" end="31" varStatus="loop">
                                                    <option value="${loop.index}">${loop.index}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>End Date</th>
                                        <td>
                                            <select id="endYear" name="endYear">
                                                <c:forEach begin="2018" end="2020" varStatus="loop">
                                                    <option value="${loop.index}">${loop.index}</option>
                                                </c:forEach>
                                            </select>
                                            <select id="endMonth" name="endMonth">
                                                <c:forEach begin="1" end="12" varStatus="loop">
                                                    <option value="${loop.index}">${loop.index}</option>
                                                </c:forEach>
                                            </select>
                                            <select id="endDay" name="endDay">
                                                <c:forEach begin="1" end="31" varStatus="loop">
                                                    <option value="${loop.index}">${loop.index}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Percent</th>
                                        <td><input type="text" value="${categoryDiscountPercent}" name="percent"
                                                   class="login-inp"/>
                                        </td>
                                    </tr>


                                    <th></th>
                                    <td><input type="submit" name="Add" value="Add" class="submit-login"/></td>
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
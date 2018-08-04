<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Purchase Policies for ${storeName}</h1>
        </div>
        <c:choose>
            <c:when test="${errorString!= null && not empty errorString}">
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
            <c:when test="${successString!= null && not empty successString}">
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
        <!-- end page-heading -->


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

                            <div style="width: 600px; float: left;">
                                <h3>Purchase Policies for Items</h3>
                                <table border="0" width="50%" cellpadding="0" cellspacing="0" id="items-purchase-policies-table">
                                    <tr>
                                        <th class="table-header-repeat line-left"><a href="">Item</a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                        <th class="table-header-repeat line-left"><a href=""></a></th>
                                    </tr>
                                    <tbody>
                                    <tr>
                                        <td>All Items</td>
                                        <td><a href="ruleDetails?itemName=&storeName=${storeName}">Show Rules</a></td>
                                        <td><a href="addRule?itemName=&storeName=${storeName}">Add Rule</a></td>
                                        <td><a href="clearRule?itemName=&storeName=${storeName}">Clear</a></td>
                                    </tr>
                                    <c:forEach items="${listItems}" var="item">
                                        <tr>
                                            <td>${item.getName()}</td>
                                            <td><a href="ruleDetails?itemName=${item.getName()}&storeName=${storeName}">Show Rules</a></td>
                                            <td><a href="addRule?itemName=${item.getName()}&storeName=${storeName}">Add Rule</a></td>
                                            <td><a href="clearRule?itemName=${item.getName()}&storeName=${storeName}">Clear</a></td>
                                        </tr>
                                    </c:forEach>


                                    </tbody>
                                </table>
                                <!--  end product-table................................... -->
                            </div>
                            <div style="margin-left: 620px;">


                                <h3>Purchase Policies for Categories</h3>
            <table border="0" width="50%" cellpadding="0" cellspacing="0" id="categories-purchase-policies-table">
                <tr>
                    <th class="table-header-repeat line-left"><a href="">Category</a></th>
                    <th class="table-header-repeat line-left"><a href=""></a></th>
                    <th class="table-header-repeat line-left"><a href=""></a></th>
                    <th class="table-header-repeat line-left"><a href=""></a></th>
                </tr>
                <c:forEach items="${listCategories}" var="category">

                <tr>
                    <td>${category}</td>
                    <td><a href="ruleDetails?itemName=&categoryName=${category}&storeName=${storeName}">Show Rules</a></td>
                    <td><a href="addRule?itemName=&categoryName=${category}&storeName=${storeName}">Add Rule</a></td>
                    <td><a href="clearRule?itemName=&categoryName=${category}&storeName=${storeName}">Clear</a></td>
                </tr>
                </c:forEach>


                </tbody>
            </table>
            <!--  end product-table................................... -->
        </div>
                            <div>

                                <c:choose>
                                    <c:when test="${ruleDetails != null && not empty ruleDetails}">
                                        <h4>Rule Details</h4>
                                        <label>${ruleDetails}</label>
                                    </c:when>
                                </c:choose>
                            </div>
                            <!--  end content-table  -->
                        </div>
                    </div>
                </td>

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
    </div>
    <div class="clear">&nbsp;</div>

</div>
<!-- end content -->
<div class="clear">&nbsp;</div>

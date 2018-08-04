<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Items in ${storeName}</h1>
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


                            <div>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">
                                    <tr>
                                        <th class="table-header-repeat line-left"><a href="">Name</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Category</a></th>
                                        <th class="table-header-repeat line-left"><a href="">In Stock</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Original Price</a></th>
                                        <th class="table-header-repeat line-left"><a href="">After Discount Price</a>
                                        </th>
                                        <th class="table-header-repeat line-left"><a href="">Normal Sale</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Raffle Sale</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Edit</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Remove</a></th>
                                        <th class="table-header-repeat line-left"><a href="">Discounts</a></th>
                                    </tr>
                                    <c:forEach items="${listItems}" var="item">
                                        <tr>
                                            <c:choose>
                                                <c:when test="${itemNameToEdit != null && itemNameToEdit == item.getName()}">
                                                    <form id="editItem" method="post"
                                                          action="${pageContext.request.contextPath}editItemInStore?storeName=${item.getStoreName()}&itemName=${item.getName()}">
                                                        <td>${item.getName()}</td>
                                                        <td>${item.getCategory()}</td>
                                                        <td><input type="text" value="${item.getNumberInStock()}"
                                                                   name="stock" class="login-inp"/></td>
                                                        <td><input type="text" value="${item.getOriginalPrice()}"
                                                                   name="price" class="login-inp"/></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td><input type="submit" name="Edit" value="Save"/></td>
                                                        <td>
                                                            <a href="removeItemFromStore?itemName=${item.getName()}&storeName=${item.getStoreName()}">Remove</a>
                                                        </td>

                                                    </form>

                                                </c:when>
                                                <c:when test="${itemNameToEdit!=item.getName()}">
                                                    <td>${item.getName()}</td>
                                                    <td>${item.getCategory()}</td>
                                                    <td>${item.getNumberInStock()}</td>
                                                    <td>${item.getOriginalPrice()}</td>
                                                    <td>${item.getPriceAfterDiscount()}</td>
                                                    <td>
                                                        <a href="addItemToSale?itemName=${item.getName()}&storeName=${item.getStoreName()}&saleType=NORMAL">Add
                                                            Normal Sale</a></td>
                                                    <td>
                                                        <a href="addItemToSale?itemName=${item.getName()}&storeName=${item.getStoreName()}&saleType=RAFFLE">Add
                                                            RAFFLE Sale</a></td>
                                                    <td>
                                                        <a href="editItemInStore?itemNameToEdit=${item.getName()}&storeName=${item.getStoreName()}">Edit</a>
                                                    </td>
                                                    <td>
                                                        <a href="removeItemFromStore?itemName=${item.getName()}&storeName=${item.getStoreName()}">Remove</a>
                                                    </td>
                                                    <td>
                                                        <a href="discountsForItem?itemName=${item.getName()}&storeName=${item.getStoreName()}">Discounts</a>
                                                    </td>
                                                </c:when>

                                            </c:choose>

                                        </tr>
                                    </c:forEach>
                                    <tr>
                                        <th>
                                            <div id="actions-box1">
                                                <a href="addItemToStore?storeName=${storeName}" class="action-add"></a>
                                                <div class="clear"></div>
                                            </div>
                                        </th>
                                    </tr>
                                    <tr>
                                        <th></th>
                                    </tr>


                                    </tbody>
                                </table>
                                <!--  end product-table................................... -->
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

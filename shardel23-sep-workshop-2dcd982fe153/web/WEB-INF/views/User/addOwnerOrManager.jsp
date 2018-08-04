<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <c:choose>
                <c:when test="${addOwner}">
                    <h1>Add Owner for ${storeName}</h1>
                </c:when>
                <c:when test="${addManager}">
                    <h1>Add Manager for ${storeName}</h1>
                </c:when>
            </c:choose>
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




                            <form id="myForm" method="post"
                                  action="${pageContext.request.contextPath}/addOwnerOrManager?storeName=${storeName}&addManager=${addManager}&addOwner=${addOwner}">
                                <table border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <th class="table-header-options">User Email</th>
                                        <td><input class="inp-form" type="text" value="${userEmail}" name="userEmail"/>
                                        </td>
                                    </tr>
                                    <c:choose>
                                        <c:when test="${addManager}">
                                            <tr>
                                                <th> Add Item To Store</th>
                                                <td>
                                                    <select class="styledselect" id="canAddItemToStore"
                                                            name="canAddItemToStore">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="table-header-options"> Remove Item From Store</th>
                                                <td>
                                                    <select class="styledselect" id="canRemoveItemFromStore"
                                                            name="canRemoveItemFromStore">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th> Edit Item Price</th>
                                                <td>
                                                    <select class="styledselect" id="canEditItemPrice"
                                                            name="canEditItemPrice">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="table-header-options"> Edit Item in Stock</th>
                                                <td>
                                                    <select class="styledselect" id="canEditItemStock"
                                                            name="canEditItemStock">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th> Edit Item Is On Sale</th>
                                                <td>
                                                    <select class="styledselect" id="canEditItemIsOnSale"
                                                            name="canEditItemIsOnSale">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="table-header-options"> Add Item Discount</th>
                                                <td>
                                                    <select class="styledselect" id="canAddItemDiscount"
                                                            name="canAddItemDiscount">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th> Remove Item Discount</th>
                                                <td>
                                                    <select class="styledselect" id="canRemoveItemDiscount"
                                                            name="canRemoveItemDiscount">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th class="table-header-options"> Add Manager</th>
                                                <td>
                                                    <select class="styledselect" id="canAddManager"
                                                            name="canAddManager">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th> Get Purchase History</th>
                                                <td>
                                                    <select class="styledselect" id="canGetPurchaseHistory"
                                                            name="canGetPurchaseHistory">
                                                        <option value="true">Yes</option>
                                                        <option value="false">No</option>
                                                    </select>
                                                </td>
                                            </tr>






                                        </c:when>
                                    </c:choose>
                                    <tr>
                                        <th></th>
                                        <td><input type="submit" name="Add" value="Add" class="form-submit"/></td>
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












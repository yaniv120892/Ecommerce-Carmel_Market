<%@ page import="Backend.Addons.TreeNode.TreeNode" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="Backend.Entities.Users.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Appointments History for ${storeName}</h1>
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

                            <div style="width: 100%; overflow: hidden;">


                            <div style="width: 600px; float: left;">
                            <!--  start product-table ..................................................................................... -->
                                <table border="0" width="50%" cellpadding="0" cellspacing="0" id="product-table">

                                    <tbody>
                                    <tr>
                                        <th class="table-header-repeat line-left"><a>Parent</a></th>
                                        <th class="table-header-repeat line-left"><a>Child</a></th>
                                        <th class="table-header-repeat line-left"><a>Date</a></th>
                                        <th class="table-header-repeat line-left"></th>

                                    </tr>
                                        <%
                                        Iterator<TreeNode<User>> iter = (Iterator<TreeNode<User>>)session.getAttribute("iter");
                                        while (iter.hasNext())
                                        { %>
                                    <tr>
                                        <% if(iter.next().getParent() != null)
                                        {
                                        %>
                                                <td>${iter.next().getParent().toString()}</td>
                                        <%
                                        }
                                        else
                                            {
                                        %>
                                                <td>No Parent</td>
                                        <%
                                            }
                                        %>
                                        <td>${iter.next().toString()}</td>
                                        <td>${iter.next().getDate()}</td>
                                        <td><a href="appointmentsHistory?storeName=${storeName}&userToShow=${iter.next().toString()}">Privileges</a></td>
                                    </tr>

       <%
           }
    %>
                                    </tbody>
                                </table>
                        </div>
                                <div style="margin-left: 620px;">
                                    <c:choose>
                                <c:when test="${not empty userToShow}">
                                    <h3>
                                        Privileges For User ${userToShow}
                                    </h3>
                                            <table border="0" width="50%" cellpadding="0" cellspacing="0" id="privileges-table">
                                                <tbody>
                                                <tr>
                                                    <td>Add Item</td>
                                                    <td>${privileges.canAddItemToStore()}</td>
                                                </tr>
                                                <tr>
                                                    <td >Edit Item in Stock</td>
                                                    <td>${privileges.canEditItemStock()}</td>
                                                </tr>
                                                <tr>
                                                    <td>Remove Item</td>
                                                    <td>${privileges.canRemoveItemFromStore()}</td>
                                                </tr>
                                                <tr>
                                                    <td >Edit Item Price</td>
                                                    <td>${privileges.canEditItemPrice()}</td>
                                                </tr>
                                                <tr>
                                                    <td >Edit Item Is On Sale</td>
                                                    <td>${privileges.canEditItemIsOnSale()}</td>
                                                </tr>
                                                <tr>
                                                    <td>Remove Item Discount</td>
                                                    <td>${privileges.canAddItemDiscount()}</td>
                                                </tr>
                                                <tr>
                                                    <td >Add Owner</td>
                                                    <td>${privileges.canAddOwner()}</td>
                                                </tr>                                                <tr>
                                                    <td >Add Manager</td>
                                                    <td>${privileges.canAddManager()}</td>
                                                </tr>
                                                <tr>
                                                    <td >Get Purchase History</td>
                                                    <td>${privileges.canGetPurchaseHistory()}</td>
                                                </tr>
                                                <tr>
                                                    <td >Remove Sale Item</td>
                                                    <td>${privileges.canRemoveSaleItemFromStore()}</td>

                                                </tr>
                                                <tr>
                                                    <td >Remove Category Discount</td>
                                                    <td>${privileges.canRemoveCategoryDiscount()}</td>

                                                </tr>
                                                <tr>
                                                    <td >Edit Item Discount</td>
                                                    <td>${privileges.canEditDiscountForItem()}</td>

                                                </tr>
                                                <tr>
                                                    <td >Close Store</td>
                                                    <td>${privileges.canCloseStore()}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                                </c:when>
                            </c:choose>
                            </div>
                            </div>
                        </div>



                    </div>

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
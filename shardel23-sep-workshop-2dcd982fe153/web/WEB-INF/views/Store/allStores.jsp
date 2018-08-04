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
                            <!--  jquery core -->
                            <script src="../../../js/jquery/autocomplete.js" type="text/javascript"></script>
                            <!--  start message-green -->
                            <div>
                                <form id="search" autocomplete="off"
                                      action="${pageContext.request.contextPath}/searchStore?userID=${userID}">
                                    <div class="autocomplete" style="width:300px;">
                                        <input id="storeName" type="text" name="storeName" placeholder="Store">
                                    </div>
                                    <input type="submit" value="Search">
                                </form>
                            </div>
                            <script>
                                var stores = [];

                                <c:forEach items="${listStores}" var="store">
                                stores.push("${store.getName()}");
                                </c:forEach>

                                autocomplete(document.getElementById("storeName"), stores);
                            </script>

                            <div>
                                <form id="search2" autocomplete="off"
                                      action="${pageContext.request.contextPath}/searchSaleItems">
                                    <div class="autocomplete" style="width:300px;">
                                        <input id="itemName" type="text" value="${itemName}" name="itemName" placeholder="Item">
                                    </div>
                                    <input type="submit" value="Search">
                                </form>
                            </div>
                            <script>
                                var itemsList = [];
                                <c:forEach items="${listItems}" var="item">
                                itemsList.push("${item}");
                                </c:forEach>
                                autocomplete(document.getElementById("itemName"), itemsList);
                            </script>

                            <div>
                                <form id="search3"
                                      action="${pageContext.request.contextPath}/searchSaleItems">
                                    <select id="categoryName" name="categoryName">
                                        <c:forEach items="${listCategories}" var="op">
                                            <option value="${op}">${op}</option>
                                        </c:forEach>
                                    </select>
                                    <input type="submit" value="Search">
                                </form>
                            </div>


                            <!--  start product-table ..................................................................................... -->
                            <table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">

                                <thead>
                                <tr>
                                    <th class="table-header-repeat line-left"><a href="">Name</a></th>
                                    <th class="table-header-repeat line-left"><a href="">Email</a></th>
                                    <th class="table-header-repeat line-left"></th>


                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${listStores}" var="store">
                                    <tr>
                                        <td>${store.getName()}</td>
                                        <td>${store.getStoreEmail()}</td>
                                        <td><a href="saleItems?storeName=${store.getName()}">Items</a></td>
                                    </tr>
                                </c:forEach>


                                </tbody>
                            </table>
                            <!--  end product-table................................... -->
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

<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">

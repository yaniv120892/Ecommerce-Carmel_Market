<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>
<%@ page import="Backend.Entities.Items.RaffleItem" %>


<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <c:choose>
                <c:when test="${storeName!= null && not empty storeName}">
                    <h1>Items in ${storeName}</h1>
                </c:when>
                <c:otherwise>
                    <h1>Search Items Result</h1>

                </c:otherwise>
            </c:choose>
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
                                <c:choose>
                                    <c:when test="${ownerView == null}">
                                        <form id="myForm" method="post"
                                              action="${pageContext.request.contextPath}/searchSaleItems">
                                            <table border="0" cellpadding="0" cellspacing="0" align="left">
                                                <tbody>
                                                <tr>
                                                    <td><input placeholder="Search by Item Name" type="text" value="" name="itemName" class="login-inp"/>
                                                    </td>
                                                    <td>
                                                        <select id="categoryName" name="categoryName">
                                                            <option value="">Search by Category</option>

                                                            <c:forEach items="${listCategories}" var="op">
                                                                <option value="${op}">${op}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </td>
                                                    <td hidden><input type="text" value="${storeName}"
                                                                      name="storeName"/></td>
                                                    <td><input type="submit" name="search" value="Search"
                                                               class="submit-login"/></td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </form>
                                    </c:when>
                                </c:choose>
                            </div>
                            <div class="clear">&nbsp;</div>



                            <div>
                                <h3>Raffle Sale Items</h3>
                                <div class="clear">&nbsp;</div>
                                <table  border="0" width="100%" cellpadding="0" cellspacing="0" id="raffle-sale-item-table">
                                    <thead>
                                    <tr>
                                        <th >Name</th>
                                        <th >Category</th>
                                        <th >Original Price</th>
                                        <th >After Discount Price</th>
                                        <th >Start Date</th>
                                        <th >End Date</th>
                                        <th >Bought Percent</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody >
                                    <c:forEach items="${listSaleItems}" var="sale">
                                        <c:choose>
                                            <c:when test="${sale.getType() eq 'RAFFLE'}">
                                                <tr class="tr-raffle-sale-item">
                                                    <td class="td-name">${sale.getItem().getName()}</td>
                                                    <td class="td-category">${sale.getItem().getCategory()}</td>
                                                    <td>${sale.getItem().getOriginalPrice()}</td>
                                                    <td class="td-price">${sale.getItem().getPriceAfterDiscount()}</td>
                                                    <td>${sale.getStartDate()}</td>
                                                    <td>${sale.getEndDate()}</td>
                                                    <td>${sale.getAlreadyBought()}</td>
                                                    <c:choose>
                                                        <c:when test="${ownerView == null}">
                                                            <td>
                                                                <a href="addSaleItemToCart?itemName=${sale.item.getName()}&storeName=${sale.getItem().getStoreName()}&saleType=${sale.getType()}">Add
                                                                    to Cart</a></td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td>
                                                                <a href="removeSaleItemFromStore?itemName=${sale.item.getName()}&storeName=${sale.getItem().getStoreName()}&saleType=${sale.getType()}">Remove
                                                                    Sale</a></td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tr>
                                            </c:when>
                                        </c:choose>
                                    </c:forEach>

                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <th >Name</th>
                                        <th >Category</th>
                                        <th >Original Price</th>
                                        <th >After Discount Price</th>
                                        <th >Start Date</th>
                                        <th >End Date</th>
                                        <th >Bought Percent</th>
                                        <th></th>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>
                            <div class="clear">&nbsp;</div>

                            <div>
                                <h3>Normal Sale Items</h3>
                                <div class="clear">&nbsp;</div>
                                <table border="0" width="100%" cellpadding="0" cellspacing="0" id="regular-sale-item-table">
                                    <thead>
                                    <tr>
                                        <th >Name</th>
                                        <th >Category</th>
                                        <th >Original Price</th>
                                        <th >After Discount Price</th>
                                        <th ></th>
                                    </tr>
                                    </thead>
                                    <tbody >
                                    <c:forEach items="${listSaleItems}" var="sale">
                                        <c:choose>
                                            <c:when test="${sale.getType() eq 'NORMAL'}">
                                                <tr>
                                                    <td>${sale.getItem().getName()}</td>
                                                    <td>${sale.getItem().getCategory()}</td>
                                                    <td>${sale.getItem().getOriginalPrice()}</td>
                                                    <td>${sale.getItem().getPriceAfterDiscount()}</td>
                                                    <c:choose>
                                                        <c:when test="${ownerView == null}">
                                                            <td>
                                                                <a href="addSaleItemToCart?itemName=${sale.item.getName()}&storeName=${sale.getItem().getStoreName()}&saleType=${sale.getType()}">Add
                                                                    to Cart</a></td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td>
                                                                <a href="removeSaleItemFromStore?itemName=${sale.item.getName()}&storeName=${sale.getItem().getStoreName()}&saleType=${sale.getType()}">Remove
                                                                    Sale</a></td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tr>
                                            </c:when>
                                        </c:choose>

                                    </c:forEach>
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <th >Name</th>
                                        <th >Category</th>
                                        <th >Original Price</th>
                                        <th >After Discount Price</th>
                                        <th></th>
                                    </tr>
                                    </tfoot>
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


<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<script type="text/javascript" charset="utf8" src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.7.1.min.js"></script>
<script type="text/javascript" charset="utf8" src="https://ajax.aspnetcdn.com/ajax/jquery.dataTables/1.9.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/v/dt/dt-1.10.12/datatables.min.js"></script>
<script>



    $(document).ready(function() {
        // Setup - add a text input to each footer cell
        $('#raffle-sale-item-table').find('tfoot th').each(function () {
            var title = $(this).text();
            $(this).html('<input class="input" type="text" placeholder="' + title + '" />');
        });

        // DataTable
        var table = $('#raffle-sale-item-table').DataTable({
            columnDefs: [
                {
                    className: 'table-header-repeat line-left'
                }
            ],
            "bPaginate": false,
            "bLengthChange": false,
            "bInfo": false



        });
        table.columns().every( function () {
            var that = this;

            $( 'input', this.footer() ).on( 'keyup change', function () {
                if ( that.search() !== this.value ) {
                    that
                        .search( this.value )
                        .draw();
                }
            } );
        } );

        $('#regular-sale-item-table tfoot th').each(function () {
            var title = $(this).text();
            $(this).html('<input type="text" placeholder="' + title + '" />');
        });

        // DataTable
        var table = $('#regular-sale-item-table').DataTable({
            "bPaginate": false,
            "bLengthChange": false,
            "bInfo": false

        });
        table.columns().every( function () {
            var that = this;

            $( 'input', this.footer() ).on( 'keyup change', function () {
                if ( that.search() !== this.value ) {
                    that
                        .search( this.value )
                        .draw();
                }
            } );
        } );




    } );

</script>
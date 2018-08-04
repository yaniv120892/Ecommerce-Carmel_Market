<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<jsp:include page="../_header.jsp"></jsp:include>







<div id="content-outer">
    <!-- start content -->
    <div id="content">

        <!--  start page-heading -->
        <div id="page-heading">
            <h1>Add New Rule</h1>
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
                           <c:choose> <c:when test="${categoryName eq null}">
                            <form id="myForm" method="post" action="${pageContext.request.contextPath}/addRule?storeName=${storeName}&itemName=${itemName}">
                                </c:when>
                                <c:otherwise>
                                    <form id="myForm" method="post" action="${pageContext.request.contextPath}/addRule?storeName=${storeName}&itemName=&categoryName=${categoryName}">
                                    </c:otherwise>
                                    </c:choose>
            <table border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <th>Rule Type</th>
                    <td>
                        <select id="ruleType" name="ruleType">
                            <option value="null">Select Rule Type</option>
                            <c:forEach items="${listRuleTypes}" var="op">
                                <option value="${op}">${op}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>


                <tr>

                    <th>Operators</th>
                    <td>
                        <select id="operator" name="operator">
                            <option value="AND">AND</option>
                            <option value="OR">OR</option>
                        </select>
                    </td>
                    <td>
                        <input type="checkbox" class="notOperand" name="notOperand" value="notOperand"> Not Operator<br>
                    </td>
                </tr>
                <tr>
                <th id="inputDetailTh" hidden> Condition Value</th>
                    <td hidden id="inputDetailTextBox"><input type="text" value="${numberDetail}"  name="numberDetail" class="login-inp"/></td>
                    <td hidden id="inputDetailDropDown">
                        <select id="stateValue" name="stateValue">
                            <c:forEach items="${listStates}" var="op">
                                <option value="${op}">${op}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                                    <tr>
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
</body>
</html>

<script>
    $("#ruleType").change(function() {
        var control = $(this);
        $("#inputDetailTh").show();
        if (control.val() == 'MIN_AMOUNT' | control.val() == 'MAX_AMOUNT') {
            $("#inputDetailDropDown").hide();
            $("#inputDetailTextBox").show();
        }
        else
        {
            $("#inputDetailDropDown").show();
            $("#inputDetailTextBox").hide();
        }
        if (control.val() == "null")
        {
            $("#inputDetailDropDown").hide();
            $("#inputDetailTextBox").hide();
            $("#inputDetailTh").hide();

        }
    });
</script>























































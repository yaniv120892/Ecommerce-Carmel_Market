<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Carmel Market</title>
    <link rel="stylesheet" href=
    <%@ page contentType="text/html; charset=UTF-8"
             pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Internet Dreams</title>
        <link rel="stylesheet" href="<c:url value="../../css/screen.css" />" type="text/css" media="screen"
              title="default"/>
        <!--[if IE]>
        <link rel="stylesheet" media="all" type="text/css" href="<c:url value=" ../../css/pro_dropline_ie.css" />" />
        <![endif]-->

        <!--  jquery core -->
        <script src="js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>

        <!--  checkbox styling script -->
        <script src="../../js/jquery/ui.core.js" type="text/javascript"></script>
        <script src="../../js/jquery/ui.checkbox.js" type="text/javascript"></script>
        <script src="../../js/jquery/jquery.bind.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(function () {
                $('input').checkBox();
                $('#toggle-all').click(function () {
                    $('#toggle-all').toggleClass('toggle-checked');
                    $('#mainform input[type=checkbox]').checkBox('toggle');
                    return false;
                });
            });
        </script>

        <![if !IE 7]>

        <!--  styled select box script version 1 -->
        <script src="../../js/jquery/jquery.selectbox-0.5.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.styledselect').selectbox({inputClass: "selectbox_styled"});
            });
        </script>


        <![endif]>

        <!--  styled select box script version 2 -->
        <script src="../../js/jquery/jquery.selectbox-0.5_style_2.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.styledselect_form_1').selectbox({inputClass: "styledselect_form_1"});
                $('.styledselect_form_2').selectbox({inputClass: "styledselect_form_2"});
            });
        </script>

        <!--  styled select box script version 3 -->
        <script src="../../js/jquery/jquery.selectbox-0.5_style_2.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $('.styledselect_pages').selectbox({inputClass: "styledselect_pages"});
            });
        </script>

        <!--  styled file upload script -->
        <script src="../../js/jquery/jquery.filestyle.js" type="text/javascript"></script>
        <script type="text/javascript" charset="utf-8">
            $(function () {
                $("input.file_1").filestyle({
                    image: "images/forms/choose-file.gif",
                    imageheight: 21,
                    imagewidth: 78,
                    width: 310
                });
            });
        </script>

        <!-- Custom jquery scripts -->
        <script src="../../js/jquery/custom_jquery.js" type="text/javascript"></script>

        <!-- Tooltips -->
        <script src="../../js/jquery/jquery.tooltip.js" type="text/javascript"></script>
        <script src="../../js/jquery/jquery.dimensions.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(function () {
                $('a.info-tooltip ').tooltip({
                    track: true,
                    delay: 0,
                    fixPNG: true,
                    showURL: false,
                    showBody: " - ",
                    top: -35,
                    left: 5
                });
            });
        </script>


        <!--  date picker script -->
        <link rel="stylesheet" href="../../css/datePicker.css" type="text/css"/>
        <script src="../../js/jquery/date.js" type="text/javascript"></script>
        <script src="../../js/jquery/jquery.datePicker.js" type="text/javascript"></script>
        <script type="text/javascript" charset="utf-8">
            $(function () {

// initialise the "Select date" link
                $('#date-pick')
                    .datePicker(
                        // associate the link with a date picker
                        {
                            createButton: false,
                            startDate: '01/01/2005',
                            endDate: '31/12/2020'
                        }
                    ).bind(
                    // when the link is clicked display the date picker
                    'click',
                    function () {
                        updateSelects($(this).dpGetSelected()[0]);
                        $(this).dpDisplay();
                        return false;
                    }
                ).bind(
                    // when a date is selected update the SELECTs
                    'dateSelected',
                    function (e, selectedDate, $td, state) {
                        updateSelects(selectedDate);
                    }
                ).bind(
                    'dpClosed',
                    function (e, selected) {
                        updateSelects(selected[0]);
                    }
                );

                var updateSelects = function (selectedDate) {
                    var selectedDate = new Date(selectedDate);
                    $('#d option[value=' + selectedDate.getDate() + ']').attr('selected', 'selected');
                    $('#m option[value=' + (selectedDate.getMonth() + 1) + ']').attr('selected', 'selected');
                    $('#y option[value=' + (selectedDate.getFullYear()) + ']').attr('selected', 'selected');
                }
// listen for when the selects are changed and update the picker
                $('#d, #m, #y')
                    .bind(
                        'change',
                        function () {
                            var d = new Date(
                                $('#y').val(),
                                $('#m').val() - 1,
                                $('#d').val()
                            );
                            $('#date-pick').dpSetSelected(d.asString());
                        }
                    );

// default the position of the selects to today
                var today = new Date();
                updateSelects(today.getTime());

// and update the datePicker to reflect it...
                $('#d').trigger('change');
            });
        </script>

        <!-- MUST BE THE LAST SCRIPT IN <HEAD></HEAD></HEAD> png fix -->
        <script src="../../js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $(document).pngFix();
            });
        </script>
    </head>
<body>
<!-- Start: page-top-outer -->
<div id="page-top-outer">
    <script src="../../js/jquery/webSocket.js" type="text/javascript"></script>
    <!-- Start: page-top -->
    <div id="page-top">

        <!-- start logo -->
        <div id="logo">
            <a href=""><img src="../../images/shared/logo.png" width="156" height="40" alt=""/></a>

            <!-- end logo -->


            <div class="clear"></div>


        </div>
        <!-- End: page-top -->

    </div>
</div>
<!-- End: page-top-outer -->

<div class="clear">&nbsp;</div>

<!--  start nav-outer-repeat................................................................................................. START -->

<div class="nav-outer-repeat">
    <div class="clear">&nbsp;</div>

    <!--  start nav-outer -->
    <div class="nav-outer">

        <!-- start nav-right -->
        <div id="nav-right">

            <div class="nav-divider">&nbsp;</div>
            <a href="logout" id="logout"><img src="../../images/shared/nav/nav_logout.gif" width="64" height="14"
                                              alt=""/></a>
            <div class="clear">&nbsp;</div>


        </div>
        <!-- end nav-right -->


        <!--  start nav -->
        <div class="nav">
            <div>
                <c:choose>
                    <c:when test="${userType == 'ADMIN'}">
                        <ul class="select">
                            <li><a href=""><b>Admin Tools</b><!--[if IE 7]><!--></a><!--<![endif]-->
                                <!--[if lte IE 6]>
                                <table>
                                    <tr>
                                        <td><![endif]-->
                                <div class="select_sub">
                                    <ul class="sub">
                                        <li><a href="adminStores">Stores</a></li>
                                        <li><a href="systemPurchasePolicies">Purchase Policies</a></li>
                                        <li><a href="adminUsers">Users</a></li>
                                        <li><a href="userStatistics">User Statistics</a></li>
                                        <li><a href="testNotifications">notifyAll</a></li>
                                    </ul>
                                </div>
                                <!--[if lte IE 6]></td></tr></table></a><![endif]-->
                            </li>
                        </ul>
                    </c:when>

                    <c:when test="${userType == 'REGISTERED'}">
                        <ul class="select">
                            <li><a href=""><b>Store Options</b><!--[if IE 7]><!--></a><!--<![endif]-->
                                <!--[if lte IE 6]>
                                <table>
                                    <tr>
                                        <td><![endif]-->
                                <div class="select_sub">
                                    <ul class="sub">
                                        <li><a href="allStores">Show All Stores</a></li>
                                        <li><a href="myStores">My Stores</a></li>
                                    </ul>
                                </div>
                                <!--[if lte IE 6]></td></tr></table></a><![endif]-->
                            </li>
                            <li><a href="myCart"><b>My Cart</b></a></li>
                            <li><a href="checkout"><b>Checkout</b></a></li>
                        </ul>
                    </c:when>

                    <c:when test="${userType == 'GUEST'}">
                        <ul class="select">
                            <li><a href=""><b>Store Options</b><!--[if IE 7]><!--></a><!--<![endif]-->
                                <!--[if lte IE 6]>
                                <table>
                                    <tr>
                                        <td><![endif]-->
                                <div class="select_sub">
                                    <ul class="sub">
                                        <li><a href="allStores">Show All Stores</a></li>
                                    </ul>
                                </div>
                                <!--[if lte IE 6]></td></tr></table></a><![endif]-->
                            </li>
                            <li><a href="myCart"><b>My Cart</b></a></li>
                            <li><a href="checkout"><b>Checkout</b></a></li>
                        </ul>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </div>

</div>


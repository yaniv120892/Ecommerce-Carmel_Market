package Servlets.Store.Discount.Item_Discount;

import Backend.Addons.EventLogger;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@WebServlet(urlPatterns = "/editDiscountForItem")
public class editDiscountForItemServlet extends HttpServlet {
    ServiceProvider sc;

    public editDiscountForItemServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String storeName = request.getParameter("storeName");
        String itemName = request.getParameter("itemName");
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {

                String userID = request.getSession().getAttribute("userID").toString();
                String discountType = request.getParameter("discountTypeEdit");
                String percent = request.getParameter("percentEdit");
                String oldStartDate = request.getParameter("oldStartDate");
                String oldEndDate = request.getParameter("oldEndDate");

            /*Calendar calStart = convertDateFormatToCaleanderDate(oldStartDate);
            String oldStartDay = calStart.get(Calendar.DAY_OF_MONTH)+"";
            String oldStartMonth = calStart.get(Calendar.MONTH)+1+"";
            String oldStartYear =calStart.get(Calendar.YEAR)+"";

            Calendar calEnd = convertDateFormatToCaleanderDate(oldEndDate);
            String oldEndDay = calEnd.get(Calendar.DAY_OF_MONTH)+"";
            String oldEndMonth = calEnd.get(Calendar.MONTH)+1+"";
            String oldEndYear = calEnd.get(Calendar.YEAR)+"";
                String oldStartDateStringFormat = AddZeros(oldStartDay) + "-" + AddZeros(oldStartMonth) + "-" + oldStartYear;
                String oldEndDateStringFormat = AddZeros(oldEndDay) + "-" + AddZeros(oldEndMonth) + "-" + oldEndYear;
*/
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String newStartDateStringFormat = AddZeros(request.getParameter("startDay")) + "-" + AddZeros(request.getParameter("startMonth")) + "-" + request.getParameter("startYear");
                String newEndDateStringFormat = AddZeros(request.getParameter("endDay")) + "-" + AddZeros(request.getParameter("endMonth")) + "-" + request.getParameter("endYear");
                //String newStartDateWithDateFormat = df.parse(newStartDateStringFormat).toString();
                //String newEndDateWithDateFormat = df.parse(newEndDateStringFormat).toString();


                String oldDiscountType = request.getParameter("oldDiscountType");
                String oldPercent = request.getParameter("oldPercent");


                sc.editDiscountFromItem(userID, itemName, storeName, discountType, newStartDateStringFormat, newEndDateStringFormat,
                        percent, oldDiscountType, oldStartDate, oldEndDate, oldPercent);
                String successString = "Discount for item" + itemName + " was updated successfully";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/discountsForItem?storeName=" + storeName + "&itemName=" + itemName + "&successString=" + successString);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/discountsForItem?storeName=" + storeName + "&itemName=" + itemName + "&errorString=" + errorString);
            dispatcher.forward(request, response);
        }
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("storeName");
        String itemName = request.getParameter("itemName");




        String startDateEdit = request.getParameter("startDate");
        String endDateEdit = request.getParameter("endDate");
        Calendar calStart = convertDateFormatToCaleanderDate(startDateEdit);
        String startDay = calStart.get(Calendar.DAY_OF_MONTH)+"";
        String startMonth = calStart.get(Calendar.MONTH)+1+"";
        String startYear =calStart.get(Calendar.YEAR)+"";

        Calendar calEnd = convertDateFormatToCaleanderDate(endDateEdit);
        String endDay = calEnd.get(Calendar.DAY_OF_MONTH)+"";
        String endMonth = calEnd.get(Calendar.MONTH)+1+"";
        String endYear = calEnd.get(Calendar.YEAR)+"";

        String discountTypeEdit = request.getParameter("Type");
        String percentEdit = request.getParameter("percentOff");
        try {
            String successString = request.getParameter("successString");
            RequestDispatcher dispatcher =
                    this.getServletContext().getRequestDispatcher
                            ("/discountsForItem?storeName=" + storeName
                                    + "&itemName=" + itemName +
                                    "&startDateEdit=" + startDateEdit + "&endDateEdit=" + endDateEdit +
                                    "&startDayEdit=" + startDay + "&endDayEdit=" + endDay +
                                    "&startMonthEdit=" + startMonth + "&endMonthEdit=" + endMonth +
                                    "&startYearEdit=" + startYear + "&endYearEdit=" + endYear +
                                    "&discountTypeEdit=" + discountTypeEdit + "&percentEdit=" + percentEdit);
            dispatcher.forward(request, response);

        } catch (Exception e) {
            EventLogger.errorLogger.info("editDiscountForItemServlet.doGet: has thrown an exception");
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName=" + storeName + "&errorString=" + errorString);
            dispatcher.forward(request, response);
        }

    }

    //get date format "Mon Jan 01 00:00:00 IST 2018" and return 01-01-2018
    private Calendar convertDateFormatToCaleanderDate(String oldFormatDate) {
        Date d = null;
        SimpleDateFormat format = new SimpleDateFormat( "E MMM dd HH:mm:ss z yyyy" );
        try {
            d = format.parse(oldFormatDate);
        } catch (Exception e) {
            EventLogger.errorLogger.info("convertDateFormatToCaleanderDate: has thrown an exception");
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return  cal;
    }

    private String AddZeros(String number) {
        if (number.length() < 2)
            return "0" + number;
        return number;
    }
}

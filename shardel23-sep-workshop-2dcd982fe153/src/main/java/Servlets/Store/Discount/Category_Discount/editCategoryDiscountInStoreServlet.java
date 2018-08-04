package Servlets.Store.Discount.Category_Discount;

import Backend.Addons.EventLogger;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.DiscountType;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@WebServlet(urlPatterns = "/editCategoryDiscountInStore")
public class editCategoryDiscountInStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public editCategoryDiscountInStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {

            String userID = request.getSession().getAttribute("userID").toString();
            String newDiscountType = request.getParameter("discountTypeEdit");

            String newPercent = request.getParameter("percentEdit");
            String storeName = request.getParameter("storeName");
            String categoryDiscountName = request.getParameter("categoryDiscountName");

            String oldStartDate = request.getParameter("oldStartDate");
            String oldEndDate = request.getParameter("oldEndDate");
            //Calendar calStart = convertDateFormatToCaleanderDate(oldStartDate);
            //String oldStartDay = calStart.get(Calendar.DAY_OF_MONTH) + "";
            //String oldStartMonth = calStart.get(Calendar.MONTH) + 1 + "";
            //String oldStartYear = calStart.get(Calendar.YEAR) + "";

            //Calendar calEnd = convertDateFormatToCaleanderDate(oldEndDate);
            //String oldEndDay = calEnd.get(Calendar.DAY_OF_MONTH) + "";
            //String oldEndMonth = calEnd.get(Calendar.MONTH) + 1 + "";
            //String oldEndYear = calEnd.get(Calendar.YEAR) + "";


            String newStartDate = AddZeros(request.getParameter("startDay")) + "-" + AddZeros(request.getParameter("startMonth")) + "-" + request.getParameter("startYear");
            String newEndDate = AddZeros(request.getParameter("endDay")) + "-" + AddZeros(request.getParameter("endMonth")) + "-" + request.getParameter("endYear");


            String oldPercent = request.getParameter("oldPercent");
            String oldDiscountType = request.getParameter("oldDiscountType");
            //String oldStartDateStringFormat = AddZeros(oldStartDay) + "-" + AddZeros(oldStartMonth) + "-" + oldStartYear;
            //String oldEndDateStringFormat = AddZeros(oldEndDay) + "-" + AddZeros(oldEndMonth) + "-" + oldEndYear;


            try {
                sc.editCategoryDiscountToScore( userID,  categoryDiscountName,  storeName,  newDiscountType,  newStartDate,  newEndDate,  newPercent
                        ,  oldDiscountType,  oldStartDate,  oldEndDate,  oldPercent);
                String successString = "Discount for category" + categoryDiscountName + " was updated successfully";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName=" + storeName + "&successString=" + successString);
                dispatcher.forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                String errorString = e.getMessage();
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName=" + storeName + "&errorString=" + errorString);
                dispatcher.forward(request, response);
            }
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("storeName");
        String categoryDiscountNameToEdit = request.getParameter("categoryDiscountName");
        String discountTypeEdit = request.getParameter("discountTypeEdit");




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

        String percentEdit = request.getParameter("percentOff");
        try {
            String successString = request.getParameter("successString");
            RequestDispatcher dispatcher =
                    this.getServletContext().getRequestDispatcher
                            ("/categoryDiscountsInStore?storeName=" + storeName
                                    + "&categoryDiscountNameToEdit=" + categoryDiscountNameToEdit +
                                    "&startDateEdit=" + startDateEdit + "&endDateEdit=" + endDateEdit +
                                    "&startDayEdit=" + startDay + "&endDayEdit=" + endDay +
                                    "&startMonthEdit=" + startMonth + "&endMonthEdit=" + endMonth +
                                    "&startYearEdit=" + startYear + "&endYearEdit=" + endYear +
                                    "&percentEdit=" + percentEdit+"&discountTypeEdit=" + discountTypeEdit);
            dispatcher.forward(request, response);

        } catch (Exception e) {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName=" + storeName + "&errorString=" + errorString);
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

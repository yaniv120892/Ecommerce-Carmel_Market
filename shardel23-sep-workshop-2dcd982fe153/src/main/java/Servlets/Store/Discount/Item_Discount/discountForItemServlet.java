package Servlets.Store.Discount.Item_Discount;

import Backend.Addons.EventLogger;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Enums.DiscountType;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@WebServlet(urlPatterns = "/discountsForItem")
public class discountForItemServlet extends HttpServlet {
    ServiceProvider sc;

    public discountForItemServlet() {
        sc = ServiceProvider.getSP();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        request.setAttribute("errorString", request.getParameter("errorString"));
        request.setAttribute("successString", request.getParameter("successString"));


        String userID = (String) request.getSession().getAttribute("userID");

            if (request.getParameter("remove") != null) {
            String storeName = request.getParameter("storeName");
            String itemName = request.getParameter("itemName");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String percentOff = request.getParameter("percentOff");
            String type = request.getParameter("Type");
            try {
                sc.removeItemDiscount(userID, itemName, storeName, type, startDate, endDate);
                String successString = "Discount for " + itemName + " was removed successfully from the Store";
                request.setAttribute("successString", successString);
                request.setAttribute("storeName", storeName);
                request.setAttribute("itemName", itemName);
                String user = request.getSession().getAttribute("userID").toString();
                Set<Discount> itemDiscounts = sc.getItemDiscounts(user, storeName, itemName);
                request.setAttribute("itemDiscounts", itemDiscounts);
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher("/WEB-INF/views/Discounts/Item_Discount/itemDiscountView.jsp");
                dispatcher.forward(request, response);
            } catch (Exception e) {
                request.setAttribute("errorString", e.getMessage());
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher("/WEB-INF/views/Discounts/Item_Discount/itemDiscountView.jsp");
                dispatcher.forward(request, response);
            }
            //remove
        } else {

            String user = request.getSession().getAttribute("userID").toString();
            String storeName = request.getParameter("storeName");
            String itemName = request.getParameter("itemName");

            try {
                //request.setAttribute("errorString", request.getParameter("errorString"));
                Set<Discount> itemDiscounts = sc.getItemDiscounts(user, storeName, itemName);
                request.setAttribute("itemDiscounts", itemDiscounts);
                request.setAttribute("storeName", storeName);
                request.setAttribute("itemName", itemName);

                request.setAttribute("startDateEdit", request.getParameter("startDateEdit"));
                request.setAttribute("endDateEdit", request.getParameter("endDateEdit"));

                request.setAttribute("startDayEdit", request.getParameter("startDayEdit"));
                request.setAttribute("endDayEdit", request.getParameter("endDayEdit"));
                request.setAttribute("startMonthEdit", request.getParameter("startMonthEdit"));
                request.setAttribute("endMonthEdit", request.getParameter("endMonthEdit"));
                request.setAttribute("startYearEdit", request.getParameter("startYearEdit"));
                request.setAttribute("endYearEdit", request.getParameter("endYearEdit"));


                request.setAttribute("discountTypeEdit", request.getParameter("discountTypeEdit"));
                request.setAttribute("percentEdit", request.getParameter("percentEdit"));
                request.setAttribute("listDiscountType", new ArrayList<>(Arrays.asList(DiscountType.values())));






                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Discounts/Item_Discount/itemDiscountView.jsp");
                dispatcher.forward(request, response);

            } catch (Exception e) {
                EventLogger.errorLogger.info("discountForItemServlet.doGet: has thrown an exception");
                request.setAttribute("errorString", e.getMessage());
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);            }
        }
    }
}

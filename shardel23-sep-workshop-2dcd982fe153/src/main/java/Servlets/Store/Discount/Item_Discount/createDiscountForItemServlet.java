package Servlets.Store.Discount.Item_Discount;

import Backend.Addons.EventLogger;
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

@WebServlet(urlPatterns = "/createDiscountForItem")
public class createDiscountForItemServlet extends HttpServlet {
    ServiceProvider sc;

    public createDiscountForItemServlet() {
        sc = ServiceProvider.getSP();
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        request.setAttribute("errorString", request.getParameter("errorString"));
        request.setAttribute("successString", request.getParameter("successString"));

        String userID = request.getSession().getAttribute("userID").toString();
        String discountType = request.getParameter("discountType");
        String percent = request.getParameter("percent");
        String storeName = request.getParameter("storeName");
        String itemName = request.getParameter("itemName");
        String startDate = AddZeros(request.getParameter("startDay")) + "-" + AddZeros(request.getParameter("startMonth")) + "-" + request.getParameter("startYear");
        String endDate = AddZeros(request.getParameter("endDay")) + "-" + AddZeros(request.getParameter("endMonth")) + "-" + request.getParameter("endYear");

        try {
            sc.addItemDiscount(userID, itemName, storeName, discountType,startDate,endDate, percent);
            String successString = "Discount for item" + itemName + " was added successfully";
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/discountsForItem?storeName=" + storeName + "&itemName=" + itemName + "&successString=" + successString);
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/discountsForItem?storeName="+storeName+"&errorString="+errorString);
            dispatcher.forward(request, response);
        }

    }


    private String AddZeros(String number) {
        if (number.length() < 2)
            return "0" + number;
        return number;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {

            String storeName = request.getParameter("storeName");
            String itemName = request.getParameter("itemName");
            request.setAttribute("storeName", storeName);
            request.setAttribute("itemName", itemName);
            request.setAttribute("listDiscountType", new ArrayList<>(Arrays.asList(DiscountType.values())));


            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Discounts/Item_Discount/createItemDiscount.jsp");
            dispatcher.forward(request, response);
        }
    }
}

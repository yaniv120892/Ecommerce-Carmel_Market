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

import java.util.ArrayList;
import java.util.Arrays;

@WebServlet(urlPatterns = "/addCategoryDiscount")
public class addCategoryDiscountServlet extends HttpServlet {
    ServiceProvider sc;

    public addCategoryDiscountServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = null;
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("errorString", request.getParameter("errorString"));
                request.setAttribute("successString", request.getParameter("successString"));

                String userID = request.getSession().getAttribute("userID").toString();
                String categoryName = request.getParameter("categoryName");
                String discountType = request.getParameter("discountType");

                String percent = request.getParameter("percent");
                storeName = request.getParameter("storeName");
                String startDate =  AddZeros(request.getParameter("startDay"))+"-"+ AddZeros(request.getParameter("startMonth"))+"-"+request.getParameter("startYear") ;
                String endDate =  AddZeros(request.getParameter("endDay"))+"-"+AddZeros(request.getParameter("endMonth"))+"-"+request.getParameter("endYear") ;
                sc.addCategoryDiscountToStore(userID,storeName,categoryName, discountType, startDate,endDate,percent);
                String successString = "Discount for category  " + categoryName + " was added successfully";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName=" + storeName + "&successString=" + successString);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName=" + storeName + "&errorString=" + errorString);
            dispatcher.forward(request, response);
        }
    }

    private String AddZeros(String number)
    {
        if(number.length() < 2)
            return  "0"+number;
        return number;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = null;
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {
                storeName = request.getParameter("storeName");
                String userID = request.getSession().getAttribute("userID").toString();
                request.setAttribute("storeName", storeName);
                request.setAttribute("listCategories", new ArrayList<>(Arrays.asList(CategoryType.values())));
                request.setAttribute("listDiscountType", new ArrayList<>(Arrays.asList(DiscountType.values())));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Discounts/Category_Discount/addCategoryDiscount.jsp");
                dispatcher.forward(request, response);

            }
        }
        catch(Exception e)
        {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName=" + storeName + "&errorString=" + errorString);
            dispatcher.forward(request, response);
        }
    }
}






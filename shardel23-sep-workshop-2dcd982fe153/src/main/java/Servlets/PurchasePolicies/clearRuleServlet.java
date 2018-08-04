package Servlets.PurchasePolicies;

import Backend.Addons.EventLogger;
import Backend.Entities.Discount.Discount;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.LogicOperator;
import Backend.Entities.Items.Item;
import ServiceLayer.ServiceProvider;
import javafx.util.Pair;

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

@WebServlet(urlPatterns = "/clearRule")
public class clearRuleServlet extends HttpServlet {
    ServiceProvider sc;

    public clearRuleServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("storeName");
        if(storeName == "")
            storeName = null;
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("errorString", request.getParameter("errorString"));
                request.setAttribute("successString", request.getParameter("successString"));


                String userID = request.getSession().getAttribute("userID").toString();
                String itemName = request.getParameter("itemName");
                String categoryName = request.getParameter("categoryName");
                sc.removePurchaseRule(userID,itemName,storeName,categoryName);
                request.setAttribute("storeName", storeName);
                String successString = "Rule was cleared successfully";
                RequestDispatcher dispatcher = null;
                if(storeName == null)
                    dispatcher  = getServletContext().getRequestDispatcher("/systemPurchasePolicies?successString="+successString);
                else
                    dispatcher  = getServletContext().getRequestDispatcher("/storePurchasePolicies?storeName="+storeName+"&successString="+successString);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = null;
            if(storeName == null)
                dispatcher  = getServletContext().getRequestDispatcher("/systemPurchasePolicies?errorString="+errorString);
            else
                dispatcher  = getServletContext().getRequestDispatcher("/storePurchasePolicies?storeName="+storeName+"&errorString="+errorString);
            dispatcher.forward(request, response);
        }
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}






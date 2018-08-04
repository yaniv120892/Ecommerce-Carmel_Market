package Servlets.PurchasePolicies;

import Backend.Addons.EventLogger;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/ruleDetails")
public class ruleDetailsServlet extends HttpServlet {
    ServiceProvider sc;

    public ruleDetailsServlet() {
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

                String itemName = request.getParameter("itemName");
                String categoryName = request.getParameter("categoryName");
                String ruleDetails = sc.getLiteralPurchaseRule(itemName,storeName,categoryName);


                RequestDispatcher dispatcher = null;
                if(storeName == null)
                    dispatcher  = getServletContext().getRequestDispatcher("/systemPurchasePolicies?ruleDetails="+ruleDetails);
                else
                    dispatcher  = getServletContext().getRequestDispatcher("/storePurchasePolicies?storeName="+storeName+"&ruleDetails="+ruleDetails);
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






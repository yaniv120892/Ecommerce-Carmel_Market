package Servlets.PurchasePolicies;

import Backend.Addons.EventLogger;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.LogicOperator;
import Backend.Entities.Enums.RuleType;
import Backend.Entities.Enums.States;
import Backend.Entities.Store;
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

@WebServlet(urlPatterns = "/addRule")
public class addRuleServlet extends HttpServlet {
    ServiceProvider sc;

    public addRuleServlet() {
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
                //String inputDetail = null;
                String ruleType = request.getParameter("ruleType");
                String operator = request.getParameter("operator");
                String itemName = request.getParameter("itemName");
                String categoryName = request.getParameter("categoryName");
                boolean notBit = (request.getParameter("notOperand") != null);
                String userID = request.getSession().getAttribute("userID").toString();
                int numberDetail = -1;
                String stringDetail = null;
                if((ruleType.equals("MAX_AMOUNT") || ruleType.equals("MIN_AMOUNT")))
                    numberDetail = Integer.parseInt(request.getParameter("numberDetail"));
                else
                    stringDetail = request.getParameter("stateValue");

                sc.addPurchaseRule(userID, itemName, notBit, operator, ruleType, numberDetail, stringDetail, storeName, categoryName);
                String successString = "Rule was added successfully";
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
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            String storeName = request.getParameter("storeName");
            String itemName = request.getParameter("itemName");
            String categoryName = request.getParameter("categoryName");
            request.setAttribute("storeName",storeName );
            request.setAttribute("itemName",itemName );
            request.setAttribute("categoryName",categoryName );
            request.setAttribute("listOperators",new ArrayList<>(Arrays.asList(LogicOperator.values())));
            request.setAttribute("listRuleTypes",new ArrayList<>(Arrays.asList(RuleType.values())));
            request.setAttribute("listStates",new ArrayList<>(Arrays.asList(States.values())));


            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/PurchasePolicies/addRule.jsp");
            dispatcher.forward(request, response);
        }
    }
}






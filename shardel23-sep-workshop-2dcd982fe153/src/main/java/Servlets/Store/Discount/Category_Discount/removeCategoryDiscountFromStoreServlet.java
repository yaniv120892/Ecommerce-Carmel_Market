package Servlets.Store.Discount.Category_Discount;

import Backend.Addons.EventLogger;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/removeCategoryDiscountFromStore")
public class removeCategoryDiscountFromStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public removeCategoryDiscountFromStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        else {
            String categoryName = request.getParameter("categoryDiscountName");
            String currStoreName = request.getParameter("storeName");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String discountType = request.getParameter("discountType");
            String userID = request.getSession().getAttribute("userID").toString();
            try {
                sc.removeCategoryDiscountFromStore(userID,categoryName,currStoreName,discountType, startDate, endDate);
                String successString =  "Discount for " + categoryName + " was removed successfully from the Store";
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName="+currStoreName+"&successString="+successString);
                dispatcher.forward(request, response);
            } catch (Exception e) {
                String errorString = e.getMessage();
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/categoryDiscountsInStore?storeName=" + currStoreName + "&errorString=" + errorString);
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}






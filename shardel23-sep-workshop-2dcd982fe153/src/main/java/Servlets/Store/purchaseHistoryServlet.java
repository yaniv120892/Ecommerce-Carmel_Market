package Servlets.Store;

import Backend.Entities.PurchaseHistory;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@WebServlet(urlPatterns = "/purchaseHistory")
public class purchaseHistoryServlet extends HttpServlet {
    ServiceProvider sc;

    public purchaseHistoryServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            String userID = request.getSession().getAttribute("userID").toString();
            String storeName = request.getParameter("storeName");
            String userIDHistory = request.getParameter("userIDHistory");
            if (storeName != null) {
                Set<PurchaseHistory> listPurchaseHistory = null;
                try {
                    listPurchaseHistory = sc.getStorePurchaseHistory(userID, storeName);
                    request.setAttribute("listPurchaseHistory", listPurchaseHistory);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("errorString", e.getMessage());
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
                    dispatcher.forward(request, response);
                }
                request.setAttribute("listPurchaseHistory", listPurchaseHistory);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Store/purchaseHistory.jsp");
                dispatcher.forward(request, response);
            }else if(userIDHistory != null){
                ArrayList<PurchaseHistory> purchaseHistoryArrayList = null;
                try{
                    purchaseHistoryArrayList = sc.getUserPurchaseHistory(userID, userIDHistory);
                }catch (Exception e){
                    e.printStackTrace();
                    request.setAttribute("errorString", e.getMessage());
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
                    dispatcher.forward(request, response);
                }
                request.setAttribute("listPurchaseHistory", purchaseHistoryArrayList);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Store/purchaseHistory.jsp");
                dispatcher.forward(request, response);
            }

        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}






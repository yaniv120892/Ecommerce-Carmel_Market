package Servlets.Store;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/closeStore")
public class closeStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public closeStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = null;
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            try {
                String userID = request.getSession().getAttribute("userID").toString();
                storeName = request.getParameter("storeName");
                sc.closeStore(userID,storeName);
                String successString= "Store "+storeName+" was closed successString";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myStores?successString="+successString);
                dispatcher.forward(request, response);
            } catch (Exception e) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myStores?errorString="+e.getMessage());
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}






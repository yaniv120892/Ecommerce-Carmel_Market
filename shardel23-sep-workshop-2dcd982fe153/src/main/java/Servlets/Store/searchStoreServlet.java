package Servlets.Store;

import Backend.Entities.Store;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/searchStore")
public class searchStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public searchStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {
                String currStoreName = request.getParameter("storeName");
                String userID = request.getSession().getAttribute("userID").toString();

                Store store = sc.getStoreByName(currStoreName);
                if (store != null) {
                    RequestDispatcher rd = request.getRequestDispatcher("saleItems?storeName=" + currStoreName);
                    rd.forward(request, response);
                } else {
                    String errorString =  "Store " + currStoreName + " doesn't exist";
                    RequestDispatcher rd = request.getRequestDispatcher("allStores?errorString="+errorString);
                    rd.forward(request, response);
                }
            }
        }
        catch(Exception e)
        {
            String errorString =  e.getMessage();
            RequestDispatcher rd = request.getRequestDispatcher("allStores?errorString="+errorString);
            rd.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

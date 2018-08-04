package Servlets.Store;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/myStores")
public class myStoresServlet extends HttpServlet {
    ServiceProvider sc;

    public myStoresServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            String userID = request.getSession().getAttribute("userID").toString();
            List listStores = null;
            try {
                listStores = sc.getMyStores(userID);
                request.setAttribute("successString", request.getParameter("successString"));
                request.setAttribute("errorString", request.getParameter("errorString"));
                request.setAttribute("listStores", listStores);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Store/myStores.jsp");
                dispatcher.forward(request, response);
            }
            catch (Exception e) {
                request.setAttribute("errorString", e.getMessage());
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Store/myStores.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}






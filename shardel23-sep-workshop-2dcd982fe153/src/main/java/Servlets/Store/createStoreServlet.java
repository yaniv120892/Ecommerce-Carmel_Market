package Servlets.Store;

import Backend.Entities.Enums.CategoryType;
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

@WebServlet(urlPatterns = "/createStore")
public class createStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public createStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {
                String newStoreName = request.getParameter("newStoreName");
                String newStoreEmail = request.getParameter("newStoreEmail");
                String userID = request.getSession().getAttribute("userID").toString();

                sc.openStore(userID, newStoreName, newStoreEmail);
                String successString = "Store " + newStoreName + " added successfully";
                RequestDispatcher rd = request.getRequestDispatcher("allStores?successString=" + successString);
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String errorString = e.getMessage();
            RequestDispatcher rd = request.getRequestDispatcher("allStores?errorString=" + errorString);
            rd.forward(request, response);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Store/createStore.jsp");
            dispatcher.forward(request, response);
        }
    }
}






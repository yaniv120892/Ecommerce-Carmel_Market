package Servlets.AdminTools;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/adminUsers")
public class adminUserServlet extends HttpServlet {
    ServiceProvider sc;

    public adminUserServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = (String) request.getSession().getAttribute("userID");
        if (userID == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }

        String toRemoveEmail = request.getParameter("userIDToRemove");
        if(toRemoveEmail != null){
            try {
                sc.removeUserFromSystem(userID, toRemoveEmail);
                request.setAttribute("successString", "User Deleted Successfully");
            } catch (Exception e) {
                //TODO: add error string
                e.printStackTrace();
            }
        }

        List listUsers = sc.getAllUsers();
        request.setAttribute("listUsers", listUsers);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/admin/adminUserView.jsp");
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
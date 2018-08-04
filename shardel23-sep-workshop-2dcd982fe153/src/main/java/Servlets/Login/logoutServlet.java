package Servlets.Login;


import ServiceLayer.ServiceProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/logout"})
public class logoutServlet extends HttpServlet {

    ServiceProvider sc;

    public logoutServlet() {
        sc = ServiceProvider.getSP();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String userID = (String) request.getSession().getAttribute("userID");
        sc.removeInSystemUser(userID);
        request.getSession().removeAttribute("userID");
        response.sendRedirect(request.getContextPath() + "/");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

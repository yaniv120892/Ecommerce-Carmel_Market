package Servlets.Login;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/doRegister"})
public class doRegisterServlet extends HttpServlet {

    ServiceProvider sc;

    public doRegisterServlet() {
        sc = ServiceProvider.getSP();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        //String nationality = request.getParameter("nationalities");

        try {
            sc.makeRegister(email, password);
            request.getSession().setAttribute("userID", email);
            request.getSession().setAttribute("userType", "REGISTERED");
            response.sendRedirect(request.getContextPath() + "/MainMenu");
        } catch (Exception e) {
            String errorString = e.getMessage();
            request.setAttribute("errorString", errorString);
            request.setAttribute("email", email);
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/registerView.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

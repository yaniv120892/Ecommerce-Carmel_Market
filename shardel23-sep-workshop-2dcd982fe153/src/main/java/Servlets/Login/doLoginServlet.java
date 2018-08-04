package Servlets.Login;

import Backend.Addons.EventLogger;
import Backend.Entities.Users.User;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/doLogin"})
public class doLoginServlet extends HttpServlet {

    ServiceProvider sc;

    public doLoginServlet() {
        sc = ServiceProvider.getSP();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check username and password
        String userID = null;
        if (request.getSession().getAttribute("userID") != null) {
            userID = (String) request.getSession().getAttribute("userID");
        }

        //user pressed login button
        if (request.getParameter("user") != null) {
            if (userID == null) {
                userID = sc.firstConnection();
            }
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            try {
                sc.makeLogin(userID, email, password);
                userID = email;
                request.getSession().setAttribute("userID", userID);
            } catch (Exception e) {
                e.printStackTrace();
                String errorString = "Login Failed!";
                request.setAttribute("errorString", errorString);
                request.setAttribute("email", email);
                request.getSession().removeAttribute("userID");
                RequestDispatcher dispatcher //
                        = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            }
        }
        //user presses login as guest
        else if (request.getParameter("guest") != null) {
            if (userID == null) {
                userID = sc.firstConnection();
            }
            request.getSession().setAttribute("userID", userID);
        }
        //user pressed register button
        else if (request.getParameter("register") != null) {
            //ArrayList<Nationality> nationalities = new ArrayList<>(Arrays.asList(Nationality.values()));
            //request.setAttribute("nat", nationalities);
            request.getSession().removeAttribute("userID");
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/registerView.jsp");
            dispatcher.forward(request, response);
        } else {
            return;
        }

        User user = sc.getInSystemUser(userID);
        request.getSession().setAttribute("userType", user.getType().toString());
        response.sendRedirect(request.getContextPath() + "/MainMenu");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

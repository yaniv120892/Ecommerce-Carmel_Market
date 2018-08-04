package Servlets.AdminTools;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(urlPatterns = "/userStatistics")
public class userStatisticsServlet extends HttpServlet {


    ServiceProvider sc;

    public userStatisticsServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userID = (String) request.getSession().getAttribute("userID");
        if (userID == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }

        response.sendRedirect(request.getContextPath() + "/MainMenu");
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userID = (String) request.getSession().getAttribute("userID");
        if (userID == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        if (request.getParameter("userID") != null) {
            doPost(request, response);
            return;
        }
        try {
            ConcurrentHashMap<String, Integer> map = sc.getWebsocketUsersStatistics();
            request.setAttribute("listStatistics", map);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/admin/userLoginStatistics.jsp");
            dispatcher.forward(request, response);
        }
        catch (Exception e)
        {
            response.sendRedirect(request.getContextPath() + "/MainMenu?errorString="+e.getMessage());
        }
    }
}

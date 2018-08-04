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
import java.util.Map;

@WebServlet(urlPatterns = "/testNotifications")
public class testNotificationsServlet extends HttpServlet {


    ServiceProvider sc;

    public testNotificationsServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userID = (String) request.getSession().getAttribute("userID");
        if (userID == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }

        String msg = request.getParameter("msg");
        if (request.getParameter("toAll") != null) {
            try {
                sc.sendNotification(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            userID = request.getParameter("userID");
            try {
                sc.sendNotification(userID, msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        Map<String, Session> map = sc.getWebsocketUsers();
        request.setAttribute("listUsers", map);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/admin/adminTestNotificationView.jsp");
        dispatcher.forward(request, response);
    }
}

package Servlets.Store;

import Backend.Addons.TreeNode.TreeNode;
import Backend.Entities.Privileges;
import Backend.Entities.Users.User;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@WebServlet(urlPatterns = "/appointmentsHistory")
public class appointmentsHistoryServlet extends HttpServlet {
    ServiceProvider sc;

    public appointmentsHistoryServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        try {
            String storeName = request.getParameter("storeName");
            Iterator<TreeNode<User>> iter = sc.getAppointmentsHistory(storeName);
            String userToShow = request.getParameter("userToShow");
            if(userToShow != null)
            {
                String userID = (String) request.getSession().getAttribute("userID");
                Privileges privileges = sc.getUserPrivilages(userID, storeName,userToShow);
                request.setAttribute("privileges",privileges);

            }
            request.getSession().setAttribute("iter",iter);
            request.setAttribute("storeName",storeName );
            request.setAttribute("userToShow",userToShow);


            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Store/appointmentsHistory.jsp");
            dispatcher.forward(request, response);

        }
        catch(Exception e)
        {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myStores?errorString="+e.getMessage());
            dispatcher.forward(request, response);
        }
    }
}

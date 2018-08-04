package Servlets.Store.Item;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/removeItemFromStore")
public class removeItemFromStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public removeItemFromStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        else {
            String currItemName = request.getParameter("itemName");
            String currStoreName = request.getParameter("storeName");
            String userID = request.getSession().getAttribute("userID").toString();
            try {
                sc.removeItemFromStore(userID,currItemName,currStoreName);
                String successString =  "Item " + currItemName + " was removed successfully from the Store";
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher("/allItems?successString="+successString);
                dispatcher.forward(request, response);
            } catch (Exception e) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?errorString="+e.getMessage());
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}






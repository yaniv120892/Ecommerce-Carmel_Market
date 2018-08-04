package Servlets.User;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/addOwnerOrManager")
public class addOwnerOrManagerServlet extends HttpServlet {
    ServiceProvider sc;

    public addOwnerOrManagerServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String successString;
        successString = null;
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            try {
                String currStoreName = request.getParameter("storeName");
                String userID = request.getSession().getAttribute("userID").toString();
                String userEmail = request.getParameter("userEmail");//save the new user to assign
                boolean addOwner = Boolean.parseBoolean(request.getParameter("addOwner"));
                boolean addManager = Boolean.parseBoolean(request.getParameter("addManager"));
                if (addOwner) {
                    sc.addNewStoreOwner(userID, userEmail, currStoreName);
                    successString = "User "+userEmail+" is a new owner of "+currStoreName;
                }
                if (addManager) {
                    boolean[] priv = new boolean[14];
                    priv[0] = Boolean.parseBoolean(request.getParameter("canAddItemToStore"));
                    priv[1] = Boolean.parseBoolean(request.getParameter("canRemoveItemFromStore"));
                    priv[2] = Boolean.parseBoolean(request.getParameter("canEditItemPrice"));
                    priv[3] = Boolean.parseBoolean(request.getParameter("canEditItemStock"));
                    priv[4] = Boolean.parseBoolean(request.getParameter("canEditItemIsOnSale"));
                    priv[5] = Boolean.parseBoolean(request.getParameter("canAddItemDiscount"));
                    priv[6] = Boolean.parseBoolean(request.getParameter("canRemoveItemDiscount"));
                    priv[7] = false;

                    priv[8] = Boolean.parseBoolean(request.getParameter("canAddManager"));
                    priv[9] = Boolean.parseBoolean(request.getParameter("canGetPurchaseHistory"));
                    priv[10] = false;
                    priv[11] = false;
                    priv[12] = false;
                    priv[13] = false;





                    sc.appointManager(userID, userEmail, currStoreName, priv);
                    successString =  "User "+userEmail+" is a new manager of "+currStoreName;
                }
            } catch (Exception e) {
                request.setAttribute("errorString", e.getMessage());
                doGet(request, response);
                return;

            }
        }
        response.sendRedirect("/myStores?successString="+successString);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            String userID = request.getSession().getAttribute("userID").toString();
            String storeName = request.getParameter("storeName");
            boolean addOwner = Boolean.parseBoolean(request.getParameter("addOwner"));
            boolean addManager = Boolean.parseBoolean(request.getParameter("addManager"));


            request.setAttribute("addOwner", addOwner);
            request.setAttribute("addManager", addManager);
            request.setAttribute("userID", userID);
            request.setAttribute("storeName", storeName);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/User/addOwnerOrManager.jsp");
            dispatcher.forward(request, response);
        }
    }
}

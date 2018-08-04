package Servlets.Cart;

import Backend.Entities.Items.CartItem;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(urlPatterns = "/removeSaleItemFromCart")
public class removeSaleItemFromCartServlet extends HttpServlet {
    ServiceProvider sc;

    public removeSaleItemFromCartServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        }
        else {
            String currItemName = request.getParameter("itemName");
            String saleType = request.getParameter("saleType");
            String currStoreName = request.getParameter("storeName");
            String userID = request.getSession().getAttribute("userID").toString();


            try {
                sc.removeFromCart(userID,currItemName,currStoreName,saleType);
                String successString = "Item " + currItemName + " was removed successfully from the Cart";
                Set<CartItem> cartItems = sc.getAllItemsFromCart(userID);
                request.setAttribute("cartItems", cartItems);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myCart?successString="+successString);
                dispatcher.forward(request, response);
            } catch (Exception e) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myCart?errorString="+e.getMessage());
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

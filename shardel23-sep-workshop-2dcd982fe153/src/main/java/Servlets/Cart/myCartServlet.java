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

@WebServlet(urlPatterns = "/myCart")
public class myCartServlet extends HttpServlet {
    ServiceProvider sc;

    public myCartServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            try {
                String userID = request.getSession().getAttribute("userID").toString();
                Set<CartItem> cartItems = sc.getAllItemsFromCart(userID);
                request.setAttribute("cartItems", cartItems);
                Set<String> listCoupon = sc.getCouponsInCart(userID);
                request.setAttribute("listCoupon", listCoupon);
                request.setAttribute("successString",request.getParameter("successString"));
                request.setAttribute("errorString",request.getParameter("errorString"));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Cart/myCart.jsp");
                dispatcher.forward(request, response);
            }
            catch (Exception e) {
                    System.out.println(e.getMessage());
                    request.setAttribute("errorString",e.getMessage());
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
                    dispatcher.forward(request, response);
                }


        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}






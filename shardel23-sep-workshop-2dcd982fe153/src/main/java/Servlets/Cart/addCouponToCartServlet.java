package Servlets.Cart;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/addCouponToCart")
public class addCouponToCartServlet extends HttpServlet {

    ServiceProvider sc;

    public addCouponToCartServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            try {//
                String couponCode = request.getParameter("couponCode");
                String userID = request.getSession().getAttribute("userID").toString();
                sc.addCouponToCart(userID, couponCode);
                String successString =  "Coupon code  " + couponCode + " was added successfully to the Cart";
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/myCart?successString="+successString);
                dispatcher.forward(request, response);
            }
            catch (Exception e) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/myCart?errorString="+e.getMessage());
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

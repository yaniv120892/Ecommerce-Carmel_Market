package Servlets.Cart;

import Backend.Entities.Enums.NationalityBackup;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Items.Item;
import Exceptions.PurchasePolicyException;
import Backend.Entities.Items.CartItem;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

@WebServlet(urlPatterns = "/checkout")
public class checkoutServlet extends HttpServlet {
    ServiceProvider sc;

    public checkoutServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            //TODO get all the information and make the payment
            String userID = request.getSession().getAttribute("userID").toString();
            String creditNumber = request.getParameter("creditNumber");
            int yearValidity = Integer.parseInt(request.getParameter("yearValidity"));
            int monthValidity = Integer.parseInt(request.getParameter("monthValidity"));
            int cvvCode = Integer.parseInt(request.getParameter("cvvCode"));
            String name = request.getParameter("name");
            String id = request.getParameter("id");
            String address = request.getParameter("address");
            String nationalities = request.getParameter("nationalities");

            try {
                //TODO: adjust checkout signature so next line works
                sc.checkout(userID,creditNumber,yearValidity,monthValidity,cvvCode,name,id,address,nationalities);
                request.setAttribute("successString", " payment process has been successfully completed");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
                dispatcher.forward(request, response);
            }
            catch (PurchasePolicyException e) {
                ArrayList<String> errorList =  e.getErrorsList();
                String totalError = "";
                for(String s : errorList){
                    totalError = totalError + s + "<br />";
                }
                request.setAttribute("errorString", totalError);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
                dispatcher.forward(request, response);
            }
            catch (Exception e) {
                request.setAttribute("errorString", e.getMessage());
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
                dispatcher.forward(request, response);
            }
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            String userID = request.getSession().getAttribute("userID").toString();
            Set<CartItem> cart;
            try {
                cart = sc.getAllItemsFromCart(userID);
                ArrayList<NationalityBackup> nationalities = new ArrayList<>(Arrays.asList(NationalityBackup.values()));
                request.setAttribute("nat", nationalities);
                request.setAttribute("cartItems", cart);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Cart/checkout.jsp");
                dispatcher.forward(request, response);
            }
            catch (Exception e) {
                request.setAttribute("errorString", e.getMessage());
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}






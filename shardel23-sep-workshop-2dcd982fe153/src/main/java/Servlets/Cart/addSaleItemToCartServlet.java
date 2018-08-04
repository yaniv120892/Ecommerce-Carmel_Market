package Servlets.Cart;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/addSaleItemToCart")
public class addSaleItemToCartServlet extends HttpServlet {
    ServiceProvider sc;

    public addSaleItemToCartServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            try {
                String currItemName = request.getParameter("itemName");
                String currStoreName = request.getParameter("storeName");
                String userID = request.getSession().getAttribute("userID").toString();
                String saleType = request.getParameter("saleType");
                int number = 0;
                if(saleType.equals("RAFFLE")) {
                    String tmp = request.getParameter("percent");
                    number = Integer.parseInt(tmp);
                }
                if(saleType.equals("NORMAL")) {
                    String tmp = request.getParameter("amount");
                    number = Integer.parseInt(tmp);
                }

                sc.addToCart(userID, currStoreName, currItemName, number, saleType);
                String successString = "Item " + currItemName + " was added successfully to the Cart";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myCart?successString="+successString);
                dispatcher.forward(request, response);
            }
            catch (NumberFormatException e){
                request.setAttribute("errorString", "Please enter a number");
                doGet(request, response);
            }
            catch (Exception e) {
                request.setAttribute("errorString", e.getMessage());
                doGet(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            String itemName = request.getParameter("itemName");
            String storeName = request.getParameter("storeName");
            String saleType = request.getParameter("saleType");



            request.setAttribute("storeName", storeName);
            request.setAttribute("itemName", itemName);
            request.setAttribute("saleType", saleType);

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Cart/addSaleItemToCart.jsp");
            dispatcher.forward(request, response);
        }
    }
}

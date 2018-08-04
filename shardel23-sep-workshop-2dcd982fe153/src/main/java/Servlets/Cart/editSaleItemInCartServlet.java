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

@WebServlet(urlPatterns = "/editSaleItemInCart")
public class editSaleItemInCartServlet extends HttpServlet {
    ServiceProvider sc;

    public editSaleItemInCartServlet() {
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
                String successString =  null;

                if(request.getParameter("amount") != null)
                {
                    int newAmount = Integer.parseInt(request.getParameter("amount"));
                    sc.changeCartItemAmount(userID,currItemName,currStoreName,newAmount);
                    successString = "Amount for item "+currItemName+" was updated successfully";
                }
                else
                {
                    int newPercent = Integer.parseInt(request.getParameter("percent"));
                    sc.changeCartItemPercent(userID,currItemName,currStoreName,newPercent);
                    successString = "Percent for item "+currItemName+" was updated successfully";

                }
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/myCart?successString="+successString);
                dispatcher.forward(request, response);
            }
            catch (Exception e) {
                request.setAttribute("errorString", e.getMessage());
                doGet(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String itemNameToEdit = request.getParameter("itemName");
            String saleType = request.getParameter("saleType");
                String userID = request.getSession().getAttribute("userID").toString();
                Set<CartItem> cartItems = sc.getAllItemsFromCart(userID);
                request.setAttribute("cartItems", cartItems);
                Set<String> listCoupon = sc.getCouponsInCart(userID);
                request.setAttribute("listCoupon", listCoupon);
                request.setAttribute("saleTypeToEdit", saleType);

                request.setAttribute("itemNameToEdit", itemNameToEdit);
                request.setAttribute("successString", request.getParameter("successString"));

            }

        catch(Exception e){
                request.setAttribute("errorString", e.getMessage());
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Cart/myCart.jsp");
            dispatcher.forward(request, response);

        }

}

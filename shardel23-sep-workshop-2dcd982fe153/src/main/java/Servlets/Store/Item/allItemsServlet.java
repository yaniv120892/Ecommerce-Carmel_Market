package Servlets.Store.Item;

import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Items.Item;
import ServiceLayer.ServiceProvider;
import javafx.util.Pair;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/allItems")
public class allItemsServlet extends HttpServlet {
    ServiceProvider sc;

    public allItemsServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("errorString", request.getParameter("errorString"));
                request.setAttribute("successString", request.getParameter("successString"));
                request.setAttribute("itemNameToEdit", request.getParameter("itemNameToEdit"));

                String userID = (String) request.getSession().getAttribute("userID");
                String selectedStoreName = request.getParameter("storeName");
                List<Item> listItems  = sc.getStoresItems(selectedStoreName);
                Map<CategoryType, Discounts> listCategoryDiscounts  = sc.getCategoryDiscountsByStoreName(selectedStoreName);

                request.setAttribute("storeName", selectedStoreName);
                request.setAttribute("listCategoryDiscounts", listCategoryDiscounts);
                request.setAttribute("listItems", listItems);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Item/allItems.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("errorString", e.getMessage());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
            dispatcher.forward(request, response);
        }
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}






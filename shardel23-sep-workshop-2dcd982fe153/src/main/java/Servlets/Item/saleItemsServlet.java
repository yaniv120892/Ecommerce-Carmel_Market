package Servlets.Item;

import Backend.Entities.Enums.CategoryType;
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
import java.util.List;
import java.util.Set;

@WebServlet(urlPatterns = "/saleItems")
public class saleItemsServlet extends HttpServlet {
    ServiceProvider sc;

    public saleItemsServlet() {
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
                request.setAttribute("saleItemNameToEdit", request.getParameter("saleItemNameToEdit"));
                request.setAttribute("listCategories", new ArrayList<>(Arrays.asList(CategoryType.values())));


                String registered = request.getParameter("registered");
                String selectedStoreName = request.getParameter("storeName");
                if (registered != null) {//Case of view of items in store with Manager/Owner
                    request.setAttribute("ownerView", true);
                }
                Set listSaleItems = sc.getStoresSaleItems(selectedStoreName);
                request.setAttribute("storeName", selectedStoreName);
                request.setAttribute("listSaleItems", listSaleItems);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Item/saleItems.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            request.setAttribute("errorString", e.getMessage());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/mainMenuView.jsp");
            dispatcher.forward(request, response);
        }
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}






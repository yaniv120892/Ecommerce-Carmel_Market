package Servlets.Store;

import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.SaleType;
import Backend.Entities.Items.SaleItem;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = "/allStores")
public class allStoresServlet extends HttpServlet {
    ServiceProvider sc;

    public allStoresServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            List<SaleItem> items = sc.getAllSaleItems();
            Set<String> itemsNameSet = new HashSet<>();
            for(SaleItem item : items){
                itemsNameSet.add(item.getItem().getName());
            }
            request.setAttribute("listItems", itemsNameSet);
            request.setAttribute("listCategories", new ArrayList<>(Arrays.asList(CategoryType.values())));

            List listStores = sc.getAllStores();
            request.setAttribute("errorString", request.getParameter("errorString"));
            request.setAttribute("successString", request.getParameter("successString"));
            request.setAttribute("listStores", listStores);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Store/allStores.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}






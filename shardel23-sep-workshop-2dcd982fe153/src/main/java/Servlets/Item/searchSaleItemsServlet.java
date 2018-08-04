package Servlets.Item;

import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Items.Item;
import Backend.Entities.Items.SaleItem;
import Exceptions.NotPremittedException;
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

@WebServlet(urlPatterns = "/searchSaleItems")
public class searchSaleItemsServlet extends HttpServlet {
    ServiceProvider sc;

    public searchSaleItemsServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currStoreName = null;
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            currStoreName = request.getParameter("storeName");
            String currItemName = request.getParameter("itemName");
            String categoryName = request.getParameter("categoryName");
            if (currStoreName == null || currStoreName.equals("")) {
                // Search in all stores
                try {
                    ArrayList<SaleItem> listSaleItems = null;
                    if (currItemName == null || currItemName.equals("")) {
                        //Searching sale Items by Item Name
                        listSaleItems = sc.getSaleItemsByCategory(categoryName);

                    } else {
                        //Searching sale Items by Item Name
                        listSaleItems = sc.getSaleItemsByItemName(currItemName);
                    }
                    request.setAttribute("listSaleItems", listSaleItems);
                    request.setAttribute("listCategories", new ArrayList<>(Arrays.asList(CategoryType.values())));
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Item/saleItems.jsp");
                    dispatcher.forward(request, response);

                } catch (Exception e) {
                    String errorString = e.getMessage();
                    RequestDispatcher rd = request.getRequestDispatcher("allStores?errorString=" + errorString);
                    rd.forward(request, response);
                }
            } else {
                //search in specific store
                try {
                    Set<SaleItem> listSaleItems = null;
                    if (currItemName == null || currItemName.equals("")) {
                        //Searching sale Items by Category Name
                        listSaleItems = sc.getStoresItemsInCategory(currStoreName, categoryName);
                        if (listSaleItems.size() == 0) {
                            throw new NotPremittedException("No Items for category  " + categoryName);
                        }
                    } else {
                        Item found_item = sc.getItemFromStore(currItemName, currStoreName);
                        if (found_item == null) {
                            throw new NotPremittedException("No Item with the name " + currItemName);
                        }
                        //Searching sale Items by Item Name
                        listSaleItems = sc.getSaleItemsByItemInStore(currItemName, currStoreName);
                    }
                    request.setAttribute("storeName", currStoreName);
                    request.setAttribute("listSaleItems", listSaleItems);
                    request.setAttribute("listCategories", new ArrayList<>(Arrays.asList(CategoryType.values())));
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Item/saleItems.jsp");
                    dispatcher.forward(request, response);


                } catch (Exception e) {
                    String errorString = e.getMessage();
                    RequestDispatcher rd = request.getRequestDispatcher("saleItems?storeName=" + currStoreName + "&errorString=" + errorString);
                    rd.forward(request, response);
                }
            }
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

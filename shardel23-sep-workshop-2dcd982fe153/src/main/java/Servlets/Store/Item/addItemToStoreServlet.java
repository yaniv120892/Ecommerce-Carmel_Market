package Servlets.Store.Item;

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

@WebServlet(urlPatterns = "/addItemToStore")
public class addItemToStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public addItemToStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = null;
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("errorString", request.getParameter("errorString"));
                request.setAttribute("successString", request.getParameter("successString"));

                String userID = request.getSession().getAttribute("userID").toString();
                String itemName = request.getParameter("itemName");
                storeName = request.getParameter("storeName");
                String categoryName = request.getParameter("categoryName");
                int amount = Integer.parseInt(request.getParameter("amount"));
                int price = Integer.parseInt(request.getParameter("price"));
                sc.addItemToStore(userID,itemName,storeName,amount,price,categoryName);
                String successString = "Item " + itemName + " was added successfully to store";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&successString="+successString);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&errorString="+errorString);
            dispatcher.forward(request, response);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = null;
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
                return;
            }
            storeName = request.getParameter("storeName");
            String userID = request.getSession().getAttribute("userID").toString();

            request.setAttribute("storeName", storeName);
            request.setAttribute("listCategories", new ArrayList<>(Arrays.asList(CategoryType.values())));
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Item/addItemToStore.jsp");
            dispatcher.forward(request, response);
        }
        catch(Exception e)
        {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&errorString="+errorString);
            dispatcher.forward(request, response);
        }
    }
}






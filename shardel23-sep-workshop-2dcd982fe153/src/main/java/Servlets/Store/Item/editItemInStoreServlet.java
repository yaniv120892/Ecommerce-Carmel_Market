package Servlets.Store.Item;

import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/editItemInStore")
public class editItemInStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public editItemInStoreServlet() {
        sc = ServiceProvider.getSP();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("userID") == null) {
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
            dispatcher.forward(request, response);
        } else {
            String itemNameToEdit = request.getParameter("itemName");
            String storeName = request.getParameter("storeName");
            try {
                String userID = request.getSession().getAttribute("userID").toString();
                double newPrice = Double.parseDouble(request.getParameter("price"));
                int newStock = Integer.parseInt(request.getParameter("stock"));
                sc.editStoreItemPrice(userID,itemNameToEdit,storeName,newPrice);
                sc.editStoreItemStock(userID,itemNameToEdit,storeName,newStock);
                String successString =  itemNameToEdit + " was updated successfully";
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&successString="+successString);
                dispatcher.forward(request, response);
            }
            catch (Exception e) {
                String errorString =  e.getMessage();
                RequestDispatcher dispatcher =
                        getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&errorString="+errorString);
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = request.getParameter("storeName");
        String itemNameToEdit = request.getParameter("itemNameToEdit");
        try {
            String successString = request.getParameter("successString");
            RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&itemNameToEdit="+itemNameToEdit);
            dispatcher.forward(request, response);

        }

        catch(Exception e){
            String errorString =  e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&errorString="+errorString);
            dispatcher.forward(request, response);
        }

    }

}

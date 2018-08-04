package Servlets.Store.Item;

import Exceptions.NotPremittedException;
import ServiceLayer.ServiceProvider;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/addItemToSale")
public class addItemToSaleServlet extends HttpServlet {
    ServiceProvider sc;

    public addItemToSaleServlet() {
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
                String startDate =  AddZeros(request.getParameter("startDay"))+"-"+ AddZeros(request.getParameter("startMonth"))+"-"+request.getParameter("startYear") ;
                String endDate =  AddZeros(request.getParameter("endDay"))+"-"+AddZeros(request.getParameter("endMonth"))+"-"+request.getParameter("endYear") ;
                String saleType = request.getParameter("saleType");
                sc.addItemToSale(userID,storeName,itemName,saleType,startDate,endDate);
                String successString = "Raffle sale for item " + itemName + " was added successfully";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&successString="+successString);
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&errorString="+errorString);
            dispatcher.forward(request, response);
        }
    }

    private String AddZeros(String number)
    {
        if(number.length() < 2)
            return  "0"+number;
        return number;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String storeName = null;
        try {
            if (request.getSession().getAttribute("userID") == null) {
                RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/loginView.jsp");
                dispatcher.forward(request, response);
                return;
            }
            String saleType = request.getParameter("saleType");
            String itemName = request.getParameter("itemName");
            storeName = request.getParameter("storeName");
            String userID = request.getSession().getAttribute("userID").toString();

            if (saleType.equals("NORMAL")) {
                sc.addItemToSale(userID,storeName,itemName,saleType);
                String successString = "Normal sale for item " + itemName + " was added successfully";
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&successString="+successString);
                dispatcher.forward(request, response);
                return;
            }
            if (saleType.equals("RAFFLE"))
            {
                //TODO create view for adding raffle
                request.setAttribute("storeName", storeName);
                request.setAttribute("itemName", itemName);
                request.setAttribute("saleType", saleType);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Item/addRaffleSaleItem.jsp");
                dispatcher.forward(request, response);
                return;
            }
            throw new NotPremittedException("Unknown sale type");
        }
        catch(Exception e)
        {
            String errorString = e.getMessage();
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/allItems?storeName="+storeName+"&errorString="+errorString);
            dispatcher.forward(request, response);
        }
    }
}






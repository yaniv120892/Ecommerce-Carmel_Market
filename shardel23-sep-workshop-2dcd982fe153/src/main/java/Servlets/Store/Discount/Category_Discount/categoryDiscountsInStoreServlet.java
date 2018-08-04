package Servlets.Store.Discount.Category_Discount;

import Backend.Entities.Discount.Discount;
import Backend.Entities.Discount.Discounts;
import Backend.Entities.Enums.CategoryType;
import Backend.Entities.Enums.DiscountType;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/categoryDiscountsInStore")
public class categoryDiscountsInStoreServlet extends HttpServlet {
    ServiceProvider sc;

    public categoryDiscountsInStoreServlet() {
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
                request.setAttribute("startDateEdit", request.getParameter("startDateEdit"));
                request.setAttribute("endDateEdit", request.getParameter("endDateEdit"));
                request.setAttribute("startDayEdit", request.getParameter("startDayEdit"));
                request.setAttribute("endDayEdit", request.getParameter("endDayEdit"));
                request.setAttribute("startMonthEdit", request.getParameter("startMonthEdit"));
                request.setAttribute("endMonthEdit", request.getParameter("endMonthEdit"));
                request.setAttribute("startYearEdit", request.getParameter("startYearEdit"));
                request.setAttribute("endYearEdit", request.getParameter("endYearEdit"));
                request.setAttribute("percentEdit", request.getParameter("percentEdit"));
                request.setAttribute("discountTypeEdit", request.getParameter("discountTypeEdit"));
                request.setAttribute("categoryDiscountNameToEdit", request.getParameter("categoryDiscountNameToEdit"));
                request.setAttribute("listDiscountType", new ArrayList<>(Arrays.asList(DiscountType.values())));

                String selectedStoreName = request.getParameter("storeName");
                Map<CategoryType, Discounts> listCategoryDiscounts  = sc.getCategoryDiscountsByStoreName(selectedStoreName);

                request.setAttribute("storeName", selectedStoreName);
                request.setAttribute("listCategoryDiscounts", listCategoryDiscounts);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/views/Discounts/Category_Discount/categoryDiscounts.jsp");
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

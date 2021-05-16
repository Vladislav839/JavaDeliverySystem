package by.bsu.deliveryshop.servlets.servlet;

import by.bsu.deliveryshop.data.Models.Customer;
import by.bsu.deliveryshop.data.Models.Role;
import by.bsu.deliveryshop.services.CustomerService;
import by.bsu.deliveryshop.services.SellerService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class RegisterServlet extends HttpServlet {
    private CustomerService customerService;
    private SellerService sellerService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        HttpSession session = request.getSession();

        customerService = (CustomerService) request.getServletContext().getAttribute("customerService");
        sellerService = (SellerService) request.getServletContext().getAttribute("sellerService");

        try {
            if(customerService.CustomerExists(login)
            || sellerService.CustomerExists(login)) {
                request.setAttribute("UserError", "Пользователь с таким именем уже сеществует");
                request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
            } else if(login == null || password == null || login == "" || password == "") {
                request.setAttribute("InputError", "Некорректный ввод");
                request.getRequestDispatcher("/WEB-INF/view/register.jsp").forward(request, response);
            } else {
                customerService.addCustomer(new Customer(login, password, address));
                Role role = Role.CUSTOMER;
                session.setAttribute("role", role);
                session.setAttribute("login", login);
                session.setAttribute("password", password);
                session.setAttribute("address", address);
                request.getRequestDispatcher("/WEB-INF/view/user_menu.jsp").forward(request, response);
            }
        } catch (Exception e) { }
    }
}

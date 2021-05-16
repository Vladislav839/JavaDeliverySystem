package by.bsu.deliveryshop.servlets.filter;

import by.bsu.deliveryshop.data.Models.Role;
import by.bsu.deliveryshop.services.CustomerService;
import by.bsu.deliveryshop.services.SellerService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {
    private CustomerService customerService;
    private SellerService sellerService;
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        HttpSession session = req.getSession();

        customerService = (CustomerService) req.getServletContext().getAttribute("customerService");
        sellerService = (SellerService) req.getServletContext().getAttribute("sellerService");

        try {
            if (session != null &&
                    session.getAttribute("login") != null &&
                    session.getAttribute("password") != null) {

                Role role = (Role) session.getAttribute("role");

                moveToMenu(req, res, role);


            } else if (customerService.CustomerExists(login)) {

                Role role = Role.CUSTOMER;

                req.getSession().setAttribute("password", password);
                req.getSession().setAttribute("login", login);
                req.getSession().setAttribute("role", role);

                moveToMenu(req, res, role);

            } else if(sellerService.CustomerExists(login)) {

                Role role = Role.SELLER;

                req.getSession().setAttribute("password", password);
                req.getSession().setAttribute("login", login);
                req.getSession().setAttribute("role", role);

                moveToMenu(req, res, role);
            } else {
                moveToMenu(req, res, Role.UNKNOWN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToMenu(final HttpServletRequest req,
                            final HttpServletResponse res,
                            final Role role)
            throws ServletException, IOException {

        if (role.equals(Role.SELLER)) {
            req.getRequestDispatcher("/WEB-INF/view/admin_menu.jsp").forward(req, res);
        } else if (role.equals(Role.CUSTOMER)) {
            req.getRequestDispatcher("/WEB-INF/view/user_menu.jsp").forward(req, res);
        } else {
            req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, res);
        }
    }
}

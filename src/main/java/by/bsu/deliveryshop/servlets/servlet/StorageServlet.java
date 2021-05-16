package by.bsu.deliveryshop.servlets.servlet;

import by.bsu.deliveryshop.data.Models.Product;
import by.bsu.deliveryshop.services.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class StorageServlet extends HttpServlet {
    private ProductService productService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        productService = (ProductService) req.getServletContext().getAttribute("productService");
        req.setCharacterEncoding("UTF8");
        try {
            List<Product> products = productService.getAllProducts();
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/view/storage.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("productIdToDelete"));
        productService = (ProductService) req.getServletContext().getAttribute("productService");
        try {
            productService.deleteProduct(id);
            List<Product> products = productService.getAllProducts();
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/view/storage.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

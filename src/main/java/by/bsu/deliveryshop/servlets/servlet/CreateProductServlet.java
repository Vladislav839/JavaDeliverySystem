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

public class CreateProductServlet extends HttpServlet {
    ProductService productService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/create_product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("productToCreateName");
        double price = Double.parseDouble(req.getParameter("productToCreatePrice"));
        int count = Integer.parseInt(req.getParameter("productToCreateCount"));
        productService = (ProductService) req.getServletContext().getAttribute("productService");
        Product product = new Product(name, price, count);
        try {
            productService.addProduct(product);
            List<Product> products = productService.getAllProducts();
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/view/storage.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

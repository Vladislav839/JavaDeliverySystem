package by.bsu.deliveryshop.servlets.servlet;

import by.bsu.deliveryshop.data.Models.Product;
import by.bsu.deliveryshop.services.ProductService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ChangeProductServlet", value = "/changeProduct")
public class ChangeProductServlet extends HttpServlet {
    private ProductService productService;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productService = (ProductService) request.getServletContext().getAttribute("productService");
        try {
            Optional<Product> product = Optional.ofNullable(productService.getProductById(id));
            if(product.isPresent()) {
                request.setAttribute("updateProduct", product.get());
                request.getRequestDispatcher("/WEB-INF/view/update_product.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("productToChangeId"));
        String name = request.getParameter("productToChangeName");
        double price = Double.parseDouble(request.getParameter("productToChangePrice"));
        int count = Integer.parseInt(request.getParameter("productToChangeCount"));

        try {
            Product updateProduct = new Product(id, name, price, count);
            productService.updateProduct(updateProduct);
            List<Product> products = productService.getAllProducts();
            request.setAttribute("products", products);
            request.getRequestDispatcher("/WEB-INF/view/storage.jsp").forward(request, response);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

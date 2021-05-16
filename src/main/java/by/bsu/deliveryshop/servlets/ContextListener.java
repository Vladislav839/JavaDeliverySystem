package by.bsu.deliveryshop.servlets;

import by.bsu.deliveryshop.services.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebListener
public class ContextListener implements ServletContextListener {
    /**
     * Fake database connector.
     */
    private DeliveryService deliveryService;
    private ProductService productService;
    private DeliveryProductService deliveryProductService;
    private SellerService sellerService;
    private CustomerService customerService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        InputStream resource = servletContext.getResourceAsStream("/WEB-INF/daosettings.json");
        String text = new BufferedReader(
                new InputStreamReader(resource, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));

        InputStream resource2 = servletContext.getResourceAsStream("/WEB-INF/pathconfig.json");
        String connectionStrings = new BufferedReader(
                new InputStreamReader(resource2, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));

        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Type type2 = new TypeToken<Map<String, Map<String, String>>>(){}.getType();
        Map<String, String> read = gson.fromJson(text, type);
        Map<String, Map<String, String>> pathsConfig = gson.fromJson(connectionStrings, type2);

        try {
            Class<?> daoClass = Class.forName(read.get("DaoClass"));
            Map<String, String> path = pathsConfig.get(read.get("DaoClass"));
            if(path == null) {
                deliveryService = new DeliveryService(daoClass, null);
                productService = new ProductService(daoClass, null);
                deliveryProductService = new DeliveryProductService(daoClass, null);
                sellerService = new SellerService(daoClass,null);
                customerService = new CustomerService(daoClass, null);
            } else {
                deliveryService = new DeliveryService(daoClass, path.get("DeliveriesPath"));
                productService = new ProductService(daoClass, path.get("ProductsPath"));
                deliveryProductService = new DeliveryProductService(daoClass, path.get("DeliveryProductsPath"));
                sellerService = new SellerService(daoClass, path.get("SellersPath"));
                customerService = new CustomerService(daoClass, path.get("CustomersPath"));
            }

            servletContext.setAttribute("deliveryService", deliveryService);
            servletContext.setAttribute("productService", productService);
            servletContext.setAttribute("deliveryProductService", deliveryProductService);
            servletContext.setAttribute("sellerService", sellerService);
            servletContext.setAttribute("customerService", customerService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        customerService = null;
        productService = null;
        deliveryProductService = null;
        sellerService = null;
        deliveryService = null;
    }
}

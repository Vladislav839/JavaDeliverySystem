package by.bsu.deliveryshop.utils;

import by.bsu.deliveryshop.data.Interfaces.Dao;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggingHandler<T extends Dao> implements InvocationHandler {
    private static Logger LOGGER = Logger.getLogger(
            LoggingHandler.class.getName());

    private FileHandler fileHandler = null;

    private final Map<String, Method> methods = new HashMap<>();

    private T target;

    public LoggingHandler(T target) {
        this.target = target;

        for(Method method: target.getClass().getDeclaredMethods()) {
            this.methods.put(method.getName(), method);
        }

        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.
                    getResource(String.valueOf(classLoader.getResource("\\Logs\\"+"MyLogFile_"
                    + format.format(Calendar.getInstance().getTime()) + ".log"))).getFile());
            fileHandler = new FileHandler(file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fileHandler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable, IOException, SecurityException {
        long start = System.nanoTime();
        Object result = methods.get(method.getName()).invoke(target, args);
        long elapsed = System.nanoTime() - start;

        LOGGER.info(String.format("Executing of method %s\n started  %s \nfinished in %d ns", method.getName(),
                LocalDateTime.now().toString(),elapsed));

        return result;
    }
}

package me.mkgl.vaadin;

import static com.vaadin.server.Sizeable.Unit.PERCENTAGE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import lombok.SneakyThrows;

@Theme("valo")
public class SandboxUI extends UI {

    @WebServlet(value = "/", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SandboxUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    @SneakyThrows
    protected void init(VaadinRequest request) {
        setSizeFull();
        VerticalLayout root = new VerticalLayout();
        root.setMargin(true);
        root.setWidth(100, PERCENTAGE);
        setContent(root);

        getVaadinServlets().entrySet().stream()
                .filter(uiClass -> uiClass != this)
                .sorted((e1, e2) -> e1.getKey().getSimpleName().compareToIgnoreCase(e2.getKey().getSimpleName()))
                .forEach(entry -> {
                    root.addComponent(new Link(entry.getKey().getSimpleName(), new ExternalResource(entry.getValue())));
                });
    }

    @SneakyThrows
    private Map<Class<? extends UI>, String> getVaadinServlets() throws ClassNotFoundException {
        Map<Class<? extends UI>, String> vaadinServlets = new HashMap<>();
        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();

        for (ServletRegistration registration : servletContext.getServletRegistrations().values()) {
            Iterator<String> mappings = registration.getMappings().iterator();
            if (registration.getName().startsWith("me.mkgl") && mappings.hasNext()) {
                Class<?> declaringClass = Class.forName(registration.getClassName()).getDeclaringClass();
                if (declaringClass != null && UI.class.isAssignableFrom(declaringClass)) {
                    Class<? extends UI> uiClass = (Class<? extends UI>) declaringClass;
                    String mapping = mappings.next();
                    int wildcardIndex = mapping.indexOf("/*");
                    if (wildcardIndex > 0) {
                        mapping = mapping.substring(0, wildcardIndex);
                    }
                    String url = servletContext.getContextPath() + mapping;
                    vaadinServlets.put(uiClass, url);
                }
            }
        }

//        servletContext.getServletRegistrations().values().stream()
//                .filter(registration -> registration.getName().startsWith("me.mkgl"))
//                .map(registration -> Class.forName(registration.getClassName()).getDeclaringClass())
//                .filter(UI.class::isAssignableFrom)
//                .filter(registration -> registration.getMappings().size())

        return vaadinServlets;
    }
}

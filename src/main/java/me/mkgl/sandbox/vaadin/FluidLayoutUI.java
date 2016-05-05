package me.mkgl.sandbox.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class FluidLayoutUI extends UI {

    @WebServlet(value = "/fluid-layout/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = FluidLayoutUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        Button b1 = new Button("B1");
        Button b2 = new Button("B2");
        b2.setHeight(100, Unit.PERCENTAGE);

        layout.addComponents(b1, b2);
        layout.setExpandRatio(b2, 1f);

        System.out.println(layout.getExpandRatio(b1));
        System.out.println(layout.getExpandRatio(b2));

        setContent(layout);
    }
}

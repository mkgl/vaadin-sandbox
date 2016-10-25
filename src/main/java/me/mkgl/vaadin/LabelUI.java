package me.mkgl.vaadin;

import javax.servlet.annotation.WebServlet;

import com.google.common.collect.Lists;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class LabelUI extends UI {

    @WebServlet(value = "/label/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = LabelUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setCaption("VerticalLayout");
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        verticalLayout.setWidth(100, Unit.PERCENTAGE);

        FormLayout formLayout = new FormLayout();
        formLayout.setCaption("FormLayout");
        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        formLayout.setWidth(100, Unit.PERCENTAGE);

        feedLabelsIntoLayout(verticalLayout);
        feedLabelsIntoLayout(formLayout);

        VerticalLayout root = new VerticalLayout(verticalLayout, formLayout);
        root.setSizeFull();
        setContent(root);
    }

    private void feedLabelsIntoLayout(Layout layout) {
        Label testA = new Label("Test-A");
        testA.setCaption("Test-A-via-caption");
        Label testB = new Label("Test-B");
        testB.setValue("Test-B-via-value");
        layout.addComponents(testA, testB);
    }
}

package me.mkgl.vaadin;

import static com.vaadin.server.Sizeable.Unit.PIXELS;
import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.annotation.WebServlet;

import com.google.common.collect.Lists;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class SelectFieldUI extends UI {

    @WebServlet(value = "/select/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SelectFieldUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        ComboBox c1 = new ComboBox("c1", Lists.newArrayList("Ananas", "Banana", "Cactus"));
        c1.setCaption(c1.getCaption() + String.format(" (%s)", c1.getItemCaptionMode()));
        c1.addValueChangeListener(this::valueChange);

        ComboBox c2 = new ComboBox("c2", Lists.newArrayList(0, 8, 63));
        c2.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
        c2.setItemCaption(0, "No access");
        c2.setItemCaption(8, "Read-only");
        c2.setItemCaption(63, "Read-Write");
        c2.setCaption(c2.getCaption() + String.format(" (%s)", c2.getItemCaptionMode()));
        c2.addValueChangeListener(this::valueChange);

        ComboBox c3 = new ComboBox("c3", Lists.newArrayList(
                new Permission(0, "No access"),
                new Permission(8, "Read-only"),
                new Permission(63, "Read-Write")
        ));
        c3.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        c3.setItemCaptionPropertyId("caption");
        c3.setCaption(c3.getCaption() + String.format(" (%s)", c3.getItemCaptionMode()));
        c3.addValueChangeListener(this::valueChange);

        ComboBox c4 = new ComboBox("c4", new BeanItemContainer<>(Permission.class, Lists.newArrayList(
                new Permission(0, "No access"),
                new Permission(8, "Read-only"),
                new Permission(63, "Read-Write")
        )));
        c4.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        c4.setItemCaptionPropertyId("caption");
        c4.setCaption(c4.getCaption() + String.format(" (%s)", c4.getItemCaptionMode()));
        c4.addValueChangeListener(this::valueChange);

        layout.addComponents(c1, c2, c3, c4);


        setContent(layout);
    }

    void valueChange(Property.ValueChangeEvent event) {
        Notification.show(String.valueOf(event.getProperty().getValue()));
    }

    public static class Permission {
        final int value;
        final String caption;

        public Permission(int value, String caption) {
            this.value = value;
            this.caption = caption;
        }

        public String getCaption() {
            return caption;
        }

        public int getValue() {
            return value;
        }
    }
}

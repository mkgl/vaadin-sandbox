package me.mkgl.sandbox.vaadin;

import java.util.Arrays;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@Theme("valo")
public class NullSelectionUI extends UI {

    @WebServlet(value = "/nullselection/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = NullSelectionUI.class)
    public static class Servlet extends VaadinServlet {
    }

    private ComboBox comboBox;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth(100, Unit.PERCENTAGE);

        List<String> options = Arrays.asList("v1", "v2", "v3", "v4");
        comboBox = new ComboBox("ComboBox", options);

        BeanItem<ComboBox> comboBoxItem = new BeanItem<>(comboBox);

        CheckBox cbNullAllowed = new CheckBox("Null selection allowed", comboBoxItem.getItemProperty("nullSelectionAllowed"));
        cbNullAllowed.setImmediate(true);
        cbNullAllowed.addValueChangeListener(this::updateIcon);
        CheckBox cbRequired = new CheckBox("Required", comboBoxItem.getItemProperty("required"));
        cbRequired.setImmediate(true);
        cbRequired.addValueChangeListener(this::updateIcon);
        TextField nullItemId = new TextField("Null itemId", comboBoxItem.getItemProperty("nullSelectionItemId"));
        nullItemId.setImmediate(true);
        nullItemId.addValueChangeListener(this::updateIcon);
        TextField currentValue = new TextField("Current value", comboBoxItem.getItemProperty("value"));
        currentValue.setReadOnly(true);

        comboBox.setPropertyDataSource(new ObjectProperty<>(null, String.class));
        comboBox.setImmediate(true);
        comboBox.addValueChangeListener(this::updateIcon);
        updateIcon(null);

        layout.addComponents(cbNullAllowed, nullItemId, cbRequired, comboBox, currentValue);
        setContent(layout);
    }

    private void updateIcon(Property.ValueChangeEvent event) {
        comboBox.setIcon(comboBox.isValid() ? FontAwesome.CHECK_CIRCLE : FontAwesome.EXCLAMATION_TRIANGLE);
    }
}

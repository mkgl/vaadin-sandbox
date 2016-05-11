package me.mkgl.vaadin;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
public class SimpleFieldValidationUI extends UI {

    @WebServlet(value = "/simplevalidation/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = SimpleFieldValidationUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

        AbstractOrderedLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth(100, Unit.PERCENTAGE);

        final TextField subjectField = new TextField((String) null);
        subjectField.setNullSettingAllowed(false);
        subjectField.setRequired(true);
        subjectField.setRequiredError("Required MOFO");
        subjectField.setValidationVisible(false);

        final TextArea bodyField = new TextArea("Message");
        bodyField.addValidator(new StringLengthValidator("WRONG LENGTH BASTARD", 10, 32, false));
        bodyField.setValidationVisible(false);

        // send button
        NativeButton sendButton = new NativeButton("Validate", (Button.ClickListener) event -> {
            subjectField.setValidationVisible(!subjectField.isValid());
            bodyField.setValidationVisible(!bodyField.isValid());
        });

        layout.addComponent(subjectField);
        layout.addComponent(bodyField);
        layout.addComponent(sendButton);

        setContent(layout);
    }
}

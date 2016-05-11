package me.mkgl.vaadin;

import javax.servlet.annotation.WebServlet;

import com.google.common.collect.Lists;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import lombok.Data;
import me.mkgl.vaadin.validation.ValidatingFieldGroup;

@Theme("valo")
public class ValidationSandboxUI extends UI {

    @WebServlet(value = "/validation/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = ValidationSandboxUI.class)
    public static class Servlet extends VaadinServlet {
    }

    private FieldGroup form;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth(100, Unit.PERCENTAGE);

        // create form and data item
        form = new ValidatingFieldGroup();
//        final PropertysetItem item = new PropertysetItem();
//        item.addItemProperty("title", new ObjectProperty<>(""));
//        item.addItemProperty("content", new ObjectProperty<>(""));
        final Message message = new Message();
        message.setSubject("");
        message.setBody("");
        BeanItem<Message> item = new BeanItem<>(message);
        form.setItemDataSource(item);

        TextField subjectField = new TextField("Subject");
        subjectField.setNullSettingAllowed(false);
        subjectField.setRequired(true);
        subjectField.setRequiredError("Required MOFO");
        TextArea bodyField = new TextArea("Message");
        bodyField.addValidator(new StringLengthValidator("WRONG LENGTH BASTARD", 10, 32, false));

        OptionGroup typeField = new OptionGroup("Message type", Lists.newArrayList(MessageType.values()));

        form.bind(subjectField, "subject");
        form.bind(bodyField, "body");
        form.bind(typeField, "type");

        layout.addComponent(subjectField);
        layout.addComponent(bodyField);
        layout.addComponent(typeField);

//        form.clear();
        clearValidation();

        // send button
        NativeButton sendButton = new NativeButton("Send Message", (Button.ClickListener) event -> {
            clearValidation();

            try {
                form.commit();
                String body = message.getBody();
                if (MessageType.SHOUT.equals(message.getType())) {
                    body = body.toUpperCase();
                } else if (MessageType.WHISPER.equals(message.getType())) {
                    body = body.toLowerCase();
                }

                Notification.show(message.getSubject(), body, Notification.Type.HUMANIZED_MESSAGE);
            } catch (FieldGroup.CommitException e) {
                if (e.getCause() instanceof Validator.InvalidValueException) {
                     showValidation();
                }
            }
        });
        sendButton.addStyleName("btn-dialog");
        sendButton.addStyleName("commit");

        // reset button
        NativeButton resetButton = new NativeButton("Reset", (Button.ClickListener) event -> {
            form.discard();
            form.clear();
            clearValidation();
            Notification.show("Form reset BITCH", Notification.Type.WARNING_MESSAGE);
        });
        resetButton.addStyleName("btn-dialog");
        resetButton.addStyleName("cancel");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addStyleName("buttons");
        buttons.setSpacing(true);
        buttons.addComponent(sendButton);
        buttons.addComponent(resetButton);
        layout.addComponent(buttons);

        setContent(layout);
    }

    private void showValidation() {
        form.getFields().stream().filter(field -> !field.isValid() && field instanceof AbstractField).forEach(field -> {
            ((AbstractField) field).setValidationVisible(true);
            Notification.show("Error", ((AbstractField) field).getErrorMessage().toString(), Notification.Type.ERROR_MESSAGE);
        });
    }

    private void clearValidation() {
        form.getFields().stream().filter(field -> field instanceof AbstractField).forEach(field -> {
            ((AbstractField) field).setValidationVisible(false);
        });
    }

    @Data
    public static class Message {
        private String subject;
        private String body;
        private MessageType type;

        public void reset() {
            setSubject(null);
            setBody(null);
            setType(null);
        }
    }

    private enum MessageType {
        WHISPER, SHOUT
    }
}

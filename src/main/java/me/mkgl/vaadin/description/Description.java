package me.mkgl.vaadin.description;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.shared.JavaScriptExtensionState;
import com.vaadin.ui.Field;

/**
 * The Description extension appends a small description caption below an input field.
 *
 * <p>This bridges the gap between field captions and descriptions (in Vaadin terms—which are merely tooltip texts).
 */
@JavaScript("description_connector.js")
public class Description extends AbstractJavaScriptExtension {

    public static Description extend(Field field) {
        Description description = new Description();
        description.extend((AbstractClientConnector) field);
        field.addAttachListener((AttachListener) attachEvent -> {
            field.addDetachListener((DetachListener) detachEvent -> {
                description.detach();
                System.out.println("detach " + field.getCaption());
            });
            System.out.println("attach " + field.getCaption());
        });
        return description;
    }

    public String getText() {
        return getState().text;
    }

    public void setText(String text) {
        getState().text = text;
    }

    @Override
    protected DescriptionState getState() {
        return (DescriptionState) super.getState();
    }

    public static class DescriptionState extends JavaScriptExtensionState {
        public String text;
    }
}

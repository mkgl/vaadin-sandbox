package me.mkgl.vaadin.validation;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Notification;

public class ValidatingFieldGroup extends FieldGroup {

    public ValidatingFieldGroup() {
    }

    public ValidatingFieldGroup(Item itemDataSource) {
        super(itemDataSource);
    }

    @Override
    public void setItemDataSource(Item itemDataSource) {
        super.setItemDataSource(itemDataSource);
        clearValidation();
    }

    public void validate() {
        getFields().stream().forEach(field ->
                field.validate()
        );
    }

    public void showValidation() {
        getFields().stream().filter(field -> !field.isValid() && field instanceof AbstractField).forEach(field -> {
            ((AbstractField) field).setValidationVisible(true);
            Notification.show("Error", ((AbstractField) field).getErrorMessage().toString(), Notification.Type.ERROR_MESSAGE);
        });
    }

    public void clearValidation() {
        getFields().stream().filter(field -> field instanceof AbstractField).forEach(field -> {
            ((AbstractField) field).setValidationVisible(false);
        });
    }
}

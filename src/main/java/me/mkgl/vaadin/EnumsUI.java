package me.mkgl.vaadin;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.UI;

import lombok.Data;

@Theme("valo")
public class EnumsUI extends UI {

    @WebServlet(value = "/enums/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = EnumsUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

//        List<GridUI.Formula1Team> enumValues = Arrays.asList(GridUI.Formula1Team.values());
//        Container enumValues = new BeanItemContainer<>(GridUI.Formula1Team.class, Arrays.asList(GridUI.Formula1Team.values()));
        List<GridUI.Formula1Team> enumNames = Arrays.stream(GridUI.Formula1Team.values())
//                .map(formula1Team -> formula1Team.name())
                .collect(toList());
        Container enumValues = new IndexedContainer(enumNames);
        enumValues.addContainerProperty("fullName", String.class, null);
        enumValues.addContainerProperty("value", GridUI.Formula1Team.class, null);
        enumNames.stream()
//                .forEach(name -> {
//                    GridUI.Formula1Team team = Enum.valueOf(GridUI.Formula1Team.class, name);
//                    enumValues.getItem(name).getItemProperty("fullName").setValue(team.getFullName());
//                    enumValues.getItem(name).getItemProperty("value").setValue(team);
                .forEach(team -> {
                    enumValues.getItem(team).getItemProperty("fullName").setValue(team.getFullName());
                    enumValues.getItem(team).getItemProperty("value").setValue(team);
                });

        IndexedContainer floaties = new IndexedContainer(Arrays.asList(1.5f, 2.5f, 3.5f));
        ComboBox floatySelect = new ComboBox("Floaty (Select)", floaties);
        floatySelect.setItemCaptionMode(ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        floatySelect.setNullSelectionAllowed(false);
        floatySelect.setItemCaption(1.5f, "One-Five");
        floatySelect.setItemCaption(2.5f, "Two-Five");
        floatySelect.setItemCaption(3.5f, "Three-Five");

        Object firstFloaty = floatySelect.getItemIds().iterator().next();

        ComboBox select = new ComboBox("Preferred Team (Select)", enumValues);
        select.setItemCaptionPropertyId("fullName");
        select.setNullSelectionAllowed(false);
        Object firstItemId = select.getItemIds().iterator().next();

        OptionGroup radio = new OptionGroup("Preferred Team (Radio)", enumValues);
        radio.setItemCaptionPropertyId("fullName");

        OptionGroup checkBoxes = new OptionGroup("Preferred Teams (CheckBoxes)", enumValues);
        checkBoxes.setItemCaptionPropertyId("fullName");
        checkBoxes.setMultiSelect(true);

        TwinColSelect twinCol = new TwinColSelect("Preferred Teams (TwinCol)", enumValues);
        twinCol.setItemCaptionPropertyId("fullName");

        BeanFieldGroup<BeanWithEnums> form = new BeanFieldGroup<>(BeanWithEnums.class);
        form.setBuffered(false);
        form.bind(floatySelect, "floaty");
        form.bind(select, "preferredTeamSelect");
        form.bind(radio, "preferredTeamRadio");
        form.bind(checkBoxes, "preferredTeamsCheckBoxes");
        form.bind(twinCol, "preferredTeamsTwinCol");
        BeanWithEnums bean = new BeanWithEnums();
        bean.setPreferredTeamSelect(GridUI.Formula1Team.FORCE_INDIA);
        form.setItemDataSource(bean);


        floatySelect.select(firstFloaty);

//        select.getPropertyDataSource().setValue(firstItemId);
//        Object firstValue = select.getItem(firstItemId).getItemProperty("value").getValue();
//        select.getPropertyDataSource().setValue(firstValue);


        FormLayout layout = new FormLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();
        layout.addComponents(floatySelect, select, radio, checkBoxes, twinCol);

        Button commit = new Button("Commit", (Button.ClickListener) event -> {
            try {
                form.commit();
            } catch (FieldGroup.CommitException e) {
                e.printStackTrace();
            }
        });
        layout.addComponent(commit);

        setContent(layout);
    }

    @Data
    public static class BeanWithEnums {
        Float floaty;
        GridUI.Formula1Team preferredTeamSelect;
        GridUI.Formula1Team preferredTeamRadio;
        Set<GridUI.Formula1Team> preferredTeamsCheckBoxes;
        Set<GridUI.Formula1Team> preferredTeamsTwinCol;
    }
}

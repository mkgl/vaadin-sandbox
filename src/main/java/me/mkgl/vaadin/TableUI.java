package me.mkgl.vaadin;

import static com.vaadin.server.Sizeable.Unit.PERCENTAGE;

import java.util.Arrays;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import me.mkgl.vaadin.GridUI.Base;
import me.mkgl.vaadin.GridUI.Formula1Team;

@Theme("grid")
public class TableUI extends UI {

    private TextArea output;

    @WebServlet(value = "/table/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = TableUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        VerticalLayout root = new VerticalLayout();
        root.setMargin(true);
        root.setSpacing(true);
        root.setSizeFull();
        setContent(root);

        Table table = new Table("Table");
        table.setSizeFull();
        table.setSelectable(true);
        table.setMultiSelect(false);
        Container.Indexed dataSource = buildContainer();
        table.setContainerDataSource(dataSource);
        table.setVisibleColumns("name+nation", "fullName", "base", "chassis", "powerUnit");
        table.setColumnHeader("name+nation", "Name");
        table.addGeneratedColumn("base", (Table.ColumnGenerator) (source, itemId, columnId) -> {
            Property property = source.getContainerProperty(itemId, columnId);
            Base value = (Base) property.getValue();
            return String.format("%s, %s", value.city, value.country.name);
        });

        // assume datasource's third item is the null item
        table.setNullSelectionAllowed(true);
        table.setNullSelectionItemId(Formula1Team.RED_BULL);
        table.setValue(Formula1Team.FERRARI);

        output = new TextArea("Output");
        output.setWidth(100, PERCENTAGE);
        root.addComponents(table, output);
        root.setExpandRatio(table, 1);

        table.addValueChangeListener(event -> appendOutput("ValueChange => " + event.getProperty().getValue()));
        table.addItemClickListener(event -> appendOutput("ItemClick => " + event.getItemId()));
        table.setImmediate(true);
    }

    private void appendOutput(String appendedText) {
        output.setValue(output.getValue() + "\n" + appendedText);
    }

    private Container.Indexed buildContainer() {
        Container.Indexed container = new BeanItemContainer<>(Formula1Team.class, Arrays.asList(Formula1Team.values()));
        GeneratedPropertyContainer generatedPropertyContainer = new GeneratedPropertyContainer(container);
        generatedPropertyContainer.addGeneratedProperty("name+nation", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                Formula1Team team = ((BeanItem<Formula1Team>) item).getBean();
                return team.nation.flag + " " + team.name;
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });

        return generatedPropertyContainer;
    }
}

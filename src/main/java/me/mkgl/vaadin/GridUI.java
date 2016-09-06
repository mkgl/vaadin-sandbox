package me.mkgl.vaadin;

import static com.vaadin.server.Sizeable.Unit.PERCENTAGE;
import static me.mkgl.vaadin.GridUI.Country.*;

import java.util.Arrays;
import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import lombok.Getter;

@Theme("grid")
public class GridUI extends UI {

    @WebServlet(value = "/grid/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = GridUI.class)
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

        Grid grid = new Grid("Grid");
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setContainerDataSource(buildContainer());
        grid.setColumns("name+nation", "fullName", "base", "chassis", "powerUnit");
        grid.getColumn("name+nation").setHeaderCaption("Name");
        grid.getColumn("base").setConverter(new Converter<String, Base>() {
            @Override
            public Base convertToModel(String value, Class<? extends Base> targetType, Locale locale) throws ConversionException {
                throw new UnsupportedOperationException("Parsing team bases from strings is not supported.");
            }

            @Override
            public String convertToPresentation(Base value, Class<? extends String> targetType, Locale locale) throws ConversionException {
                return String.format("%s, %s", value.city, value.country.name);
            }

            @Override
            public Class<Base> getModelType() {
                return Base.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });

        root.addComponents(new TextField("Random text"), grid);
        root.setExpandRatio(grid, 1);
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

    @Getter
    public enum Formula1Team {
        MERCEDES(" Mercedes", "Mercedes AMG Petronas Formula One Team", GERMANY, new Base("Brackley", UK), "Toto Wolff", "Paddy Lowe", "F1 W07 Hybrid", "Mercedes"),
        FERRARI("Ferrari", "Scuderia Ferrari", ITALY, new Base("Maranello", ITALY), "Maurizio Arrivabene", "James Allison", "SF16-H", "Ferrari"),
        RED_BULL("Red Bull", "Red Bull Racing", AUSTRIA, new Base("Milton Keynes", UK), "Christian Horner", "Adrian Newey", "RB12", "TAG Heuer"),
        WILLIAMS("Williams", "Williams Martini Racing", UK, new Base("Grove", UK), "Frank Williams", "Pat Symonds", "FW38", "Mercedes"),
        TORO_ROSSO("Toro Rosso", "Scuderia Toro Rosso", ITALY, new Base("Faenza", ITALY), "Franz Tost", "James Key", "STR11", "Ferrari"),
        MC_LAREN("McLaren", "McLaren Honda Formula 1 Team", UK, new Base("Woking", UK), "Eric Boullier", "Tim Goss", "MP4-31", "Honda"),
        FORCE_INDIA("Force India", "Sahara Force India F1 Team", INDIA, new Base("Silverstone", UK), "Vijay Mallya", "Andrew Green", "VJM09", "Mercedes"),
        RENAULT("Renault", "Renault Sport Formula 1 Team", FRANCE, new Base("Enstone", UK), "Frederic Vasseur", "Bob Bell", "R.S.16", "Renault"),
        SAUBER("Sauber", "Sauber F1 Team", SWITZERLAND, new Base("Hinwil", SWITZERLAND), "Monisha Kaltenborn", "TBC", "C35", "Ferrari"),
        HAAS("Haas", "Haas F1 Team", USA, new Base("Kannapolis", USA), "Guenther Steiner", "Rob Taylor", "VF-16", "Ferrari"),
        MANOR("Manor Racing", "Manor Racing MRT", UK, new Base("Banbury", UK), "Dave Ryan", "John McQuilliam", "MRT05", "Mercedes");

        final String name;
        final String fullName;
        final Country nation;
        final Base base;
        final String teamChief;
        final String technicalChief;
        final String chassis;
        final String powerUnit;

        Formula1Team(String name, String fullName, Country nation, Base base, String teamChief, String technicalChief, String chassis, String powerUnit) {
            this.name = name;
            this.fullName = fullName;
            this.nation = nation;
            this.base = base;
            this.teamChief = teamChief;
            this.technicalChief = technicalChief;
            this.chassis = chassis;
            this.powerUnit = powerUnit;
        }

    }

    enum Country {
        GERMANY("Germany", "\uD83C\uDDE9\uD83C\uDDEA"),
        ITALY("Italy", "\uD83C\uDDEE\uD83C\uDDF9"),
        AUSTRIA("Austria", "\uD83C\uDDE6\uD83C\uDDF9"),
        UK("United Kingdom", "\uD83C\uDDEC\uD83C\uDDE7"),
        INDIA("India", "\uD83C\uDDEE\uD83C\uDDF3"),
        FRANCE("France", "\uD83C\uDDEB\uD83C\uDDF7"),
        SWITZERLAND("Switzerland", "\uD83C\uDDE8\uD83C\uDDED"),
        USA("United States", "\uD83C\uDDFA\uD83C\uDDF8");

        String name;
        String flag;

        Country(String name, String flag) {
            this.name = name;
            this.flag = flag;
        }
    }

    static class Base {
        String city;
        Country country;

        Base(String city, Country country) {
            this.city = city;
            this.country = country;
        }
    }

}

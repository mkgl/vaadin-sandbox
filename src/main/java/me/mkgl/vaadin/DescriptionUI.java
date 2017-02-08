package me.mkgl.vaadin;

import static com.vaadin.server.Sizeable.Unit.PERCENTAGE;
import static com.vaadin.server.Sizeable.Unit.PIXELS;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

import me.mkgl.vaadin.description.Description;

@Theme("valo")
public class DescriptionUI extends UI {

    private static final String TITLE_DESC = "Enter the title of your event, in plain english.";
    private static final String START_DATE_DESC = "Paris, Central European Time (UTC+1)";
    private static final String LOCATION_DESC = "Please select a meeting room";
    private Description titleDesc;

    @WebServlet(value = "/description/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DescriptionUI.class, resourceCacheTime = 0)
    public static class Servlet extends VaadinServlet {
    }

    private Date date;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();

        WebBrowser browserInfo = Page.getCurrent().getWebBrowser();
        date = browserInfo.getCurrentDate(); // aka request date

        TextField title = new TextField("Title");
        titleDesc = Description.extend(title);
        titleDesc.setText(TITLE_DESC);
        title.setDescription(TITLE_DESC);

        TextField subtitle = new TextField("Subtitle");
        subtitle.setDescription("Enter some subtitle, maybe");

        DateField startDate = new PopupDateField("Start Date");
        startDate.setResolution(Resolution.MINUTE);
//         startDate.setWidth(400, PIXELS);
        Description startDateDesc = Description.extend(startDate);
        startDateDesc.setText(START_DATE_DESC);
        startDate.setDescription(START_DATE_DESC);

        ComboBox location = new ComboBox("Location", Arrays.asList("AC/DC", "Kraftwerk", "Miles Davis", "The Velvet Underground"));
        Description locationDesc = Description.extend(location);
        locationDesc.setText(LOCATION_DESC);
        location.setDescription(LOCATION_DESC);

        FormLayout layout = new FormLayout(title, subtitle, startDate, location);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setWidth(720, PIXELS);
        layout.setHeightUndefined();

        Stream.of(title, subtitle, startDate, location)
                .forEach(field -> field.setWidth(100, PERCENTAGE));

        Button toggleVisible = new Button("Toggle Visible", (clickEvent) -> title.setVisible(!title.isVisible()));
        Button toggleDetach = new Button("Attach/Detach", (clickEvent) -> {
            if (title.isAttached()) layout.removeComponent(title);
            else layout.addComponent(title, 0);
        });
        Button toggleDescription = new Button("Reverse text", (clickEvent) -> {
            StringBuilder sb = new StringBuilder(titleDesc.getText());
            titleDesc.setText(sb.reverse().toString());
        });
        Button toggleExtension = new Button("Toggle extension", (clickEvent) -> {
            if (!title.getExtensions().isEmpty()) {
                title.removeExtension(titleDesc);
            } else {
                titleDesc = Description.extend(title);
                titleDesc.setText(TITLE_DESC);
            }
        });

        layout.addComponent(new HorizontalLayout(toggleVisible, toggleDetach, toggleDescription, toggleExtension));

        setContent(layout);
    }
}

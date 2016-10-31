package me.mkgl.vaadin;

import static com.vaadin.server.Sizeable.Unit.PIXELS;
import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
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
public class DateFieldUI extends UI {

    @WebServlet(value = "/date/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DateFieldUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeFull();

        WebBrowser browserInfo = Page.getCurrent().getWebBrowser();
        int offset = browserInfo.getTimezoneOffset();
        int rawOffset = browserInfo.getRawTimezoneOffset();
        boolean dst = browserInfo.isDSTInEffect();
        int dstSavings = browserInfo.getDSTSavings();
        Date requestDate = browserInfo.getCurrentDate();

//        List<TimeZone> timeZoneCandidates = Arrays.stream(TimeZone.getAvailableIDs())
        List<TimeZone> timeZoneCandidates = Arrays.stream(TimeZone.getAvailableIDs(rawOffset))
                .map(TimeZone::getTimeZone)
                .filter(z -> z.inDaylightTime(requestDate) == dst)
                .filter(z -> z.getDSTSavings() == dstSavings)
                .collect(toList());

        System.out.println(timeZoneCandidates.size());
        System.out.println(timeZoneCandidates.stream()
                .map(TimeZone::getID)
                .collect(joining("\n", "\n-----\n", "\n-----\n")));
        Optional<TimeZone> timeZone = timeZoneCandidates.stream().findFirst();
        System.out.println(timeZone);

        DateField dateField = new PopupDateField("Date");
        dateField.setResolution(Resolution.MINUTE);
        dateField.setWidth(400, PIXELS);
        dateField.addValueChangeListener(this::logDate);

        ComboBox timeZoneField = new ComboBox("Time zone", new BeanItemContainer<>(TimeZone.class, timeZoneCandidates));
        timeZoneField.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        timeZoneField.setItemCaptionPropertyId("ID");
        timeZoneField.setNullSelectionAllowed(false);
        timeZoneField.setFilteringMode(FilteringMode.CONTAINS);
        timeZoneField.addValueChangeListener(e -> {
            TimeZone newTimeZone = (TimeZone) e.getProperty().getValue();
            dateField.setTimeZone(newTimeZone);
            logTimeZone(newTimeZone);
        });

//        if (timeZone.isPresent()) {
//            timeZoneField.setValue(timeZone.get());
//        }
        TimeZone rawTimeZoneWithOffset = TimeZone.getTimeZone("UTC");
        // we need to account for DST here, simply no way to set DST separately
        rawTimeZoneWithOffset.setRawOffset(offset);
        dateField.setTimeZone(rawTimeZoneWithOffset);
        dateField.setValue(requestDate);

        HorizontalLayout testDateRow = new HorizontalLayout(timeZoneField, dateField);
        testDateRow.setCaption("Test schedule");
        testDateRow.setSpacing(true);
        layout.addComponent(testDateRow);
        setContent(layout);
    }

    private void logDate(Property.ValueChangeEvent e) {
        String message = "Date changed to " + e.getProperty().getValue() + " (Server time)";
        Notification.show(message);
        System.out.println(message);
    }

    private void logTimeZone(TimeZone newTimeZone) {
        String message = "Time zone changed to " + newTimeZone.getID();
        Notification.show(message);
        System.out.println(message);
    }
}

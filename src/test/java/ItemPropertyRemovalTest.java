import org.junit.Test;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

public class ItemPropertyRemovalTest {

    @Test
    public void removeItemProperyWhileIteratingOverPropertyIdsWithPropertysetItem() throws Exception {

        Item item = new PropertysetItem();
        item.addItemProperty("a", new ObjectProperty<>("Ananas"));
        item.addItemProperty("b", new ObjectProperty<>("Banana"));
        item.addItemProperty("c", new ObjectProperty<>("Cactus"));

        item.getItemPropertyIds().stream()
                .filter(propertyId -> propertyId.equals("b"))
                .forEach(propertyId -> item.removeItemProperty("b"));
    }
}

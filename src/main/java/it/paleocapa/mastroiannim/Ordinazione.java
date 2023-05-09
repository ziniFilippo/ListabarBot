package it.paleocapa.mastroiannim;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;


@RefreshScope
@Configuration
public class Ordinazione {
    HashMap<String, Double> menu;

    @Value("${menu.items}")
    private String[] menuItems = {"Brioche-cioccolato=0.90", "Brioche-marmellata=0.90"};

    public Ordinazione(){
        menu =  new HashMap<>();
        for (String menuItem : menuItems) {
            String[] menuItemParts = menuItem.split("=");
            String itemName = menuItemParts[0];
            Double itemPrice = Double.parseDouble(menuItemParts[1]);
            menu.put(itemName, itemPrice);
        }
    }
    @Override
    public String toString(){
        String msg = menu.keySet().stream().map(x -> x + ": "+ String.valueOf(menu.get(x)) +"€").reduce("", String::concat);
        return msg;
    }

}

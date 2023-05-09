package it.paleocapa.mastroiannim;

import java.util.*;

public class Ordinazione {
    private LinkedList<Prodotto> menu;

    
    public Ordinazione(LinkedList<Prodotto>menu){
        this.menu = menu;
    }

    public double searchItem(String prodotto){
        for (Prodotto prod : menu) {
            if(prod.nome.equals(prodotto)){
                return prod.prezzo;
            }
        }
        return 0.0;
    }

    @Override
    public String toString() {
        //String msg = menu.keySet().stream().map(x -> x.toString() + ": " + String.valueOf(menu.get(x)) + "€").reduce("", String::concat);
        String msg="";
        for (Prodotto p : menu) {
            msg+=p.nome+": "+ String.valueOf(p.prezzo)+"€\n";
        }
        return msg;
    }
}

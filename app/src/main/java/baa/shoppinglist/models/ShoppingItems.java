package baa.shoppinglist.models;

/**
 * Created by User on 18.12.2016 г..
 */

public class ShoppingItems {
    private String label;
    private int qty;

    public ShoppingItems(){}

    public ShoppingItems(String label, int qty){
        this.label = label;
        this.qty = qty;
    }

    public String getLabel(){

        return this.label;
    }

    public int getQty(){

        return this.qty;

    }
}

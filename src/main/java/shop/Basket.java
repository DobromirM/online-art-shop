package shop;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Basket
{
    
    Map<Product, Integer> items;
    ShopDB db;
    
    public static void main(String[] args)
    {
        Basket b = new Basket();
        b.addItem("art1");
        System.out.println(b.getTotalString());
        b.clearBasket();
        System.out.println(b.getTotalString());
        // check that adding a null String causes no problems
        String pid = null;
        b.addItem(pid);
        System.out.println(b.getTotalString());
    }
    
    public Basket()
    {
        db = ShopDB.getSingleton();
        items = new HashMap<>();
    }
    
    /**
     * @return Map of Product items that are stored in the basket
     * <p>
     * Each item is a pair of product object and quantity
     * <p>
     *
     */
    public Map<Product, Integer> getItems()
    {
        return items;
    }
    
    /**
     * empty the basket - the basket should contain no items after calling this method
     */
    public void clearBasket()
    {
        items.clear();
    }
    
    /**
     * Adds an item specified by its product code to the shopping basket
     *
     * @param pid - the product code
     */
    public void addItem(String pid)
    {
        
        // need to look the product name up in the
        // database to allow this kind of item adding...
        
        addItem(db.getProduct(pid));
    }
    
    public void addItem(Product p)
    {
        // ensure that we don't add any nulls to the item list
        if (p != null)
        {
            if (items.containsKey(p))
            {
                Integer count = items.get(p) + 1;
                items.put(p, count);
            }
            else
            {
                items.put(p, 1);
            }
        }
    }
    
    /**
     * Remove an item specified by its product code from the shopping basket
     *
     * @param pid - the product code
     */
    public void removeItem(String pid)
    {
        items.remove(db.getProduct(pid));
    }
    
    /**
     * Change quantity of an item specified by its product code
     *
     * @param pid - the product code
     */
    public void changeQunatity(String pid, String quantityString)
    {
        Product product = db.getProduct(pid);
        
        Integer quantity = Integer.parseInt(quantityString);
        
        items.remove(product);
        items.put(product, quantity);
    }
    
    /**
     * @return the total value for one item entry (Value * Quantity)
     */
    public int getItemTotal(Product p)
    {
        Integer total = 0;
        
        total = total + p.price * items.get(p);
        
        
        // return the total
        return total;
    }
    
    /**
     * @return the total value for one item entry (Value * Quantity) in the basket as
     * a pounds and pence String with two decimal places
     */
    public String getItemTotalString(Product p)
    {
        Double total = (double) getItemTotal(p) / 100;
        DecimalFormat df = new DecimalFormat("0.00");
        
        return df.format(total);
    }
    
    /**
     * @return the total value of items in the basket in pence
     */
    public int getTotal()
    {
        Integer total = 0;
        
        for (Map.Entry<Product, Integer> p: items.entrySet())
        {
            total = total + p.getKey().price * p.getValue();
        }
    
        // return the total
        return total;
    }
    
    /**
     * @return the total value of items in the basket as
     * a pounds and pence String with two decimal places - hence
     * suitable for inclusion as a total in a web page
     */
    public String getTotalString()
    {
        Double total = (double) getTotal() / 100;
        DecimalFormat df = new DecimalFormat("0.00");
        
        return df.format(total);
    }
}

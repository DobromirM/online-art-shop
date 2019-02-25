package shop;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;

public class ShopDB
{
    
    Connection con;
    static int nOrders = 0;
    static ShopDB singleton;
    
    public static void main(String[] args) throws Exception
    {
        // simple method to test that ShopDB works
        System.out.println("Got this far...");
        ShopDB db = new ShopDB();
        System.out.println("created shop db");
        shop.Basket basket = new shop.Basket();
        System.out.println("created the basket");
        
        System.out.println("Testing getAllProducts");
        Collection c = db.getAllProducts();
        for (Iterator i = c.iterator(); i.hasNext(); )
        {
            Product p = (Product) i.next();
            System.out.println(p);
        }
        System.out.println("Testing getProduct(pid)");
        Product product = db.getProduct("art1");
        System.out.println(product);
        
        System.out.println("Testing order: ");
        basket.addItem(product);
        System.out.println("added an item");
        db.order(basket, "Simon", "simon@fake.com");
        System.out.println("order done");
    }
    
    public ShopDB()
    {
        try
        {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            System.out.println("loaded class");
            
            con = DriverManager.getConnection("jdbc:hsqldb:mem:shopdb", "SA", "");
            
            System.out.println("created con");
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e);
        }
    }
    
    public static ShopDB getSingleton()
    {
        if (singleton == null)
        {
            singleton = new ShopDB();
        }
        return singleton;
    }
    
    /**
     * Get all products from the database
     *
     * @return - Collection of all the products in the database
     */
    public Collection<Product> getAllProducts()
    {
        return getProductCollection("Select * from Product");
    }
    
    /**
     * Get all products from the database by specific artist
     *
     * @return - Collection of all the products in the database from a specific artist
     */
    public Collection<Product> getProductsByArtist(String artistName)
    {
        LinkedList<Product> list = new LinkedList<Product>();
        
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM PRODUCT WHERE UPPER(ARTIST) LIKE UPPER(?)");
            ps.setString(1, "%" + artistName + "%");
            
            ResultSet resultSet = ps.executeQuery();
    
            createList(list, resultSet);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get all products from the database in a specific price range
     *
     * @return - Collection of all the products in the database within a specific price range
     */
    public Collection<Product> getProductsByPrice(String minPriceString, String maxPriceString)
    {
        Integer minPrice = Math.round(( Float.parseFloat(minPriceString) * 100));
        Integer maxPrice = Math.round(( Float.parseFloat(maxPriceString) * 100));
        
        LinkedList<Product> list = new LinkedList<Product>();
        
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM PRODUCT WHERE PRICE > ? AND PRICE < ?");
            ps.setInt(1, minPrice);
            ps.setInt(2, maxPrice);
            
            ResultSet resultSet = ps.executeQuery();
            
            createList(list, resultSet);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return list;
    }
    
    /**
     * Get product by given pid
     *
     * @param pid - Id of the product
     *
     * @return - The product with the given pid
     */
    public Product getProduct(String pid)
    {
        try
        {
            // re-use the getProductCollection method
            // even though we only expect to get a single Product Object
            String query = "Select * from Product where PID = '" + pid + "'";
            Collection<Product> c = getProductCollection(query);
            Iterator<Product> i = c.iterator();
            return i.next();
        }
        catch (Exception e)
        {
            // unable to find the product matching that pid
            return null;
        }
    }
    
    /**
     * Get a collection of products from the database
     *
     * @param query - Query for the products
     *
     * @return - List of products
     */
    public Collection<Product> getProductCollection(String query)
    {
        LinkedList<Product> list = new LinkedList<Product>();
        try
        {
            Statement s = con.createStatement();
            
            ResultSet rs = s.executeQuery(query);
            return createList(list, rs);
        }
        catch (Exception e)
        {
            System.out.println("Exception in getProducts(): " + e);
            return null;
        }
    }
    
    /**
     * Create a list of products from a given Result Set
     *
     * @param list - List of products
     * @param rs   - Result Set
     *
     * @return - Collection of the products
     *
     * @throws SQLException - Exception
     */
    private Collection<Product> createList(LinkedList<Product> list, ResultSet rs) throws SQLException
    {
        while (rs.next())
        {
            Product product = new Product(rs.getString("PID"), rs.getString("Artist"), rs.getString("Title"), rs.getString("Description"), rs.getInt("price"), rs.getString("thumbnail"), rs.getString("fullimage"));
            list.add(product);
        }
        return list;
    }
    
    /**
     * Create orders for each item in the basket
     *
     * @param basket        - Basket with items
     * @param customerName  - Name of customer
     * @param customerEmail - Email of customer
     */
    public void order(Basket basket, String customerName, String customerEmail)
    {
        try
        {
            // create a unique order id
            String orderId = System.currentTimeMillis() + ":" + nOrders++;
            
            // iterate over the basket of contents ...
            Iterator<Map.Entry<Product, Integer>> i = basket.getItems().entrySet().iterator();
            
            while (i.hasNext())
            {
                Map.Entry<Product, Integer> product = i.next();
                // and place the order for each one
                order(con, product, orderId, customerName, customerEmail);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Add an order to the database
     *
     * @param con           - Connection
     * @param p             - Map entry with information about the product and the quantity
     * @param orderId       - The id of the order
     * @param customerName  - The name of the customer
     * @param customerEmail - The email of the customer
     *
     * @throws Exception - Exception
     */
    private void order(Connection con, Map.Entry<Product, Integer> p, String orderId, String customerName, String customerEmail) throws Exception
    {
        PreparedStatement ps = con.prepareStatement("INSERT INTO ORDERS (PID, ORDERID, NAME, EMAIL, QUANTITY, PRICE) VALUES (?,?,?,?,?,?)");
        ps.setString(1, p.getKey().PID);
        ps.setString(2, orderId);
        ps.setString(3, customerName);
        ps.setString(4, customerEmail);
        ps.setInt(5, p.getValue());
        ps.setInt(6, p.getKey().price);
        
        ps.executeUpdate();
    }
}

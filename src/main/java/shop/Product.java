package shop;

import java.text.DecimalFormat;
import java.util.Objects;

public class Product
{
    public String PID;
    public String artist;
    public String title;
    public String description;
    public int price;
    public String thumbnail;
    public String fullimage;
    
    public Product(String PID, String artist, String title, String description, int price, String thumbnail, String fullimage)
    {
        this.PID = PID;
        this.artist = artist;
        this.title = title;
        this.description = description;
        this.price = price;
        this.thumbnail = thumbnail;
        this.fullimage = fullimage;
    }
    
    public String toString()
    {
        return title + "\t " + price;
    }
    
    public String getPrice()
    {
        Double price = (double) this.price / 100;
        DecimalFormat df = new DecimalFormat("0.00");
    
        return df.format(price);
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Product product = (Product) o;
        
        return Objects.equals(PID, product.PID);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(PID, artist, title, description, price, thumbnail, fullimage);
    }
}

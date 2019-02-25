<%@ include file = "header.jsp" %>

<%@ page import="shop.Product"%>

<%@ page import="java.util.Collection" %>

<jsp:useBean id='db'
             scope='session'
             class='shop.ShopDB' />

<div id="searchOptions">
    
    <h1>Search By Artist</h1>
    <form action="search.jsp" method="post">
        
        <label for="artist-name">Artist:
            <input type="text" name="artist-name" id="artist-name" min="2" size="20">
        </label>
        
        <input type="submit" id="artist-search" value="Search" />
        
        <div class="error" id="artistError">Please enter at least 2 characters of the name!</div>
        
    </form>
    
    
    <h1>Search By Price Range</h1>
    <form action="search.jsp" method="post">
        
        <label for="min-price">Minimum price:
            <input type="number" name="min-price" id="min-price" min="0" step="0.01" size="20">
        </label>
        
        <label for="max-price">Maximum price:
            <input type="number" name="max-price" id="max-price" min="0" step="0.01" size="20">
        </label>
        
        <input type="submit" id="price-search" value="Search" />
    
        <div class="error" id="priceError">Please enter a valid decimal numbers for the prices!</div>
        <div class="error" id="rangeError">The minimum price must not be higher than the maximum price!</div>
        
    </form>
    
</div>

<%
    String artistName = request.getParameter("artist-name");
    String minPriceString = request.getParameter("min-price");
    String maxPriceString = request.getParameter("max-price");
    
    if(artistName != null || minPriceString != null)
    {
        Collection<Product> products;
        
        if(artistName != null)
        {
            products = db.getProductsByArtist(artistName);
        }
        else
        {
            products = db.getProductsByPrice(minPriceString, maxPriceString);
        }
        
        if (products.size() > 0)
        {
%>

    <h1>Products Found:</h1>
    <table>
        <tr>
            <th> Title </th> <th> Price </th> <th> Picture </th>
        </tr>
        
        <%
            for (Product product : products) {
        %>
        <tr>
            <td> <%= product.title %> </td>
            <td> <%= product.getPrice() %>&pound;</td>
            <td> <a href = '<%="viewProduct.jsp?pid="+product.PID%>'> <img src="<%= product.thumbnail %>"/> </a> </td>
        </tr>
        <%
            }
        %>
    </table>

<%
        }
        else
        {
%>
    <h1>No Products Found!</h1>
<%
        }
    }
%>

</body>
</html>

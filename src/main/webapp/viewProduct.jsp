<%@ include file = "header.jsp" %>

<%@ page import="shop.Product"%>

<jsp:useBean id='db'
             scope='session'
             class='shop.ShopDB' />

<%
    String pid = request.getParameter("pid");
    Product product = db.getProduct(pid);
    // out.println("pid = " + pid);
    if (product == null) {
        // do something sensible!!!
        out.println( product );
    }
    else {
        %>
        <div id="center-div" align="center">
        <h2> <%= product.title %>  by <%= product.artist %> </h2>
        <img src="<%= product.fullimage %>" />
        <p> <%= product.description %> </p>
        <p><a href='<%="basket.jsp?addItem="+product.PID%>'>Add to basket</a></p>
        </div>
        <%
    }
%>

</div>
</body>
</html>

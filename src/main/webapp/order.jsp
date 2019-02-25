<%@ include file = "header.jsp" %>

<jsp:useBean id='basket'
         scope='session'
         class='shop.Basket'
          />

<jsp:useBean id = 'db'
             scope='page'
             class='shop.ShopDB' />

<% String custName = request.getParameter("name");
   String custEmail = request.getParameter("email");

    if (custName != null) {
        db.order(basket, custName, custEmail);
        basket.clearBasket();
    %>
    <h2> Dear <%= custName %> ! Thank you for your order. </h2>
    <%
    }
        else {
        %>
        <h2> please go back and supply a name </h2>
        <%
    }

%>
</body>

</html>

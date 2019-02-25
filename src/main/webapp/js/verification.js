$(document).ready(function () {
    
    $("#placeOrder").click(function (e) {
        
        var name = $("#name");
        var email = $("#email");
        
        if (name.val().trim() !== "")
        {
            $("#nameError").hide();
        }
        else
        {
            $("#nameError").show();
            e.preventDefault();
        }
        
        if (/\S+@\S+\.\S/.test(email.val()))
        {
            $("#emailError").hide();
        }
        else
        {
            $("#emailError").show();
            e.preventDefault();
        }
       
    });
    
    $("#artist-search").click(function (e) {
        
        var artistName = $("#artist-name");
        
        if (artistName.val().trim().length < 2)
        {
            $("#artistError").show();
            e.preventDefault();
        }
        
    });
    
    $("#price-search").click(function (e) {
    
        $("#priceError").hide();
        $("#rangeError").hide();
        
        var minPrice = $("#min-price");
        var maxPrice = $("#max-price");
        
        if (minPrice.val() === "" || maxPrice.val() === "")
        {
            $("#priceError").show();
            e.preventDefault();
        }
        else
        {
            if (parseFloat(minPrice.val()) > parseFloat(maxPrice.val()))
            {
                $("#rangeError").show();
                e.preventDefault();
            }
        }
    });
    
});
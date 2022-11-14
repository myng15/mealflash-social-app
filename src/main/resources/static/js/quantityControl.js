$(document).ready(function() {
    $(".minusBtn").on("click", function(e) {
        e.preventDefault();
        itemID = $(this).attr("itemID");
        quantityInput = $("#quantity" + itemID);

        if (parseFloat(quantityInput.val()) > 0) newQuantity = parseFloat(quantityInput.val()) - 0.5;
        if (newQuantity >= 0) quantityInput.val(newQuantity);
    })

    $(".plusBtn").on("click", function(e) {
        e.preventDefault();
        itemID = $(this).attr("itemID");
        quantityInput = $("#quantity" + itemID);

        newQuantity = parseFloat(quantityInput.val()) + 0.5;
        if (newQuantity >= 0) quantityInput.val(newQuantity);
    })
})
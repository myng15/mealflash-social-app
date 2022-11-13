$(document).ready(function() {
    $(".minusBtn").on("click", function(e) {
        e.preventDefault();
        decreaseServings($(this));
    })

    $(".plusBtn").on("click", function(e) {
        e.preventDefault();
        increaseServings($(this));
    })
})

function decreaseServings(minusBtn){
        itemID = minusBtn.attr("itemID");
        quantityInput = $("#quantity" + itemID);

        newQuantity = parseFloat(quantityInput.val()) - 0.5;
        if (newQuantity > 0) quantityInput.val(newQuantity);
        updateServingsAndIngredientSubtotals(itemID, newQuantity);
}

function increaseServings(plusBtn){
         itemID = plusBtn.attr("itemID");
         quantityInput = $("#quantity" + itemID);

         newQuantity = parseFloat(quantityInput.val()) + 0.5;
         if (newQuantity > 0) quantityInput.val(newQuantity);
         updateServingsAndIngredientSubtotals(itemID, newQuantity);
}

function updateServingsAndIngredientSubtotals(recipeID, quantity) {
    url = "/MealFlash/shopping-list/update/" + recipeID + "/" + quantity;
    $.ajax({
            type: "POST",
            url: url,
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeaderName, csrfValue);
            }
        })
        .done(function(newSubtotals) {
//            $('#test').html(data.text);
//            console.log(newSubtotals);
            updateIngredientSubtotals(newSubtotals, recipeID); //newSubtotals is the ResponseBody from the POST method in Controller, can also be written as "response" i.e. function(response) { updateIngredientSubtotals(response) } (as normally seen in ajax calls)
        }).fail(function() {
            $("#addToShoppingListMessage").addClass("alert-danger");
            $("#addToShoppingListMessage").text("Error occurred while updating shopping list. Please try again.");
        });
}

function updateIngredientSubtotals(newSubtotals, recipeID) {
    for (let i = 0; i < newSubtotals.length; i++) {
//        console.log($("#subtotal" + recipeID + "_" + i).text());
        $("#subtotal" + recipeID + "_" + i).text(newSubtotals[i]);
    }
}
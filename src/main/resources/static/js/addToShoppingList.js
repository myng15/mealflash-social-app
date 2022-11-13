$(document).ready(function() {
    $(".addToShoppingListBtn").on("click", function(e) {
        addToShoppingList($(this));
//    recipeID = $(this).attr("recipeID");
//    recipeTitle = $(this).attr("recipeTitle");
//    console.log(recipeID);
//    console.log(recipeTitle);
    });
});

function addToShoppingList(btn) {
    recipeID = btn.attr("recipeID");
    recipeTitle = btn.attr("recipeTitle");
    url = "/MealFlash/recipes/into-shopping-list/" + recipeID;
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    })
    .done(function(response) {
        $("#addToShoppingListMessage").removeClass("alert-danger");
        $("#addToShoppingListMessage").addClass("alert-success");
        $("#addToShoppingListMessage").text("Recipe for 1 serving of " + recipeTitle + " was added to your shopping list.");
    }).fail(function() {
         $("#addToShoppingListMessage").removeClass("alert-success");
        $("#addToShoppingListMessage").addClass("alert-danger");
        $("#addToShoppingListMessage").text("Error occurred while adding recipe to your shopping list. Please try again.");
    });
}
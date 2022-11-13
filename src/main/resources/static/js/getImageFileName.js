//$(document).ready(function() {
//    $(".submitBtn").on("click", function(e) {
//        getImageFileName($('form'));
//    });
//});

function getImageFileName(form) {
//    imageFileName = $("#imageFile").val();
//    imageFileName = btn.attr("imageFileName");
    imageFileName = $("#imageFileName").val();
    url = "/MealFlash/recipes/new";
    params = {imageFileName: imageFileName};

    $.ajax({
        type: "POST",
        contentType: "application/json",
        dataType: 'json',
        url: url,
        data: JSON.stringify(params),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    })
    .done(function(response) {
         form.submit();
         $("#message").addClass("alert-success");
         $("#message").text("success, " + imageFileName);
    }).fail(function() {

         $("#message").addClass("alert-success");
         $("#message").text("error, " + imageFileName);
    });
//    return false;
}
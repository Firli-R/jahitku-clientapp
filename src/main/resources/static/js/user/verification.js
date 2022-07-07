let expired = "";
let tokenUser = null;
let tokenUserStatus = null;
$(document).ready(() => {
    var url_string = window.location.href;
    var url = new URL(url_string);
    var param = url.searchParams.get("token");
    tokenUser = getVerification(param);
    console.log(tokenUser);
    tokenUserStatus = getVerificationStatus(param);
    console.log(tokenUserStatus);
    onClickPushEmail();
    getDateCount();
});

function getVerification(param) {
    return JSON.parse(
        $.ajax({
            type: "GET",
            url: "/verification/" + param,
            dataType: "json",
            global: false,
            async: false,
            success: function(response) {
                return response;
            },
        }).responseText
    );
}

function getVerificationStatus(param) {
    return JSON.parse(
        $.ajax({
            type: "GET",
            url: "/verify?token=" + param,
            dataType: "json",
            global: false,
            async: false,
            success: function(response) {
                return response;
            },
        }).responseText
    );
}

function getDateCount() {
    var countDownDate = new Date(tokenUser.expiresAt);
    var x = setInterval(function() {
        // Get today's date and time
        var now = new Date();

        // Find the distance between now and the count down date
        var distance = Date.parse(countDownDate) - Date.parse(now);

        // Time calculations for days, hours, minutes and seconds

        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);
        // Output the result in an element with id="demo"
        document.getElementById("countDown").innerHTML =
            minutes + "m " + seconds + "s ";
        //   $("#countDown").text(minutes + "m");
        $("#pushEmailBtn").prop("disabled", true);
        $("#backLoginBtn").addClass("d-none");

        // If the count down is over, write some text
        //   if (tokenUserStatus.status === "200") {
        //       document.getElementById("countDown").innerHTML =
        //           tokenUserStatus.message;
        //       $("#pushEmailBtn").prop("disabled", false);
        //   }
        if (tokenUser.confirmedAt != null) {
            document.getElementById("countDown").innerHTML =
                tokenUserStatus.message;
            $("#pushEmailBtn").prop("disabled", true);
            $("#backLoginBtn").removeClass("d-none");
        } else if (distance < 0) {
            clearInterval(x);
            document.getElementById("countDown").innerHTML = "EXPIRED";
            $("#pushEmailBtn").prop("disabled", false);
        }
        // if (distance < 0) {
        //    clearInterval(x);
        //    document.getElementById("countDown").innerHTML = "EXPIRED";
        //    $("#pushEmailBtn").prop("disabled", false);
        // }
    }, 1000);
}

const _csrf_token = $("meta[name='_csrf']").attr("content");

function onClickPushEmail() {
    $("#pushEmailBtn").click(function(e) {
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "/verification",
            contentType: "application/json",
            headers: {
                "X-CSRF-TOKEN": _csrf_token,
            },
            success: function(response) {
                toast().fire({
                    icon: response.status,
                    title: response.message,
                });
                getDateCount();
                $(document).ready(function() {
                    setTimeout(function() {
                        // alert("Reloading Page");
                        location.reload(true);
                    }, 2000);
                });
            },
            error: (err) => {
                toast().fire({
                    icon: "error",
                    title: "failed",
                });
            },
        });
    });
}

function toast() {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 2500,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener("mouseenter", Swal.stopTimer);
            toast.addEventListener("mouseleave", Swal.resumeTimer);
        },
    });

    return Toast;
}
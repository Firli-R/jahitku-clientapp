$(document).ready(() => {
    onUpdatePassword();
    // onUpdateEmail();
    $("#oldEmail").val(user.email);

    $("#showPass").click(function() {
        if ($("#oldPassword").attr("type") === "password") {
            $("#oldPassword").attr("type", "text");
        } else {
            $("#oldPassword").attr("type", "password");
        }
    });
    $("#showPass2").click(function() {
        if ($("#newPassword").attr("type") === "password") {
            $("#newPassword").attr("type", "text");
        } else {
            $("#newPassword").attr("type", "password");
        }
    });
});

function getUser() {
    return JSON.parse(
        $.ajax({
            type: "GET",
            url: "/profile/user",
            dataType: "json",
            global: false,
            async: false,
            success: function(data) {
                return data;
            },
        }).responseText
    );
}
const user = getUser();

function onUpdatePassword() {
    $("#submitButtonPass").click(function(e) {
        e.preventDefault();

        const dataPass = {
            username: "null",
            dataLama: $("#oldPassword").val(),
            dataBaru: $("#newPassword").val(),
        };

        $.ajax({
            type: "PUT",
            url: "/profile/user/change-password",
            data: JSON.stringify(dataPass),
            contentType: "application/json",
            headers: { "X-CSRF-TOKEN": _csrf_token },
            success: function(response) {
                toast().fire({
                    icon: "success",
                    title: response.message,
                });
                $(document).ready(function() {
                    setTimeout(function() {
                        location.reload(true);
                        $(document).ready(function() {
                            setTimeout(function() {
                                location.replace("http://localhost:8085/login");
                            }, 3000);
                        });
                    }, 1000);
                });
            },
            error: (err) => {
                toast().fire({
                    icon: "error",
                    title: "Update failed",
                });
            },
        });
    });
}

function onUpdateEmail() {
    $("#submitButtonEmail").click(function(e) {
        e.preventDefault();

        const dataPass = {
            username: "null",
            dataLama: $("#oldEmail").val(),
            dataBaru: $("#newEmail").val(),
        };

        $.ajax({
            type: "PUT",
            url: "/profile/user/change-email",
            data: JSON.stringify(dataPass),
            contentType: "application/json",
            headers: { "X-CSRF-TOKEN": _csrf_token },
            success: function(response) {
                toast().fire({
                    icon: "success",
                    title: "Update sucess",
                });
            },
            error: (err) => {
                toast().fire({
                    icon: "error",
                    title: "Update failed",
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
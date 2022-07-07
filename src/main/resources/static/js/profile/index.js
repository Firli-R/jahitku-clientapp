$(document).ready(() => {
    getProfile();
});
const user = getUser();

function getProfile() {
    $("#nama").val(user.nama).prop("disabled", true);
    $("#phone").val(user.phone).prop("disabled", true);
    $("#username").val(user.username).prop("disabled", true);
    $("#alamat").val(user.alamat).prop("disabled", true);
    $("#submitButton").addClass("d-none");
    $("#editButton").removeClass("d-none");
    $("#editButton").click(function(e) {
        e.preventDefault();
        $("#nama").val(user.nama).prop("disabled", false);
        $("#phone").val(user.phone).prop("disabled", false);
        $("#username").val(user.username).prop("disabled", false);
        $("#alamat").val(user.alamat).prop("disabled", false);
        $("#submitButton").removeClass("d-none");
        $("#editButton").addClass("d-none");

        $("#submitButton").click(function(e) {
            e.preventDefault();
            onUpdateUser();
        });
    });
}

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

function onUpdateUser() {
    const newUser = {
        nama: $("#nama").val(),
        phone: $("#phone").val(),
        username: $("#username").val(),
        alamat: $("#alamat").val(),
    };
    $.ajax({
        url: "/profile/user",
        type: "PUT",
        contentType: "application/json",
        headers: {
            "X-CSRF-TOKEN": _csrf_token,
        },
        data: JSON.stringify(newUser),
        success: function(response) {
            toast().fire({
                icon: "success",
                title: response.message,
            });
            $(document).ready(function() {
                setTimeout(function() {
                    // alert("Reloading Page");
                    location.reload(true);
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
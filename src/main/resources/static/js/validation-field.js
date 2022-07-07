/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(() => {
    validasiLogin();
    forgotPasword();
});

function validationField(error) {
    if (error.status === 400) {
        console.log(error);
        error.responseJSON.forEach((f) => {
            $(`#${f.field}`).addClass("is-invalid");
            $(`#${f.field}`).after(
                `<div class="invalid-feedback">${f.defaultMessage}</div>`
            );
        });
    } else {
        toast().fire({
            icon: "error",
            title: error.responseJSON.error,
        });
    }
}

function clearValidation() {
    $(".form-control").removeClass("is-invalid");
    $(".invalid-feedback").remove();
}

function toast() {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener("mouseenter", Swal.stopTimer);
            toast.addEventListener("mouseleave", Swal.resumeTimer);
        },
    });

    return Toast;
}

function validasiLogin() {
    $("#submitLogin").click(function(e) {
        let username = document.getElementById("username").value;
        $.ajax({
            type: "GET",
            url: "/validation/" + username,
            dataType: "json",
            success: function(response) {
                if (response.status === "error") {
                    window.stop();
                    toast().fire({
                        icon: response.status,
                        title: response.message,
                    });
                }
            },
            error: (err) => {
                alert("username atau password kurang tepat");
                window.stop();
            },
        });
    });
}

function forgotPasword() {
    $("#submitBtn").click(function(e) {
        e.preventDefault();
        $("#submitBtn").addClass("d-none");
        $("#spiner").removeClass("d-none");
        let valueEmail = $("#emailPass").val();
        console.log(valueEmail);
        $.ajax({
            type: "GET",
            url: "/forgotPassword?email=" + valueEmail,
            contentType: "aplication/json",
            success: function(response) {
                $("#spiner").addClass("d-none");
                $("#submitBtn").removeClass("d-none");
                toast().fire({
                    icon: response.status,
                    title: response.message,
                });
                $(document).ready(function() {
                    setTimeout(function() {
                        location.replace("http://localhost:8085/login");
                    }, 3000);
                });
                if (response.status === "error") {
                    $("#emailPass").addClass("is-invalid");
                    $("#emailPass-feedback").text("email not valid");
                } else {
                    $("#emailPass").removeClass("is-invalid");
                    $("#emailPass").addClass("is-valid");
                }
            },
        });
    });
}
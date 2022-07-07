// let tabel = null;
let listOfOrder = [];
$(document).ready(() => {
    getAllJenisJahitan();
    $(document).on("click", "#delete", function() {
        listOfOrder.pop();
        $(this).parent().remove();
        if (listOfOrder.length == 0) {
            $("#onAdd").attr("disabled", true);
        }
    });
});

// let getJenisJahitan = getAllJenisJahitan();
function getAllJenisJahitan() {
    $.ajax({
        url: "/jenisjahitan/get-all",
        type: "GET",
        success: (res) => {
            let html = "";
            $.each(res, (i, jenisJahitan) => {
                html += `<option value="${jenisJahitan.id}">${jenisJahitan.nama}</option>`;
            });
            $("#jenisJahitanId").append(html);
        },
        error: (err) => {
            console.log(err);
        },
    });
}

function onAdd() {
    var jenisJahitan = $("#jenisJahitanId").val();
    var kuantitas = $("#kuantitas").val();
    var keterangan = $("#keterangan").val();

    var dataOrder = {
        jenisJahitanId: jenisJahitan,
        kuantitas: kuantitas,
        keterangan: keterangan,
    };
    // console.log(jenisJahitan);
    let namaJenis = $("#jenisJahitanId option:selected").text();
    if (jenisJahitan != null && dataOrder.kuantitas != "") {
        $("#orderList").append(
            `<li class="list-group-item" value="${jenisJahitan}">${namaJenis}
                                    <button type="submit" id="delete" class="btn btn-sm btn-danger">Delete</button>
                                </li>`
        );
        listOfOrder.push(dataOrder);
        $("#onAdd").removeAttr("disabled");
    } else {
        $("#onAdd").attr("disabled", true);
    }

    console.log(listOfOrder);
}

function onOrder() {
    console.log(listOfOrder);
    let url = window.location.href;
    let urlData = url.substring(22);

    $.ajax({
        url: "/order",
        type: "POST",
        contentType: "application/json",
        headers: {
            "X-CSRF-TOKEN": _csrf_token,
        },
        data: JSON.stringify(listOfOrder),
        success: function(response) {
            toast().fire({
                icon: "success",
                title: response.message,
            });

            $(document).ready(function() {
                setTimeout(function() {
                    // alert("Reloading Page");
                    if (urlData === "order/admin") {
                        location.replace("http://localhost:8085/orderlist");
                    } else {
                        location.replace("http://localhost:8085/status");
                    }
                }, 2500);
            });
        },
        error: (err) => {
            toast().fire({
                icon: "error",
                title: "failed to add",
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
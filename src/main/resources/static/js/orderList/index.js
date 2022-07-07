let tabel = null;
let orderId = null;
let totalHarga = null;

$(document).ready(() => {
    getAllOrders();
});

function onCreateOrder(order) {
    $.ajax({
        url: "/order/create",
        type: "POST",
        contentType: "application/json",
        headers: {
            "X-CSRF-TOKEN": _csrf_token,
        },
        data: JSON.stringify(order),
        success: function(response) {
            toast().fire({
                icon: "success",
                title: response.message,
            });

            $("#orderModal").modal("hide");
            table.ajax.reload();
        },
        error: (err) => {
            toast().fire({
                icon: "error",
                title: "failed to add",
            });
        },
    });
}

function getAllOrders() {
    tabel = $("#orderTable").DataTable({
        responsive: true,
        ajax: {
            url: "/orderlist/get-all",
            dataSrc: "",
        },
        columnDefs: [{ width: "23%", targets: [0, 2, 3, 4] }],
        order: [
            [2, "desc"]
        ],
        columns: [
            { data: "noOrder" },
            {
                render: function(i, row, orders) {
                    return orders.user.nama;
                },
            },
            {
                data: "tanggalMasuk",
            },

            { data: "tanggalSelesai" },
            {
                render: function(i, row, orders) {
                    if (orders.pembayaran.statusPembayaran === "BELUM_DIBAYAR") {
                        return `<span class="badge bg-warning" style="color:black;font-size: 14px;">${orders.pembayaran.statusPembayaran}</span>`;
                    } else if (orders.pembayaran.statusPembayaran === "BATAL") {
                        return `<span class="badge bg-danger" style="color:black;font-size: 14px;">${orders.pembayaran.statusPembayaran}</span>`;
                    } else {
                        return `<span class="badge bg-success" style="color:black;font-size: 14px;">${orders.pembayaran.statusPembayaran}</span>`;
                    }
                },
            },
            {
                render: function(i, row, orders) {
                    if (orders.statusPesanan === "MENUNGGU_APPROVAL") {
                        return `<span class="badge bg-warning" style="color:black;font-size: 14px;">${orders.statusPesanan}</span>`;
                    } else if (orders.statusPesanan === "BATAL") {
                        return `<span class="badge bg-danger" style="color:black;font-size: 14px;">${orders.statusPesanan}</span>`;
                    } else if (orders.statusPesanan === "BELUM_DIKERJAKAN") {
                        return `<span class="badge bg-primary" style="color:black;font-size: 14px;">${orders.statusPesanan}</span>`;
                    } else if (orders.statusPesanan === "DALAM_PROSES") {
                        return `<span class="badge bg-warning" style="color:black;font-size: 14px;">${orders.statusPesanan}</span>`;
                    } else {
                        return `<span class="badge bg-success" style="color:black;font-size: 14px;">${orders.statusPesanan}</span>`;
                    }
                },
            },
            {
                sortable: false,
                render: function(i, row, orders) {
                    return `<button class="btn btn-sm btn-warning me-2" 
                            onclick='onShowUpdate(${JSON.stringify(
                               orders
                            )})'>Update</button>`;
                },
            },
        ],
    });
}

function getAllJenis() {
    return JSON.parse(
        $.ajax({
            type: "GET",
            url: "/jenisjahitan/get-all",
            dataType: "json",
            global: false,
            async: false,
            success: function(data) {
                return data;
            },
        }).responseText
    );
}

function onShowUpdate(orders) {
    this.orderId = orders.id;
    let listOrder = [];
    let listHarga = [];
    let jenisJahitan = getAllJenis();
    $("#orderListModal").modal("show");
    $("#nomerOrder").text(orders.noOrder);
    $("#status").text(orders.statusPesanan);
    $("#name").val(orders.user.nama);
    $("#username").val(orders.user.username).prop("disabled", true);
    $("#lokasi").val(orders.lokasiBarang);
    $("#tanggalMasuk").val(orders.tanggalMasuk).prop("disabled", true);
    $("#tanggalSelesai").val(orders.tanggalSelesai);
    getStatusPesanan(orders.statusPesanan);
    getStatusPembayaran(orders.pembayaran.statusPembayaran);

    let html = "";
    $.each(orders.jenisJahitanOrder, function(i, jenisJahitanOrder) {
        html += `
            <tr class="listJenisOrder">
                <td>
                    <select class="form-select form-control" style="width: 100%;" id="jenisJahitanId" name="jenisJahitanId">
                        <option disabled selected value="${jenisJahitanOrder.jenisJahitan.id}">
                        ${jenisJahitanOrder.jenisJahitan.nama}</option>
                        </select>
                </td>
                <td><textarea type="text" name="keterangan" id="keterangan" value="${jenisJahitanOrder.keterangan}">${jenisJahitanOrder.keterangan}</textarea>
                </td>
                <td><input type="number" name="kuantitas" id="kuantitas" style="width: 35%;" value="${jenisJahitanOrder.kuantitas}"/></td>

                <td ><input type="number" name="harga" id="harga" style="width: 80%;" value="${jenisJahitanOrder.harga}"/></td>
                <td>
                    <div class="form-check">
                        `;
        // $.each(jenisJahitan, function(i, data) {
        //     if (jenisJahitanOrder.jenisJahitan.nama != data.nama) {
        //         console.log(jenisJahitanOrder.jenisJahitan.nama);
        //         html += `<option value="${data.id}">${data.nama}</option>;`;
        //     }
        // });

        listHarga.push(jenisJahitanOrder.harga * jenisJahitanOrder.kuantitas);

        if (jenisJahitanOrder.status == true) {
            return (html += `<input class="form-check-input" type="checkbox" checked
                                    name="statusPengerjaan" id="statusPengerjaan">
                                </div>
                    </td>
            </tr>
                                    `);
        } else {
            return (html += `<input class="form-check-input" type="checkbox" 
                                    name="statusPengerjaan" id="statusPengerjaan">
                                      </div>
                    </td>
            </tr
                                    `);
        }
    });
    // $.ajax({
    //     type: "GET",
    //     url: "/jenisjahitan/get-all",
    //     success: function(response) {
    //         let html = "";
    //         $.each(response, function(indexInArray, jenisJahitan) {
    //             html += `<option value="${jenisJahitan.id}">${jenisJahitan.nama}</option>`;
    //         });
    //         $("#jenisJahitanId").append(html);
    //     },
    //     error: (err) => {
    //         console.log(err);
    //     },
    // });

    $("#orderModalTable tbody").html(html);

    this.totalHarga = listHarga.reduce(getSum, 0);
    $("#total").val(orders.pembayaran.totalBiaya);

    $("#orderListForm").on("submit", (e) => {
        e.preventDefault();

        let listJenis = [];
        $(".listJenisOrder").each((idx, data) => {
            listJenis.push({
                jenisJahitanId: $(
                    $("[name=jenisJahitanId] option:selected")[idx]
                ).val(),
                orderId: orders.id,
                kuantitas: $($("[name=kuantitas]")[idx]).val(),
                harga: $($("[name=harga]")[idx]).val(),
                keterangan: $($("[name=keterangan]")[idx]).val(),
                status: $($("[name=statusPengerjaan]")[idx]).is(":checked"),
            });
            // $.ajax({
            //     type: "PUT",
            //     url: "orderlist/jenis-jahitan-order",
            //     data: JSON.stringify(jenisJahitanOrder),
            //     contentType: "application/json",
            //     headers: {
            //         "X-CSRF-TOKEN": _csrf_token,
            //     },
            //     success: function(response) {
            //         listJenis.push(response);
            //     },
            // });
        });

        let order = {
            noOrder: $("#nomerOrder").text(),
            tanggalMasuk: $("#tanggalMasuk").val(),
            tanggalSelesai: $("#tanggalSelesai").val(),
            statusPesanan: $("#statusPemesanan option:selected").text(),
            nama: $("#name").val(),
            username: $("#username").val(),
            lokasiBarang: $("#lokasi").val(),
            statusPembayaran: $("#statusPembayaran option:selected").text(),
            totalBiaya: $("#total").val(),
            jenisJahitanOrder: listJenis,
        };
        listOrder.push(order);

        if (listOrder.length < 2) {
            onUpdateOrder(order, this.orderId);
            console.log(order);
        } else {
            listOrder.pop();
        }
    });
}

function getSum(total, num) {
    return total + Math.round(num);
}

function onUpdateOrder(order, id) {
    $.ajax({
        url: "orderlist/" + id,
        type: "PUT",
        contentType: "application/json",
        headers: {
            "X-CSRF-TOKEN": _csrf_token,
        },
        data: JSON.stringify(order),
        success: (res) => {
            toast().fire({
                icon: "success",
                title: res.message,
            });
            $("#orderList").modal("hide");
            tabel.ajax.reload();
            this.orderId = null;
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

function getStatusPesanan(statusPesanan) {
    let statusMenunggu = {
        id: 1,
        name: "MENUNGGU_APPROVAL",
    };
    let statusBatal = {
        id: 2,
        name: "BATAL",
    };
    let statusBelumDikerjakan = {
        id: 3,
        name: "BELUM_DIKERJAKAN",
    };
    let statusDalam = {
        id: 4,
        name: "DALAM_PROSES",
    };
    let statusSelesai = {
        id: 5,
        name: "SELESAI",
    };
    let statusPemesanan = [
        statusMenunggu,
        statusBatal,
        statusBelumDikerjakan,
        statusDalam,
        statusSelesai,
    ];
    let htmlStatusPemesanan = "";
    $.each(statusPemesanan, function(indexInArray, data) {
        if (statusPesanan != data.name) {
            htmlStatusPemesanan += `
            <option value="${data.name}">${data.name}</option>
        `;
        }
    });

    return $("#statusPemesanan").html(
        `<option disabled selected value="${statusPesanan}">${statusPesanan}</option>` +
        htmlStatusPemesanan
    );
}

function getStatusPembayaran(statusBayar) {
    let statusBelum = {
        id: 1,
        name: "BELUM_DIBAYAR",
    };
    let statusSudah = {
        id: 2,
        name: "SUDAH_DIBAYAR",
    };
    let statusPembayaran = [statusBelum, statusSudah];

    let htmlStatusPembayaran = "";
    $.each(statusPembayaran, function(i, data) {
        if (statusBayar != data.name) {
            htmlStatusPembayaran += `
            <option value="${data.id}">${data.name}</option>
        `;
        }
    });
    return $("#statusPembayaran").html(
        `<option selected disabled value="${statusBayar}">${statusBayar}</option>` +
        htmlStatusPembayaran
    );
}

function setOrderId(orderId) {
    this.orderId = orderId;
    $("#submitButton").removeClass("d-none");
    $("#editButton").addClass("d-none");
    $("#orderListForm input").prop("disabled", false);
    $("#orderListForm select").prop("disabled", false);

    if (!orderId) {
        $("#name").val("");
        $("#tanggalMasuk").val("");
        $("#tanggalSelesai").val("");
        $("#jenisJahitanId").val("");
        $("#kuantitas").val("");
        $("#harga").val("");
        $("#keterangan").val("");
        $("#statusPengerjaan").val("");
        $("#total").val("");
        $("#statusPembayaran").val("");
        $("#statusPemesanan").val("");
    }
}
let tabel = null;
let orderId = null;
let statusBayar = "";

$(document).ready(() => {
    getAllOrders();
    $("#pembayaran").addClass("d-none");
});

function getAllOrders() {
    tabel = $("#statusTable").DataTable({
        //   responsive: true,
        ajax: {
            url: "/status/get-orders",
            dataSrc: "",
        },
        order: [
            [1, "desc"]
        ],
        columns: [
            { data: "noOrder" },
            { data: "tanggalMasuk" },
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
                    if (
                        orders.pembayaran.statusPembayaran === "BATAL" ||
                        orders.statusPesanan === "BATAL"
                    ) {
                        return `<p>
                        Pesanan Dibatalkan
                     </p>`;
                    } else if (orders.statusPesanan === "MENUNGGU_APPROVAL") {
                        return `<span class="badge bg-warning" style="color:black;font-size: 14px;">${orders.statusPesanan}</span>`;
                    } else if (orders.statusPesanan === "DALAM_PROSES") {
                        return `
                        <span class="badge bg-warning" style="color:black;font-size: 14px; margin-left:15px;">${orders.statusPesanan}</span>
                        <div class="progress">
                        <div class="progress-bar" style="width: ${orders.progress}%;color:black;">${orders.progress}%</div>
                        </div>
                        `;
                    } else if (orders.statusPesanan === "BELUM_DIKERJAKAN") {
                        return `<span class="badge bg-primary" style="color:black;font-size: 14px;">${orders.statusPesanan}</span>`;
                    } else {
                        if (orders.progress === 100) {
                            return `<div class="progress mt-2">
                            <div class="progress-bar" style="width: ${orders.progress}%;color:black">Selesai</div>
                     </div>`;
                        } else {
                            return `<div class="progress mt-2">
                            <div class="progress-bar" style="width: ${orders.progress}%;color:black;">${orders.progress}%</div>
                     </div>`;
                        }
                    }
                },
            },
            {
                sortable: false,
                render: function(i, row, orders) {
                    var html = `<button class="btn btn-sm btn-info mr-1 mb-2" id ="detail" onclick='onShowUpdate(${JSON.stringify(
                  orders
               )})'>Detail</button>`;
                    if (
                        orders.pembayaran.statusPembayaran === "SUDAH_DIBAYAR" ||
                        orders.pembayaran.statusPembayaran === "BAYAR_DITEMPAT"
                    ) {
                        html += `<a class="btn btn-sm btn-primary mr-1 mb-2" id="downloadNota" href="http://localhost:8083/api/order/export-pdf/${orders.noOrder}">
                        <span><i class="fa-solid fa-down"></i>Nota</span></a>`;
                    } else if (
                        orders.pembayaran.statusPembayaran === "BELUM_DIBAYAR" &&
                        orders.statusPesanan === "BELUM_DIKERJAKAN"
                    ) {
                        html += `<button class="btn btn-sm btn-success mr-1 mb-2" id="bayarVirtual" 
                           onclick='createPayment(${JSON.stringify(
                              orders
                           )})'>Bayar Virtual</button>
                           <button class="btn btn-sm btn-warning mr-1 mb-2" id="bayarDitempat" 
                           onclick='onUpdateBayar(${JSON.stringify(
                              orders
                           )},"BAYAR_DITEMPAT")'>Bayar Ditempat</button>
                           <button class="btn btn-sm btn-danger mr-1 mb-2" data-toggle="modal" data-target="#batalModal" 
                           >Batal</button>
                           
                        <div class="modal fade" id="batalModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="exampleModalLabel">Batalkan Pesanan?</h5>
                                        <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">×</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">Select "Batal" below if you are want to cancel your order.</div>
                                    <div class="modal-footer">
                                        <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                                        <button class="btn btn-danger" id="batal"  onclick='onUpdateBayar(${JSON.stringify(
                                           orders
                                        )},"BATAL")'>Batal</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        `;
                    }
                    return html;
                },
            },
        ],
    });
}

//function getDetail(order) {
//   $("#detailModal").modal("show");
//
//   $("#orderNo").val(order.noOrder);
//   $("#nama").val(order.user.nama);
//   $.each(orders.jenisJahitanOrder, function (i, jenisJahitanOrder) {
//
//   }
//   $('#kuantitas').val(order.jenisJahitanOrder.kuantitas);
//   $('#keterangan').val(order.jenisJahitanOrder.keterangan)
//
//   $("#countryForm input").prop("disabled", true);
//   $("#countryForm select").prop("disabled", true);
//
//   $("#countrySubmit").addClass("d-none");
//   $("#editButton").removeClass("d-none");
//   this.orderId = order.id;
//}

function createPayment(orders) {
    const csrfToken = $("meta[name='_csrf']").attr("content");
    const checkoutOrder = {
        orderId: orders.noOrder,
        orderAmount: orders.pembayaran.totalBiaya,
    };
    console.log(checkoutOrder);
    $.ajax({
        url: "/status",
        type: "POST",
        headers: {
            "X-CSRF-TOKEN": csrfToken,
        },
        contentType: "application/json",
        data: JSON.stringify(checkoutOrder),
        success: (res) => {
            console.log(res);

            window.snap.pay(res, {
                onSuccess: function(result) {
                    /* You may add your own implementation here */
                    alert("payment success!");
                    console.log(result);
                },
                onPending: function(result) {
                    /* You may add your own implementation here */
                    alert("wating your payment!");
                    console.log(result);
                },
                onError: function(result) {
                    /* You may add your own implementation here */
                    alert("payment failed!");
                    console.log(result);
                },
                onClose: function() {
                    /* You may add your own implementation here */
                    alert("you closed the popup without finishing the payment");
                },
            });
            tabel.ajax.reload();
        },
        error: (err) => {
            console.log(err);
        },
    });
}

function onUpdateBayar(orders, parameter) {
    let statusBayarData = {
        jenisJahitanId: orders.pembayaran.totalBiaya,
        orderId: orders.id,
        statusBayar: parameter,
    };
    console.log(parameter);

    $.ajax({
        url: "/payment/status-pembayaran",
        type: "PUT",
        contentType: "application/json",
        headers: {
            "X-CSRF-TOKEN": _csrf_token,
        },
        data: JSON.stringify(statusBayarData),
        success: (res) => {
            toast().fire({
                icon: res.status,
                title: res.message,
            });
            // tabel.ajax.reload();

            $(document).ready(function() {
                setTimeout(function() {
                    location.reload(true);
                }, 1500);
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
    let listHarga = [];
    let jenisJahitan = getAllJenis();
    let html = "";
    let status = null;
    $("#orderListModal").modal("show");
    $("#nomerOrder").text(orders.noOrder);
    $("#status").text(orders.statusPesanan);
    $("#name").val(orders.user.nama);
    $("#tanggalMasuk").val(orders.tanggalMasuk);
    $("#tanggalSelesai").val(orders.tanggalSelesai);

    getStatusPesanan(orders.statusPesanan);
    getStatusPembayaran(orders.pembayaran.statusPembayaran);
    $.each(orders.jenisJahitanOrder, function(i, jenisJahitanOrder) {
        html += `
            <tr>
                <td>
                    <select class="form-select form-control" id="jenisJahitanId" name="jenisJahitanId" disabled>
                        <option disabled selected value="${jenisJahitanOrder.jenisJahitan.id}">
                        ${jenisJahitanOrder.jenisJahitan.nama}</option>`;
        listHarga.push(jenisJahitanOrder.harga * jenisJahitanOrder.kuantitas);
        $.each(jenisJahitan, function(i, data) {
            if (jenisJahitanOrder.jenisJahitan.nama != data.nama) {
                console.log(jenisJahitanOrder.jenisJahitan.nama);
                html += `
                              <option value="${data.id}">
                              ${data.nama}</option>;
                             `;
            }
        });
        html += `
                    </select>
                </td>
                <td><textarea class="form-control" id="keterangan" rows="2" disabled>
                    ${jenisJahitanOrder.keterangan}
                </textarea></td>
                <td id="kuantitas" disabled>${jenisJahitanOrder.kuantitas}</td>
                <td id="harga" disabled>${jenisJahitanOrder.harga}</td>
                <td>
                    <div class="form-check">`;
        if (jenisJahitanOrder.status == true) {
            return (html += `<input class="form-check-input" disabled type="checkbox" checked
                                    name="statusPengerjaan" id="statusPengerjaan">
                                </div>
                    </td>
            </tr>
                                    `);
        } else {
            return (html += `<input class="form-check-input" disabled type="checkbox" 
                                    name="statusPengerjaan" id="statusPengerjaan">
                                      </div>
                    </td>
            </tr
                                    `);
        }
    });
    $("#orderModalTable tbody").html(html);
    console.log(jenisJahitan);

    let totalHarga = listHarga.reduce(getSum, 0);
    console.log(totalHarga);
    $("#total").val(totalHarga);
}

function getSum(total, num) {
    return total + Math.round(num);
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
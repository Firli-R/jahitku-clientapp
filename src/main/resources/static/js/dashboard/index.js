let tabel = null;

$(document).ready(() => {
   getRecentOrders();
   getUnpaidOrders();
   getStatus();
});

function getRecentOrders() {
   tabel = $("#recentTable").DataTable({
      ajax: {
         url: "/order/recent",
         dataSrc: "",
      },
      columns: [
         { data: "noOrder" },
         {
            render: function (i, row, orders) {
               return orders.user.nama;
            },
         },
         { data: "tanggalMasuk" },
         {
            render: function (i, row, orders) {
               let jenisJahitan = [];
               $.each(orders.jenisJahitanOrder, function (indexInArray, data) {
                  jenisJahitan.push(data.jenisJahitan.nama);
               });

               //    console.log(jenisJahitan);
               return jenisJahitan;
            },
         },
         { data: "statusPesanan" },
      ],
   });
}
function getUnpaidOrders() {
   tabel = $("#unpaidTable").DataTable({
      ajax: {
         url: "/order/unpaid",
         dataSrc: "",
      },
      columns: [
         { data: "noOrder" },
         {
            render: function (i, row, orders) {
               return orders.user.nama;
            },
         },
         { data: "tanggalMasuk" },
         {
            render: function (i, row, orders) {
               let jenisJahitan = [];
               $.each(orders.jenisJahitanOrder, function (indexInArray, data) {
                  jenisJahitan.push(data.jenisJahitan.nama);
               });

               //    console.log(jenisJahitan);
               return jenisJahitan;
            },
         },
         {
            render: function (i, row, orders) {
               return orders.pembayaran.statusPembayaran;
            },
         },
      ],
   });
}

function getStatus() {
   $.ajax({
      type: "GET",
      url: "/order/count",
      contentType: "application/json",
      success: function (response) {
         $("#dalamProses").text(response.Dalam_Proses);
         $("#butuhApproval").text(response.Butuh_Approval);
         $("#belumDibayar").text(response.Belum_Dibayar);
         $("#selesai").text(response.Selesai);
         $("#belumDikerjakan").text(response.Belum_Dikerjakan);
         $("#batal").text(response.Batal);
      },
   });
}

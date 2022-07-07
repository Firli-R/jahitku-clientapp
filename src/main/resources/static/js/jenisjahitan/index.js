let jenisJahitanId = null;

let table = null;
$(document).ready(
    () => {
        getAllJenisJahitan();
        onSubmitJenisJahitanForm();
    });

function getAllJenisJahitan() {
    table = $("#jenisJahitanTable").DataTable({
       ajax: {
         url: "/jenisjahitan/get-all",
         dataSrc: "",
      },
      columns: [
         { data: "nama" },
         {
            sortable: false,
            render: function (i, row, jenisJahitan) {
               return `<button class="btn btn-sm btn-info me-2" 
                            onclick='getJenisJahitanDetail(${JSON.stringify(
                               jenisJahitan
                            )})'>Detail</button>

                            <button class="btn btn-sm btn-warning me-2"
                            onclick='onShowUpdate(${JSON.stringify(
                               jenisJahitan
                            )})'>Update</button>

                            <button onclick="onDeleteJenisJahitan(${
                               jenisJahitan.id
                            })" type="submit" class="btn btn-sm btn-danger">Delete</button>`;
            },
         },
      ],

    });
}

function onSubmitJenisJahitanForm() {
   $("#jenisJahitanForm").on("submit", (e) => {
      e.preventDefault();

      const jenisJahitan = {
         nama: $("#name").val(),
      };
      if (this.jenisJahitanId) {
         onUpdateJenisJahitan(jenisJahitan);
      } else {
         onCreateJenisJahitan(jenisJahitan);
      }
   });
}


function onCreateJenisJahitan(jenisJahitan) {
   $.ajax({
      url: "/jenisjahitan/create",
      type: "POST",
      contentType: "application/json",
      headers: {
         "X-CSRF-TOKEN": _csrf_token,
      },
      data: JSON.stringify(jenisJahitan),
      success: function (response) {
         toast().fire({
            icon: response.status,
            title: response.message,
         });

         $("#jenisJahitanModal").modal("hide");
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

function onUpdateJenisJahitan(jenisJahitan) {
   $.ajax({
      url: "/jenisjahitan/update/"+ this.jenisJahitanId,
      type: "PUT",
      contentType: "application/json",
      headers: {
         "X-CSRF-TOKEN": _csrf_token,
      },
      data: JSON.stringify(jenisJahitan),
      success: function (response) {
         toast().fire({
            icon: response.status,
            title: response.message,
         });

         $("#jenisJahitanModal").modal("hide");
         table.ajax.reload();
      },
      error: (err) => {
         toast().fire({
            icon: "error",
            title: "update failed ",
         });
      },
   });
}

function onShowUpdate(Object) {
   $("#jenisJahitanModal").modal("show");
   $("#name").val(Object.nama);
   setJenisJahitanId(Object.id);
}
function setJenisJahitanId(jenisJahitanId) {
   this.jenisJahitanId = jenisJahitanId;
   $("#submitButton").removeClass("d-none");
   $("#editButton").addClass("d-none");
   $("#jenisJahitanForm input").prop("disabled", false);

   if (!jenisJahitanId) {
      $("#name").val("");
   }
}

function onDeleteJenisJahitan(id) {
   Swal.fire({
      title: "Are you sure?",
      text: "do you want to delete this data?",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Delete",
   }).then((result) => {
      if (result.isConfirmed) {
         $.ajax({
            url: "/jenisjahitan/delete/"+ id,
            headers: {
               "X-CSRF-TOKEN": _csrf_token,
            },
            type: "DELETE",
            success: (res) => {
               toast().fire({
                  icon: res.status,
                  title: res.message,
               });
               table.ajax.reload();
            },
            error: (err) => {
               toast().fire({
                  icon: "error",
                  title: err.message,
               });
            },
         });
      }
   });
}

function getJenisJahitanDetail(jenisJahitan) {
   $("#jenisJahitanModal").modal("show");
   $("#name").val(jenisJahitan.nama);

   $("#jenisJahitanForm input").prop("disabled", true);
   $("#submitButton").addClass("d-none");
   $("#editButton").removeClass("d-none");
   this.jenisJahitanId = jenisJahitan.id;
   $("#editButton").click(function (e) {
      e.preventDefault();
      $("#jenisJahitanForm input").prop("disabled", false);
      $("#editButton").addClass("d-none");
      $("#submitButton").removeClass("d-none");
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


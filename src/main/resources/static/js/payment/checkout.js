/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const _csrf_token = $("meta[name='_csrf']").attr('content');
function createPayment() {
    $.ajax({
        url: '/payment',
        type: 'POST',
        headers: {
            'X-CSRF-TOKEN': _csrf_token
        },
        contentType: 'application/json',
        data: JSON.stringify(checkoutOrder),
        success: (res) => {
            console.log(res);

            window.snap.pay(res, {
                onSuccess: function (result) {
                    /* You may add your own implementation here */
                    alert("payment success!");
                    console.log(result);
                },
                onPending: function (result) {
                    /* You may add your own implementation here */
                    alert("wating your payment!");
                    console.log(result);
                },
                onError: function (result) {
                    /* You may add your own implementation here */
                    alert("payment failed!");
                    console.log(result);
                },
                onClose: function () {
                    /* You may add your own implementation here */
                    alert('you closed the popup without finishing the payment');
                }
            })
        },
        error: (err) => {
            console.log(err)
        }
    });
}

const checkoutOrder = {
    orderId: "JK-2022-0005",
    orderAmount: 35000
};

//            $.ajax({
//                type: 'POST',
//                url: '/payment/pembayaran',
//                contentType: 'application/json',
//                data: JSON.stringify(checkoutOrder),
//                success: (res) => {
//                    console.log(res);
//                }
//            })

function getAllOrder(){
    $.ajax
}



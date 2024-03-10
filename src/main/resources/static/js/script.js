console.log("This is script file")

const toggleSidebar = () => {
	if ($('.sidebar').is(":visible")) {
		//true
		//than close it
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");


	} else {
		//false
		//than show it
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};

const search = () => {
	//console.log("Called searching...");

	let query = $("#search-input").val();
	console.log(query);

	if (query == '') {
		$(".search-result").hide();

	}
	else {
		//search
		console.log(query);

		//sending request to server

		let url = `http://localhost:1991/search/${query}`;

		fetch(url).then((response) => {
			return response.json();

		}).then(data => {
			//data....
			//console.log(data);

			// let text=`<div class='list-group'>`
			// data.forEach((contact)=>{
			//     text+='<a href="#" class="list-group-item list-group-action">${contact.name}</a>'

			// });


			// text+='</div>'

			let text = `<div class='list-group'>`;
			data.forEach((contact) => {
				text += `<a href="/user/${contact.cId}/contact/" class='list-group-item list-group-item-action'>${contact.name}</a>`;
			});
			text += `</div>`;


			$(".search-result").html(text);
			$(".search-result").show();
		});


		$(".search-result").show();
	}
}

//first request to server to create order

const paymentStart = () => {
	console.log("Payment started..")
	let amount = $("#paymentId").val();
	console.log(amount);
	if (amount == "" || amount == null) {
		//alert("amount is required !!");
		swal("Failed!!", "amount is required!!", "error");		
		return;
	}

	//code..
	//we will use ajax to send request to server-jquery


	
	fetch("/user/create_order", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({ amount: amount, info: "order_request" }),
	})
		.then(response => response.json())
		.then(response => {
			console.log(response);
			if (response.status == 'created') {
				//open payment form

				// Your Razorpay-related code here

				var options = {
					key: "rzp_test_eYHbxIlEE9T9Ut",
					amount: response.amount,
					currency: "INR",
					name: "Smart Contact Manager",
					description: "Donation",
					image: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjhMD99sWi4fBhEtVwEt6k7ccjtTkXtHD7_w&usqp=CAU",
					order_id: response.id,
					handler: function(response) {
						console.log(response.razorpay_payment_id)
						console.log(response.razorpay_order_id)
						console.log(response.razorpay_signature);
						console.log("payment successful !!")
						//alert("congrate !! Payment successful !!")


						updatePaymentOnServer(
							response.razorpay_payment_id,
							response.razorpay_order_id,
							"paid"
							);

					},
					"prefill": { //We recommend using the prefill parameter to auto-fill customer's contact information especially their phone number
						"name": "", //your customer's name
						"email": "",
						"contact": "" //Provide the customer's phone number for better conversion rates 
					},

					"notes": {
						"address": "Sonali Code in java.."
					},

					"theme": {
						"color": "#3399cc"
					},

				};
 

				var rzp1 = new Razorpay(options);

				rzp1.on('payment.failed', function(response) {
					console.log(response.error.code);
					console.log(response.error.description);
					console.log(response.error.source);
					console.log(response.error.step);
					console.log(response.error.reason);
					console.log(response.error.metadata.order_id);
					console.log(response.error.metadata.payment_id);
					//alert("Opps prevent failed !!")
						

							});
                rzp1.open();
			}
		})
		.catch(error => {
			console.log(error);
			alert("Something went wrong!!");
		});

}

function updatePaymentOnServer(payment_id,order_id,status)
{
	fetch("/user/update_order", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify({ payment_id: payment_id, order_id:order_id,status:status }),
		success:function(response){

			swal("Good job!", "congrate !! Payment successful !!", "success");
		},
		error:function(error){
			swal("Failed!!", "Your payment is successful,but we did not get not server,we will contact you as soon as possible !!", "error");	
		}
	})

}




//code...

//  jQuery 








//new code..

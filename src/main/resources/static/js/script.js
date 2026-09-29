$(function(){

// User Register validation

	var $userRegister=$("#userRegister");

	$userRegister.validate({
		
		rules:{
			name:{
				required:true,
				lettersonly:true
			}
			,
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNumber: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			password: {
				required: true,
				space: true

			},
			confirmpassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			},
			address: {
				required: true,
				all: true

			},

			city: {
				required: true,
				space: true

			},
			state: {
				required: true,


			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true

			}, img: {
				required: true,
			}
			
		},
		messages:{
			name:{
				required:'Vui lòng nhập họ và tên',
				lettersonly:'Tên chứa ký tự không hợp lệ'
			},
			email: {
				required: 'Vui lòng nhập email',
				space: 'Không được chứa khoảng trắng',
				email: 'Email không đúng định dạng'
			},
			mobileNumber: {
				required: 'Vui lòng nhập số điện thoại',
				space: 'Không được chứa khoảng trắng',
				numericOnly: 'Số điện thoại không hợp lệ',
				minlength: 'Số điện thoại phải có ít nhất 10 số',
				maxlength: 'Số điện thoại không quá 12 số'
			},

			password: {
				required: 'Vui lòng nhập mật khẩu',
				space: 'Không được chứa khoảng trắng'

			},
			confirmpassword: {
				required: 'Vui lòng xác nhận mật khẩu',
				space: 'Không được chứa khoảng trắng',
				equalTo: 'Mật khẩu xác nhận không trùng khớp'

			},
			address: {
				required: 'Vui lòng nhập địa chỉ',
				all: 'invalid'

			},

			city: {
				required: 'Vui lòng nhập Tỉnh / Thành phố',
				space: 'Không được chứa khoảng trắng'

			},
			state: {
				required: 'Vui lòng nhập Phường / Xã',
				space: 'Không được chứa khoảng trắng'

			},
			pincode: {
				required: 'Vui lòng nhập mã bưu chính',
				space: 'Không được chứa khoảng trắng',
				numericOnly: 'Mã bưu chính không hợp lệ'

			},
			img: {
				required: 'Vui lòng chọn ảnh đại diện',
			}
		}
	})
	
	
// Orders Validation

var $orders=$("#orders");

$orders.validate({
		rules:{
			fullName:{
				required:true,
				lettersonly:true
			},
			email: {
				required: true,
				space: true,
				email: true
			},
			phone: {
				required: true,
				space: true,
				numericOnly: true,
				minlength: 10,
				maxlength: 12

			},
			detailAddress: {
				required: true,
				all: true

			},

			province: {
				required: true,
				space: true

			},
			ward: {
				required: true,


			},
			paymentType:{
			required: true
			}
		},
		messages:{
			fullName:{
				required:'Vui lòng nhập họ và tên',
				lettersonly:'Tên chứa ký tự không hợp lệ'
			},
			email: {
				required: 'Vui lòng nhập email',
				space: 'Không được chứa khoảng trắng',
				email: 'Email không đúng định dạng'
			},
			phone: {
				required: 'Vui lòng nhập số điện thoại',
				space: 'Không được chứa khoảng trắng',
				numericOnly: 'Số điện thoại không hợp lệ',
				minlength: 'Số điện thoại phải có ít nhất 10 số',
				maxlength: 'Số điện thoại không quá 12 số'
			}
		   ,
			detailAddress: {
				required: 'Vui lòng nhập địa chỉ',
				all: 'invalid'

			},

			province: {
				required: 'Vui lòng nhập Tỉnh / Thành phố',
				space: 'Không được chứa khoảng trắng'

			},
			ward: {
				required: 'Vui lòng nhập Phường / Xã',
				space: 'Không được chứa khoảng trắng'

			},
			paymentType:{
			required: 'Vui lòng chọn hình thức thanh toán'
			}
		}	
})

// Reset Password Validation

var $resetPassword=$("#resetPassword");

$resetPassword.validate({
		
		rules:{
			password: {
				required: true,
				space: true

			},
			confirmPassword: {
				required: true,
				space: true,
				equalTo: '#pass'

			}
		},
		messages:{
		   password: {
				required: 'Vui lòng nhập mật khẩu',
				space: 'Không được chứa khoảng trắng'

			},
			confirmPassword: {
				required: 'Vui lòng xác nhận mật khẩu',
				space: 'Không được chứa khoảng trắng',
				equalTo: 'Mật khẩu xác nhận không trùng khớp'

			}
		}	
})

})

jQuery.validator.addMethod('lettersonly', function(value, element) {
		return /^[a-zA-ZÀ-ỹà-ỹĐđ\s]+$/.test(value);
	});
	
		jQuery.validator.addMethod('space', function(value, element) {
		return /^[^-\s]+$/.test(value);
	});

	jQuery.validator.addMethod('all', function(value, element) {
		return /^[a-zA-Z0-9À-ỹà-ỹĐđ\s,.\/()-]+$/.test(value);
	});


	jQuery.validator.addMethod('numericOnly', function(value, element) {
		return /^[0-9]+$/.test(value);
	});

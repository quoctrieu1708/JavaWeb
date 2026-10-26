$(function() {

	// Helper: Lock submit button on valid form submission to prevent double-submit
	function lockSubmitButton(form) {
		var $btn = $(form).find('button[type="submit"]');
		if ($btn.length && !$btn.prop('disabled')) {
			$btn.prop('disabled', true);
			$btn.html('<i class="fa-solid fa-circle-notch fa-spin me-2"></i>Đang xử lý...');
		}
		form.submit();
	}

	// 1. User Register validation
	var $userRegister = $("#userRegister");
	if ($userRegister.length) {
		$userRegister.validate({
			rules: {
				name: {
					required: true,
					lettersonly: true
				},
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
					all: true
				},
				state: {
					required: true,
					all: true
				},
				pincode: {
					required: true,
					space: true,
					numericOnly: true
				},
				img: {
					required: true
				}
			},
			messages: {
				name: {
					required: 'Vui lòng nhập họ và tên',
					lettersonly: 'Tên chứa ký tự không hợp lệ'
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
					all: 'Địa chỉ chứa ký tự không hợp lệ'
				},
				city: {
					required: 'Vui lòng nhập Tỉnh / Thành phố',
					all: 'Tên Tỉnh / Thành phố không hợp lệ'
				},
				state: {
					required: 'Vui lòng nhập Phường / Xã',
					all: 'Tên Phường / Xã không hợp lệ'
				},
				pincode: {
					required: 'Vui lòng nhập mã bưu chính',
					space: 'Không được chứa khoảng trắng',
					numericOnly: 'Mã bưu chính không hợp lệ'
				},
				img: {
					required: 'Vui lòng chọn ảnh đại diện'
				}
			},
			submitHandler: function(form) {
				lockSubmitButton(form);
			}
		});
	}

	// 2. Orders Validation
	var $orders = $("#orders");
	if ($orders.length) {
		$orders.validate({
			rules: {
				fullName: {
					required: true,
					lettersonly: true
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
					all: true
				},
				ward: {
					required: true,
					all: true
				},
				paymentType: {
					required: true
				}
			},
			messages: {
				fullName: {
					required: 'Vui lòng nhập họ và tên',
					lettersonly: 'Tên chứa ký tự không hợp lệ'
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
				},
				detailAddress: {
					required: 'Vui lòng nhập địa chỉ nhận hàng',
					all: 'Địa chỉ chứa ký tự không hợp lệ'
				},
				province: {
					required: 'Vui lòng nhập Tỉnh / Thành phố',
					all: 'Tên Tỉnh / Thành phố không hợp lệ'
				},
				ward: {
					required: 'Vui lòng nhập Phường / Xã',
					all: 'Tên Phường / Xã không hợp lệ'
				},
				paymentType: {
					required: 'Vui lòng chọn hình thức thanh toán'
				}
			},
			submitHandler: function(form) {
				lockSubmitButton(form);
			}
		});
	}

	// 3. Reset Password Validation
	var $resetPassword = $("#resetPassword");
	if ($resetPassword.length) {
		$resetPassword.validate({
			rules: {
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
			messages: {
				password: {
					required: 'Vui lòng nhập mật khẩu',
					space: 'Không được chứa khoảng trắng'
				},
				confirmPassword: {
					required: 'Vui lòng xác nhận mật khẩu',
					space: 'Không được chứa khoảng trắng',
					equalTo: 'Mật khẩu xác nhận không trùng khớp'
				}
			},
			submitHandler: function(form) {
				lockSubmitButton(form);
			}
		});
	}

});

jQuery.validator.addMethod('lettersonly', function(value, element) {
	return this.optional(element) || /^[a-zA-ZÀ-ỹà-ỹĐđ\s]+$/.test(value.trim());
});

jQuery.validator.addMethod('space', function(value, element) {
	return this.optional(element) || /^[^-\s]+$/.test(value);
});

jQuery.validator.addMethod('all', function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9À-ỹà-ỹĐđ\s,./()-]+$/.test(value.trim());
});

jQuery.validator.addMethod('numericOnly', function(value, element) {
	return this.optional(element) || /^[0-9]+$/.test(value);
});

/* ==========================================================================
   Global UI, Live Search Autocomplete & 1-on-1 Live Chat
   ========================================================================== */
var searchDebounceTimer = null;
var stompClient = null;
var chatConnected = false;
var customerId = '';
var customerName = '';

function escapeHtml(text) {
	if (!text) return '';
	return String(text)
		.replace(/&/g, "&amp;")
		.replace(/</g, "&lt;")
		.replace(/>/g, "&gt;")
		.replace(/"/g, "&quot;")
		.replace(/'/g, "&#039;");
}

function handleLiveSearch(input, dropdownId) {
	clearTimeout(searchDebounceTimer);
	var query = input.value.trim();
	var dropdown = document.getElementById(dropdownId);
	if (!dropdown) return;

	if (query.length < 1) {
		dropdown.innerHTML = '';
		dropdown.classList.add('d-none');
		return;
	}

	searchDebounceTimer = setTimeout(function() {
		fetch('/api/products/search?q=' + encodeURIComponent(query))
			.then(function(res) { return res.json(); })
			.then(function(data) {
				renderSearchResults(data, query, dropdown);
			})
			.catch(function(err) {
				console.error("Live search failed:", err);
			});
	}, 200);
}

function renderSearchResults(items, query, dropdown) {
	if (!items || items.length === 0) {
		dropdown.innerHTML = '<div class="p-3 text-center text-muted small"><i class="fa-solid fa-magnifying-glass me-1 opacity-50"></i>Không tìm thấy sản phẩm cho "<strong>' + escapeHtml(query) + '</strong>"</div>';
		dropdown.classList.remove('d-none');
		return;
	}

	var html = '';
	items.forEach(function(item) {
		var priceFormatted = Number(item.discountPrice || item.price).toLocaleString('vi-VN') + 'đ';
		var oldPriceFormatted = item.discount > 0 ? (Number(item.price).toLocaleString('vi-VN') + 'đ') : '';
		var discountBadge = item.discount > 0 ? '<span class="badge bg-danger rounded-pill ms-1">-' + item.discount + '%</span>' : '';
		var stockNotice = item.stock <= 0 ? '<span class="badge bg-secondary ms-1">Hết hàng</span>' : '';

		html += '<a href="/product/' + item.id + '" class="search-result-item">'
			+ '<div class="search-result-thumb"><img src="/img/product_img/' + (item.image || 'default.png') + '" alt="' + escapeHtml(item.title) + '" loading="lazy"></div>'
			+ '<div class="search-result-info">'
			+ '<div class="search-result-title" title="' + escapeHtml(item.title) + '">' + escapeHtml(item.title) + '</div>'
			+ '<div class="search-result-meta">'
			+ '<span class="badge bg-light text-secondary border small">' + escapeHtml(item.category || 'Sản phẩm') + '</span>'
			+ '<span class="search-result-price">' + priceFormatted + '</span>'
			+ (oldPriceFormatted ? '<span class="text-decoration-line-through text-muted small">' + oldPriceFormatted + '</span>' : '')
			+ discountBadge
			+ stockNotice
			+ '</div>'
			+ '</div>'
			+ '<i class="fa-solid fa-chevron-right text-muted small ms-auto"></i>'
			+ '</a>';
	});

	html += '<a href="/products?ch=' + encodeURIComponent(query) + '" class="search-result-footer">'
		+ 'Xem tất cả ' + items.length + '+ kết quả cho "<strong>' + escapeHtml(query) + '</strong>" <i class="fa-solid fa-arrow-right ms-1"></i>'
		+ '</a>';

	dropdown.innerHTML = html;
	dropdown.classList.remove('d-none');
}

function toggleChatBox() {
	var box = document.getElementById('chat-box');
	if (!box) return;
	box.classList.toggle('d-none');
	if (!box.classList.contains('d-none')) {
		if (!chatConnected) {
			connectChat();
		} else {
			loadChatHistory();
		}
	}
}

function connectChat() {
	if (typeof SockJS === 'undefined' || typeof Stomp === 'undefined') return;
	var socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);
	stompClient.debug = null;
	stompClient.connect({}, function() {
		chatConnected = true;
		stompClient.subscribe('/topic/chat/' + customerId, function(message) {
			var msg = JSON.parse(message.body);
			showMessage(msg);
		});
		loadChatHistory();
	}, function(err) {
		console.error("Chat connection failed:", err);
	});
}

function loadChatHistory() {
	fetch('/api/chat/history?customerId=' + encodeURIComponent(customerId))
		.then(function(res) { return res.json(); })
		.then(function(messages) {
			var container = document.getElementById('chat-messages');
			if (!container) return;
			container.innerHTML = '<div class="text-center text-muted small my-2">Trò chuyện trực tiếp với CSKH</div>';
			if (messages && messages.length > 0) {
				messages.forEach(function(m) {
					renderMessage(m.role === 'USER' || m.sender === customerName, m.sender, m.role, m.content, m.formattedTime);
				});
			} else {
				var welcomeDiv = document.createElement('div');
				welcomeDiv.className = 'd-flex mb-2';
				welcomeDiv.innerHTML = '<div class="p-2 rounded bg-white shadow-sm border" style="max-width: 85%;">'
					+ '<small class="fw-bold text-warning d-block">Hỗ trợ viên CSKH</small>'
					+ '<span>Xin chào ' + escapeHtml(customerName) + '! Chúng tôi có thể hỗ trợ gì cho bạn hôm nay?</span></div>';
				container.appendChild(welcomeDiv);
			}
			container.scrollTop = container.scrollHeight;
		})
		.catch(function(err) {
			console.error("Failed to fetch chat history:", err);
		});
}

function sendChatMessage(event) {
	if (event) event.preventDefault();
	var input = document.getElementById('chat-input');
	if (!input) return;
	var messageContent = input.value.trim();
	if (messageContent && stompClient) {
		var chatMessage = {
			customerId: customerId,
			customerName: customerName,
			sender: customerName,
			content: messageContent,
			type: 'CHAT',
			role: 'USER'
		};
		stompClient.send("/app/chat.sendToAdmin", {}, JSON.stringify(chatMessage));
		input.value = '';
	}
}

function showMessage(msg) {
	if (msg.type === 'JOIN' || msg.type === 'LEAVE') return;
	var isMe = (msg.role === 'USER' || msg.sender === customerName);
	renderMessage(isMe, msg.sender, msg.role, msg.content, msg.timestamp);
}

function renderMessage(isMe, sender, role, content, time) {
	var container = document.getElementById('chat-messages');
	if (!container) return;
	var wrapper = document.createElement('div');
	wrapper.className = 'd-flex mb-2 ' + (isMe ? 'justify-content-end' : 'justify-content-start');

	var bubble = document.createElement('div');
	bubble.className = 'p-2 rounded-3 shadow-sm ' + (isMe ? 'bg-dark text-white border border-secondary' : 'bg-white border text-dark');
	bubble.style.maxWidth = '85%';

	var nameTag = document.createElement('small');
	nameTag.className = 'd-block fw-bold ' + (isMe ? 'text-warning' : 'text-primary');
	nameTag.innerText = (isMe ? 'Bạn' : 'Hỗ trợ viên CSKH') + (time ? (' • ' + time) : '');

	var contentTag = document.createElement('span');
	contentTag.innerText = content;

	bubble.appendChild(nameTag);
	bubble.appendChild(contentTag);
	wrapper.appendChild(bubble);
	container.appendChild(wrapper);
	container.scrollTop = container.scrollHeight;
}

document.addEventListener('DOMContentLoaded', function() {
	// 1. Initialize Bootstrap Toasts
	if (typeof bootstrap !== 'undefined' && bootstrap.Toast) {
		var toastElList = [].slice.call(document.querySelectorAll('.toast'));
		toastElList.forEach(function(toastEl) {
			var t = new bootstrap.Toast(toastEl, { autohide: true, delay: 3500 });
			t.show();
		});
	}

	// 2. Navbar scroll effect
	window.addEventListener('scroll', function() {
		var nav = document.getElementById('mainNavbar');
		if (!nav) return;
		if (window.scrollY > 40) {
			nav.style.boxShadow = '0 12px 35px rgba(0, 0, 0, 0.45)';
			nav.style.borderBottomColor = 'rgba(212, 175, 55, 0.55)';
		} else {
			nav.style.boxShadow = '0 10px 30px rgba(0, 0, 0, 0.25)';
			nav.style.borderBottomColor = 'rgba(212, 175, 55, 0.28)';
		}
	});

	// 3. Initialize customer identity for 1-on-1 Live Chat from data-* attributes
	var chatWidget = document.getElementById('chat-widget');
	if (chatWidget) {
		var loggedInEmail = chatWidget.getAttribute('data-user-email') || '';
		var loggedInName = chatWidget.getAttribute('data-user-name') || '';

		customerId = loggedInEmail;
		if (!customerId) {
			customerId = localStorage.getItem('ecom_guest_chat_id');
			if (!customerId) {
				customerId = 'guest_' + Math.random().toString(36).substring(2, 9);
				localStorage.setItem('ecom_guest_chat_id', customerId);
			}
		}
		customerName = loggedInName ? loggedInName : ('Khách (' + customerId.substring(customerId.length - 4) + ')');
	}

	// 4. Bind Chat UI events (CSP-safe, no inline handlers required)
	var chatToggleBtn = document.getElementById('chat-toggle-btn');
	if (chatToggleBtn) {
		chatToggleBtn.addEventListener('click', toggleChatBox);
	}
	var chatCloseBtn = document.getElementById('chat-close-btn');
	if (chatCloseBtn) {
		chatCloseBtn.addEventListener('click', toggleChatBox);
	}
	var chatForm = document.getElementById('chat-form');
	if (chatForm) {
		chatForm.addEventListener('submit', sendChatMessage);
	}

	// 5. Bind Live Search inputs via data-search-target attribute
	document.querySelectorAll('[data-search-target]').forEach(function(input) {
		var targetId = input.getAttribute('data-search-target');
		input.addEventListener('input', function() {
			handleLiveSearch(input, targetId);
		});
	});

	// 6. Bind Newsletter form
	var newsletterForm = document.getElementById('newsletter-form');
	if (newsletterForm) {
		newsletterForm.addEventListener('submit', function(e) {
			e.preventDefault();
			alert('Cảm ơn bạn đã đăng ký nhận bản tin ưu đãi từ E-COMMERCE!');
			newsletterForm.reset();
		});
	}

	// 7. Close search dropdown when clicking outside or pressing Escape
	document.addEventListener('click', function(e) {
		document.querySelectorAll('.nav-search-bar').forEach(function(container) {
			if (!container.contains(e.target)) {
				var dd = container.querySelector('.search-autocomplete-dropdown');
				if (dd) dd.classList.add('d-none');
			}
		});
	});

	document.addEventListener('keydown', function(e) {
		if (e.key === 'Escape') {
			document.querySelectorAll('.search-autocomplete-dropdown').forEach(function(dd) {
				dd.classList.add('d-none');
			});
		}
	});
});

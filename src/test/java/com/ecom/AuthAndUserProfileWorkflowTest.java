package com.ecom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.model.UserDtls;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;

/**
 * WORKFLOW TEST NHÓM 1: Xác thực, Bảo mật, Khóa tài khoản & Hồ sơ người dùng.
 * Thay thế thao tác thủ công cho các kịch bản AUTH-01 -> AUTH-06 và PRF-01 -> PRF-03.
 */
@SpringBootTest
@Transactional
class AuthAndUserProfileWorkflowTest {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private UserDtls createSampleUser(String emailPrefix) {
		UserDtls user = new UserDtls();
		user.setName("Nguyễn Văn Kiểm Thử");
		user.setEmail(emailPrefix + "_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com");
		user.setMobileNumber("0912345678");
		user.setAddress("Số 10 Đường Láng");
		user.setCity("Hà Nội"); // Có khoảng trắng tiếng Việt
		user.setState("Quận Đống Đa"); // Có khoảng trắng tiếng Việt
		user.setPincode("100000");
		user.setPassword("MatKhau123");
		user.setProfileImage("default.jpg");
		return user;
	}

	@Test
	@DisplayName("AUTH-01 & AUTH-02: Đăng ký tài khoản mới (hỗ trợ tiếng Việt có khoảng trắng), mã hóa BCrypt và kiểm tra trùng Email")
	void testRegisterUserAndDuplicateEmailCheck() {
		UserDtls rawUser = createSampleUser("auth01");
		String email = rawUser.getEmail();

		assertFalse(userService.existsEmail(email), "Email chưa từng đăng ký phải trả về false");

		UserDtls saved = userService.saveUser(rawUser);
		assertNotNull(saved.getId(), "User sau khi lưu phải có ID");
		assertEquals("ROLE_USER", saved.getRole(), "Vai trò mặc định phải là ROLE_USER");
		assertTrue(saved.getIsEnable(), "Tài khoản mới phải được kích hoạt");
		assertTrue(saved.getAccountNonLocked(), "Tài khoản mới không bị khóa");
		assertEquals("Hà Nội", saved.getCity(), "Phải lưu chính xác tên Tỉnh/Thành phố có khoảng trắng");
		assertEquals("Quận Đống Đa", saved.getState(), "Phải lưu chính xác tên Quận/Huyện có khoảng trắng");
		assertTrue(passwordEncoder.matches("MatKhau123", saved.getPassword()), "Mật khẩu phải được băm bằng BCrypt");

		assertTrue(userService.existsEmail(email), "Kiểm tra trùng email phải trả về true sau khi đăng ký");
	}

	@Test
	@DisplayName("AUTH-05: Tự động tăng số lần nhập sai và khóa tài khoản sau 3 lần thất bại")
	void testAccountLockAfterFailedAttempts() {
		UserDtls saved = userService.saveUser(createSampleUser("auth05"));

		for (int i = 0; i < AppConstant.ATTEMPT_TIME; i++) {
			userService.increaseFailedAttempt(saved);
		}

		UserDtls reloaded = userService.getUserByEmail(saved.getEmail());
		assertEquals((int) AppConstant.ATTEMPT_TIME, reloaded.getFailedAttempt(), "Số lần sai phải bằng ATTEMPT_TIME (3)");

		userService.userAccountLock(reloaded);
		UserDtls lockedUser = userService.getUserByEmail(saved.getEmail());
		assertFalse(lockedUser.getAccountNonLocked(), "Tài khoản phải chuyển sang trạng thái bị khóa (accountNonLocked = false)");
		assertNotNull(lockedUser.getLockTime(), "Phải ghi nhận thời điểm khóa tài khoản (lockTime)");
	}

	@Test
	@DisplayName("AUTH-06: Quên mật khẩu - Khởi tạo Reset Token, tìm người dùng qua Token và đặt lại mật khẩu mới")
	void testForgotAndResetPasswordTokenFlow() {
		UserDtls saved = userService.saveUser(createSampleUser("auth06"));
		String resetToken = UUID.randomUUID().toString();

		userService.updateUserResetToken(saved.getEmail(), resetToken);

		UserDtls userByToken = userService.getUserByToken(resetToken);
		assertNotNull(userByToken, "Phải tìm thấy người dùng thông qua resetToken");
		assertEquals(saved.getId(), userByToken.getId());

		// Đặt lại mật khẩu mới và xóa token
		userByToken.setPassword(passwordEncoder.encode("NewSecret789"));
		userByToken.setResetToken(null);
		userService.updateUser(userByToken);

		UserDtls updated = userService.getUserByEmail(saved.getEmail());
		assertNull(updated.getResetToken(), "Reset token phải được xóa về null sau khi sử dụng");
		assertTrue(passwordEncoder.matches("NewSecret789", updated.getPassword()), "Mật khẩu mới phải có hiệu lực");
	}

	@Test
	@DisplayName("PRF-01 & ADM-06: Cập nhật hồ sơ người dùng, tạo Admin mới và khóa/mở khóa tài khoản bởi Admin")
	void testUpdateProfileAndAdminStatusToggle() {
		UserDtls user = userService.saveUser(createSampleUser("prf01"));

		// Cập nhật thông tin hồ sơ
		user.setName("Trần Thị Cập Nhật");
		user.setMobileNumber("0988776655");
		user.setCity("TP Hồ Chí Minh");
		user.setState("Phường Bến Nghé");
		UserDtls updatedProfile = userService.updateUserProfile(user, null);

		assertEquals("Trần Thị Cập Nhật", updatedProfile.getName());
		assertEquals("TP Hồ Chí Minh", updatedProfile.getCity());
		assertEquals("Phường Bến Nghé", updatedProfile.getState());

		// Admin khóa tài khoản (isEnable = false)
		Boolean lockedResult = userService.updateAccountStatus(user.getId(), false);
		assertTrue(lockedResult);
		assertFalse(userService.getUserByEmail(user.getEmail()).getIsEnable(), "Tài khoản phải bị vô hiệu hóa");

		// Admin mở khóa lại tài khoản (isEnable = true)
		Boolean unlockedResult = userService.updateAccountStatus(user.getId(), true);
		assertTrue(unlockedResult);
		assertTrue(userService.getUserByEmail(user.getEmail()).getIsEnable(), "Tài khoản phải được kích hoạt lại");

		// Tạo tài khoản Admin
		UserDtls admin = createSampleUser("admin01");
		UserDtls savedAdmin = userService.saveAdmin(admin);
		assertEquals("ROLE_ADMIN", savedAdmin.getRole(), "Tài khoản quản trị phải có ROLE_ADMIN");
	}
}

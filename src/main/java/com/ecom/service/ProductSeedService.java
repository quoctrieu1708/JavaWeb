package com.ecom.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;

@Service
public class ProductSeedService {

	@Autowired
	private ProductRepository productRepository;

	/**
	 * Tạo danh sách đúng 100 sản phẩm mẫu chia đều cho 8 danh mục Figma 2.0:
	 * - Điện thoại: 13
	 * - Laptop: 13
	 * - Màn hình: 12
	 * - Gia dụng: 13
	 * - Thời trang: 13
	 * - Làm đẹp: 12
	 * - Sách: 12
	 * - Thực phẩm: 12
	 * Tổng = 100 sản phẩm.
	 */
	public List<Product> generate100Products() {
		List<Product> list = new ArrayList<>(100);

		// ==================== 1. ĐIỆN THOẠI (13 sản phẩm) ====================
		list.add(createP("iPhone 15 Pro Max 256GB Titan Tự Nhiên",
				"iPhone 15 Pro Max thiết kế khung viền titan siêu bền, chip Apple A17 Pro cực mạnh, camera tiềm vọng zoom quang học 5x sắc nét, cổng Type-C tốc độ cao.",
				"Điện thoại", 31990000.0, 5, 25, "iphone 14.jpg", 4.9, 142));
		list.add(createP("Samsung Galaxy S24 Ultra 5G 12GB/256GB",
				"Galaxy S24 Ultra trang bị vi xử lý Snapdragon 8 Gen 3 for Galaxy, tích hợp quyền năng Galaxy AI phiên dịch trực tiếp, bút S-Pen đa năng, camera 200MP zoom 100x.",
				"Điện thoại", 27490000.0, 8, 30, "mobile.jpg", 4.8, 98));
		list.add(createP("Xiaomi 14 Ultra 5G Hệ thống ống kính Leica",
				"Xiaomi 14 Ultra sở hữu cụm 4 camera Leica 50MP cảm biến 1 inch, khẩu độ thay đổi vô cấp f/1.63-f/4.0, sạc nhanh 90W HyperCharge và kháng nước IP68.",
				"Điện thoại", 29990000.0, 10, 15, "mobile.jpg", 4.8, 45));
		list.add(createP("OPPO Find X7 Ultra 5G Camera kép kính tiềm vọng",
				"OPPO Find X7 Ultra màn hình 2K ProXDR 120Hz, chip Snapdragon 8 Gen 3, hợp tác cùng Hasselblad mang lại chất lượng nhiếp ảnh chân dung đỉnh cao.",
				"Điện thoại", 22990000.0, 6, 18, "mobile.jpg", 4.7, 36));
		list.add(createP("OnePlus 12 5G Snapdragon 8 Gen 3 16GB/512GB",
				"OnePlus 12 trang bị màn hình 2K siêu sáng 4500 nits, tản nhiệt buồng hơi kép VC khổng lồ, sạc có dây 100W SuperVOOC sạc đầy trong 26 phút.",
				"Điện thoại", 17990000.0, 10, 20, "oneplus mobile.jpg", 4.8, 52));
		list.add(createP("Google Pixel 8 Pro 128GB AI Camera thuần Google",
				"Google Pixel 8 Pro dùng chip Tensor G3 tối ưu cho các thuật toán AI chụp ảnh Best Take, Magic Editor và cập nhật hệ điều hành Android thuần 7 năm.",
				"Điện thoại", 19490000.0, 12, 14, "mobile.jpg", 4.7, 40));
		list.add(createP("ASUS ROG Phone 8 Pro 16GB Gaming Phone chuyên nghiệp",
				"Điện thoại gaming tối thượng với màn hình AMOLED 165Hz, nút bấm siêu âm AirTrigger, tản nhiệt AeroActive Cooler và pin trâu 5500mAh.",
				"Điện thoại", 24990000.0, 5, 12, "mobile.jpg", 4.9, 31));
		list.add(createP("iPhone 13 128GB Chính Hãng Apple (VN/A)",
				"iPhone 13 giữ trọn giá trị với chip A15 Bionic bền bỉ, màn hình Super Retina XDR sắc nét, chế độ quay phim Điện ảnh Cinematic Mode chuyên nghiệp.",
				"Điện thoại", 13490000.0, 10, 35, "iphone 14.jpg", 4.9, 210));
		list.add(createP("Samsung Galaxy Z Flip 5 5G 256GB Màn hình ngoài Flex Window",
				"Điện thoại gập thời trang với màn hình phụ 3.4 inch hiển thị thông minh, bản lề Flex không kẽ hở, chống nước IPX8.",
				"Điện thoại", 15990000.0, 15, 22, "mobile.jpg", 4.6, 75));
		list.add(createP("Realme GT5 Pro 5G Snapdragon 8 Gen 3",
				"Màn hình 1.5K 144Hz 4500 nits, camera tiềm vọng Sony IMX890, pin 5400mAh kèm sạc nhanh 100W mang lại hiệu năng/giá thành vô đối.",
				"Điện thoại", 12990000.0, 8, 28, "mobile.jpg", 4.7, 63));
		list.add(createP("Xiaomi Redmi Note 13 Pro+ 5G Camera 200MP OIS",
				"Màn hình cong AMOLED 1.5K 120Hz, camera 200MP chống rung quang học OIS, sạc siêu tốc 120W HyperCharge đầy pin trong 19 phút.",
				"Điện thoại", 8990000.0, 12, 50, "mobile.jpg", 4.8, 180));
		list.add(createP("Vivo X100 Pro 5G Ống kính Zeiss T*",
				"Camera Zeiss APO Telephoto chuẩn điện ảnh, chip Dimensity 9300 tối tân, chip xử lý hình ảnh Vivo V3 chuyên biệt.",
				"Điện thoại", 18990000.0, 7, 16, "mobile.jpg", 4.8, 44));
		list.add(createP("Samsung Galaxy A55 5G 8GB/128GB Viền nhôm nguyên khối",
				"Thiết kế khung viền kim loại sang trọng, vi xử lý Exynos 1480 đồ họa AMD, màn hình Super AMOLED 120Hz mượt mà và bảo mật Knox Vault.",
				"Điện thoại", 8490000.0, 10, 40, "mobile.jpg", 4.7, 115));

		// ==================== 2. LAPTOP (13 sản phẩm) ====================
		list.add(createP("Apple MacBook Pro 14 inch M3 Pro 18GB/512GB Space Black",
				"MacBook Pro 14 M3 Pro trang bị CPU 11 nhân, GPU 14 nhân, màn hình Liquid Retina XDR độ sáng 1600 nits, thời lượng pin ấn tượng lên đến 18 giờ liên tục.",
				"Laptop", 49990000.0, 4, 15, "laptop.jpg", 4.9, 87));
		list.add(createP("Apple MacBook Air 13 inch M2 8GB/256GB Midnight",
				"MacBook Air M2 siêu mỏng nhẹ 1.24kg, màn hình Liquid Retina 13.6 inch viền mỏng, sạc MagSafe 3 an toàn, thiết kế unibody hoàn hảo.",
				"Laptop", 24490000.0, 6, 25, "laptop.jpg", 4.9, 160));
		list.add(createP("Dell XPS 15 9530 Core i7-13700H RTX 4050 OLED 3.5K",
				"Laptop doanh nhân cao cấp khung nhôm nguyên khối, chiếu nghỉ tay sợi carbon, màn hình cảm ứng OLED 3.5K chuẩn 100% DCI-P3.",
				"Laptop", 46990000.0, 7, 10, "laptop.jpg", 4.8, 38));
		list.add(createP("Lenovo ThinkPad X1 Carbon Gen 11 Core i7-1365U 16GB",
				"Biểu tượng laptop doanh nghiệp bền bỉ chuẩn quân đội MIL-STD-810H, trọng lượng siêu nhẹ 1.12kg, bàn phím gõ êm ái hàng đầu thế giới.",
				"Laptop", 41990000.0, 8, 12, "laptop.jpg", 4.8, 29));
		list.add(createP("ASUS ZenBook 14 OLED UX3405 Intel Core Ultra 7 155H",
				"Laptop AI thời thượng mỏng 14.9mm, màn hình ASUS Lumina OLED 3K 120Hz, pin dung lượng lớn 75Wh cho trải nghiệm làm việc cả ngày dài.",
				"Laptop", 28990000.0, 10, 20, "laptop.jpg", 4.7, 54));
		list.add(createP("HP Envy x360 2-in-1 14 inch Core i7 OLED Cảm ứng",
				"Laptop xoay gập 360 độ linh hoạt kèm bút cảm ứng, màn hình OLED 2.8K chân thực, camera 5MP có nút gạt vật lý bảo mật riêng tư.",
				"Laptop", 25490000.0, 12, 18, "hp laptop.jpg", 4.7, 43));
		list.add(createP("Acer Swift Go 14 Core Ultra 5 OLED 100% sRGB",
				"Laptop văn phòng thế hệ mới tích hợp NPU Intel AI Boost, màn hình OLED 2.8K 90Hz rực rỡ, webcam 1440p khử nhiễu AI thông minh.",
				"Laptop", 19990000.0, 9, 22, "laptop.jpg", 4.6, 61));
		list.add(createP("MSI Stealth 16 Studio Core i7-13700H RTX 4070 32GB",
				"Laptop gaming kiêm đồ họa mỏng nhẹ chứng nhận NVIDIA Studio, màn hình QHD+ 240Hz, hệ thống 6 loa Dynaudio sống động.",
				"Laptop", 52990000.0, 5, 8, "laptop.jpg", 4.9, 19));
		list.add(createP("LG Gram 17 2024 Siêu nhẹ 1.35kg Core Ultra 7 32GB",
				"Màn hình lớn 17 inch WQXGA IPS chống chói nhưng trọng lượng chỉ 1.35kg, pin 80Wh cực trâu, độ bền chuẩn quân sự Hoa Kỳ.",
				"Laptop", 38990000.0, 11, 14, "laptop.jpg", 4.8, 35));
		list.add(createP("Lenovo Legion 5 Pro Gaming Ryzen 7 RTX 4060",
				"Laptop gaming quốc dân màn hình 16 inch 2.5K 240Hz 500 nits, hệ thống tản nhiệt Legion Coldfront 5.0 làm mát vượt trội.",
				"Laptop", 33490000.0, 10, 16, "laptop.jpg", 4.8, 77));
		list.add(createP("ASUS ROG Zephyrus G14 OLED AMD Ryzen 9 RTX 4060",
				"Tuyệt tác laptop gaming 14 inch mỏng nhẹ khung nhôm CNC, màn hình ROG Nebula OLED 3K 120Hz 0.2ms, dải đèn Slash Lighting độc đáo.",
				"Laptop", 45990000.0, 6, 10, "laptop.jpg", 4.9, 48));
		list.add(createP("HP Pavilion 15 Core i5 16GB SSD 512GB Bàn phím số",
				"Laptop học tập và làm việc văn phòng đa dụng, màn hình 15.6 inch Full HD IPS chống chói, bàn phím fullsize nhập liệu kế toán tiện lợi.",
				"Laptop", 15490000.0, 8, 30, "hp laptop.jpg", 4.7, 105));
		list.add(createP("Dell Inspiron 14 5430 Core i5-1335U 16GB SSD 512GB",
				"Thiết kế thanh lịch màu bạc sang trọng, tỉ lệ màn hình 16:10 hiển thị nhiều nội dung hơn, âm thanh Waves MaxxAudio Pro chân thực.",
				"Laptop", 17990000.0, 10, 25, "laptop.jpg", 4.6, 68));

		// ==================== 3. MÀN HÌNH (12 sản phẩm) ====================
		list.add(createP("Màn hình đồ họa Dell UltraSharp U2724D 2K 120Hz IPS Black",
				"Tấm nền IPS Black nâng độ tương phản lên 2000:1, tần số quét mượt mà 120Hz, độ phủ màu 98% DCI-P3 kèm chứng nhận bảo vệ mắt TÜV Rheinland.",
				"Màn hình", 11490000.0, 8, 20, "monitor.jpg", 4.9, 85));
		list.add(createP("Màn hình cong Samsung Odyssey OLED G9 49 inch 240Hz",
				"Màn hình tỷ lệ siêu rộng 32:9 độ phân giải Dual QHD, tấm nền OLED thời gian phản hồi 0.03ms, công nghệ Neo Quantum Processor Pro.",
				"Màn hình", 34990000.0, 10, 6, "monitor.jpg", 4.9, 24));
		list.add(createP("Màn hình chuyên đồ họa ASUS ProArt PA278CV 27 inch 2K sRGB",
				"Màn hình cân màu sẵn từ nhà máy Calman Verified đạt Delta E < 2, chuẩn 100% sRGB và 100% Rec. 709, hỗ trợ cổng USB-C truyền dữ liệu và sạc 65W.",
				"Màn hình", 8990000.0, 7, 24, "monitor.jpg", 4.8, 62));
		list.add(createP("Màn hình gaming LG UltraGear 27GR95QE-B 27 inch OLED 240Hz",
				"Màn hình gaming OLED độ phân giải 2K, tần số quét 240Hz, thời gian đáp ứng siêu nhanh 0.03ms GtG, hỗ trợ NVIDIA G-SYNC Compatible.",
				"Màn hình", 19490000.0, 12, 15, "monitor.jpg", 4.9, 41));
		list.add(createP("Màn hình BenQ DesignVue PD2705U 27 inch 4K Type-C",
				"Độ phân giải 4K UHD sắc nét, tính năng KVM Switch chuyển đổi mượt mà giữa 2 máy tính, bàn phím điều khiển Hotkey Puck G2 tiện ích.",
				"Màn hình", 14290000.0, 5, 12, "monitor.jpg", 4.8, 30));
		list.add(createP("Màn hình Gigabyte M28U 28 inch 4K 144Hz SS-IPS KVM",
				"Màn hình chuẩn cổng HDMI 2.1 chơi game console 4K 120Hz mượt mà, tần số quét 144Hz trên PC, màu sắc 8-bit chân thực.",
				"Màn hình", 12990000.0, 10, 18, "monitor.jpg", 4.7, 53));
		list.add(createP("Màn hình Dell S2722QC 27 inch 4K UHD Type-C 65W",
				"Thiết kế viền siêu mỏng 3 cạnh tinh tế, loa tích hợp kép 3W, chân đế nâng hạ xoay dọc công thái học và kết nối 1 cáp USB-C tiện dụng.",
				"Màn hình", 8790000.0, 9, 25, "monitor.jpg", 4.7, 88));
		list.add(createP("Màn hình gaming AOC 24G2SP 24 inch IPS 165Hz",
				"Màn hình gaming quốc dân giá tốt, tấm nền IPS 165Hz 1ms MPRT, độ bao phủ màu 125% sRGB, chân đế đa năng nâng hạ xoay linh hoạt.",
				"Màn hình", 3490000.0, 15, 40, "monitor.jpg", 4.8, 195));
		list.add(createP("Màn hình cong Philips Evnia 34M2C8600 QD-OLED 34 inch",
				"Màn hình siêu phẩm cong 1800R chuẩn tỉ lệ 21:9 WQHD, công nghệ chấm lượng tử Quantum Dot OLED mang lại màu đen tuyệt đối và độ sáng rực rỡ.",
				"Màn hình", 26990000.0, 6, 8, "monitor.jpg", 4.9, 15));
		list.add(createP("Màn hình MSI Optix MAG274QRF-QD 27 inch 2K 165Hz",
				"Tấm nền Rapid IPS kết hợp công nghệ Quantum Dot mang lại dải màu 97% DCI-P3 chuẩn điện ảnh, góc nhìn rộng 178 độ sắc nét.",
				"Màn hình", 8590000.0, 10, 22, "monitor.jpg", 4.8, 47));
		list.add(createP("Màn hình Xiaomi Gaming G27i 27 inch Fast IPS 165Hz",
				"Độ phân giải Full HD mượt mà, công nghệ đồng bộ AMD FreeSync Premium chống xé hình, chứng nhận lọc ánh sáng xanh bảo vệ mắt.",
				"Màn hình", 3190000.0, 12, 50, "monitor.jpg", 4.6, 120));
		list.add(createP("Màn hình ViewSonic VG2755-2K 27 inch IPS Ergonomic",
				"Màn hình doanh nghiệp chuyên dụng làm việc văn phòng đa nhiệm, cổng USB-C sạc 60W, thiết kế bao bì thân thiện môi trường.",
				"Màn hình", 7490000.0, 8, 20, "monitor.jpg", 4.7, 34));

		// ==================== 4. GIA DỤNG (13 sản phẩm) ====================
		list.add(createP("Nồi chiên không dầu Philips XXL HD9650 dung tích 7.3L",
				"Công nghệ Twin TurboStar loại bỏ đến 90% dầu mỡ trong thực phẩm, công suất mạnh mẽ 2225W, chiên vừa cả con gà 1.4kg giòn rụm bên ngoài mọng nước bên trong.",
				"Gia dụng", 5890000.0, 15, 35, "appli.png", 4.9, 230));
		list.add(createP("Robot hút bụi lau nhà Dreame L20 Ultra tự giặt sấy giẻ",
				"Lực hút khủng 7000Pa, công nghệ xòe giẻ lau MopExtend sát cạnh mép tường, tự động thêm nước lau sàn và đổ rác rảnh tay đến 75 ngày.",
				"Gia dụng", 18990000.0, 10, 16, "appli.png", 4.9, 78));
		list.add(createP("Máy lọc không khí Xiaomi Smart Air Purifier 4 Pro",
				"Lọc sạch 99.97% bụi mịn PM2.5, phấn hoa và mùi hôi nhờ bộ lọc 3 trong 1 HEPA hiệu suất cao, cảm biến laser kép, diện tích hiệu quả tới 60m².",
				"Gia dụng", 4290000.0, 14, 30, "appli.png", 4.8, 145));
		list.add(createP("Nồi cơm điện cao tần Tiger JKT-D18V 1.8L Nhật Bản",
				"Công nghệ đốt nóng cảm ứng nhiệt IH cao tần đối lưu, lòng nồi 3 lớp phủ men gốm hồng ngoại dày dặn nấu hạt cơm dẻo ngọt tự nhiên.",
				"Gia dụng", 7490000.0, 8, 20, "appli.png", 4.9, 64));
		list.add(createP("Lò vi sóng điện tử có nướng Toshiba ER-SGS34 34L",
				"Dung tích lớn 34 lít khoang nướng rộng rãi, công suất vi sóng 1000W kèm nướng 1100W, 11 mức điều chỉnh nhiệt độ và chế độ rã đông nhanh.",
				"Gia dụng", 3190000.0, 12, 25, "appli.png", 4.7, 52));
		list.add(createP("Máy ép chậm hoa quả Hurom H400 công nghệ ép trục vít",
				"Công nghệ ép tự động thông minh All-in-One không cần cắt nhỏ trái cây, giữ trọn vẹn 100% vitamin và enzyme tự nhiên không bị oxy hóa.",
				"Gia dụng", 9990000.0, 10, 18, "appli.png", 4.9, 89));
		list.add(createP("Máy hút bụi cầm tay không dây Dyson V12 Detect Slim",
				"Tia laser xanh soi rõ bụi mắt thường không thấy, cảm biến Piezo đo kích thước hạt bụi hiển thị thời gian thực trên màn hình LCD.",
				"Gia dụng", 16490000.0, 7, 12, "appli.png", 4.9, 46));
		list.add(createP("Bàn ủi hơi nước đứng Philips ComfortTouch Plus",
				"Đầu ủi FlexHead linh hoạt, công nghệ khóa gập ván ủi tạo nếp gấp sắc sảo, khử mùi diệt khuẩn 99.9% trên mọi chất liệu vải mềm mại.",
				"Gia dụng", 3890000.0, 15, 28, "appli.png", 4.7, 73));
		list.add(createP("Máy rửa bát độc lập Bosch Series 6 SMS6ZCI49E Zeolith",
				"Công nghệ sấy hạt khoáng chất Zeolith khô hoàn hảo đồ nhựa, giàn rửa VarioDrawer 3 tầng chứa 14 bộ đồ ăn châu Âu, độ ồn siêu êm 42dB.",
				"Gia dụng", 26990000.0, 8, 10, "appli.png", 4.9, 39));
		list.add(createP("Tủ lạnh Inverter Hitachi Multi Door 540L R-FW690PGV7",
				"Hệ thống làm lạnh quạt kép Dual Fan Cooling, màng lọc Nano Titanium kháng khuẩn khử mùi, cảm biến nhiệt Eco tiết kiệm điện vượt trội.",
				"Gia dụng", 22490000.0, 9, 8, "fridge.png", 4.8, 42));
		list.add(createP("Máy giặt sấy thông minh LG AI DD Inverter 11kg/7kg",
				"Động cơ truyền động trực tiếp tích hợp trí tuệ nhân tạo AI DD tự nhận biết độ mềm sợi vải, giặt hơi nước diệt khuẩn ngừa dị ứng Steam+.",
				"Gia dụng", 14990000.0, 12, 15, "washing_machine.png", 4.8, 65));
		list.add(createP("Máy xay sinh tố đa năng cối thủy tinh chịu lực 800W",
				"Lưỡi dao thép không gỉ 6 cánh răng cưa sắc bén xay đá viên nhuyễn mịn, 2 tốc độ kèm nút nhồi trộn gia vị tiện dụng cho nhà bếp.",
				"Gia dụng", 790000.0, 18, 45, "grinder.jpg", 4.6, 110));
		list.add(createP("Quạt không cánh kèm lọc không khí Dyson Purifier Hot+Cool",
				"Làm ấm tức thì vào mùa đông và làm mát êm ái vào mùa hè, màng lọc HEPA H13 khử sạch virus và bụi mịn khép kín 360 độ.",
				"Gia dụng", 17990000.0, 6, 14, "appli.png", 4.8, 28));

		// ==================== 5. THỜI TRANG (13 sản phẩm) ====================
		list.add(createP("Áo sơ mi nam Oxford cao cấp dệt 100% cotton",
				"Chất liệu vải Oxford dày dặn mềm mịn, đứng form áo, thấm hút mồ hôi thông thoáng cả ngày, phù hợp mặc công sở và dạo phố.",
				"Thời trang", 350000.0, 20, 100, "blue shirt.jfif", 4.8, 310));
		list.add(createP("Quần jeans nam basic xanh đậm co giãn 4 chiều",
				"Form dáng Slimfit trẻ trung, chất vải denim dày dặn không bai dão sau nhiều lần giặt, đường may khóa kéo đồng cao cấp.",
				"Thời trang", 420000.0, 15, 80, "jeans blue.jfif", 4.7, 245));
		list.add(createP("Giày thể thao Sneaker Classic trắng đế êm",
				"Chất liệu da vi sợi kháng nước dễ vệ sinh, lót giày cao su non êm chân giảm chấn động khi di chuyển, phong cách phối đồ linh hoạt.",
				"Thời trang", 650000.0, 25, 60, "white shoe.jfif", 4.9, 180));
		list.add(createP("Áo thun nam Polo pique dệt tổ ong thoáng khí",
				"Chất liệu cotton pha sợi spandex co giãn tốt, bo cổ dệt dặn dặn không quăn mép, gam màu trung tính nam tính lịch lãm.",
				"Thời trang", 290000.0, 15, 90, "blue shirt.jfif", 4.7, 160));
		list.add(createP("Quần âu tây nam công sở dáng Slimfit cao cấp",
				"Vải chéo Hàn Quốc chống nhăn tuyệt đối, cạp quần tăng đơ co giãn tiện lợi, tôn dáng đôi chân thon gọn khi đi giày âu.",
				"Thời trang", 480000.0, 10, 75, "jeans blue.jfif", 4.8, 135));
		list.add(createP("Áo khoác Bomber gió 2 lớp cản nước chống gió",
				"Lớp ngoài vải tráng PU chống thấm mưa phùn, lớp lót nỉ giữ ấm cơ thể mùa đông, khóa kéo YKK mượt mà.",
				"Thời trang", 550000.0, 18, 50, "blue shirt.jfif", 4.7, 95));
		list.add(createP("Giày lười Loafer nam da bò dập vân tinh tế",
				"Chất liệu da bò thật 100% đánh bóng thủ công, đế cao su chống trượt tạo độ bám tối đa, phong cách smart casual sang trọng.",
				"Thời trang", 890000.0, 20, 40, "lofer.jfif", 4.8, 70));
		list.add(createP("Áo len nam dệt kim cổ tròn thu đông ấm áp",
				"Sợi len nhân tạo cao cấp không gây ngứa dặm, bề mặt len mịn màng giữ nhiệt hoàn hảo, không phai màu khi giặt máy.",
				"Thời trang", 380000.0, 12, 65, "blue shirt.jfif", 4.6, 85));
		list.add(createP("Bộ nỉ thể thao nam phối sọc năng động",
				"Set gồm áo khoác kéo khóa và quần dài bo gấu, chất liệu nỉ da cá co giãn thoải mái cho các hoạt động thể thao ngoài trời.",
				"Thời trang", 520000.0, 15, 45, "blue shirt.jfif", 4.7, 110));
		list.add(createP("Quần short đũi nam thoáng khí mùa hè",
				"Chất liệu đũi tự nhiên siêu mát và mềm nhẹ, cạp chun có dây rút linh hoạt phù hợp đi biển, du lịch dã ngoại mùa hè.",
				"Thời trang", 220000.0, 20, 85, "printed short.jfif", 4.6, 140));
		list.add(createP("Giày da công sở Oxford da bò nhập khẩu",
				"Phom giày chuẩn quý ông công sở, đường chỉ may viền tỉ mỉ tay nghề cao, đế gót gỗ đệm cao su tôn chiều cao 3.5cm.",
				"Thời trang", 1250000.0, 15, 35, "official shoe.jfif", 4.9, 58));
		list.add(createP("Áo blazer nam phong cách Hàn Quốc form rộng",
				"Thiết kế 2 hàng khuy trẻ trung hiện đại, đệm vai đứng phom tôn vóc dáng, phối đẹp cùng áo phông trắng và quần âu.",
				"Thời trang", 790000.0, 10, 40, "blue shirt.jfif", 4.8, 72));
		list.add(createP("Váy đầm nữ xòe thiết kế hoa nhí cổ V nữ tính",
				"Chất liệu vải voan tơ cát mềm rủ thướt tha, lót lụa mềm kín đáo, họa tiết hoa nhí vintage tôn vóc dáng thon thả ngọt ngào.",
				"Thời trang", 450000.0, 15, 55, "kruti.jfif", 4.8, 92));

		// ==================== 6. LÀM ĐẸP (12 sản phẩm) ====================
		list.add(createP("Tinh chất chống lão hóa phục hồi Estée Lauder ANR 50ml",
				"Serum phục hồi ban đêm huyền thoại số 1 thế giới với công nghệ Chronolux Power Signal độc quyền giúp giảm nếp nhăn, thu nhỏ lỗ chân lông và căng bóng làn da.",
				"Làm đẹp", 2850000.0, 10, 30, "beuty.png", 4.9, 175));
		list.add(createP("Kem dưỡng ẩm phục hồi làm dịu da La Roche-Posay B5+ 40ml",
				"Chứa tổ hợp khoáng chất Đồng - Kẽm - Mangan và 5% Panthenol B5 làm dịu tức thì các kích ứng da, phục hồi màng bảo vệ da sau nặn mụn hoặc peel da.",
				"Làm đẹp", 360000.0, 8, 70, "beuty.png", 4.9, 290));
		list.add(createP("Nước tẩy trang dịu nhẹ cho da nhạy cảm Bioderma Hồng 500ml",
				"Công nghệ hạt Micellar tự nhiên tương thích cấu trúc da nhẹ nhàng làm sạch 99% cặn trang điểm và bụi mịn PM2.5 mà không gây khô rát hay cay mắt.",
				"Làm đẹp", 420000.0, 15, 85, "beuty.png", 4.9, 340));
		list.add(createP("Sữa chống nắng kiềm dầu Anessa Perfect UV Milk SPF50+ 60ml",
				"Công nghệ độc quyền Auto Booster giúp lớp màng chống UV trở nên mạnh mẽ hơn khi tiếp xúc với nhiệt độ cao, độ ẩm, mồ hôi và ma sát.",
				"Làm đẹp", 540000.0, 12, 60, "beuty.png", 4.9, 215));
		list.add(createP("Sữa rửa mặt tạo bọt dịu nhẹ Cerave Foaming Cleanser 473ml",
				"Chứa 3 loại Ceramide thiết yếu (1, 3, 6-II) kết hợp Niacinamide và Hyaluronic Acid giúp làm sạch sâu dầu thừa mà không làm tổn thương hàng rào ẩm tự nhiên.",
				"Làm đẹp", 390000.0, 10, 65, "beuty.png", 4.8, 195));
		list.add(createP("Son thỏi lì MAC Powder Kiss Lipstick màu Devoted To Chili",
				"Chất son lì mịn như nhung lướt nhẹ trên môi, hiệu ứng làm mờ rãnh môi ảo diệu, tone màu đỏ gạch tôn da thời thượng.",
				"Làm đẹp", 590000.0, 10, 45, "beuty.png", 4.8, 130));
		list.add(createP("Phấn nước Cushion kiềm dầu che phủ cao Clio Kill Cover",
				"Độ che phủ khuyết điểm hoàn hảo đến 54 giờ, lớp finish bán lì mịn màng tự nhiên không vỡ mốc phấn hay xuống tông cuối ngày.",
				"Làm đẹp", 480000.0, 15, 50, "beuty.png", 4.7, 110));
		list.add(createP("Nước hoa nam cao cấp Dior Sauvage Eau De Parfum 100ml",
				"Hương thơm nam tính phóng khoáng từ cam bergamot Calabria tươi mát hòa quyện vani Papua New Guinea và hổ phách ấm áp đầy mê hoặc.",
				"Làm đẹp", 3450000.0, 5, 25, "beuty.png", 4.9, 90));
		list.add(createP("Tinh chất Vitamin C sáng da mờ thâm Kiehl's Clearly Corrective",
				"Thành phần Activated C tác động nhanh làm sáng đều màu da và mờ thâm mụn rõ rệt chỉ sau 2 tuần sử dụng, không gây kích ứng da.",
				"Làm đẹp", 1950000.0, 8, 35, "beuty.png", 4.8, 85));
		list.add(createP("Mặt nạ ngủ dưỡng môi hương quả mọng Laneige Lip Mask 20g",
				"Công nghệ Moisture Wrap khóa ẩm suốt đêm, chiết xuất hỗn hợp quả mọng Berry Fruit Complex giàu chất chống oxy hóa cho đôi môi căng mọng hồng hào.",
				"Làm đẹp", 380000.0, 12, 80, "beuty.png", 4.9, 260));
		list.add(createP("Dung dịch tẩy tế bào chết hóa học Paula's Choice BHA 2% 118ml",
				"Salicylic Acid 2% thâm nhập sâu vào lỗ chân lông loại bỏ bã nhờn tích tụ, giảm mụn đầu đen và mụn ẩn, cải thiện kết cấu da thô ráp.",
				"Làm đẹp", 850000.0, 15, 40, "beuty.png", 4.8, 140));
		list.add(createP("Bộ gội xả phục hồi tóc hư tổn Moroccanoil Moisture 500ml",
				"Chiết xuất từ tinh dầu Argan quý giá giàu chất chống oxy hóa, keratin tái tạo cấu trúc sợi tóc khô xơ chẻ ngọn trở nên bóng mượt bồng bềnh.",
				"Làm đẹp", 1290000.0, 10, 30, "beuty.png", 4.9, 75));

		// ==================== 7. SÁCH (12 sản phẩm) ====================
		list.add(createP("Đắc Nhân Tâm - Dale Carnegie (Bản dịch kinh điển First News)",
				"Cuốn sách nghệ thuật ứng xử nổi tiếng nhất mọi thời đại, đưa ra những nguyên tắc vàng trong giao tiếp thu phục lòng người và dẫn lối thành công.",
				"Sách", 86000.0, 20, 120, "book.jpg", 4.9, 450));
		list.add(createP("Nhà Giả Kim - Paulo Coelho (Bản bìa mềm tái bản mới nhất)",
				"Hành trình phiêu lưu tìm kiếm kho báu của chàng trai chăn cừu Santiago gửi gắm thông điệp sâu sắc: Khi bạn thật sự khao khát điều gì, cả vũ trụ sẽ hợp lực giúp bạn.",
				"Sách", 79000.0, 15, 110, "book.jpg", 4.9, 390));
		list.add(createP("Tư Duy Nhanh Và Chậm - Daniel Kahneman (Tâm lý học hành vi)",
				"Tác phẩm đoạt giải Nobel kinh tế giải mã hai hệ thống tư duy chi phối mọi quyết định và định kiến vô thức của con người trong cuộc sống.",
				"Sách", 215000.0, 18, 75, "book.jpg", 4.8, 120));
		list.add(createP("Sapiens: Lược Sử Loài Người - Yuval Noah Harari",
				"Cái nhìn toàn cảnh đầy khai sáng về tiến trình lịch sử tiến hóa của loài người từ thời kỳ đồ đá cho đến kỷ nguyên trí tuệ nhân tạo.",
				"Sách", 198000.0, 15, 80, "book.jpg", 4.9, 210));
		list.add(createP("Tâm Lý Học Về Tiền - Morgan Housel (Bí quyết quản trị tài chính)",
				"19 câu chuyện ngắn khai mở góc nhìn chân thực về cách con người suy nghĩ, cảm xúc và đưa ra quyết định liên quan đến tiền bạc và sự giàu có.",
				"Sách", 145000.0, 20, 90, "book.jpg", 4.8, 180));
		list.add(createP("Cây Cam Ngọt Của Tôi - José Mauro de Vasconcelos",
				"Tác phẩm văn học kinh điển lay động hàng triệu trái tim về tuổi thơ của cậu bé Zezé thông minh và tình yêu thương ấm áp chữa lành tâm hồn.",
				"Sách", 92000.0, 15, 105, "book.jpg", 4.9, 310));
		list.add(createP("Clean Code: Nghệ Thuật Viết Code Sạch - Robert C. Martin",
				"Cẩm nang kinh điển dành cho mọi lập trình viên chuyên nghiệp hướng dẫn các nguyên tắc viết mã nguồn trong sáng, dễ đọc và dễ bảo trì.",
				"Sách", 320000.0, 10, 50, "book.jpg", 4.9, 85));
		list.add(createP("Atomic Habits - Thay Đổi Tí Hon Hiệu Quả Bất Ngờ (James Clear)",
				"Hệ thống phương pháp khoa học giúp bạn xây dựng những thói quen tích cực nhỏ bé mỗi ngày để tạo nên bước nhảy vọt vĩ đại trong cuộc đời.",
				"Sách", 169000.0, 20, 95, "book.jpg", 4.9, 280));
		list.add(createP("Tuổi Trẻ Đáng Giá Bao Nhiêu? - Rosie Nguyễn",
				"Cuốn sách truyền cảm hứng sống đẹp, khám phá tiềm năng bản thân và định hướng tương lai dành riêng cho các bạn trẻ Việt Nam.",
				"Sách", 78000.0, 15, 115, "book.jpg", 4.7, 260));
		list.add(createP("Không Gia Đình - Hector Malot (Tiểu thuyết văn học kinh điển)",
				"Hành trình phiêu bạt đầy nghị lực và lòng nhân ái của cậu bé Rémi mồ côi cùng gánh xiếc rong trên khắp các nẻo đường nước Pháp.",
				"Sách", 135000.0, 20, 70, "book.jpg", 4.8, 140));
		list.add(createP("Dạy Con Làm Giàu Tập 1 - Để Không Có Tiền Vẫn Tạo Ra Tiền",
				"Robert Kiyosaki đập tan những ngộ nhận về tiền bạc và mở ra tư duy tài chính độc lập thông qua câu chuyện giữa hai người cha.",
				"Sách", 85000.0, 15, 130, "book.jpg", 4.8, 320));
		list.add(createP("Nghĩ Giàu Và Làm Giàu - Napoleon Hill (Bản dịch đầy đủ)",
				"13 nguyên tắc thành công được đúc kết từ hàng trăm nhân vật giàu có nhất nước Mỹ, kim chỉ nam cho tư duy tự do tài chính.",
				"Sách", 110000.0, 18, 85, "book.jpg", 4.8, 175));

		// ==================== 8. THỰC PHẨM (12 sản phẩm) ====================
		list.add(createP("Hạt Macca Úc sấy nứt vỏ thượng hạng túi zip 500g",
				"Hạt macca nhập khẩu 100% từ Úc được sấy nứt tự nhiên, nhân to tròn đều béo ngậy, giàu axit béo không no Omega-3 và vitamin tốt cho tim mạch.",
				"Thực phẩm", 165000.0, 15, 100, "groccery.jpg", 4.9, 210));
		list.add(createP("Cà phê hạt Arabica Cầu Đất rang mộc nguyên chất 1kg",
				"Hạt cà phê Arabica trồng tại vùng cao Cầu Đất Đà Lạt ở độ cao 1600m, rang mộc chuẩn medium roast hương thơm nồng nàn vị chua thanh hậu ngọt.",
				"Thực phẩm", 290000.0, 10, 80, "groccery.jpg", 4.8, 155));
		list.add(createP("Mật ong hoa rừng nguyên chất Tây Bắc chai thủy tinh 1000ml",
				"Mật ong đa hoa thu hoạch tự nhiên từ vùng núi cao Tây Bắc sánh đặc óng ánh, vị ngọt thanh dịu không lắng đường, bồi bổ sức khỏe mỗi sáng.",
				"Thực phẩm", 220000.0, 12, 60, "groccery.jpg", 4.9, 180));
		list.add(createP("Trà xanh Tân Cương Thái Nguyên đinh ngọc hảo hạng 500g",
				"Búp trà non 1 tôm 1 lá được hái thủ công vào sáng sớm, sao suốt truyền thống giữ trọn hương cốm nồng nàn, nước xanh trong vị tiền chát hậu ngọt sâu.",
				"Thực phẩm", 350000.0, 8, 50, "groccery.jpg", 4.8, 95));
		list.add(createP("Yến mạch cán dẹt nguyên chất hữu cơ Úc túi 1kg",
				"100% yến mạch nguyên cám giàu chất xơ hòa tan beta-glucan giúp kiểm soát cân nặng, giảm cholesterol và hỗ trợ hệ tiêu hóa khỏe mạnh.",
				"Thực phẩm", 115000.0, 15, 90, "groccery.jpg", 4.8, 160));
		list.add(createP("Bột ngũ cốc dinh dưỡng 12 loại hạt siêu mịn hộp 800g",
				"Kết hợp tinh túy từ hạt óc chó, hạnh nhân, yến mạch, hạt sen, đậu đen xanh lòng giúp bổ sung năng lượng sạch cho bữa sáng tiện lợi.",
				"Thực phẩm", 185000.0, 10, 75, "groccery.jpg", 4.7, 130));
		list.add(createP("Dầu ô liu nguyên chất Extra Virgin Địa Trung Hải 500ml",
				"Dầu ô liu ép nguội đầu tiên (Cold Pressed) giữ trọn vẹn chất chống oxy hóa Polyphenol và Vitamin E, hoàn hảo trộn salad và nấu ăn dinh dưỡng.",
				"Thực phẩm", 195000.0, 10, 65, "groccery.jpg", 4.9, 115));
		list.add(createP("Nho khô vàng quả to sấy dẻo tự nhiên Chile hộp 500g",
				"Nho vàng không hạt sấy dẻo giữ ẩm tự nhiên ngọt thanh mọng nước, không thêm đường hay chất bảo quản, món ăn vặt lành mạnh cho cả gia đình.",
				"Thực phẩm", 135000.0, 15, 85, "groccery.jpg", 4.8, 140));
		list.add(createP("Hạt điều rang muối Bình Phước loại A cồi to giòn rụm 500g",
				"Hạt điều còn vỏ lụa rang củi thủ công chuẩn vùng đất đỏ Bazan Bình Phước giòn bùi béo ngậy, vị mặn nhẹ vừa vặn không gắt.",
				"Thực phẩm", 175000.0, 12, 95, "groccery.jpg", 4.9, 220));
		list.add(createP("Trà đen Earl Grey Anh Quốc túi lọc thơm thảo mộc hộp 100 tép",
				"Sự kết hợp tinh tế giữa lá trà đen Ceylon thượng hạng và tinh dầu vỏ cam Bergamot đem lại hương thơm thư thái sảng khoái tinh thần.",
				"Thực phẩm", 180000.0, 10, 70, "groccery.jpg", 4.8, 85));
		list.add(createP("Hạt chia hữu cơ nhập khẩu Nam Mỹ bổ sung Omega-3 túi 500g",
				"Hạt chia Organic giàu chất xơ, canxi và protein thực vật, dễ dàng pha cùng nước cam, sữa chua hoặc nước detox thanh lọc cơ thể.",
				"Thực phẩm", 125000.0, 15, 85, "groccery.jpg", 4.8, 105));
		list.add(createP("Bánh quy yến mạch mè đen dinh dưỡng ít đường hộp 400g",
				"Bánh giòn rụm thơm lừng mùi mè đen và yến mạch nướng, vị ngọt nhẹ từ đường ăn kiêng Isomalt thích hợp cho người ăn kiêng tiểu đường.",
				"Thực phẩm", 95000.0, 10, 110, "groccery.jpg", 4.7, 125));

		return list;
	}

	@Transactional
	public Map<String, Object> seed100Products(boolean reset) {
		List<Product> productsToSeed = generate100Products();
		int inserted = 0;
		int skipped = 0;

		if (reset) {
			// Xóa các sản phẩm đã seed trước đó có cùng tiêu đề
			List<String> titles = productsToSeed.stream().map(Product::getTitle).toList();
			List<Product> existing = productRepository.findAll();
			for (Product ep : existing) {
				if (titles.contains(ep.getTitle())) {
					productRepository.delete(ep);
				}
			}
		}

		for (Product p : productsToSeed) {
			boolean exists = productRepository.findAll().stream()
					.anyMatch(ep -> ep.getTitle() != null && ep.getTitle().equalsIgnoreCase(p.getTitle()));
			if (!exists) {
				productRepository.save(p);
				inserted++;
			} else {
				skipped++;
			}
		}

		Map<String, Long> countByCategory = productRepository.findAll().stream()
				.collect(Collectors.groupingBy(p -> p.getCategory() == null ? "Khác" : p.getCategory(), Collectors.counting()));

		Map<String, Object> result = new LinkedHashMap<>();
		result.put("status", "success");
		result.put("seededProducts", inserted);
		result.put("skippedDuplicates", skipped);
		result.put("totalProductsInDb", productRepository.count());
		result.put("distributionByCategory", countByCategory);
		return result;
	}

	private Product createP(String title, String desc, String cat, Double price, int discount, int stock, String img, Double rating, Integer reviewCount) {
		Product p = new Product();
		p.setTitle(title);
		p.setDescription(desc);
		p.setCategory(cat);
		p.setPrice(price);
		p.setDiscount(discount);
		if (discount > 0) {
			double dp = price - (price * discount / 100.0);
			p.setDiscountPrice(Math.round(dp * 100.0) / 100.0);
		} else {
			p.setDiscountPrice(price);
		}
		p.setStock(stock);
		p.setImage(img);
		p.setIsActive(true);
		p.setRating(rating);
		p.setReviewCount(reviewCount);
		return p;
	}
}

ECOMMERCE PROJECT

Dự án Java Web sử dụng Maven để quản lý phụ thuộc và cấu hình. Dự án này có thể được triển khai trên máy chủ ứng dụng như Apache Tomcat.

Yêu cầu hệ thống

  Java Development Kit (JDK) 8 trở lên
  Apache Maven 3.6 trở lên
  Apache Tomcat 9 trở lên (hoặc máy chủ ứng dụng tương thích)
  Eclipse IDE for Enterprise Java and Web Developers (hoặc IDE tương đương)
  
Hướng dẫn cài đặt 

1. Clone dự án

   git clone https://github.com/quoctrieu1708/JavaWeb.git
   
2. Tạo cơ sở dữ liệu

   create database ecommerce_db

3. Cấu hình kết nối cơ sở dữ liệu.

    spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
  
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  
    spring.datasource.username=root
  
    spring.datasource.password=..............

4. Truy cập website

   Run As -> Spring Boot App 

   Truy cập http://localhost:8080
   

# Camera Pro Max

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B-green.svg)](https://developer.android.com)
[![Shizuku](https://img.shields.io/badge/Shizuku-API%20v23-blueviolet.svg)](https://shizuku.rikka.app)
[![Version](https://img.shields.io/badge/Version-v1.0.0-orange.svg)](https://github.com/huanbv2002/CameraProMax/releases)
[![Author](https://img.shields.io/badge/Author-Huanbv2002-lightgrey.svg)](https://github.com/huanbv2002)

[Tiếng Việt](#tiếng-việt) | [English](#english)

---

## Tiếng Việt

Camera Pro Max là ứng dụng mã nguồn mở (Free & Open Source) giúp tùy chỉnh độ phân giải màn hình, tỉ lệ khung hình (Aspect Ratio) và mật độ điểm ảnh (DPI) trên điện thoại và máy tính bảng Android mà không cần Root máy, thông qua hệ thống Shizuku API.

### 1. Tính Năng Nổi Bật

- Không cần Root: Sử dụng quyền ADB shell an toàn qua Shizuku API, không làm mất bảo hành và không cần can thiệp hệ thống sâu.
- 4 Tỉ Lệ Khung Hình Chuẩn Tối Ưu Sẵn (Fast Presets):
  - Ultra-Wide 21:9 (2560 x 1080 - 240 DPI): Mở rộng góc nhìn ngang tối đa cho game thủ MOBA, Tốc Chiến, Liên Quân Mobile, PUBG.
  - Super-Wide 24:9 (2560 x 960 - 220 DPI): Góc nhìn toàn cảnh siêu rộng cho máy tính bảng.
  - Điện Thoại 19.5:9 (2560 x 1180 - 260 DPI): Tỉ lệ chuẩn smartphone hiện đại.
  - Tablet PC 16:10 (2560 x 1536 - 320 DPI): Tỉ lệ màn hình gốc sắc nét cho máy tính bảng.
- Tùy Chỉnh Tự Do Pixel-by-Pixel:
  - Thanh kéo Slider và hộp nhập số trực tiếp giúp điều chỉnh chính xác từng đơn vị Width, Height và DPI.
- Khôi Phục 1-Chạm (Reset):
  - Nút Khôi Phục Gốc giúp đưa màn hình về độ phân giải mặc định của nhà sản xuất ngay lập tức sau khi chơi game.
- Tự Động Kiểm Tra Cập Nhật (In-App Auto Update):
  - Tích hợp hộp thoại thông báo cập nhật tự động khi có phiên bản mới trên GitHub Releases, cho phép cập nhật ngay hoặc để sau.
- Hỗ Trợ Song Ngữ:
  - Chuyển đổi nhanh giữa Tiếng Việt và Tiếng Anh trực tiếp trên thanh tiêu đề.
- Thiết Kế Tối Giản (Clean Minimalist):
  - Giao diện trắng hiện đại, không quảng cáo rác, không thu thập dữ liệu người dùng.

### 2. Yêu Cầu Hệ Thống

- Android 8.0 (API level 26) trở lên.
- Đã cài đặt ứng dụng Shizuku và khởi chạy thành công qua Gỡ lỗi Wi-Fi (Wireless Debugging) hoặc máy tính.

### 3. Hướng Dẫn Cài Đặt & Kích Hoạt Shizuku

1. Cài đặt ứng dụng Shizuku từ Google Play Store hoặc GitHub của Rikka.
2. Vào Cài đặt > Tùy chọn nhà phát triển (Developer Options) > Bật Gỡ lỗi Wi-Fi (Wireless Debugging).
3. Mở app Shizuku, chọn Ghép nối và Khởi chạy.
4. Mở app Camera Pro Max, cấp quyền Shizuku khi có hộp thoại yêu cầu là có thể bắt đầu sử dụng.

### 4. Hướng Dẫn Build Từ Mã Nguồn

Yêu cầu môi trường: Java JDK 17 trở lên, Android SDK 34.

```bash
# Clone mã nguồn
git clone https://github.com/huanbv2002/CameraProMax.git
cd CameraProMax

# Build APK Debug
./gradlew assembleDebug

# Hoặc trên Windows PowerShell:
gradlew.bat assembleDebug
```
File APK sau khi build: `app/build/outputs/apk/debug/app-debug.apk`

### 5. Cơ Chế Cập Nhật & Quản Lý Link

Ứng dụng đọc cấu hình trực tiếp từ GitHub Pages: `docs/config.json`.  
Khi cần cập nhật link YouTube hướng dẫn, link Donate hoặc số phiên bản mới, tác giả chỉ cần sửa file `docs/config.json` trên GitHub mà không cần build lại app.

### 6. Bản Quyền & Giấy Phép

Phát hành theo giấy phép MIT License.  
Tác giả: Huanbv2002 (https://github.com/huanbv2002)

---

## English

Camera Pro Max is a free and open-source Android utility designed to customize screen resolution, aspect ratio, and display density (DPI) without requiring Root access, powered by the Shizuku API.

### 1. Key Features

- Non-Root Access: Uses ADB shell commands safely through Shizuku API without voiding device warranty.
- 4 Optimized Fast Presets:
  - Ultra-Wide 21:9 (2560 x 1080 - 240 DPI): Maximum horizontal field of view for MOBA and FPS mobile games.
  - Super-Wide 24:9 (2560 x 960 - 220 DPI): Panoramic ultra-wide screen mode for tablets.
  - Smartphone 19.5:9 (2560 x 1180 - 260 DPI): Modern standard smartphone aspect ratio.
  - Tablet PC 16:10 (2560 x 1536 - 320 DPI): Native crisp tablet display ratio.
- Pixel-by-Pixel Customization:
  - Responsive sliders and numeric inputs for precise width, height, and DPI adjustments.
- One-Click Restore:
  - Instantly resets the display to manufacturer default settings after gaming sessions.
- In-App Auto-Update System:
  - Automatically notifies users when a newer release is published on GitHub, with Update Now and Later options.
- Dual Language Support:
  - Seamlessly toggle between Vietnamese and English.
- Clean & Ad-Free Interface:
  - Elegant white aesthetic, zero ads, zero tracking, lightweight performance.

### 2. System Requirements

- Android 8.0 (API level 26) or higher.
- Shizuku app installed and running via Wireless Debugging or computer ADB.

### 3. Shizuku Setup & Usage Guide

1. Install Shizuku from Google Play Store or Rikka GitHub Releases.
2. Go to Settings > Developer Options > Enable Wireless Debugging.
3. Open Shizuku, pair and start the service.
4. Open Camera Pro Max, grant Shizuku permission when prompted, and start customizing your display.

### 4. Build From Source

Prerequisites: Java JDK 17+, Android SDK 34.

```bash
git clone https://github.com/huanbv2002/CameraProMax.git
cd CameraProMax

# Build Debug APK
./gradlew assembleDebug

# Windows Command:
gradlew.bat assembleDebug
```
Output APK file: `app/build/outputs/apk/debug/app-debug.apk`

### 5. Dynamic Configuration & Auto-Update

The application reads configuration from GitHub Pages: `docs/config.json`.  
Authors can update the YouTube tutorial URL, Donate URL, and version number by editing `docs/config.json` directly on GitHub without rebuilding the app.

### 6. License

Released under the MIT License.  
Author: Huanbv2002 (https://github.com/huanbv2002)

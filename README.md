# Camera Pro Max

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B-green.svg)](https://developer.android.com)
[![Shizuku](https://img.shields.io/badge/Shizuku-API%20v23-blueviolet.svg)](https://shizuku.rikka.app)
[![Version](https://img.shields.io/badge/Version-v1.0.0-orange.svg)](https://github.com/huanbv2002/Camera-Pro-Max/releases)
[![Author](https://img.shields.io/badge/Author-Huanbv2002-lightgrey.svg)](https://github.com/huanbv2002)

Camera Pro Max la ung dung ma nguon mo (Free & Open Source) giup tuy chinh do phan giai man hinh, ti le khung hinh (Aspect Ratio) va mat do diem anh (DPI) tren dien thoai va may tinh bang Android ma khong can Root, thong qua he thong Shizuku API.

---

## 1. Tinh Nang Chinh (Key Features)

- Khong can Root: Su dung Shizuku de thuc thi lenh ADB shell an toan, khong lam mat bao hanh thiet bi.
- Ti Le Khung Hinh Tinh San (Fast Presets):
  - Ultra-Wide 21:9 (2560 x 1080): Mo rong tam nhin toi da cho cac tua game MOBA, FPS (Lien Quan Mobile, Toc Chien, PUBG Mobile).
  - Super-Wide 24:9 (2560 x 960): Goc nhin sieu rong ngang.
  - Smartphone 19.5:9 (2560 x 1180): Ti le dien thoai tieu chuan.
  - Tablet PC 16:10 (2560 x 1536): Ti le goc toi uu cho may tinh bang.
- Tuy Chinh Tu Do (Custom Parameters):
  - Thanh keo Slider va o nhap so truc tiep cho phep dieu chinh tung pixel chieu rong (Width), chieu cao (Height) va mat do (DPI).
- Khoi Phuc Goc 1-Cham:
  - Nut Reset All giup dua man hinh tro ve do phan giai mac dinh cua nha san xuat ngay lap tuc.
- Tu Dong Kiem Tra Cap Nhat (In-App GitHub Update Checker):
  - Tu dong thong bao khi co ban cap nhat moi tu GitHub Releases.
  - Nguoi dung co the chon Cap nhat ngay hoac De sau.
- Ho Tro Song Ngu:
  - Chuyen doi nhanh giua Tieng Viet va Tieng Anh.
- Giao Dien Hien Dai, Thuan Khiet:
  - Thiet ke Minimalist trang thanh lich, bo goc hien dai, icon vector Flaticon sac net.

---

## 2. Yeu Cau He Thong (System Requirements)

- Android 8.0 (API level 26) tro len.
- Da cai dat ung dung Shizuku va kich hoat thanh cong qua Wireless Debugging (Go loi khong day) hoac cap quyen qua ADB may tinh mot lan.

---

## 3. Huong Dan Cai Dat & Su Dung

### Buoc 1: Kich hoat Shizuku
1. Cai dat ung dung Shizuku tu Google Play hoac GitHub Releases.
2. Vao Cai dat nha phat trien (Developer Options) tren thiet bi, bat Go loi Wi-Fi (Wireless Debugging).
3. Mo app Shizuku va chon Ghap noi / Khoi chay. Khi Shizuku bao Running la thanh cong.

### Buoc 2: Su dung Camera Pro Max
1. Cai dat file APK Camera Pro Max tu muc Releases.
2. Mo app va cap quyen Shizuku khi co thong bao yeu cau.
3. Chon mot trong cac Che Do Nhanh (Ultra-Wide, Super-Wide...) hoac tu dieu chinh thanh keo theo y muon.
4. Nhan "Ap Dung Thay Doi" de ap dung ngay lap tuc.
5. De quay ve man hinh goc, chi can nhan "Khoi Phuc".

---

## 4. Huong Dan Build Tu Ma Nguon (Build From Source)

### Yeu cau moi truong:
- Java JDK 17 tro len.
- Android SDK Build-Tools 34.0.0.

### Lenh build APK:
```bash
# Clone repository
git clone https://github.com/huanbv2002/Camera-Pro-Max.git
cd Camera-Pro-Max

# Build ban Debug APK
./gradlew assembleDebug

# Hoac tren Windows Command Prompt / PowerShell:
gradlew.bat assembleDebug
```
File APK dau ra:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 5. Co Che Tu Dong Cap Nhat (Auto-Update Mechanism)

Ung dung tich hop san `UpdateManager` ket noi voi GitHub REST API:
- Endpoint: `https://api.github.com/repos/huanbv2002/Camera-Pro-Max/releases/latest`
- Khi ban tao mot Release moi tren GitHub (vi du tag `v1.0.1`) va dinh kem file APK vao phan Assets, ung dung tren may nguoi dung se tu dong phat hien va hien thi Dialog thong bao cap nhat.
- Nguoi dung cung co the cham truc tiep vao so phien ban tren thanh tieu de de kiem tra cap nhat thu cong.

---

## 6. Ban Quyen & Giay Phep (License)

Du an duoc phat hanh theo giay phep MIT License.
Ban co quyen su dung, chinh sua va phan phoi hoan toan mien phi.

Tac gia: Huanbv2002  
GitHub: https://github.com/huanbv2002

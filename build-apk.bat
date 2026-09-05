@echo off
chcp 65001 >nul
title Camera Pro Max - Build APK (By Huanbv2002)

echo =======================================================
echo         CAMERA PRO MAX - BUILD APK SCRIPT
echo                 By Huanbv2002
echo =======================================================
echo.

set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
set "ANDROID_HOME=C:\Users\banhtieu\AppData\Local\Android\Sdk"
set "PATH=%JAVA_HOME%\bin;%ANDROID_HOME%\platform-tools;%PATH%"

echo [*] Kiểm tra môi trường...
echo     - JAVA_HOME: %JAVA_HOME%
echo     - ANDROID_HOME: %ANDROID_HOME%
echo.

echo [*] Đang build APK (Debug)...
call gradlew.bat assembleDebug

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [x] BUILD THẤT BẠI! Vui lòng kiểm tra lỗi ở trên.
    pause
    exit /b 1
)

set "APK_OUTPUT=app\build\outputs\apk\debug\app-debug.apk"

if exist "%APK_OUTPUT%" (
    echo.
    echo =======================================================
    echo [V] BUILD THÀNH CÔNG RỰC RỠ!
    echo     File APK: %CD%\%APK_OUTPUT%
    echo =======================================================
    echo.
    
    echo [*] Kiểm tra thiết bị ADB đang kết nối...
    adb devices
    
    echo.
    set /p INSTALL_CHOICE="Bạn có muốn cài đặt APK trực tiếp vào máy qua ADB luôn không? (Y/N): "
    if /i "%INSTALL_CHOICE%"=="Y" (
        echo [*] Đang cài đặt vào thiết bị...
        adb install -r "%APK_OUTPUT%"
        if %ERRORLEVEL% EQU 0 (
            echo.
            echo [V] ĐÃ CÀI ĐẶT THÀNH CÔNG LÊN THIẾT BỊ!
            echo [*] Khởi động ứng dụng Camera Pro Max...
            adb shell am start -n com.huanbv2002.camerapromax/.SplashActivity
        ) else (
            echo [x] Cài đặt thất bại, hãy chắc chắn thiết bị đã bật USB Debugging.
        )
    )
) else (
    echo [x] Không tìm thấy file APK đầu ra!
)

echo.
echo Hoàn tất! Nhấn phím bất kỳ để thoát.
pause >nul

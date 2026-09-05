package com.huanbv2002.camerapromax.shizuku;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rikka.shizuku.Shizuku;

public class ShizukuManager {
    private static final String TAG = "CameraProMax";

    public interface ExecutionCallback {
        void onSuccess(String output);
        void onError(String error);
    }

    public static boolean isShizukuAvailable() {
        try {
            return Shizuku.pingBinder();
        } catch (Throwable t) {
            return false;
        }
    }

    public static boolean hasShizukuPermission() {
        try {
            if (!isShizukuAvailable()) return false;
            if (Build.VERSION.SDK_INT >= 23) {
                return Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED;
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    public static void requestPermission(int requestCode) {
        try {
            if (isShizukuAvailable() && !hasShizukuPermission()) {
                Shizuku.requestPermission(requestCode);
            }
        } catch (Throwable t) {
            Log.e(TAG, "Failed to request Shizuku permission", t);
        }
    }

    /**
     * Executes a shell command via Shizuku process or fallback
     */
    public static String runShellCommand(String cmd) throws Exception {
        if (!hasShizukuPermission()) {
            throw new IllegalStateException("Chưa cấp quyền Shizuku. Vui lòng cấp quyền trước!");
        }

        java.lang.reflect.Method newProcessMethod = Shizuku.class.getDeclaredMethod("newProcess", String[].class, String[].class, String.class);
        newProcessMethod.setAccessible(true);
        Process process = (Process) newProcessMethod.invoke(null, new String[]{"sh", "-c", cmd}, null, null);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        process.waitFor();
        return output.toString().trim();
    }

    /**
     * Applies screen resolution with Smart Orientation Swap for tablet panels
     */
    public static void applyResolution(int width, int height, int dpi, ExecutionCallback callback) {
        new Thread(() -> {
            try {
                // Check physical size to prevent unexpected orientation flips
                String sizeOut = runShellCommand("wm size");
                int physW = 1536;
                int physH = 2560;

                Pattern pattern = Pattern.compile("Physical size:\\s*(\\d+)x(\\d+)");
                Matcher matcher = pattern.matcher(sizeOut);
                if (matcher.find()) {
                    physW = Integer.parseInt(matcher.group(1));
                    physH = Integer.parseInt(matcher.group(2));
                }

                int targetW = width;
                int targetH = height;

                // Smart auto-swap for portrait-native panels (e.g. 1536x2560)
                if (physW < physH && width > height) {
                    targetW = height;
                    targetH = width;
                } else if (physW > physH && width < height) {
                    targetW = height;
                    targetH = width;
                }

                String cmd = "wm size " + targetW + "x" + targetH;
                if (dpi > 0) {
                    cmd += " && wm density " + dpi;
                }

                String result = runShellCommand(cmd);
                if (callback != null) {
                    callback.onSuccess("Đã cập nhật màn hình: " + width + "x" + height + " (DPI " + dpi + ")");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error applying resolution", e);
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Resets screen resolution and density to hardware defaults
     */
    public static void resetResolution(ExecutionCallback callback) {
        new Thread(() -> {
            try {
                runShellCommand("wm size reset && wm density reset");
                if (callback != null) {
                    callback.onSuccess("Đã khôi phục toàn bộ kích thước về gốc nguyên bản!");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error resetting resolution", e);
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Gets current active screen resolution and density
     */
    public static String[] getCurrentScreenInfo() {
        String[] info = new String[]{"Chưa rõ", "Chưa rõ"};
        try {
            if (!hasShizukuPermission()) return info;
            String sizeOut = runShellCommand("wm size");
            String densityOut = runShellCommand("wm density");

            Pattern pSize = Pattern.compile("(Override size|Physical size):\\s*(\\d+x\\d+)");
            Matcher mSize = pSize.matcher(sizeOut);
            String lastSize = "1536x2560";
            while (mSize.find()) {
                lastSize = mSize.group(2);
            }
            info[0] = lastSize;

            Pattern pDpi = Pattern.compile("(Override density|Physical density):\\s*(\\d+)");
            Matcher mDpi = pDpi.matcher(densityOut);
            String lastDpi = "320";
            while (mDpi.find()) {
                lastDpi = mDpi.group(2);
            }
            info[1] = lastDpi + " DPI";
        } catch (Exception e) {
            Log.e(TAG, "Error reading screen info", e);
        }
        return info;
    }
}

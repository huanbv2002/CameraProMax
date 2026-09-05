package com.huanbv2002.camerapromax;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class UpdateManager {

    public static final String GITHUB_OWNER = "huanbv2002";
    public static final String GITHUB_REPO = "Camera-Pro-Max";
    public static final String GITHUB_REPO_URL = "https://github.com/" + GITHUB_OWNER + "/" + GITHUB_REPO;
    public static final String GITHUB_API_LATEST = "https://api.github.com/repos/" + GITHUB_OWNER + "/" + GITHUB_REPO + "/releases/latest";

    public static final String CURRENT_VERSION = "1.0.0";

    /**
     * Kiem tra ban cap nhat tu GitHub Releases.
     * @param activity Activity goi ham
     * @param isManual True neu nguoi dung chu dong bam nut kiem tra
     */
    public static void checkForUpdates(Activity activity, boolean isManual) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        if (isManual) {
            Toast.makeText(activity, R.string.update_toast_checking, Toast.LENGTH_SHORT).show();
        }

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(GITHUB_API_LATEST);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "CameraProMax-Android");
                conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);

                int code = conn.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    String tagName = json.optString("tag_name", "");
                    String body = json.optString("body", "");
                    String htmlUrl = json.optString("html_url", GITHUB_REPO_URL + "/releases");

                    // Tim file apk download truc tiep neu co
                    String downloadUrl = htmlUrl;
                    JSONArray assets = json.optJSONArray("assets");
                    if (assets != null && assets.length() > 0) {
                        for (int i = 0; i < assets.length(); i++) {
                            JSONObject asset = assets.optJSONObject(i);
                            if (asset != null) {
                                String name = asset.optString("name", "");
                                if (name.endsWith(".apk")) {
                                    downloadUrl = asset.optString("browser_download_url", htmlUrl);
                                    break;
                                }
                            }
                        }
                    }

                    final String finalDownloadUrl = downloadUrl;
                    if (isNewerVersion(tagName, CURRENT_VERSION)) {
                        activity.runOnUiThread(() -> showUpdateDialog(activity, tagName, body, finalDownloadUrl));
                    } else if (isManual) {
                        activity.runOnUiThread(() -> Toast.makeText(activity, R.string.update_toast_latest, Toast.LENGTH_SHORT).show());
                    }
                } else {
                    // Chua co ban release tren GitHub hoac loi mang
                    if (isManual) {
                        activity.runOnUiThread(() -> Toast.makeText(activity, R.string.update_toast_latest, Toast.LENGTH_SHORT).show());
                    }
                }
            } catch (Exception e) {
                if (isManual) {
                    activity.runOnUiThread(() -> Toast.makeText(activity, R.string.update_toast_error, Toast.LENGTH_SHORT).show());
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }

    /**
     * So sanh chuoi phien ban (vi du "v1.0.1" vs "1.0.0")
     */
    public static boolean isNewerVersion(String remoteTag, String localVersion) {
        if (remoteTag == null || remoteTag.trim().isEmpty()) {
            return false;
        }

        String remote = remoteTag.trim().toLowerCase().replaceAll("[^0-9.]", "");
        String local = localVersion.trim().toLowerCase().replaceAll("[^0-9.]", "");

        String[] rParts = remote.split("\\.");
        String[] lParts = local.split("\\.");

        int length = Math.max(rParts.length, lParts.length);
        for (int i = 0; i < length; i++) {
            int rVal = i < rParts.length ? parseSafe(rParts[i]) : 0;
            int lVal = i < lParts.length ? parseSafe(lParts[i]) : 0;

            if (rVal > lVal) return true;
            if (rVal < lVal) return false;
        }

        return false;
    }

    private static int parseSafe(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Hien thi Dialog thong bao co ban cap nhat moi
     */
    public static void showUpdateDialog(Activity activity, String newVersion, String changelog, String downloadUrl) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        msg.append(activity.getString(R.string.update_dialog_msg_version, newVersion, CURRENT_VERSION));

        if (changelog != null && !changelog.trim().isEmpty()) {
            msg.append("\n\n").append(activity.getString(R.string.update_dialog_changelog)).append("\n").append(changelog.trim());
        }

        new AlertDialog.Builder(activity)
                .setTitle(R.string.update_dialog_title)
                .setMessage(msg.toString())
                .setCancelable(true)
                .setPositiveButton(R.string.update_btn_now, (dialog, which) -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl));
                        activity.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.update_btn_later, (dialog, which) -> dialog.dismiss())
                .show();
    }
}

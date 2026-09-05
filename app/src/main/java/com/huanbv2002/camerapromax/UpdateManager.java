package com.huanbv2002.camerapromax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String GITHUB_REPO = "CameraProMax-";
    public static final String GITHUB_REPO_URL = "https://github.com/" + GITHUB_OWNER + "/" + GITHUB_REPO;
    public static final String GITHUB_PAGES_CONFIG = "https://" + GITHUB_OWNER + ".github.io/" + GITHUB_REPO + "/config.json";
    public static final String GITHUB_API_LATEST = "https://api.github.com/repos/" + GITHUB_OWNER + "/" + GITHUB_REPO + "/releases/latest";

    public static final String CURRENT_VERSION = "1.0.0";

    public static final String PREF_NAME = "camera_pro_max_prefs";
    public static final String KEY_REMOTE_YOUTUBE = "remote_youtube_url";
    public static final String KEY_REMOTE_DONATE = "remote_donate_url";

    /**
     * Kiem tra ban cap nhat va doc link dong tu GitHub Pages config.json hoac GitHub Releases.
     */
    public static void checkForUpdates(Activity activity, boolean isManual) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        if (isManual) {
            Toast.makeText(activity, R.string.update_toast_checking, Toast.LENGTH_SHORT).show();
        }

        new Thread(() -> {
            boolean handled = checkViaPagesConfig(activity, isManual);
            if (!handled) {
                checkViaGitHubReleases(activity, isManual);
            }
        }).start();
    }

    /**
     * Phuong thuc 1: Doc truc tiep tu GitHub Pages config.json
     */
    private static boolean checkViaPagesConfig(Activity activity, boolean isManual) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(GITHUB_PAGES_CONFIG);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() == 200) {
                String jsonStr = readStream(conn);
                JSONObject json = new JSONObject(jsonStr);

                String latestVer = json.optString("latest_version", "");
                String downloadUrl = json.optString("apk_download_url", GITHUB_REPO_URL + "/releases");
                String changelog = json.optString("changelog", "");
                String ytUrl = json.optString("youtube_tutorial_url", "");
                String donateUrl = json.optString("donate_url", "");

                // Luu vao SharedPreferences de MainActivity su dung
                SharedPreferences.Editor editor = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
                if (!ytUrl.isEmpty()) editor.putString(KEY_REMOTE_YOUTUBE, ytUrl);
                if (!donateUrl.isEmpty()) editor.putString(KEY_REMOTE_DONATE, donateUrl);
                editor.apply();

                if (isNewerVersion(latestVer, CURRENT_VERSION)) {
                    activity.runOnUiThread(() -> showUpdateDialog(activity, latestVer, changelog, downloadUrl));
                } else if (isManual) {
                    activity.runOnUiThread(() -> Toast.makeText(activity, R.string.update_toast_latest, Toast.LENGTH_SHORT).show());
                }
                return true;
            }
        } catch (Exception ignored) {
        } finally {
            if (conn != null) conn.disconnect();
        }
        return false;
    }

    /**
     * Phuong thuc 2: Du phong kiem tra qua GitHub Releases API
     */
    private static void checkViaGitHubReleases(Activity activity, boolean isManual) {
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
                String jsonStr = readStream(conn);
                JSONObject json = new JSONObject(jsonStr);
                String tagName = json.optString("tag_name", "");
                String body = json.optString("body", "");
                String htmlUrl = json.optString("html_url", GITHUB_REPO_URL + "/releases");

                String downloadUrl = htmlUrl;
                JSONArray assets = json.optJSONArray("assets");
                if (assets != null && assets.length() > 0) {
                    for (int i = 0; i < assets.length(); i++) {
                        JSONObject asset = assets.optJSONObject(i);
                        if (asset != null && asset.optString("name", "").endsWith(".apk")) {
                            downloadUrl = asset.optString("browser_download_url", htmlUrl);
                            break;
                        }
                    }
                }

                final String finalDownloadUrl = downloadUrl;
                if (isNewerVersion(tagName, CURRENT_VERSION)) {
                    activity.runOnUiThread(() -> showUpdateDialog(activity, tagName, body, finalDownloadUrl));
                } else if (isManual) {
                    activity.runOnUiThread(() -> Toast.makeText(activity, R.string.update_toast_latest, Toast.LENGTH_SHORT).show());
                }
            } else if (isManual) {
                activity.runOnUiThread(() -> Toast.makeText(activity, R.string.update_toast_latest, Toast.LENGTH_SHORT).show());
            }
        } catch (Exception e) {
            if (isManual) {
                activity.runOnUiThread(() -> Toast.makeText(activity, R.string.update_toast_error, Toast.LENGTH_SHORT).show());
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
    }

    private static String readStream(HttpURLConnection conn) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    public static boolean isNewerVersion(String remoteTag, String localVersion) {
        if (remoteTag == null || remoteTag.trim().isEmpty()) return false;

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

    public static void showUpdateDialog(Activity activity, String newVersion, String changelog, String downloadUrl) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) return;

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

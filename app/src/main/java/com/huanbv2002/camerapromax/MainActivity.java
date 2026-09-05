package com.huanbv2002.camerapromax;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.huanbv2002.camerapromax.shizuku.ShizukuManager;

import java.util.Locale;

import rikka.shizuku.Shizuku;

public class MainActivity extends AppCompatActivity {

    // =========================================================================
    // CÁC ĐƯỜNG LINK CỦA DỰ ÁN:
    // =========================================================================
    public static final String WEB_OFFICIAL_URL = "https://huanbv2002.github.io/CameraProMax/";
    public static final String YOUTUBE_TUTORIAL_URL = "https://huanbv2002.github.io/CameraProMax/#shizuku";
    public static final String DONATE_URL = "https://huanbv2002.github.io/CameraProMax/#config";
    // =========================================================================

    private static final int SHIZUKU_REQ_CODE = 1001;
    private static final String PREF_NAME = "camera_pro_max_prefs";
    private static final String KEY_LANG = "language_code";

    private View dotShizuku;
    private TextView tvShizukuStatus;
    private Button btnReqShizuku;

    private TextView tvCurrentRes;
    private TextView tvCurrentDpi;

    private SeekBar sbWidth, sbHeight, sbDensity;
    private EditText etWidth, etHeight, etDensity;

    // 4 Compact Tiles
    private LinearLayout tilePreset21_9, tilePreset24_9, tilePreset19_5, tilePresetPc;
    private ImageView imgIcon21_9, imgIcon24_9, imgIcon19_5, imgIconPc;
    private TextView tvBadge21_9, tvRatio21_9, tvRes21_9;
    private TextView tvBadge24_9, tvRatio24_9, tvRes24_9;
    private TextView tvBadge19_5, tvRatio19_5, tvRes19_5;
    private TextView tvBadgePc, tvRatioPc, tvResPc;

    private Button btnApplyAll, btnResetAll;

    // Header Language & Footer Links
    private View btnSwitchLanguage;
    private TextView tvCurrentLang;
    private View btnDonate;
    private View btnTutorial;
    private View btnGithub;
    private View btnVersionUpdate;

    private boolean isUpdatingFromCode = false;

    private final Shizuku.OnRequestPermissionResultListener permissionListener =
            (requestCode, grantResult) -> {
                if (requestCode == SHIZUKU_REQ_CODE) {
                    runOnUiThread(this::updateShizukuUI);
                }
            };

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String lang = prefs.getString(KEY_LANG, "vi");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
        setupSeekBars();
        setupPresets();
        setupFooterAndLanguage();

        Shizuku.addRequestPermissionResultListener(permissionListener);
        updateShizukuUI();

        // Tự động kiểm tra bản cập nhật mới trên GitHub trong nền
        UpdateManager.checkForUpdates(this, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Shizuku.removeRequestPermissionResultListener(permissionListener);
        } catch (Throwable ignored) {}
    }

    private void initViews() {
        dotShizuku = findViewById(R.id.dot_shizuku);
        tvShizukuStatus = findViewById(R.id.tv_shizuku_status);
        btnReqShizuku = findViewById(R.id.btn_req_shizuku);

        tvCurrentRes = findViewById(R.id.tv_current_res);
        tvCurrentDpi = findViewById(R.id.tv_current_dpi);

        sbWidth = findViewById(R.id.sb_width);
        sbHeight = findViewById(R.id.sb_height);
        sbDensity = findViewById(R.id.sb_density);

        etWidth = findViewById(R.id.et_width);
        etHeight = findViewById(R.id.et_height);
        etDensity = findViewById(R.id.et_density);

        // Compact Tiles
        tilePreset21_9 = findViewById(R.id.tile_preset_21_9);
        tilePreset24_9 = findViewById(R.id.tile_preset_24_9);
        tilePreset19_5 = findViewById(R.id.tile_preset_19_5);
        tilePresetPc = findViewById(R.id.tile_preset_pc);

        imgIcon21_9 = findViewById(R.id.img_icon_21_9);
        imgIcon24_9 = findViewById(R.id.img_icon_24_9);
        imgIcon19_5 = findViewById(R.id.img_icon_19_5);
        imgIconPc = findViewById(R.id.img_icon_pc);

        tvBadge21_9 = findViewById(R.id.tv_badge_21_9);
        tvRatio21_9 = findViewById(R.id.tv_ratio_21_9);
        tvRes21_9 = findViewById(R.id.tv_res_21_9);

        tvBadge24_9 = findViewById(R.id.tv_badge_24_9);
        tvRatio24_9 = findViewById(R.id.tv_ratio_24_9);
        tvRes24_9 = findViewById(R.id.tv_res_24_9);

        tvBadge19_5 = findViewById(R.id.tv_badge_19_5);
        tvRatio19_5 = findViewById(R.id.tv_ratio_19_5);
        tvRes19_5 = findViewById(R.id.tv_res_19_5);

        tvBadgePc = findViewById(R.id.tv_badge_pc);
        tvRatioPc = findViewById(R.id.tv_ratio_pc);
        tvResPc = findViewById(R.id.tv_res_pc);

        btnApplyAll = findViewById(R.id.btn_apply_all);
        btnResetAll = findViewById(R.id.btn_reset_all);

        btnSwitchLanguage = findViewById(R.id.btn_switch_language);
        tvCurrentLang = findViewById(R.id.tv_current_lang);
        btnDonate = findViewById(R.id.btn_donate);
        btnTutorial = findViewById(R.id.btn_tutorial);
        btnGithub = findViewById(R.id.btn_github);
        btnVersionUpdate = findViewById(R.id.btn_version_update);
    }

    private void setupFooterAndLanguage() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String currentLang = prefs.getString(KEY_LANG, "vi");
        tvCurrentLang.setText(currentLang.toUpperCase());

        // Chuyển đổi ngôn ngữ Tiếng Anh / Tiếng Việt
        btnSwitchLanguage.setOnClickListener(v -> {
            String newLang = currentLang.equalsIgnoreCase("vi") ? "en" : "vi";
            prefs.edit().putString(KEY_LANG, newLang).apply();
            recreate();
        });

        // Xử lý nút Donate (Đọc từ config.json hoặc mã nguồn)
        btnDonate.setOnClickListener(v -> {
            String url = prefs.getString(UpdateManager.KEY_REMOTE_DONATE, "").trim();
            if (url.isEmpty()) url = DONATE_URL != null ? DONATE_URL.trim() : "";

            if (!url.isEmpty()) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.toast_error_prefix, e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.toast_donate_placeholder, Toast.LENGTH_LONG).show();
            }
        });

        // Xử lý nút Hướng dẫn YouTube (Đọc từ config.json hoặc mã nguồn)
        btnTutorial.setOnClickListener(v -> {
            String url = prefs.getString(UpdateManager.KEY_REMOTE_YOUTUBE, "").trim();
            if (url.isEmpty()) url = YOUTUBE_TUTORIAL_URL != null ? YOUTUBE_TUTORIAL_URL.trim() : "";

            if (!url.isEmpty()) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.toast_error_prefix, e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.toast_tutorial_placeholder, Toast.LENGTH_LONG).show();
            }
        });

        // Xử lý nút GitHub Repository
        if (btnGithub != null) {
            btnGithub.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateManager.GITHUB_REPO_URL));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, getString(R.string.toast_error_prefix, e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Nhan vao Version Badge de kiem tra cap nhat thu cong
        if (btnVersionUpdate != null) {
            btnVersionUpdate.setOnClickListener(v -> UpdateManager.checkForUpdates(this, true));
            btnVersionUpdate.setOnLongClickListener(v -> {
                UpdateManager.showUpdateDialog(this, "v1.1.0", "- Cải thiện hiệu năng Shizuku\n- Thêm preset góc nhìn 32:9 siêu rộng\n- Tối ưu bộ nhớ đệm", UpdateManager.GITHUB_REPO_URL);
                return true;
            });
        }
    }

    private void updateShizukuUI() {
        boolean available = ShizukuManager.isShizukuAvailable();
        boolean granted = ShizukuManager.hasShizukuPermission();

        if (granted) {
            dotShizuku.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.status_green));
            tvShizukuStatus.setText(R.string.shizuku_ready);
            btnReqShizuku.setVisibility(View.GONE);
            refreshDisplayInfo();
        } else if (available) {
            dotShizuku.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.status_amber));
            tvShizukuStatus.setText(R.string.shizuku_not_granted);
            btnReqShizuku.setText(R.string.shizuku_btn_request);
            btnReqShizuku.setVisibility(View.VISIBLE);
        } else {
            dotShizuku.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.status_red));
            tvShizukuStatus.setText(R.string.shizuku_not_running);
            btnReqShizuku.setText(R.string.shizuku_btn_open);
            btnReqShizuku.setVisibility(View.VISIBLE);
        }
    }

    private void refreshDisplayInfo() {
        new Thread(() -> {
            String[] info = ShizukuManager.getCurrentScreenInfo();
            runOnUiThread(() -> {
                tvCurrentRes.setText(info[0]);
                tvCurrentDpi.setText(info[1]);
            });
        }).start();
    }

    private void setupListeners() {
        btnReqShizuku.setOnClickListener(v -> {
            if (ShizukuManager.isShizukuAvailable()) {
                ShizukuManager.requestPermission(SHIZUKU_REQ_CODE);
            } else {
                Toast.makeText(this, R.string.toast_shizuku_required, Toast.LENGTH_LONG).show();
            }
        });

        btnApplyAll.setOnClickListener(v -> {
            try {
                int w = Integer.parseInt(etWidth.getText().toString().trim());
                int h = Integer.parseInt(etHeight.getText().toString().trim());
                int dpi = Integer.parseInt(etDensity.getText().toString().trim());

                if (w < 400 || h < 400) {
                    Toast.makeText(this, R.string.toast_res_too_small, Toast.LENGTH_SHORT).show();
                    return;
                }

                btnApplyAll.setEnabled(false);
                btnApplyAll.setText(R.string.btn_applying);

                ShizukuManager.applyResolution(w, h, dpi, new ShizukuManager.ExecutionCallback() {
                    @Override
                    public void onSuccess(String output) {
                        runOnUiThread(() -> {
                            btnApplyAll.setEnabled(true);
                            btnApplyAll.setText(R.string.btn_apply_changes);
                            String msg = getString(R.string.toast_apply_success, w + " × " + h);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            refreshDisplayInfo();
                        });
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> {
                            btnApplyAll.setEnabled(true);
                            btnApplyAll.setText(R.string.btn_apply_changes);
                            Toast.makeText(MainActivity.this, getString(R.string.toast_error_prefix, error), Toast.LENGTH_LONG).show();
                        });
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.toast_input_invalid, Toast.LENGTH_SHORT).show();
            }
        });

        btnResetAll.setOnClickListener(v -> {
            btnResetAll.setEnabled(false);
            ShizukuManager.resetResolution(new ShizukuManager.ExecutionCallback() {
                @Override
                public void onSuccess(String output) {
                    runOnUiThread(() -> {
                        btnResetAll.setEnabled(true);
                        Toast.makeText(MainActivity.this, R.string.toast_reset_success, Toast.LENGTH_SHORT).show();
                        refreshDisplayInfo();
                        clearPresetTileSelection();
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {
                        btnResetAll.setEnabled(true);
                        Toast.makeText(MainActivity.this, getString(R.string.toast_error_prefix, error), Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }

    private void setupSeekBars() {
        // Width SeekBar & EditText
        sbWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    clearPresetTileSelection();
                    isUpdatingFromCode = true;
                    etWidth.setText(String.valueOf(progress));
                    isUpdatingFromCode = false;
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        etWidth.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!isUpdatingFromCode && s != null && s.length() > 0) {
                    try {
                        int val = Integer.parseInt(s.toString());
                        sbWidth.setProgress(val);
                    } catch (NumberFormatException ignored) {}
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Height SeekBar & EditText
        sbHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    clearPresetTileSelection();
                    isUpdatingFromCode = true;
                    etHeight.setText(String.valueOf(progress));
                    isUpdatingFromCode = false;
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        etHeight.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!isUpdatingFromCode && s != null && s.length() > 0) {
                    try {
                        int val = Integer.parseInt(s.toString());
                        sbHeight.setProgress(val);
                    } catch (NumberFormatException ignored) {}
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Density SeekBar & EditText
        sbDensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    clearPresetTileSelection();
                    isUpdatingFromCode = true;
                    etDensity.setText(String.valueOf(progress));
                    isUpdatingFromCode = false;
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        etDensity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int count, int after) {
                if (!isUpdatingFromCode && s != null && s.length() > 0) {
                    try {
                        int val = Integer.parseInt(s.toString());
                        sbDensity.setProgress(val);
                    } catch (NumberFormatException ignored) {}
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupPresets() {
        tilePreset21_9.setOnClickListener(v -> selectPresetTile(tilePreset21_9, 2560, 1080, 240));
        tilePreset24_9.setOnClickListener(v -> selectPresetTile(tilePreset24_9, 2560, 960, 220));
        tilePreset19_5.setOnClickListener(v -> selectPresetTile(tilePreset19_5, 2560, 1180, 240));
        tilePresetPc.setOnClickListener(v -> selectPresetTile(tilePresetPc, 2560, 1536, 240));
    }

    private void clearPresetTileSelection() {
        resetTileStyle(tilePreset21_9, imgIcon21_9, tvBadge21_9, tvRatio21_9, tvRes21_9);
        resetTileStyle(tilePreset24_9, imgIcon24_9, tvBadge24_9, tvRatio24_9, tvRes24_9);
        resetTileStyle(tilePreset19_5, imgIcon19_5, tvBadge19_5, tvRatio19_5, tvRes19_5);
        resetTileStyle(tilePresetPc, imgIconPc, tvBadgePc, tvRatioPc, tvResPc);
    }

    private void resetTileStyle(LinearLayout tile, ImageView icon, TextView badge, TextView ratio, TextView res) {
        tile.setBackgroundResource(R.drawable.bg_tile_unselected);
        icon.setImageTintList(ContextCompat.getColorStateList(this, R.color.text_secondary));
        badge.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        ratio.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        res.setTextColor(ContextCompat.getColor(this, R.color.text_muted));
    }

    private void selectPresetTile(LinearLayout selectedTile, int w, int h, int dpi) {
        clearPresetTileSelection();

        selectedTile.setBackgroundResource(R.drawable.bg_tile_selected);
        if (selectedTile == tilePreset21_9) {
            imgIcon21_9.setImageTintList(ContextCompat.getColorStateList(this, R.color.primary_blue));
            tvBadge21_9.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvRatio21_9.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvRes21_9.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        } else if (selectedTile == tilePreset24_9) {
            imgIcon24_9.setImageTintList(ContextCompat.getColorStateList(this, R.color.primary_blue));
            tvBadge24_9.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvRatio24_9.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvRes24_9.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        } else if (selectedTile == tilePreset19_5) {
            imgIcon19_5.setImageTintList(ContextCompat.getColorStateList(this, R.color.primary_blue));
            tvBadge19_5.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvRatio19_5.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvRes19_5.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        } else if (selectedTile == tilePresetPc) {
            imgIconPc.setImageTintList(ContextCompat.getColorStateList(this, R.color.primary_blue));
            tvBadgePc.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvRatioPc.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            tvResPc.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        }

        isUpdatingFromCode = true;
        etWidth.setText(String.valueOf(w));
        sbWidth.setProgress(w);

        etHeight.setText(String.valueOf(h));
        sbHeight.setProgress(h);

        etDensity.setText(String.valueOf(dpi));
        sbDensity.setProgress(dpi);
        isUpdatingFromCode = false;

        String msg = getString(R.string.toast_preset_selected, w + " × " + h);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
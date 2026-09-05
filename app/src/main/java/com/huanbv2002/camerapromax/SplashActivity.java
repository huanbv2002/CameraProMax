package com.huanbv2002.camerapromax;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private View splashContainer;
    private TextView tvSplashStatus;
    private TextView tvSplashFooter;
    private ProgressBar progressSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashContainer = findViewById(R.id.layout_splash_container);
        tvSplashStatus = findViewById(R.id.tv_splash_status);
        tvSplashFooter = findViewById(R.id.tv_splash_footer);
        progressSplash = findViewById(R.id.progress_splash);

        startCinematicSplashAnimation();
    }

    /**
     * Hiệu ứng chuyển động điện ảnh 3 giai đoạn:
     * 1. Entrance: Nổi lên và bung nở (Scale-up + Fade-in + Overshoot nảy nhẹ như camera mở khẩu độ).
     * 2. Pulse / Scan: Nhịp thở công nghệ (Breathing Pulse) mô phỏng quét hệ thống.
     * 3. Exit: Phóng to xuyên qua ống kính (Camera Zoom-through + Fade-out) chuyển cảnh mượt mà vào Dashboard.
     */
    private void startCinematicSplashAnimation() {
        // Trạng thái ban đầu
        splashContainer.setAlpha(0f);
        splashContainer.setScaleX(0.78f);
        splashContainer.setScaleY(0.78f);
        splashContainer.setTranslationY(50f);

        if (tvSplashFooter != null) {
            tvSplashFooter.setAlpha(0f);
        }

        // Giai đoạn 1: Bung nở nhẹ nhàng & bay lên vị trí chuẩn (650ms)
        splashContainer.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(650)
                .setInterpolator(new OvershootInterpolator(1.15f))
                .withEndAction(() -> {
                    // Giai đoạn 2: Nhịp thở công nghệ (Pulse breathing - 750ms)
                    if (tvSplashStatus != null) {
                        tvSplashStatus.setText(R.string.splash_status_ready);
                    }

                    splashContainer.animate()
                            .scaleX(1.035f)
                            .scaleY(1.035f)
                            .setDuration(750)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .withEndAction(() -> {
                                // Giai đoạn 3: Phóng to xuyên ống kính & mờ dần (Zoom-in Dissolve - 400ms)
                                splashContainer.animate()
                                        .alpha(0f)
                                        .scaleX(1.22f)
                                        .scaleY(1.22f)
                                        .setDuration(400)
                                        .setInterpolator(new AccelerateInterpolator(1.4f))
                                        .withEndAction(this::navigateToMain)
                                        .start();

                                if (tvSplashFooter != null) {
                                    tvSplashFooter.animate()
                                            .alpha(0f)
                                            .setDuration(300)
                                            .start();
                                }
                            })
                            .start();
                })
                .start();

        // Footer xuất hiện mượt mà
        if (tvSplashFooter != null) {
            tvSplashFooter.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .setStartDelay(200)
                    .start();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}

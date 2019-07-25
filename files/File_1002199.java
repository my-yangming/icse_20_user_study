package com.liskovsoft.smartyoutubetv.bootstrap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.smartyoutubetv.BuildConfig;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.smartyoutubetv.misc.LangUpdater;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.smartyoutubetv.misc.OldPackageRemover;
import com.liskovsoft.smartyoutubetv.prefs.SmartPreferences;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.InitializationCallback;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class BootstrapActivityBase extends AppCompatActivity {
    private static final String TAG = BootstrapActivityBase.class.getSimpleName();

    @Override
    protected void onCreate(Bundle icicle) {
        setupLang();

        super.onCreate(icicle);

        initLogger();
        setupCrashLogs();
        setupFonSize();
        makeActivityFullscreen();
        makeActivityHorizontal();
        hideTitleBar();
        uninstallOldVersions();
    }

    private void initLogger() {
        Context ctx = getApplicationContext();
        SmartPreferences prefs = SmartPreferences.instance(ctx);
        Log.init(ctx, prefs.getLogType(), AppInfoHelpers.getActivityLabel(ctx, prefs.getBootstrapActivityName()));
    }

    private void makeActivityHorizontal() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    private void hideTitleBar() {
        // NOTE: we must set theme before ui instantiation
        setTheme(com.liskovsoft.browser.R.style.SimpleUITheme);
    }

    private void makeActivityFullscreen() {
        getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);

        if (VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void restart() {
        Intent intent = new Intent();
        intent.setClass(this, BootstrapActivity.class);
        intent.putExtra(BootstrapActivity.SKIP_RESTORE, true);
        startActivity(intent);

        System.exit(0);
    }

    /**
     * Detect {@link Crashlytics} from the property file. See <em>build.gradle</em>. <a href="https://docs.fabric.io/android/crashlytics/build-tools.html">More info</a>
     */
    private void setupCrashLogs() {
        if (BuildConfig.CRASHLYTICS_ENABLED) {
            Log.d(TAG, "Crashlytics is enabled. Running setup...");

            CrashlyticsCore core = new CrashlyticsCore.Builder()
                    .disabled(BuildConfig.DEBUG)
                    .build();

            Fabric.with(
                    new Fabric.Builder(this).kits(
                            new Crashlytics.Builder()
                            .core(core)
                            .build())
                    .initializationCallback(new InitializationCallback<Fabric>() {
                        @Override
                        public void success(Fabric fabric) {
                            setupGlobalExceptionHandler();
                        }

                        @Override
                        public void failure(Exception e) {

                        }
                    })
                    .build());
        }
    }

    private void setupGlobalExceptionHandler() {
        final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            StringWriter out = new StringWriter();
            PrintWriter writer = new PrintWriter(out);
            throwable.printStackTrace(writer);
            Log.e(TAG, out);
            Log.flush();

            if (oldHandler != null) {
                oldHandler.uncaughtException(thread, throwable); // Delegates to Android's error handling}
            } else {
                System.exit(2); // Prevents the service/app from freezing}
            }
        });
    }

    private void setupCrashLogsOld() {
        if (BuildConfig.CRASHLYTICS_ENABLED) {
            Fabric.with(this, new Crashlytics());
        }
    }

    private void setupFonSize() {
        Helpers.adjustFontScale(getResources().getConfiguration(), this);
    }

    private void setupLang() {
        new LangUpdater(this).update();
    }

    private void uninstallOldVersions() {
        OldPackageRemover remover = new OldPackageRemover(this);
        remover.remove();
    }
}

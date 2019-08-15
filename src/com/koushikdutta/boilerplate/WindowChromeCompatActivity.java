package com.koushikdutta.boilerplate;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.SimpleFuture;

import java.util.HashMap;

/**
 * Created by koush on 6/3/16.
 */
public class WindowChromeCompatActivity extends AppCompatActivity {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void goTranslucentStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void goFullscreenLayout() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // note that android P includes a cutout which may be of wonky height.
        // no way to see what the icon_list_drawer_activity_statusbar_height value is outside of runtime
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                goFullscreenLayout();
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                goTranslucentStatusBar();
        }
    }

    int currentRequestCode = 0x00008000;
    HashMap<Integer, SimpleFuture<Intent>> callbacks = new HashMap<>();
    public Future<Intent> startActivityForResult(Intent intent) {
        int requestCode = currentRequestCode++;
        SimpleFuture<Intent> ret = new SimpleFuture<>();
        callbacks.put(requestCode, ret);
        startActivityForResult(intent, requestCode);
        return ret;
    }

    @Override
    protected void onDestroy() {
        callbacks.clear();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleFuture<Intent> callback = callbacks.remove(requestCode);
        if (callback != null) {
            if (resultCode == RESULT_CANCELED || data == null)
                callback.cancel();
            else
                callback.setComplete(data);
        }
    }
}

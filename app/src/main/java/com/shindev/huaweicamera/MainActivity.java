package com.shindev.huaweicamera;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.shindev.huaweicamera.adapter.PaperAdapter;
import com.shindev.huaweicamera.common.Global;
import com.shindev.huaweicamera.fragment.SurfaceFragment;
import com.shindev.huaweicamera.fragment.TextureFragment;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import java.util.ArrayList;

import static com.shindev.huaweicamera.camfilter.widget.LuoGLCameraView.cameraEngine;

public class MainActivity extends AppCompatActivity {

    public Global.CameraMode cameraMode = Global.CameraMode.PHOTO;

    private ImageView img_page01, img_page02, img_page03;
    private ViewPager viewPager;

    private SurfaceFragment surfaceFragment;
    private TextureFragment textureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSelfPermission();
    }

    private void checkSelfPermission () {
        ArrayList<String> arrayList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != 0) {
            arrayList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != 0) {
            arrayList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != 0) {
            arrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (arrayList.size() == 0) {
            //initialize Activity UIs
            initiaizeActivity();
            return;
        }
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        ActivityCompat.requestPermissions(this, strArr, 0);
    }

    private void initiaizeActivity() {
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loadFragment();

        viewPager = findViewById(R.id.vpPager);
        FragmentPagerAdapter adapterViewPager = new PaperAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //
            }

            @Override
            public void onPageSelected(int i) {
                initPageIndexUI(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //
            }
        });

        img_page01 = findViewById(R.id.img_main_page01);
        img_page02 = findViewById(R.id.img_main_page02);
        img_page03 = findViewById(R.id.img_main_page03);

        initPageIndexUI(1);
    }

    private void initPageIndexUI(int i) {
        img_page01.setBackground(getDrawable(R.drawable.back_page_normal));
        img_page02.setBackground(getDrawable(R.drawable.back_page_normal));
        img_page03.setBackground(getDrawable(R.drawable.back_page_normal));
        switch (i) {
            case 0:
                img_page01.setBackground(getDrawable(R.drawable.back_page_sel));
                break;
            case 1:
                img_page02.setBackground(getDrawable(R.drawable.back_page_sel));
                break;
            case 2:
                img_page03.setBackground(getDrawable(R.drawable.back_page_sel));
                break;
        }
    }

    public void loadFragment() {
        Fragment fragment = null;
        switch (cameraMode) {
            case PHOTO:
            case CAMERA:
            case FITLER:
                if (surfaceFragment == null)
                    surfaceFragment = new SurfaceFragment();
                fragment = surfaceFragment;
                break;
            case PHOTOPRO:
            case NIGHT:
            case HDR:
            case LANDSCAPE:
                textureFragment = new TextureFragment();
                fragment = textureFragment;
                break;
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frg_main_view, fragment);
        fragmentTransaction.commit();
    }

    public void resetFilterMode(Global.CameraFilterType type) {
        loadFragment();

        viewPager.setCurrentItem(1);
        surfaceFragment.setFilter(type);
    }

    public void resetCameraMode() {
        viewPager.setCurrentItem(1);
        loadFragment();
    }
}

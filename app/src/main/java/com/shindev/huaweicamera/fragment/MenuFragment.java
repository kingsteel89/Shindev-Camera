package com.shindev.huaweicamera.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shindev.huaweicamera.common.Global;
import com.shindev.huaweicamera.MainActivity;
import com.shindev.huaweicamera.R;

public class MenuFragment extends Fragment {

    private MainActivity activity;

    private LinearLayout llt_mode, llt_filter;

    private TextView txt_filter_none, txt_filter_inkwell, txt_filter_sketch, txt_filter_fairytale, txt_filter_nostalgia
            , txt_filter_cool, txt_filter_earlybird, txt_filter_toastero, txt_filter_walden;

    private TextView txt_normal, txt_pro, txt_camera, txt_night, txt_hdr, txt_filter, txt_landscape;
    private ImageView img_normal, img_pro, img_camera, img_night, img_hdr, img_filter, img_landscape;

    private Global.CameraFilterType type = Global.CameraFilterType.NONE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initUIViews(view);
        return view;
    }

    private void initUIViews(View view) {
        activity = (MainActivity) getActivity();

        img_normal = view.findViewById(R.id.img_menu_normal);
        img_pro = view.findViewById(R.id.img_menu_pro);
        img_camera = view.findViewById(R.id.img_menu_camera);
        img_night = view.findViewById(R.id.img_menu_night);
        img_hdr = view.findViewById(R.id.img_menu_hdr);
        img_filter = view.findViewById(R.id.img_menu_filter);
        img_landscape = view.findViewById(R.id.img_menu_landscape);

        txt_normal = view.findViewById(R.id.txt_menu_normal);
        txt_pro = view.findViewById(R.id.txt_menu_pro);
        txt_camera = view.findViewById(R.id.txt_menu_camera);
        txt_night = view.findViewById(R.id.txt_menu_night);
        txt_hdr = view.findViewById(R.id.txt_menu_hdr);
        txt_filter = view.findViewById(R.id.txt_menu_filter);
        txt_landscape = view.findViewById(R.id.txt_menu_landscape);

        txt_filter_none = view.findViewById(R.id.txt_menu_filter_none);
        txt_filter_inkwell = view.findViewById(R.id.txt_menu_filter_inkwell);
        txt_filter_sketch = view.findViewById(R.id.txt_menu_filter_sketch);
        txt_filter_fairytale = view.findViewById(R.id.txt_menu_filter_fairytale);
        txt_filter_nostalgia = view.findViewById(R.id.txt_menu_filter_nostalgia);
        txt_filter_cool = view.findViewById(R.id.txt_menu_filter_cool);
        txt_filter_earlybird = view.findViewById(R.id.txt_menu_filter_earlybird);
        txt_filter_toastero = view.findViewById(R.id.txt_menu_filter_toastero);
        txt_filter_walden = view.findViewById(R.id.txt_menu_filter_walden);
        resetUIViews();

        llt_filter = view.findViewById(R.id.llt_menu_filter_group);
        llt_mode = view.findViewById(R.id.llt_menu_mode);
        initModeView();

        view.findViewById(R.id.llt_menu_normal).setOnClickListener(modeClickListener);
        view.findViewById(R.id.llt_menu_pro).setOnClickListener(modeClickListener);
        view.findViewById(R.id.llt_menu_camera).setOnClickListener(modeClickListener);
        view.findViewById(R.id.llt_menu_night).setOnClickListener(modeClickListener);
        view.findViewById(R.id.llt_menu_hdr).setOnClickListener(modeClickListener);
        view.findViewById(R.id.llt_menu_landscape).setOnClickListener(modeClickListener);

        view.findViewById(R.id.llt_menu_filter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llt_filter.setVisibility(View.VISIBLE);
                llt_mode.setVisibility(View.GONE);

                activity.cameraMode = Global.CameraMode.FITLER;
                resetUIViews();
            }
        });

        view.findViewById(R.id.llt_menu_filter_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initModeView();
            }
        });

        view.findViewById(R.id.llt_filter_item_01).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_02).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_03).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_04).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_05).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_06).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_07).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_08).setOnClickListener(filterClickListener);
        view.findViewById(R.id.llt_filter_item_09).setOnClickListener(filterClickListener);
    }

    private void initModeView() {
        llt_filter.setVisibility(View.GONE);
        llt_mode.setVisibility(View.VISIBLE);
    }

    private void resetUIViews() {
        img_normal.setImageResource(R.drawable.ico_photo);
        img_pro.setImageResource(R.drawable.ico_photo_pro);
        img_camera.setImageResource(R.drawable.ico_camera);
        img_night.setImageResource(R.drawable.ico_night);
        img_hdr.setImageResource(R.drawable.ico_hdr);
        img_filter.setImageResource(R.drawable.ico_filter);
        img_landscape.setImageResource(R.drawable.ico_landscape);

        txt_normal.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_pro.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_camera.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_night.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_hdr.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_landscape.setTextColor(getResources().getColor(R.color.color_txt_white));

        txt_filter_none.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_inkwell.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_sketch.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_fairytale.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_nostalgia.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_cool.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_earlybird.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_toastero.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_filter_walden.setTextColor(getResources().getColor(R.color.color_txt_white));

        switch (activity.cameraMode) {
            case LANDSCAPE:
                txt_landscape.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                img_landscape.setImageResource(R.drawable.ico_landscape_sel);
                break;
            case HDR:
                txt_hdr.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                img_hdr.setImageResource(R.drawable.ico_hdr_sel);
                break;
            case NIGHT:
                txt_night.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                img_night.setImageResource(R.drawable.ico_night_sel);
                break;
            case CAMERA:
                txt_camera.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                img_camera.setImageResource(R.drawable.ico_camera_sel);
                break;
            case PHOTOPRO:
                txt_pro.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                img_pro.setImageResource(R.drawable.ico_photo_pro_sel);
                break;
            case PHOTO:
                txt_normal.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                img_normal.setImageResource(R.drawable.ico_photo_sel);
                break;
            case FITLER:
                txt_filter.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                img_filter.setImageResource(R.drawable.ico_filter_sel);
                break;
        }

        switch (type) {
            case EARLYBIRD:
                txt_filter_earlybird.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case TOASTERO:
                txt_filter_toastero.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case FIRYTALE:
                txt_filter_fairytale.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case INKWELL:
                txt_filter_inkwell.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case WALDEN:
                txt_filter_walden.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case SKETCH:
                txt_filter_sketch.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case COOL:
                txt_filter_cool.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case NONE:
                txt_filter_none.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case NOSTALGIA:
                txt_filter_nostalgia.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
        }
    }

    private View.OnClickListener modeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llt_menu_normal:
                    activity.cameraMode = Global.CameraMode.PHOTO;
                    break;
                case R.id.llt_menu_pro:
                    activity.cameraMode = Global.CameraMode.PHOTOPRO;
                    break;
                case R.id.llt_menu_camera:
                    activity.cameraMode = Global.CameraMode.CAMERA;
                    break;
                case R.id.llt_menu_night:
                    activity.cameraMode = Global.CameraMode.NIGHT;
                    break;
                case R.id.llt_menu_hdr:
                    activity.cameraMode = Global.CameraMode.HDR;
                    break;
                case R.id.llt_menu_landscape:
                    activity.cameraMode = Global.CameraMode.LANDSCAPE;
                    break;
            }
            activity.resetCameraMode();
            resetUIViews();
        }
    };

    private View.OnClickListener filterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llt_filter_item_01:
                    type = Global.CameraFilterType.NONE;
                    break;
                case R.id.llt_filter_item_02:
                    type = Global.CameraFilterType.INKWELL;
                    break;
                case R.id.llt_filter_item_03:
                    type = Global.CameraFilterType.SKETCH;
                    break;
                case R.id.llt_filter_item_04:
                    type = Global.CameraFilterType.FIRYTALE;
                    break;
                case R.id.llt_filter_item_05:
                    type = Global.CameraFilterType.NOSTALGIA;
                    break;
                case R.id.llt_filter_item_06:
                    type = Global.CameraFilterType.COOL;
                    break;
                case R.id.llt_filter_item_07:
                    type = Global.CameraFilterType.EARLYBIRD;
                    break;
                case R.id.llt_filter_item_08:
                    type = Global.CameraFilterType.TOASTERO;
                    break;
                case R.id.llt_filter_item_09:
                    type = Global.CameraFilterType.WALDEN;
                    break;
            }
            initModeView();
            resetUIViews();
            activity.resetFilterMode(type);
        }
    };
}

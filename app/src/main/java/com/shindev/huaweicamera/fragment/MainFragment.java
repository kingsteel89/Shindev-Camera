package com.shindev.huaweicamera.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shindev.huaweicamera.adapter.MenuAdapter;
import com.shindev.huaweicamera.adapter.MenuBean;

import com.shindev.huaweicamera.MainActivity;
import com.shindev.huaweicamera.R;
import com.shindev.huaweicamera.common.Global;
import com.shindev.huaweicamera.utils.ClickUtils;
import com.shindev.huaweicamera.utils.ZIP;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import java.util.ArrayList;

import static com.shindev.huaweicamera.camfilter.widget.LuoGLCameraView.cameraEngine;

public class MainFragment extends Fragment implements  View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private MainActivity activity;

    //Linearlayout Columns
    private LinearLayout llt_flash_col, llt_focuse_col, llt_ar_col, llt_face_col, llt_face_seek;

    private ImageView img_flash, img_focus, img_face, img_ar
            , img_face_rosy, img_face_brasion, img_face_white, img_face_lifting, img_face_eyes
            , img_flash_on, img_flash_off, img_flash_auto, img_flash_light
            , img_focus_auto, img_focus_infinite, img_focus_macro;

    private RecyclerView recyclerView;

    private SeekBar seekBarScale, seekBarFace;

    private int seekBarValue = 0;

    private TextView txt_face_smooth, txt_face_white, txt_face_slimming, txt_face_bigeye;

    ArrayList<MenuBean> mStickerData = new ArrayList<>();

    private enum FACEEFFECT {
        ROSY,
        WHITE,
        BRASION,
        LIFTING,
        BIGEYES
    }
    private FACEEFFECT faceeffect = FACEEFFECT.ROSY;
    private int smoothVal, whiteVal, liftingVal, bigEyesVal, redValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch (View v, MotionEvent event) {
                initMenuIcons();
//                activity.reFocusOnTouch(event);
                return true;
            }
        });

        initUIViews(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    private void initUIViews(View view) {
        img_flash = view.findViewById(R.id.img_main_flash);
        img_focus = view.findViewById(R.id.img_main_focus);
        img_face = view.findViewById(R.id.img_main_face);
        img_ar = view.findViewById(R.id.img_main_ar);

        img_face_brasion = view.findViewById(R.id.img_main_face_brasion);
        img_face_rosy = view.findViewById(R.id.img_main_face_rosy);
        img_face_white = view.findViewById(R.id.img_main_face_white);
        img_face_lifting = view.findViewById(R.id.img_main_face_lifting);
        img_face_eyes = view.findViewById(R.id.img_main_face_eyes);

        img_flash_auto = view.findViewById(R.id.img_main_flash_auto);
        img_flash_on = view.findViewById(R.id.img_main_flash_on);
        img_flash_off = view.findViewById(R.id.img_main_flash_off);
        img_flash_light = view.findViewById(R.id.img_main_flash_light);

        img_focus_auto = view.findViewById(R.id.img_main_focus_auto);
        img_focus_infinite = view.findViewById(R.id.img_main_focus_infinite);
        img_focus_macro = view.findViewById(R.id.img_main_focus_macro);

        txt_face_smooth = view.findViewById(R.id.txt_main_face_smoothly);
        txt_face_white = view.findViewById(R.id.txt_main_face_white);
        txt_face_slimming = view.findViewById(R.id.txt_main_face_slimming);
        txt_face_bigeye = view.findViewById(R.id.txt_main_face_bigeye);

        //Linearlayout Columns
        llt_flash_col = view.findViewById(R.id.llt_main_flash_col);
        llt_focuse_col = view.findViewById(R.id.llt_main_focus_col);
        llt_ar_col = view.findViewById(R.id.llt_main_ar_effect);
        llt_face_col = view.findViewById(R.id.llt_main_face_effect);
        llt_face_seek = view.findViewById(R.id.llt_main_face_seekbar);

        view.findViewById(R.id.llt_main_flash).setOnClickListener(this);
        view.findViewById(R.id.llt_main_flash_on).setOnClickListener(this);
        view.findViewById(R.id.llt_main_flash_off).setOnClickListener(this);
        view.findViewById(R.id.llt_main_flash_auto).setOnClickListener(this);
        view.findViewById(R.id.llt_main_flash_toggle).setOnClickListener(this);

        view.findViewById(R.id.llt_main_focus).setOnClickListener(this);
        view.findViewById(R.id.llt_main_focus_auto).setOnClickListener(this);
        view.findViewById(R.id.llt_main_focus_macro).setOnClickListener(this);
        view.findViewById(R.id.llt_main_focus_infinit).setOnClickListener(this);

        view.findViewById(R.id.llt_main_face).setOnClickListener(this);
        view.findViewById(R.id.llt_main_face_rosy).setOnClickListener(this);
        view.findViewById(R.id.llt_main_face_white).setOnClickListener(this);
        view.findViewById(R.id.llt_main_face_brasion).setOnClickListener(this);
        view.findViewById(R.id.llt_main_face_lifting).setOnClickListener(this);
        view.findViewById(R.id.llt_main_face_eyes).setOnClickListener(this);

        view.findViewById(R.id.img_main_shutter).setOnClickListener(this);

        view.findViewById(R.id.llt_main_ar).setOnClickListener(this);

        view.findViewById(R.id.llt_main_switch).setOnClickListener(this);

        seekBarScale = view.findViewById(R.id.skb_main_scale);
        seekBarScale.setOnSeekBarChangeListener(this);

        seekBarFace = view.findViewById(R.id.skb_main_face);
        seekBarFace.setOnSeekBarChangeListener(this);

        initEffectMenu();
        final MenuAdapter mStickerAdapter = new MenuAdapter(getContext(), mStickerData);

        recyclerView = view.findViewById(R.id.rcy_main_effect);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mStickerAdapter.setOnClickListener(new ClickUtils.OnClickListener() {
            @Override
            public void onClick(View v, int type, int pos, int child) {
                MenuBean m=mStickerData.get(pos);
                String name=m.name;
                String path = m.path;
                if (name.equals("无")) {
                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(false);
                    mStickerAdapter.checkPos = pos;
                    v.setSelected(true);
                }else{
                    String stickerPaperdir = XJGArSdkApi.getPrivateResDataDir(getContext());
                    stickerPaperdir = stickerPaperdir +"/StickerPapers/"+ path;
                    ZIP.unzipAStickPaperPackages(stickerPaperdir);

                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(true);
                    XJGArSdkApi.XJGARSDKChangeStickpaper(path);
                    mStickerAdapter.checkPos=pos;
                    v.setSelected(true);
                }
                mStickerAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(mStickerAdapter);

        smoothVal = 0;
        whiteVal = 0;
        liftingVal = 0;
        bigEyesVal = 0;
        redValue = 0;

        initMenuIcons();
    }

    private void initMenuIcons () {
        img_focus.setImageResource(R.drawable.ico_focus);
        img_flash.setImageResource(R.drawable.ico_flash);
        img_face.setImageResource(R.drawable.ico_face);
        img_ar.setImageResource(R.drawable.ico_ar);

        img_face_eyes.setImageResource(R.drawable.ico_bigeye);
        img_face_brasion.setImageResource(R.drawable.ico_smoothly);
        img_face_white.setImageResource(R.drawable.ico_whitening);
        img_face_rosy.setImageResource(R.drawable.ico_face);
        img_face_lifting.setImageResource(R.drawable.ico_slimming);

        txt_face_smooth.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_face_white.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_face_slimming.setTextColor(getResources().getColor(R.color.color_txt_white));
        txt_face_bigeye.setTextColor(getResources().getColor(R.color.color_txt_white));

        llt_face_col.setVisibility(View.GONE);
        llt_ar_col.setVisibility(View.GONE);
        llt_focuse_col.setVisibility(View.GONE);
        llt_flash_col.setVisibility(View.GONE);
        llt_face_seek.setVisibility(View.GONE);
    }

    private void initFlashMenu() {
        img_flash_auto.setImageResource(R.drawable.ico_flash_auto);
        img_flash_on.setImageResource(R.drawable.ico_flash_on);
        img_flash_off.setImageResource(R.drawable.ico_flash_off);
        img_flash_light.setImageResource(R.drawable.ico_flash_light);
    }

    private void initFocusMenu() {
        img_focus_auto.setImageResource(R.drawable.ico_focus_auto);
        img_focus_infinite.setImageResource(R.drawable.ico_focus_infinit);
        img_focus_macro.setImageResource(R.drawable.ico_focus_macro);
    }

    private void initFaceMenu(int value) {
        img_face.setImageResource(R.drawable.ico_face_sel);
        llt_face_col.setVisibility(View.VISIBLE);
        llt_face_seek.setVisibility(View.VISIBLE);
        seekBarFace.setProgress(value);
    }

    @Override
    public void onClick(View v) {
        initMenuIcons();

        switch (v.getId()) {
            case R.id.llt_main_switch:
                setSwitchCamera();
                break;

                // ----- Flash column -----
            case R.id.llt_main_flash:
                img_flash.setImageResource(R.drawable.ico_flash_sel);
                llt_flash_col.setVisibility(View.VISIBLE);
                break;
            case R.id.llt_main_flash_on:
                initFlashMenu();
                img_flash_on.setImageResource(R.drawable.ico_flash_on_sel);
                resetFlashMode(Global.FlashMode.ON);
                break;
            case R.id.llt_main_flash_off:
                initFlashMenu();
                img_flash_off.setImageResource(R.drawable.ico_flash_off_sel);
                resetFlashMode(Global.FlashMode.OFF);
                break;
            case R.id.llt_main_flash_auto:
                initFlashMenu();
                img_flash_auto.setImageResource(R.drawable.ico_flash_auto_sel);
                resetFlashMode(Global.FlashMode.AUTO);
                break;
            case R.id.llt_main_flash_toggle:
                initFlashMenu();
                img_flash_light.setImageResource(R.drawable.ico_flash_light_sel);
                resetFlashMode(Global.FlashMode.LIGHT);
                break;

                // ----- Focus column -----
            case R.id.llt_main_focus:
                img_focus.setImageResource(R.drawable.ico_focus_sel);
                llt_focuse_col.setVisibility(View.VISIBLE);
                break;
            case R.id.llt_main_focus_auto:
                if (cameraEngine.getCameraId() == 1) {
                    Toast.makeText(getContext(), getString(R.string.frg_setting_mirror_detail), Toast.LENGTH_SHORT).show();
                    return;
                }
                initFocusMenu();
                img_focus_auto.setImageResource(R.drawable.ico_focus_auto_sel);
                resetFocusMode(Global.FocusMode.AUTO);
                break;
            case R.id.llt_main_focus_infinit:
                if (cameraEngine.getCameraId() == 1) {
                    Toast.makeText(getContext(), getString(R.string.frg_setting_mirror_detail), Toast.LENGTH_SHORT).show();
                    return;
                }
                initFocusMenu();
                img_focus_infinite.setImageResource(R.drawable.ico_focus_infinit_sel);
                resetFocusMode(Global.FocusMode.INFINIT);
                break;
            case R.id.llt_main_focus_macro:
                if (cameraEngine.getCameraId() == 1) {
                    Toast.makeText(getContext(), getString(R.string.frg_setting_mirror_detail), Toast.LENGTH_SHORT).show();
                    return;
                }
                initFocusMenu();
                img_focus_macro.setImageResource(R.drawable.ico_focus_macro_sel);
                resetFocusMode(Global.FocusMode.MACRO);
                break;

                // ----- Face column -----
            case R.id.llt_main_face:
                img_face.setImageResource(R.drawable.ico_face_sel);
                llt_face_col.setVisibility(View.VISIBLE);
                break;
            case R.id.llt_main_face_rosy:
                faceeffect = FACEEFFECT.ROSY;
                initFaceMenu(redValue);
                img_face_rosy.setImageResource(R.drawable.ico_face_sel);
                break;
            case R.id.llt_main_face_brasion:
                faceeffect = FACEEFFECT.BRASION;
                initFaceMenu(smoothVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly_sel);
                txt_face_smooth.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case R.id.llt_main_face_white:
                faceeffect = FACEEFFECT.WHITE;
                initFaceMenu(whiteVal);
                img_face_white.setImageResource(R.drawable.ico_whitening_sel);
                txt_face_white.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case R.id.llt_main_face_lifting:
                faceeffect = FACEEFFECT.LIFTING;
                initFaceMenu(liftingVal);
                img_face_lifting.setImageResource(R.drawable.ico_slimming_sel);
                txt_face_slimming.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case R.id.llt_main_face_eyes:
                faceeffect = FACEEFFECT.BIGEYES;
                initFaceMenu(bigEyesVal);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye_sel);
                txt_face_bigeye.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;

                // ----- AR column -----
            case R.id.llt_main_ar:
                img_ar.setImageResource(R.drawable.ico_ar_sel);
                llt_ar_col.setVisibility(View.VISIBLE);
                break;

            case R.id.img_main_shutter:
//                activity.clickShutterBtn();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarValue = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.skb_main_scale:
//                activity.changeCameraZoomExposure(seekBarValue, true);
                break;
            case R.id.skb_main_face:
                switch (faceeffect) {
                    case ROSY:
                        redValue = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetRedFaceParam(redValue);
                        break;
                    case WHITE:
                        whiteVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetWhiteSkinParam(whiteVal);
                        break;
                    case BIGEYES:
                        bigEyesVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetBigEyeParam(bigEyesVal);
                        break;
                    case BRASION:
                        smoothVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetSkinSmoothParam(smoothVal);
                        break;
                    case LIFTING:
                        liftingVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetThinChinParam(liftingVal);
                        break;
                }
                break;
        }
    }

    //初始化特效按钮菜单
    private void initEffectMenu() {
        MenuBean bean=new MenuBean();
        bean.name="无";
        bean.path="";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="天使";
        bean.path="angel";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="财神爷";
        bean.path="caishen";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="罐头狗";
        bean.path="cangou";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="潜水镜";
        bean.path="diving";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="花环";
        bean.path="huahuan";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="蕾丝";
        bean.path="leisi";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="路飞";
        bean.path="lufei";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="鹿花";
        bean.path="lvhua";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="梦兔";
        bean.path="mengtu";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="血族女";
        bean.path="xuezunv";
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name=" 西瓜猫";
        bean.path="stpaper900224";
        mStickerData.add(bean);
    }

    private void resetFlashMode(Global.FlashMode mode) {
        cameraEngine.setFlashValue(Global.getFlashString(mode));
    }

    private void resetFocusMode(Global.FocusMode mode) {
        cameraEngine.setFocusValue(Global.getFocusString(mode));
        String str_parameter = cameraEngine.getFocusValue();
        Log.d("MainFragment", str_parameter);
    }

    private void setSwitchCamera() {
        cameraEngine.switchCamera();
    }
}

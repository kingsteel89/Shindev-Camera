package com.shindev.huaweicamera.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.shindev.huaweicamera.common.CameraPara;
import com.shindev.huaweicamera.MainActivity;
import com.shindev.huaweicamera.R;
import com.shindev.huaweicamera.dialog.CrossPatternDialog;
import com.shindev.huaweicamera.dialog.PhotoSizeDialog;
import com.shindev.huaweicamera.dialog.ShutterDelayDialog;
import com.shindev.huaweicamera.dialog.SoundKeyDialog;
import com.shindev.huaweicamera.dialog.VideoSizeDialog;

public class SettingFragment extends Fragment {

    private MainActivity activity;
    private CameraPara para;

    private TextView txt_shtter_delay, txt_sound_key, txt_photo_size, txt_cross_pattern, txt_video_size;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        activity = (MainActivity) getActivity();
        initUIViews(view);
        return view;
    }

    private void initUIViews(final View view) {
        para = CameraPara.getCameraPara(activity);

        Switch swt_sdcard = view.findViewById(R.id.swt_setting_sdcard);
        swt_sdcard.setChecked(para.isSDCardSave);
        swt_sdcard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                para.isSDCardSave = isChecked;
                para.saveCameraPara(activity);
            }
        });

        Switch swt_shutter_sound = view.findViewById(R.id.swt_setting_shutter_sound);
        swt_shutter_sound.setChecked(para.isShutterSound);
        swt_shutter_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                para.isShutterSound = isChecked;
                para.saveCameraPara(activity);
            }
        });

        Switch swt_mirror_effect = view.findViewById(R.id.swt_setting_mirror_effect);
        swt_mirror_effect.setChecked(para.isMirrorEffect);
        swt_mirror_effect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                para.isMirrorEffect = isChecked;
                para.saveCameraPara(activity);
            }
        });

        Switch swt_touch_effect = view.findViewById(R.id.swt_setting_touch_effect);
        swt_touch_effect.setChecked(para.isMirrorEffect);
        swt_touch_effect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                para.isTouchFocus = isChecked;
                para.saveCameraPara(activity);
            }
        });

        Switch swt_sound_record = view.findViewById(R.id.swt_setting_sound_record);
        swt_sound_record.setChecked(para.isSoundRecord);
        swt_sound_record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                para.isSoundRecord = isChecked;
                para.saveCameraPara(activity);
            }
        });

        txt_shtter_delay = view.findViewById(R.id.txt_setting_shutter_delay);
        changeShutterDelay();

        view.findViewById(R.id.llt_setting_shutter_delay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShutterDelayDialog dialog = new ShutterDelayDialog(activity);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        para = CameraPara.getCameraPara(activity);
                        changeShutterDelay();
                    }
                });
            }
        });

        txt_sound_key = view.findViewById(R.id.txt_setting_sound_key);
        changeSoundKey();

        view.findViewById(R.id.llt_setting_sound_key).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundKeyDialog dialog = new SoundKeyDialog(activity);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        para = CameraPara.getCameraPara(activity);
                        changeSoundKey();
                    }
                });
            }
        });

        txt_photo_size = view.findViewById(R.id.txt_setting_photo_size);
        changePhotoSize();

        view.findViewById(R.id.llt_setting_photo_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoSizeDialog dialog = new PhotoSizeDialog(activity);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        para = CameraPara.getCameraPara(activity);
                        changePhotoSize();
                    }
                });
            }
        });

        txt_cross_pattern = view.findViewById(R.id.txt_setting_cross_pattern);
        changePhotoSize();

        view.findViewById(R.id.llt_setting_cross_pattern).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrossPatternDialog dialog = new CrossPatternDialog(activity);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        para = CameraPara.getCameraPara(activity);
                        changeCrossPattern();
                    }
                });
            }
        });

        txt_video_size = view.findViewById(R.id.txt_setting_video_size);
        changeVideoSize();

        view.findViewById(R.id.llt_setting_video_size).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoSizeDialog dialog = new VideoSizeDialog(activity);
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        para = CameraPara.getCameraPara(activity);
                        changeVideoSize();
                    }
                });
            }
        });

        view.findViewById(R.id.llt_setting_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.alert_reset_content);
                builder.setCancelable(true);

                builder.setPositiveButton(
                        R.string.alert_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                para = new CameraPara();
                                para.saveCameraPara(getActivity());
                                initUIViews(view);
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        R.string.alert_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });
    }

    private void changeVideoSize() {
        String[] str = {
                getString(R.string.dia_16M),
                getString(R.string.dia_12M),
                getString(R.string.dia_8M),
                getString(R.string.dia_4M),
                getString(R.string.dia_2M)
        };
        txt_video_size.setText(str[para.videosize.ordinal()]);
    }

    private void changeCrossPattern() {
        String[] str = {
                getString(R.string.frg_none),
                getString(R.string.dia_rect_pattern),
                getString(R.string.dia_gold_rate_pattern),
                getString(R.string.dia_left_rate_pattern),
                getString(R.string.dia_right_rate_pattern)
        };
        txt_cross_pattern.setText(str[para.crosspattern.ordinal()]);
    }

    private void changePhotoSize() {
        String[] str = {
                getString(R.string.dia_16M),
                getString(R.string.dia_12M),
                getString(R.string.dia_8M),
                getString(R.string.dia_4M),
                getString(R.string.dia_2M)
        };
        txt_photo_size.setText(str[para.photosize.ordinal()]);
    }

    private void changeShutterDelay() {
        String[] str = {
                getString(R.string.frg_none),
                getString(R.string.time_second),
                getString(R.string.time_five),
                getString(R.string.time_ten)
        };
        txt_shtter_delay.setText(str[para.shutterdelay.ordinal()]);
    }

    private void changeSoundKey() {
        String[] str = {
                getString(R.string.frg_default),
                getString(R.string.frg_setting_video),
                getString(R.string.dia_zoom)
        };
        txt_sound_key.setText(str[para.soundkey.ordinal()]);
    }

}

package com.shindev.huaweicamera.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shindev.huaweicamera.R;
import com.shindev.huaweicamera.common.CameraPara;

import java.util.ArrayList;

public class SoundKeyDialog extends Dialog {

    private Activity activity;
    private CameraPara para;

    public SoundKeyDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
        para = CameraPara.getCameraPara(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sound_key);

        initUIViews();
    }

    @SuppressLint("ResourceType")
    private void initUIViews() {
        RadioGroup radioGroup = findViewById(R.id.rad_sound_key);
        int count = radioGroup.getChildCount();
        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listOfRadioButtons.add((RadioButton)o);
            }
        }
        RadioButton button = listOfRadioButtons.get(para.soundkey.ordinal());
        button.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selIndex = 0;
                switch (checkedId) {
                    case R.id.rab_sound_default:
                        selIndex = 0;
                        break;
                    case R.id.rab_sound_video:
                        selIndex = 1;
                        break;
                    case R.id.rab_sound_zoom:
                        selIndex = 2;
                        break;
                }

                para.soundkey = CameraPara.SOUNDKEY.values()[selIndex];
                para.saveCameraPara(activity);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

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

import com.shindev.huaweicamera.common.CameraPara;
import com.shindev.huaweicamera.R;

import java.util.ArrayList;

public class ShutterDelayDialog extends Dialog {

    private Activity activity;
    private CameraPara para;

    public ShutterDelayDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
        para = CameraPara.getCameraPara(this.activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_shutter_delay);

        initUIViews();
    }

    @SuppressLint("ResourceType")
    private void initUIViews() {
        RadioGroup radioTimeGroup = findViewById(R.id.rad_shutter_delay);
        int count = radioTimeGroup.getChildCount();
        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View o = radioTimeGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listOfRadioButtons.add((RadioButton)o);
            }
        }
        RadioButton button = listOfRadioButtons.get(para.shutterdelay.ordinal());
        button.setChecked(true);
        radioTimeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selIndex = 0;
                switch (checkedId) {
                    case R.id.rab_shutter_none:
                        selIndex = 0;
                        break;
                    case R.id.rab_shutter_2:
                        selIndex = 1;
                        break;
                    case R.id.rab_shutter_5:
                        selIndex = 2;
                        break;
                    case R.id.rab_shutter_10:
                        selIndex = 3;
                        break;
                }

                para.shutterdelay = CameraPara.SHUTTERDELAY.values()[selIndex];
                para.saveCameraPara(activity);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

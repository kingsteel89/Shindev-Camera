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

public class PhotoSizeDialog extends Dialog {

    private Activity activity;
    private CameraPara para;

    public PhotoSizeDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
        para = CameraPara.getCameraPara(this.activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_photo_size);

        initUIViews();
    }

    @SuppressLint("ResourceType")
    private void initUIViews() {
        RadioGroup radioGroup = findViewById(R.id.rad_photo_size);
        int count = radioGroup.getChildCount();
        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            View o = radioGroup.getChildAt(i);
            if (o instanceof RadioButton) {
                listOfRadioButtons.add((RadioButton)o);
            }
        }
        RadioButton button = listOfRadioButtons.get(para.photosize.ordinal());
        button.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selIndex = 0;
                switch (checkedId) {
                    case R.id.rab_photo_16:
                        selIndex = 0;
                        break;
                    case R.id.rab_photo_12:
                        selIndex = 1;
                        break;
                    case R.id.rab_photo_8:
                        selIndex = 2;
                        break;
                    case R.id.rab_photo_4:
                        selIndex = 3;
                        break;
                    case R.id.rab_photo_2:
                        selIndex = 4;
                        break;
                }

                para.photosize = CameraPara.CAMERASIZE.values()[selIndex];
                para.saveCameraPara(activity);
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}

package com.shindev.huaweicamera.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shindev.huaweicamera.R;
import com.shindev.huaweicamera.camfilter.utils.OpenGlUtils;
import com.shindev.huaweicamera.camfilter.widget.LuoGLCameraView;
import com.shindev.huaweicamera.common.Global;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

public class SurfaceFragment extends Fragment {
    private LuoGLCameraView cameraView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_surface, container, false);
        initFragmentUI(mainView);

        return mainView;
    }

    private void initFragmentUI(View mainView) {
        String licenseText = "hMPthC0oBIbtMp515TWb9jZvrLAKWIMvA4Dhf03n51QvnJr7jZowVe86d0WwU0NK9QGRFaXQn628fRu941qyr3FtsI5R7Y6v1XEpL6YvQNWQCkFEt1SAb0hyawimOYf1tfG2lIaNE63c5e+OxXssOVUWvw8tOr2glVwWVzh79NmZMahrnS8l69SoeoXLMKCYlvAt/qJFFk4+6Aq3QvOv3o72fq5p90yty+YWg7o0HirZpMSP9P5/DHYPFqR/ud7twTJ+Yo2+ZzYvodqRQbGG0HseZn8Xpt7fZdFuZbc2HGRMVk56vNDMRlcGZZXAjENk7m2UMhi1ohhuSf4WmIgXCZFiJXvYFByaY625gXKtEI7+b7t81nWQYHP9BEbzURwL";
        XJGArSdkApi.XJGARSDKInitialization(getContext(),licenseText,"DoctorLuoInvitedUser:teacherluo", "LuoInvitedCompany:www.xiaojigou.cn");

        XJGArSdkApi.XJGARSDKSetOptimizationMode(2);
        XJGArSdkApi.XJGARSDKSetShowStickerPapers(false);
        XJGArSdkApi.XJGARSDKSetSkinSmoothParam(0);
        XJGArSdkApi.XJGARSDKSetWhiteSkinParam(0);
        XJGArSdkApi.XJGARSDKSetRedFaceParam(0);

        cameraView = mainView.findViewById(R.id.glsurfaceview_camera);

        OpenGlUtils.mContext = getContext();
    }

    public void setFilter(Global.CameraFilterType type) {
        cameraView.setFilter(type);
    }

}

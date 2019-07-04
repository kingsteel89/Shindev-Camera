package com.shindev.huaweicamera.camfilter.advanced;

import android.opengl.GLES20;

import com.shindev.huaweicamera.MainActivity;
import com.shindev.huaweicamera.R;
import com.shindev.huaweicamera.camfilter.LuoGPUImgBaseFilter;
import com.shindev.huaweicamera.camfilter.utils.OpenGlUtils;


public class MagicNoneFilter extends LuoGPUImgBaseFilter {

	public MagicNoneFilter(){
		super(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
	}

}

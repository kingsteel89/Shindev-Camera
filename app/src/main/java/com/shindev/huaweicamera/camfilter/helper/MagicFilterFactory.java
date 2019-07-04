package com.shindev.huaweicamera.camfilter.helper;

import android.content.Context;

import com.shindev.huaweicamera.camfilter.LuoGPUImgBaseFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicCoolFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicEarlyBirdFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicFairytaleFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicInkwellFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicNoneFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicNostalgiaFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicSketchFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicToasterFilter;
import com.shindev.huaweicamera.camfilter.advanced.MagicWaldenFilter;
import com.shindev.huaweicamera.common.Global;

public class MagicFilterFactory{
	
	private static Global.CameraFilterType filterType = Global.CameraFilterType.NONE;
	
	public static LuoGPUImgBaseFilter initFilters(Global.CameraFilterType type, Context context){
		filterType = type;
		switch (type) {
			case WALDEN:
				return new MagicWaldenFilter(context);
			case EARLYBIRD:
				return new MagicEarlyBirdFilter(context);
			case INKWELL:
				return new MagicInkwellFilter(context);
			case TOASTERO:
				return new MagicToasterFilter(context);
			case COOL:
				return new MagicCoolFilter();
			case NOSTALGIA:
				return new MagicNostalgiaFilter();
			case FIRYTALE:
				return new MagicFairytaleFilter(context);
			case SKETCH:
				return new MagicSketchFilter();
			default:
				return new MagicNoneFilter();
		}
	}
	
	public Global.CameraFilterType getCurrentFilterType(){
		return filterType;
	}
}

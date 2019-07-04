/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shindev.huaweicamera.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;

import com.shindev.huaweicamera.camfilter.CameraController;
import com.shindev.huaweicamera.camfilter.CameraController2;
import com.shindev.huaweicamera.camfilter.CameraControllerException;
import com.shindev.huaweicamera.camfilter.CameraEngine;
import com.shindev.huaweicamera.camfilter.utils.OpenGlUtils;
import com.shindev.huaweicamera.camfilter.utils.Rotation;
import com.shindev.huaweicamera.camfilter.utils.TextureRotationUtil;
import com.shindev.huaweicamera.camfilter.widget.LuoGLBaseView;

/**
 * A {@link TextureView} that can be adjusted to a specified aspect ratio.
 */
public class AutoFitTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private Activity activity;

    public static CameraController2 cameraController;

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public AutoFitTextureView(Context context) {
        this(context, null);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setSurfaceTextureListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        cameraController = new CameraController2(this, width, height, 1);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        //
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}

package com.shindev.huaweicamera.camfilter.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.shindev.huaweicamera.camfilter.CameraControllerException;
import com.shindev.huaweicamera.camfilter.CameraEngine;
import com.shindev.huaweicamera.camfilter.LuoGPUCameraInputFilter;
import com.shindev.huaweicamera.camfilter.LuoGPUImgBaseFilter;
import com.shindev.huaweicamera.camfilter.SavePictureTask;
import com.shindev.huaweicamera.camfilter.utils.OpenGlUtils;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LuoGLCameraView extends LuoGLBaseView {
    public static CameraEngine cameraEngine;

    private LuoGPUCameraInputFilter cameraInputFilter;
    private SurfaceTexture surfaceTexture;

    public LuoGLCameraView(Context context) {
        this(context, null);
    }

    public LuoGLCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getHolder().addCallback(this);
        scaleType = ScaleType.CENTER_CROP;
        XJGArSdkApi.XJGARSDKReleaseAllOpenglResources();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if(cameraInputFilter == null)
            cameraInputFilter = new LuoGPUCameraInputFilter();
        cameraInputFilter.init();

        if(filter == null)
            filter = new LuoGPUImgBaseFilter();
        filter.init();

        if (textureId == OpenGlUtils.NO_TEXTURE) {
            textureId = OpenGlUtils.getExternalOESTextureID();
            if (textureId != OpenGlUtils.NO_TEXTURE) {
                surfaceTexture = new SurfaceTexture(textureId);
                surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        openCamera();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        if(surfaceTexture == null)
            return;
        surfaceTexture.updateTexImage();
        float[] mtx = new float[16];
        surfaceTexture.getTransformMatrix(mtx);
        cameraInputFilter.setTextureTransformMatrix(mtx);
        int resultTexture=0;
        resultTexture = cameraInputFilter.onDrawToTexture(textureId,gLCubeBuffer,gLTextureBuffer);
//        int[] resultTex = new int[1];
//        XJGArSdkApi.XJGARSDKRenderGLTexToGLTex(resultTexture,imageWidth,imageHeight,resultTex);
//        resultTexture =  resultTex[0];
//        filter.onDrawFrame(resultTexture,gLCubeBuffer,gLTextureBuffer);

//        XJGArSdkApi.XJGARSDKRenderGLTexture(resultTexture,imageWidth,imageHeight);
        GLES20.glViewport(0,0,surfaceWidth, surfaceHeight);
        int finalTexture = XJGArSdkApi.XJGARSDKRenderGLTexToGLTex(resultTexture, imageWidth, imageHeight);
        GLES20.glViewport(0,0,surfaceWidth, surfaceHeight);
//        filter.onDrawFrame(finalTexture,gLCubeBuffer,gLTextureBuffer);
        filter.onDrawFrame(finalTexture,filter.mGLCubeBuffer,filter.mGLTextureBuffer);



        long timer = System.currentTimeMillis();
        timeCounter.add(timer);
        while (start < timeCounter.size() && timeCounter.get(start) < timer - 1000) {
            start++;
        }
        fps = timeCounter.size() - start;
        if (start > 100) {
            timeCounter = timeCounter.subList(start,
                    timeCounter.size() - 1);
            start = 0;
        }

        int targetFPS = 30;
        if(fps>targetFPS)
        {
            float targetFrameTime = 1000.f/(float)targetFPS;
            float currentFrameTime = 1000.f/(float)fps;
            float timeToSleep =  targetFrameTime - currentFrameTime;
            if(timeToSleep>1.0)
            {
                try {
                    Thread.sleep((long)timeToSleep);//休眠
                }
                catch (InterruptedException e) {
                }
            }
        }

    }

    List<Long> timeCounter = new ArrayList<Long>();
    int start = 0;
    int fps =0;

    private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener
            = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            requestRender();
        }
    };

    //打开相机，同时通知相机输入过滤器大小发生了变化，然后调整视图与图像的大小，同时设置相机预览的对象为surfacetexture
    private void openCamera(){
        try {
            cameraEngine = new CameraEngine(1);
            CameraEngine.CameraEngineInfo info = cameraEngine.getCameraInfo();
            if(info.orientation == 90 || info.orientation == 270){
                imageWidth = info.previewHeight;
                imageHeight = info.previewWidth;
            }else{
                imageWidth = info.previewWidth;
                imageHeight = info.previewHeight;
            }

            int orientation = info.orientation;
            orientation = (orientation + 180) % 360;
            adjustSize(orientation, info.isFront, true);

            cameraInputFilter.onInputSizeChanged(imageWidth, imageHeight);
            cameraInputFilter.initCameraFrameBuffer(imageWidth, imageHeight);
            filter.onInputSizeChanged(imageWidth,imageHeight);

            if(surfaceTexture != null)
                cameraEngine.startPreview(surfaceTexture);
        } catch (CameraControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        cameraEngine.releaseCamera();
        cameraInputFilter.destroyFramebuffers();
        XJGArSdkApi.XJGARSDKReleaseAllOpenglResources();
    }

    public void changeRecordingState(boolean isRecording) {
//        recordingEnabled = isRecording;
    }

    @Override
    public void savePicture(final SavePictureTask savePictureTask) {
        cameraEngine.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                cameraEngine.stopPreview();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap photo = drawPhoto(bitmap, cameraEngine.getCameraInfo().isFront);
                        GLES20.glViewport(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        if (photo != null) {
                            savePictureTask.execute(photo);
//                            savePictureTask.execute(bitmap);
                        }
                    }
                });
                cameraEngine.startPreview();
            }
        });
    }

    private Bitmap drawPhoto(Bitmap bitmap,boolean isRotated){
        Bitmap result;
        if(isRotated)// 自拍相机
            result = XJGArSdkApi.XJGARSDKRenderImage(bitmap,true);
        else
            result = XJGArSdkApi.XJGARSDKRenderImage(bitmap,false);
        return result;
    }

}

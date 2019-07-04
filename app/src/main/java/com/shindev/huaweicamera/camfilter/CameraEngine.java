package com.shindev.huaweicamera.camfilter;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Handler;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

//对相机进行操作的：如打开相机、设置预览尺寸、设置回调函数、切换相机，截图等
public class CameraEngine extends CameraController {
    private Camera camera;
    private int display_orientation = 0;
    private Camera.CameraInfo camera_info = new Camera.CameraInfo();

    private static SurfaceTexture surfaceTexture;

    @Override
    public void release() {
        camera.release();
        camera = null;
    }

    public CameraEngine(int cameraId) throws CameraControllerException {
        super(cameraId);
        try {
            camera = Camera.open(cameraId);
        }
        catch(RuntimeException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
        Camera.getCameraInfo(cameraId, camera_info);
    }

    public Camera getCamera(){
        return camera;
    }

    public boolean openCamera(){
        if(camera == null){
            try{
                camera = Camera.open(cameraId);
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public boolean openCamera(int id){
        if(camera == null){
            try{
                camera = Camera.open(id);
                cameraId = id;
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public void releaseCamera(){
        if(camera != null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void resumeCamera(){
        openCamera();
    }

    public void setParameters(Parameters parameters){
        camera.setParameters(parameters);
    }

    public Parameters getParameters(){
        if(camera != null)
            return camera.getParameters();
        return null;
    }

    public void switchCamera(){
        releaseCamera();
        cameraId = cameraId == 0 ? 1 : 0;
        openCamera(cameraId);
        startPreview(surfaceTexture);
    }

    private void setDefaultParameters(){
        Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
//        Size previewSize = getLargePreviewSize(camera);
        Camera.Size previewSize = getNearestPreviewSize(camera, 1280, 720);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
//        Size pictureSize = getLargePictureSize(camera);
        Camera.Size pictureSize = getNearestPictureSize(camera, 1280, 720);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        parameters.setRotation(90);
        camera.setParameters(parameters);
    }

    @Override
    public CameraController.Size getPreviewSize() {
        Camera.Parameters parameters = this.getParameters();
        Camera.Size camera_size = parameters.getPreviewSize();
        return new CameraController.Size(camera_size.width, camera_size.height);
    }

    @Override
    public CameraController.Size getPictureSize() {
        Camera.Parameters parameters = this.getParameters();
        Camera.Size camera_size = parameters.getPictureSize();
        return new CameraController.Size(camera_size.width, camera_size.height);
    }

    @Override
    public void setPictureSize(int width, int height) {

    }

    public void startPreview(SurfaceTexture surfaceTexture){
        if(camera != null)
            try {
                camera.setPreviewTexture(surfaceTexture);
                CameraEngine.surfaceTexture = surfaceTexture;
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void startPreview(){
        if(camera != null)
            camera.startPreview();
    }

    public void stopPreview(){
        camera.stopPreview();
    }

    //by luo
    private static int mcameraId = 0;
    public static int getOrientation(){
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(mcameraId, cameraInfo);
        return cameraInfo.orientation;
    }

    //by luo
    public static boolean isFlipHorizontal(){
        return true;
        //return LuoCameraEngine.getCameraInfo().facing == CameraInfo.CAMERA_FACING_FRONT ? true : false;
    }

    public void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback jpegCallback){
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public static class CameraEngineInfo {

        public int previewWidth;

        public int previewHeight;

        public int orientation;

        public boolean isFront;

        public int pictureWidth;

        public int pictureHeight;
    }

    public CameraEngineInfo getCameraInfo(){
        CameraEngineInfo info = new CameraEngineInfo();
        CameraController.Size size = getPreviewSize();
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        info.previewWidth = size.width;
        info.previewHeight = size.height;
        info.orientation = cameraInfo.orientation;
        info.isFront = cameraId == 1 ? true : false;
        size = getPictureSize();
        info.pictureWidth = size.width;
        info.pictureHeight = size.height;
        return info;
    }

    //获取相机所支持的最接近的图像的尺寸
    public Camera.Size getNearestPictureSize(Camera camera, int targetWidth, int targetHeight){
        if(camera != null){

            float minDistance = 1000000000;
            int minIndex =-1;
            List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes();
            Camera.Size temp;
            for(int i = 0;i < sizes.size();i ++){

                float curWidth = sizes.get(i).width;
                float curHeight = sizes.get(i).height;
                float distance = (curWidth - targetWidth)*(curWidth - targetWidth)
                        + (curHeight - targetHeight)*(curHeight - targetHeight);

                float scale = (float)(sizes.get(i).height) / sizes.get(i).width;
//                if(scale < 0.6f && scale > 0.5f)
//                {
//
//                }

                if(distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }

            temp = sizes.get(minIndex);
            return temp;
        }
        return null;
    }

    //获取相机所支持的最接近的预览尺寸的大小
    public Camera.Size getNearestPreviewSize(Camera camera, int targetWidth, int targetHeight){
        if(camera != null){
            //获取相机的预览尺寸列表，查找最接近的

            float minDistance = 1000000000;
            int minIndex =-1;
            List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
            Camera.Size temp;
            for(int i = 0;i < sizes.size();i ++){

                float curWidth = sizes.get(i).width;
                float curHeight = sizes.get(i).height;
                float distance = (curWidth - targetWidth)*(curWidth - targetWidth)
                        + (curHeight - targetHeight)*(curHeight - targetHeight);

//                float scale = (float)(sizes.get(i).height) / sizes.get(i).width;
                if(distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }

            temp = sizes.get(minIndex);
            return temp;
        }
        return null;
    }

    //获取相机所支持的最大的图像的尺寸
    public Camera.Size getLargePictureSize(Camera camera){
        if(camera != null){
            List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes();
            Camera.Size temp = sizes.get(0);
            for(int i = 1;i < sizes.size();i ++){
                float scale = (float)(sizes.get(i).height) / sizes.get(i).width;
                if(temp.width < sizes.get(i).width && scale < 0.6f && scale > 0.5f)
                    temp = sizes.get(i);
            }
            return temp;
        }
        return null;
    }

    //获取相机所支持的最大的预览尺寸的大小
    public Camera.Size getLargePreviewSize(Camera camera){
        if(camera != null){
            //获取相机的预览尺寸列表，查找最大的
            List<Camera.Size> sizes = camera.getParameters().getSupportedPreviewSizes();
            Camera.Size temp = sizes.get(0);
            for(int i = 1;i < sizes.size();i ++){
                if(temp.width < sizes.get(i).width)
                    temp = sizes.get(i);
            }
            return temp;
        }
        return null;
    }

    // Add Cameracontroller Feature
    private List<String> convertFlashModesToValues(List<String> supported_flash_modes) {
        List<String> output_modes = new Vector<>();
        if( supported_flash_modes != null ) {
            // also resort as well as converting
            if( supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_OFF) ) {
                output_modes.add("flash_off");
            }
            if( supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_AUTO) ) {
                output_modes.add("flash_auto");
            }
            if( supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_ON) ) {
                output_modes.add("flash_on");
            }
            if( supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_TORCH) ) {
                output_modes.add("flash_torch");
            }
            if( supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_RED_EYE) ) {
                output_modes.add("flash_red_eye");
            }
        }
        return output_modes;
    }

    private List<String> convertFocusModesToValues(List<String> supported_focus_modes) {
        List<String> output_modes = new Vector<>();
        if( supported_focus_modes != null ) {
            // also resort as well as converting
            if( supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_AUTO) ) {
                output_modes.add("focus_mode_auto");
            }
            if( supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY) ) {
                output_modes.add("focus_mode_infinity");
            }
            if( supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_MACRO) ) {
                output_modes.add("focus_mode_macro");
            }
            if( supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_AUTO) ) {
                output_modes.add("focus_mode_locked");
            }
            if( supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_FIXED) ) {
                output_modes.add("focus_mode_fixed");
            }
            if( supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_EDOF) ) {
                output_modes.add("focus_mode_edof");
            }
            if( supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) ) {
                output_modes.add("focus_mode_continuous_video");
            }
        }
        return output_modes;
    }

    public String getAPI() {
        return "Camera";
    }

    public CameraFeatures getCameraFeatures() {
        Camera.Parameters parameters = this.getParameters();
        CameraFeatures camera_features = new CameraFeatures();
        camera_features.is_zoom_supported = parameters.isZoomSupported();
        if( camera_features.is_zoom_supported ) {
            camera_features.max_zoom = parameters.getMaxZoom();
            try {
                camera_features.zoom_ratios = parameters.getZoomRatios();
            }
            catch(NumberFormatException e) {
                // crash java.lang.NumberFormatException: Invalid int: " 500" reported in v1.4 on device "es209ra", Android 4.1, 3 Jan 2014
                // this is from java.lang.Integer.invalidInt(Integer.java:138) - unclear if this is a bug in Open Camera, all we can do for now is catch it
                e.printStackTrace();
                camera_features.is_zoom_supported = false;
                camera_features.max_zoom = 0;
                camera_features.zoom_ratios = null;
            }
        }

        camera_features.supports_face_detection = parameters.getMaxNumDetectedFaces() > 0;

        // get available sizes
        List<Camera.Size> camera_picture_sizes = parameters.getSupportedPictureSizes();
        camera_features.picture_sizes = new ArrayList<CameraController.Size>();
        //camera_features.picture_sizes.add(new CameraController.Size(1920, 1080)); // test
        for(Camera.Size camera_size : camera_picture_sizes) {
            camera_features.picture_sizes.add(new CameraController.Size(camera_size.width, camera_size.height));
        }

        //camera_features.supported_flash_modes = parameters.getSupportedFlashModes(); // Android format
        List<String> supported_flash_modes = parameters.getSupportedFlashModes(); // Android format
        camera_features.supported_flash_values = convertFlashModesToValues(supported_flash_modes); // convert to our format (also resorts)

        List<String> supported_focus_modes = parameters.getSupportedFocusModes(); // Android format
        camera_features.supported_focus_values = convertFocusModesToValues(supported_focus_modes); // convert to our format (also resorts)
        camera_features.max_num_focus_areas = parameters.getMaxNumFocusAreas();

        camera_features.is_exposure_lock_supported = parameters.isAutoExposureLockSupported();

        camera_features.is_video_stabilization_supported = parameters.isVideoStabilizationSupported();

        camera_features.min_exposure = parameters.getMinExposureCompensation();
        camera_features.max_exposure = parameters.getMaxExposureCompensation();
        try {
            camera_features.exposure_step = parameters.getExposureCompensationStep();
        }
        catch(Exception e) {
            // received a NullPointerException from StringToReal.parseFloat() beneath getExposureCompensationStep() on Google Play!
            e.printStackTrace();
            camera_features.exposure_step = 1.0f/3.0f; // make up a typical example
        }

        List<Camera.Size> camera_video_sizes = parameters.getSupportedVideoSizes();
        if( camera_video_sizes == null ) {
            // if null, we should use the preview sizes - see http://stackoverflow.com/questions/14263521/android-getsupportedvideosizes-allways-returns-null
            camera_video_sizes = parameters.getSupportedPreviewSizes();
        }
        camera_features.video_sizes = new ArrayList<>();
        //camera_features.video_sizes.add(new CameraController.Size(1920, 1080)); // test
        for(Camera.Size camera_size : camera_video_sizes) {
            camera_features.video_sizes.add(new CameraController.Size(camera_size.width, camera_size.height));
        }

        List<Camera.Size> camera_preview_sizes = parameters.getSupportedPreviewSizes();
        camera_features.preview_sizes = new ArrayList<>();
        for(Camera.Size camera_size : camera_preview_sizes) {
            camera_features.preview_sizes.add(new CameraController.Size(camera_size.width, camera_size.height));
        }

        // Camera.canDisableShutterSound requires JELLY_BEAN_MR1 or greater
        camera_features.can_disable_shutter_sound = camera_info.canDisableShutterSound;

        return camera_features;
    }

    public long getDefaultExposureTime() {
        // not supported for CameraController1
        return 0l;
    }

    // important, from docs:
    // "Changing scene mode may override other parameters (such as flash mode, focus mode, white balance).
    // For example, suppose originally flash mode is on and supported flash modes are on/off. In night
    // scene mode, both flash mode and supported flash mode may be changed to off. After setting scene
    // mode, applications should call getParameters to know if some parameters are changed."
    public SupportedValues setSceneMode(String value) {
        String default_value = getDefaultSceneMode();
        Camera.Parameters parameters = this.getParameters();
        List<String> values = parameters.getSupportedSceneModes();
		/*{
			// test
			values = new ArrayList<String>();
			values.add("auto");
		}*/
        SupportedValues supported_values = checkModeIsSupported(values, value, default_value);
        if( supported_values != null ) {
            if( !parameters.getSceneMode().equals(supported_values.selected_value) ) {
                parameters.setSceneMode(supported_values.selected_value);
                setParameters(parameters);
            }
        }
        return supported_values;
    }

    public String getSceneMode() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getSceneMode();
    }

    public SupportedValues setColorEffect(String value) {
        String default_value = getDefaultColorEffect();
        Camera.Parameters parameters = this.getParameters();
        List<String> values = parameters.getSupportedColorEffects();
        SupportedValues supported_values = checkModeIsSupported(values, value, default_value);
        if( supported_values != null ) {
            if( !parameters.getColorEffect().equals(supported_values.selected_value) ) {
                parameters.setColorEffect(supported_values.selected_value);
                setParameters(parameters);
            }
        }
        return supported_values;
    }

    public String getColorEffect() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getColorEffect();
    }

    public SupportedValues setWhiteBalance(String value) {
        String default_value = getDefaultWhiteBalance();
        Camera.Parameters parameters = this.getParameters();
        List<String> values = parameters.getSupportedWhiteBalance();
        SupportedValues supported_values = checkModeIsSupported(values, value, default_value);
        if( supported_values != null ) {
            if( !parameters.getWhiteBalance().equals(supported_values.selected_value) ) {
                parameters.setWhiteBalance(supported_values.selected_value);
                setParameters(parameters);
            }
        }
        return supported_values;
    }

    public String getWhiteBalance() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getWhiteBalance();
    }

    @Override
    public SupportedValues setISO(String value) {
        return null;
    }

    @Override
    public String getISOKey() {
        return null;
    }

    @Override
    public int getISO() {
        return 0;
    }

    @Override
    public boolean setISO(int iso) {
        return false;
    }

    @Override
    public long getExposureTime() {
        return 0;
    }

    @Override
    public boolean setExposureTime(long exposure_time) {
        return false;
    }

    public void setPreviewSize(int width, int height) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setPreviewSize(width, height);
        setParameters(parameters);
    }

    public void setVideoStabilization(boolean enabled) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setVideoStabilization(enabled);
        setParameters(parameters);
    }

    public boolean getVideoStabilization() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getVideoStabilization();
    }

    public int getJpegQuality() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getJpegQuality();
    }

    public void setJpegQuality(int quality) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setJpegQuality(quality);
        setParameters(parameters);
    }

    public int getZoom() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getZoom();
    }

    public void setZoom(int value) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setZoom(value);
        setParameters(parameters);
    }

    public int getExposureCompensation() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getExposureCompensation();
    }

    // Returns whether exposure was modified
    public boolean setExposureCompensation(int new_exposure) {
        Camera.Parameters parameters = this.getParameters();
        int current_exposure = parameters.getExposureCompensation();
        if( new_exposure != current_exposure ) {
            parameters.setExposureCompensation(new_exposure);
            setParameters(parameters);
            return true;
        }
        return false;
    }

    public void setPreviewFpsRange(int min, int max) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setPreviewFpsRange(min, max);
        setParameters(parameters);
    }

    public List<int []> getSupportedPreviewFpsRange() {
        Camera.Parameters parameters = this.getParameters();
        try {
            List<int []> fps_ranges = parameters.getSupportedPreviewFpsRange();
            return fps_ranges;
        }
        catch(StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setFocusValue(String focus_value) {
        Camera.Parameters parameters = this.getParameters();
        if( focus_value.equals("focus_mode_auto") || focus_value.equals("focus_mode_locked") ) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        else if( focus_value.equals("focus_mode_infinity") ) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
        }
        else if( focus_value.equals("focus_mode_macro") ) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        }
        else if( focus_value.equals("focus_mode_fixed") ) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
        }
        else if( focus_value.equals("focus_mode_edof") ) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
        }
        else if( focus_value.equals("focus_mode_continuous_video") ) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        setParameters(parameters);
    }

    private String convertFocusModeToValue(String focus_mode) {
        // focus_mode may be null on some devices; we return ""
        String focus_value = "";
        if( focus_mode == null ) {
            // ignore, leave focus_value at null
        }
        else if( focus_mode.equals(Camera.Parameters.FOCUS_MODE_AUTO) ) {
            focus_value = "focus_mode_auto";
        }
        else if( focus_mode.equals(Camera.Parameters.FOCUS_MODE_INFINITY) ) {
            focus_value = "focus_mode_infinity";
        }
        else if( focus_mode.equals(Camera.Parameters.FOCUS_MODE_MACRO) ) {
            focus_value = "focus_mode_macro";
        }
        else if( focus_mode.equals(Camera.Parameters.FOCUS_MODE_FIXED) ) {
            focus_value = "focus_mode_fixed";
        }
        else if( focus_mode.equals(Camera.Parameters.FOCUS_MODE_EDOF) ) {
            focus_value = "focus_mode_edof";
        }
        else if( focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) ) {
            focus_value = "focus_mode_continuous_video";
        }
        return focus_value;
    }

    @Override
    public String getFocusValue() {
        // returns "" if Parameters.getFocusMode() returns null
        Camera.Parameters parameters = this.getParameters();
        String focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play
        return convertFocusModeToValue(focus_mode);
    }

    @Override
    public float getFocusDistance() {
        return 0;
    }

    @Override
    public boolean setFocusDistance(float focus_distance) {
        return false;
    }

    private String convertFlashValueToMode(String flash_value) {
        String flash_mode = "";
        if( flash_value.equals("flash_off") ) {
            flash_mode = Camera.Parameters.FLASH_MODE_OFF;
        }
        else if( flash_value.equals("flash_auto") ) {
            flash_mode = Camera.Parameters.FLASH_MODE_AUTO;
        }
        else if( flash_value.equals("flash_on") ) {
            flash_mode = Camera.Parameters.FLASH_MODE_ON;
        }
        else if( flash_value.equals("flash_torch") ) {
            flash_mode = Camera.Parameters.FLASH_MODE_TORCH;
        }
        else if( flash_value.equals("flash_red_eye") ) {
            flash_mode = Camera.Parameters.FLASH_MODE_RED_EYE;
        }
        return flash_mode;
    }

    public void setFlashValue(String flash_value) {
        Camera.Parameters parameters = this.getParameters();
        if( parameters.getFlashMode() == null )
            return; // flash mode not supported
        final String flash_mode = convertFlashValueToMode(flash_value);
        if( flash_mode.length() > 0 && !flash_mode.equals(parameters.getFlashMode()) ) {
            if( parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH) && !flash_mode.equals(Camera.Parameters.FLASH_MODE_OFF) ) {
                // workaround for bug on Nexus 5 and Nexus 6 where torch doesn't switch off until we set FLASH_MODE_OFF
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                setParameters(parameters);
                // need to set the correct flash mode after a delay
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        Camera.Parameters parameters = getParameters();
                        parameters.setFlashMode(flash_mode);
                        setParameters(parameters);
                    }
                }, 100);
            }
            else {
                parameters.setFlashMode(flash_mode);
                setParameters(parameters);
            }
        }
    }

    private String convertFlashModeToValue(String flash_mode) {
        // flash_mode may be null, meaning flash isn't supported; we return ""
        String flash_value = "";
        if( flash_mode == null ) {
            // ignore, leave flash_value at null
        }
        else if( flash_mode.equals(Camera.Parameters.FLASH_MODE_OFF) ) {
            flash_value = "flash_off";
        }
        else if( flash_mode.equals(Camera.Parameters.FLASH_MODE_AUTO) ) {
            flash_value = "flash_auto";
        }
        else if( flash_mode.equals(Camera.Parameters.FLASH_MODE_ON) ) {
            flash_value = "flash_on";
        }
        else if( flash_mode.equals(Camera.Parameters.FLASH_MODE_TORCH) ) {
            flash_value = "flash_torch";
        }
        else if( flash_mode.equals(Camera.Parameters.FLASH_MODE_RED_EYE) ) {
            flash_value = "flash_red_eye";
        }
        return flash_value;
    }

    public String getFlashValue() {
        // returns "" if flash isn't supported
        Camera.Parameters parameters = this.getParameters();
        String flash_mode = parameters.getFlashMode(); // will be null if flash mode not supported
        return convertFlashModeToValue(flash_mode);
    }

    public void setRecordingHint(boolean hint) {
        Camera.Parameters parameters = this.getParameters();
        // Calling setParameters here with continuous video focus mode causes preview to not restart after taking a photo on Galaxy Nexus?! (fine on my Nexus 7).
        // The issue seems to specifically be with setParameters (i.e., the problem occurs even if we don't setRecordingHint).
        // In addition, I had a report of a bug on HTC Desire X, Android 4.0.4 where the saved video was corrupted.
        // This worked fine in 1.7, then not in 1.8 and 1.9, then was fixed again in 1.10
        // The only thing in common to 1.7->1.8 and 1.9-1.10, that seems relevant, was adding this code to setRecordingHint() and setParameters() (unclear which would have been the problem),
        // so we should be very careful about enabling this code again!
        // Update for v1.23: the bug with Galaxy Nexus has come back (see comments in Preview.setPreviewFps()) and is now unavoidable,
        // but I've still kept this check here - if nothing else, because it apparently caused video recording problems on other devices too.
        String focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play
        if( focus_mode != null && !focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) ) {
            parameters.setRecordingHint(hint);
            setParameters(parameters);
        }
    }

    public void setAutoExposureLock(boolean enabled) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setAutoExposureLock(enabled);
        setParameters(parameters);
    }

    public boolean getAutoExposureLock() {
        Camera.Parameters parameters = this.getParameters();
        if( !parameters.isAutoExposureLockSupported() )
            return false;
        return parameters.getAutoExposureLock();
    }

    public void setRotation(int rotation) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setRotation(rotation);
        setParameters(parameters);
    }

    public void setLocationInfo(Location location) {
        Camera.Parameters parameters = this.getParameters();
        parameters.removeGpsData();
        parameters.setGpsTimestamp(System.currentTimeMillis() / 1000); // initialise to a value (from Android camera source)
        parameters.setGpsLatitude(location.getLatitude());
        parameters.setGpsLongitude(location.getLongitude());
        parameters.setGpsProcessingMethod(location.getProvider()); // from http://boundarydevices.com/how-to-write-an-android-camera-app/
        if( location.hasAltitude() ) {
            parameters.setGpsAltitude(location.getAltitude());
        }
        else {
            // Android camera source claims we need to fake one if not present
            // and indeed, this is needed to fix crash on Nexus 7
            parameters.setGpsAltitude(0);
        }
        if( location.getTime() != 0 ) { // from Android camera source
            parameters.setGpsTimestamp(location.getTime() / 1000);
        }
        setParameters(parameters);
    }

    public void removeLocationInfo() {
        Camera.Parameters parameters = this.getParameters();
        parameters.removeGpsData();
        setParameters(parameters);
    }

    public void enableShutterSound(boolean enabled) {
        camera.enableShutterSound(enabled);
    }

    public boolean setFocusAndMeteringArea(List<CameraController.Area> areas) {
        List<Camera.Area> camera_areas = new ArrayList<Camera.Area>();
        for(CameraController.Area area : areas) {
            camera_areas.add(new Camera.Area(area.rect, area.weight));
        }
        Camera.Parameters parameters = this.getParameters();
        String focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play
        if( parameters.getMaxNumFocusAreas() != 0 && focus_mode != null && ( focus_mode.equals(Camera.Parameters.FOCUS_MODE_AUTO) || focus_mode.equals(Camera.Parameters.FOCUS_MODE_MACRO) || focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) || focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) ) ) {
            parameters.setFocusAreas(camera_areas);

            // also set metering areas
            if( parameters.getMaxNumMeteringAreas() > 0 ) {
                parameters.setMeteringAreas(camera_areas);
            }
            setParameters(parameters);

            return true;
        }
        else if( parameters.getMaxNumMeteringAreas() != 0 ) {
            parameters.setMeteringAreas(camera_areas);

            setParameters(parameters);
        }
        return false;
    }

    public void clearFocusAndMetering() {
        Camera.Parameters parameters = this.getParameters();
        boolean update_parameters = false;
        if( parameters.getMaxNumFocusAreas() > 0 ) {
            parameters.setFocusAreas(null);
            update_parameters = true;
        }
        if( parameters.getMaxNumMeteringAreas() > 0 ) {
            parameters.setMeteringAreas(null);
            update_parameters = true;
        }
        if( update_parameters ) {
            setParameters(parameters);
        }
    }

    public List<CameraController.Area> getFocusAreas() {
        Camera.Parameters parameters = this.getParameters();
        List<Camera.Area> camera_areas = parameters.getFocusAreas();
        if( camera_areas == null )
            return null;
        List<CameraController.Area> areas = new ArrayList<>();
        for(Camera.Area camera_area : camera_areas) {
            areas.add(new CameraController.Area(camera_area.rect, camera_area.weight));
        }
        return areas;
    }

    public List<CameraController.Area> getMeteringAreas() {
        Camera.Parameters parameters = this.getParameters();
        List<Camera.Area> camera_areas = parameters.getMeteringAreas();
        if( camera_areas == null )
            return null;
        List<CameraController.Area> areas = new ArrayList<>();
        for(Camera.Area camera_area : camera_areas) {
            areas.add(new CameraController.Area(camera_area.rect, camera_area.weight));
        }
        return areas;
    }

    public boolean supportsAutoFocus() {
        Camera.Parameters parameters = this.getParameters();
        String focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play from the below line (v1.7),
        // on Galaxy Tab 10.1 (GT-P7500), Android 4.0.3 - 4.0.4; HTC EVO 3D X515m (shooteru), Android 4.0.3 - 4.0.4
        if( focus_mode != null && ( focus_mode.equals(Camera.Parameters.FOCUS_MODE_AUTO) || focus_mode.equals(Camera.Parameters.FOCUS_MODE_MACRO) ) ) {
            return true;
        }
        return false;
    }

    public boolean focusIsVideo() {
        Camera.Parameters parameters = this.getParameters();
        String current_focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play
        boolean focus_is_video = current_focus_mode != null && current_focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        return focus_is_video;
    }

    @Override
    public
    void reconnect() throws CameraControllerException {
        try {
            camera.reconnect();
        }
        catch(IOException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
    }

    @Override
    public void setPreviewDisplay(SurfaceHolder holder) throws CameraControllerException {
        try {
            camera.setPreviewDisplay(holder);
        }
        catch(IOException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
    }

    @Override
    public void setPreviewTexture(SurfaceTexture texture) throws CameraControllerException {
        try {
            camera.setPreviewTexture(texture);
        }
        catch(IOException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
    }

    // returns false if RuntimeException thrown (may include if face-detection already started)
    public boolean startFaceDetection() {
        try {
            camera.startFaceDetection();
        }
        catch(RuntimeException e) {
            return false;
        }
        return true;
    }

    public void setFaceDetectionListener(final CameraController.FaceDetectionListener listener) {
        class CameraFaceDetectionListener implements Camera.FaceDetectionListener {
            @Override
            public void onFaceDetection(Camera.Face[] camera_faces, Camera camera) {
                Face [] faces = new Face[camera_faces.length];
                for(int i=0;i<camera_faces.length;i++) {
                    faces[i] = new Face(camera_faces[i].score, camera_faces[i].rect);
                }
                listener.onFaceDetection(faces);
            }
        }
        camera.setFaceDetectionListener(new CameraFaceDetectionListener());
    }

    public void autoFocus(final CameraController.AutoFocusCallback cb) {
        Camera.AutoFocusCallback camera_cb = new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                cb.onAutoFocus(success);
            }
        };
        try {
            camera.autoFocus(camera_cb);
        }
        catch(RuntimeException e) {
            // just in case? We got a RuntimeException report here from 1 user on Google Play:
            // 21 Dec 2013, Xperia Go, Android 4.1
            e.printStackTrace();
            // should call the callback, so the application isn't left waiting (e.g., when we autofocus before trying to take a photo)
            cb.onAutoFocus(false);
        }
    }

    public void cancelAutoFocus() {
        try {
            camera.cancelAutoFocus();
        }
        catch(RuntimeException e) {
            // had a report of crash on some devices, see comment at https://sourceforge.net/p/opencamera/tickets/4/ made on 20140520
            e.printStackTrace();
        }
    }

    public void takePicture(final CameraController.PictureCallback raw, final CameraController.PictureCallback jpeg, final ErrorCallback error) {
        Camera.ShutterCallback shutter = new Camera.ShutterCallback() {
            // don't do anything here, but we need to implement the callback to get the shutter sound (at least on Galaxy Nexus and Nexus 7)
            public void onShutter() {
                //
            }
        };
        Camera.PictureCallback camera_raw = raw == null ? null : new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera cam) {
                // n.b., this is automatically run in a different thread
                raw.onPictureTaken(data);
            }
        };
        Camera.PictureCallback camera_jpeg = jpeg == null ? null : new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera cam) {
                // n.b., this is automatically run in a different thread
                jpeg.onPictureTaken(data);
            }
        };

        try {
            camera.takePicture(shutter, camera_raw, camera_jpeg);
        }
        catch(RuntimeException e) {
            // just in case? We got a RuntimeException report here from 1 user on Google Play; I also encountered it myself once of Galaxy Nexus when starting up
            e.printStackTrace();
            error.onError();
        }
    }

    public void setDisplayOrientation(int degrees) {
        if( camera == null ) {
            // shouldn't be null if camera_controller is non-null, but have had reported crashes on Google Play?! Possibly release()ed in another thread?
            return;
        }
        int result = 0;
        if( camera_info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ) {
            result = (camera_info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        }
        else {
            result = (camera_info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
        this.display_orientation = result;
    }

    public int getDisplayOrientation() {
        return this.display_orientation;
    }

    public int getCameraOrientation() {
        return camera_info.orientation;
    }

    public boolean isFrontFacing() {
        return (camera_info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    public void unlock() {
        this.stopPreview(); // although not documented, we need to stop preview to prevent device freeze or video errors shortly after video recording starts on some devices (e.g., device freeze on Samsung Galaxy S2 - I could reproduce this on Samsung RTL; also video recording fails and preview becomes corrupted on Galaxy S3 variant "SGH-I747-US2"); also see http://stackoverflow.com/questions/4244999/problem-with-video-recording-after-auto-focus-in-android
        camera.unlock();
    }

    @Override
    public void initVideoRecorderPrePrepare(MediaRecorder video_recorder) {
        video_recorder.setCamera(camera);
    }

    @Override
    public void initVideoRecorderPostPrepare(MediaRecorder video_recorder) throws CameraControllerException {
        // no further actions necessary
    }

    @Override
    public String getParametersString() {
        String string = "";
        try {
            string = this.getParameters().flatten();
        }
        catch(Exception e) {
            // received a StringIndexOutOfBoundsException from beneath getParameters().flatten() on Google Play!
            e.printStackTrace();
        }
        return string;
    }

}

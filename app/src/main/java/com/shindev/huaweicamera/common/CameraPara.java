package com.shindev.huaweicamera.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CameraPara {

    public boolean isSDCardSave = false;
    final static private String keySDCardSave = "SDCardSave";

    public boolean isShutterSound = false;
    final static private String keyShutterSound = "ShutterSound";

    public enum SHUTTERDELAY {
        NONE,
        SECOND,
        FIVE,
        TEN
    }

    public SHUTTERDELAY shutterdelay = SHUTTERDELAY.NONE;
    final static private String keyShutterDelay = "ShutterDelay";

    public enum SOUNDKEY {
        DEFAULT,
        VIDEO,
        ZOOM
    }

    public SOUNDKEY soundkey = SOUNDKEY.DEFAULT;
    final static private String keySoundKey = "SoundKey";

    public enum CAMERASIZE {
        VERYBIG,
        BIG,
        NORMAL,
        SMALL,
        VERYSMALL
    }

    public CAMERASIZE photosize = CAMERASIZE.NORMAL;
    final static private String keyPhotoSize = "PhotoSize";

    public enum CROSSPATTERN {
        NONE,
        RECT,
        GLOADRATE,
        LEFTRATE,
        RIGHT
    }

    public CROSSPATTERN crosspattern = CROSSPATTERN.NONE;
    final static private String keyCrossPattern = "CrossPattern";

    public boolean isMirrorEffect = false;
    final static private String keyMirrorEffect = "MirrorEffect";

    public boolean isTouchFocus = false;
    final static private String keyTouchFocus = "TouchFocus";

    public CAMERASIZE videosize = CAMERASIZE.NORMAL;
    final static private String keyVideoSize = "VideoSize";

    public boolean isSoundRecord = false;
    final static private String keySoundRecord = "SoundRecord";

    static public CameraPara getCameraPara (Activity activity) {
        CameraPara cameraPara = new CameraPara();

        SharedPreferences prefs = activity.getSharedPreferences("com.shindev.huaweicamera", Context.MODE_PRIVATE);
        cameraPara.isSDCardSave = prefs.getBoolean(keySDCardSave, false);
        cameraPara.isShutterSound = prefs.getBoolean(keyShutterSound, false);
        cameraPara.shutterdelay = SHUTTERDELAY.values()[prefs.getInt(keyShutterDelay, 0)];
        cameraPara.soundkey = SOUNDKEY.values()[prefs.getInt(keySoundKey, 0)];
        cameraPara.photosize = CAMERASIZE.values()[prefs.getInt(keyPhotoSize, 0)];
        cameraPara.crosspattern = CROSSPATTERN.values()[prefs.getInt(keyCrossPattern, 0)];
        cameraPara.isMirrorEffect = prefs.getBoolean(keyMirrorEffect, false);
        cameraPara.isTouchFocus = prefs.getBoolean(keyTouchFocus, false);
        cameraPara.videosize = CAMERASIZE.values()[prefs.getInt(keyVideoSize, 0)];
        cameraPara.isSoundRecord = prefs.getBoolean(keySoundRecord, false);

        return cameraPara;
    }

    public void saveCameraPara(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("com.shindev.huaweicamera", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(keySDCardSave, isSDCardSave);
        editor.putBoolean(keyShutterSound, isShutterSound);
        editor.putInt(keyShutterDelay, shutterdelay.ordinal());
        editor.putInt(keySoundKey, soundkey.ordinal());
        editor.putInt(keyPhotoSize, photosize.ordinal());
        editor.putInt(keyCrossPattern, crosspattern.ordinal());
        editor.putBoolean(keyMirrorEffect, isMirrorEffect);
        editor.putBoolean(keyTouchFocus, isTouchFocus);
        editor.putInt(keyVideoSize, videosize.ordinal());
        editor.putBoolean(keySoundRecord, isSoundRecord);

        editor.apply();
    }

}

package com.shindev.huaweicamera.common;

public class Global {

    public enum FlashMode {
        AUTO,
        ON,
        OFF,
        LIGHT
    }

    public static final String getFlashString(FlashMode mode) {
        switch (mode) {
            case AUTO:
                return "flash_auto";
            case OFF:
                return "flash_off";
            case ON:
                return "flash_on";
            case LIGHT:
                return "flash_torch";
        }
        return "";
    }

    public enum FocusMode {
        AUTO,
        INFINIT,
        MACRO
    }

    public static final String getFocusString(FocusMode mode) {
        switch (mode) {
            case AUTO:
                return "focus_mode_auto";
            case INFINIT:
                return "focus_mode_infinity";
            case MACRO:
                return "focus_mode_macro";
        }
        return "";
    }

    public enum CameraMode {
        PHOTO,
        PHOTOPRO,
        CAMERA,
        NIGHT,
        HDR,
        FITLER,
        LANDSCAPE
    }

    public enum CameraFilterType {
        NONE,
        INKWELL,
        SKETCH,
        FIRYTALE,
        NOSTALGIA,
        COOL,
        EARLYBIRD,
        TOASTERO,
        WALDEN
    }

}

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.view;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.util.DisplayMetrics;


/**
 * Describes the characteristics of a particular logical display.
 * @hide
 */
public final class DisplayInfo implements Parcelable {
    /**
     * The surface flinger layer stack associated with this logical display.
     */
    public int layerStack;

    /**
     * Display flags.
     */
    public int flags;

    /**
     * Display type.
     */
    public int type;

    /**
     * Display address, or null if none.
     * Interpretation varies by display type.
     */
    public String address;

    /**
     * The human-readable name of the display.
     */
    public String name;

    /**
     * The width of the portion of the display that is available to applications, in pixels.
     * Represents the size of the display minus any system decorations.
     */
    public int appWidth;

    /**
     * The height of the portion of the display that is available to applications, in pixels.
     * Represents the size of the display minus any system decorations.
     */
    public int appHeight;

    /**
     * The smallest value of {@link #appWidth} that an application is likely to encounter,
     * in pixels, excepting cases where the width may be even smaller due to the presence
     * of a soft keyboard, for example.
     */
    public int smallestNominalAppWidth;

    /**
     * The smallest value of {@link #appHeight} that an application is likely to encounter,
     * in pixels, excepting cases where the height may be even smaller due to the presence
     * of a soft keyboard, for example.
     */
    public int smallestNominalAppHeight;

    /**
     * The largest value of {@link #appWidth} that an application is likely to encounter,
     * in pixels, excepting cases where the width may be even larger due to system decorations
     * such as the status bar being hidden, for example.
     */
    public int largestNominalAppWidth;

    /**
     * The largest value of {@link #appHeight} that an application is likely to encounter,
     * in pixels, excepting cases where the height may be even larger due to system decorations
     * such as the status bar being hidden, for example.
     */
    public int largestNominalAppHeight;

    /**
     * The logical width of the display, in pixels.
     * Represents the usable size of the display which may be smaller than the
     * physical size when the system is emulating a smaller display.
     */
    public int logicalWidth;

    /**
     * The logical height of the display, in pixels.
     * Represents the usable size of the display which may be smaller than the
     * physical size when the system is emulating a smaller display.
     */
    public int logicalHeight;

    /**
     * @hide
     * Number of overscan pixels on the left side of the display.
     */
    public int overscanLeft;

    /**
     * @hide
     * Number of overscan pixels on the top side of the display.
     */
    public int overscanTop;

    /**
     * @hide
     * Number of overscan pixels on the right side of the display.
     */
    public int overscanRight;

    /**
     * @hide
     * Number of overscan pixels on the bottom side of the display.
     */
    public int overscanBottom;

    /**
     * The rotation of the display relative to its natural orientation.
     * May be one of {@link android.view.Surface#ROTATION_0},
     * {@link android.view.Surface#ROTATION_90}, {@link android.view.Surface#ROTATION_180},
     * {@link android.view.Surface#ROTATION_270}.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public int rotation;

    /**
     * The refresh rate of this display in frames per second.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public float refreshRate;

    /**
     * The logical display density which is the basis for density-independent
     * pixels.
     */
    public int logicalDensityDpi;

    /**
     * The exact physical pixels per inch of the screen in the X dimension.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public float physicalXDpi;

    /**
     * The exact physical pixels per inch of the screen in the Y dimension.
     * <p>
     * The value of this field is indeterminate if the logical display is presented on
     * more than one physical display.
     * </p>
     */
    public float physicalYDpi;

    /**
     * The UID of the application that owns this display, or zero if it is owned by the system.
     * <p>
     * If the display is private, then only the owner can use it.
     * </p>
     */
    public int ownerUid;

    /**
     * The package name of the application that owns this display, or null if it is
     * owned by the system.
     * <p>
     * If the display is private, then only the owner can use it.
     * </p>
     */
    public String ownerPackageName;

    public static final Creator<DisplayInfo> CREATOR = new Creator<DisplayInfo>() {
        @Override
        public DisplayInfo createFromParcel(Parcel source) {
        	throw new RuntimeException("Stub");
        }

        @Override
        public DisplayInfo[] newArray(int size) {
        	throw new RuntimeException("Stub");
        }
    };

    public DisplayInfo() {
    }

    public DisplayInfo(DisplayInfo other) {
    	throw new RuntimeException("Stub");
    }

    private DisplayInfo(Parcel source) {
    	throw new RuntimeException("Stub");
    }

    @Override
    public boolean equals(Object o) {
    	throw new RuntimeException("Stub");
    }

    public boolean equals(DisplayInfo other) {
    	throw new RuntimeException("Stub");
    }

    @Override
    public int hashCode() {
        return 0; // don't care
    }

    public void copyFrom(DisplayInfo other) {
    	throw new RuntimeException("Stub");
    }

    public void readFromParcel(Parcel source) {
    	throw new RuntimeException("Stub");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	throw new RuntimeException("Stub");
    }

    @Override
    public int describeContents() {
    	throw new RuntimeException("Stub");
    }

    public void getAppMetrics(DisplayMetrics outMetrics) {
    	throw new RuntimeException("Stub");
    }

    public void getAppMetrics(DisplayMetrics outMetrics, DisplayAdjustments displayAdjustments) {
    	throw new RuntimeException("Stub");
    }

    public void getAppMetrics(DisplayMetrics outMetrics, CompatibilityInfo ci, IBinder token) {
    	throw new RuntimeException("Stub");
    }

    public void getLogicalMetrics(DisplayMetrics outMetrics, CompatibilityInfo compatInfo,
            IBinder token) {
    	throw new RuntimeException("Stub");
    }

    public int getNaturalWidth() {
    	throw new RuntimeException("Stub");
    }

    public int getNaturalHeight() {
    	throw new RuntimeException("Stub");
    }

    /**
     * Returns true if the specified UID has access to this display.
     */
    public boolean hasAccess(int uid) {
    	throw new RuntimeException("Stub");
    }

    private void getMetricsWithSize(DisplayMetrics outMetrics, CompatibilityInfo compatInfo,
            IBinder token, int width, int height) {
    	throw new RuntimeException("Stub");
    }

    // For debugging purposes
    @Override
    public String toString() {
    	throw new RuntimeException("Stub");
    }

    private static String flagsToString(int flags) {
    	throw new RuntimeException("Stub");
    }
}

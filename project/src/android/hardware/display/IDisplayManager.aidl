package android.hardware.display;
import android.view.DisplayInfo;

interface IDisplayManager {
  DisplayInfo getDisplayInfo(int displayId);
  int[] getDisplayIds();
}

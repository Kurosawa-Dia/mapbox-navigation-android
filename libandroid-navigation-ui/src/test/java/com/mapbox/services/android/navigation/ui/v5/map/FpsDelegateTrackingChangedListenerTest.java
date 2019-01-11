package com.mapbox.services.android.navigation.ui.v5.map;

import com.mapbox.services.android.navigation.ui.v5.camera.NavigationCamera;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FpsDelegateTrackingChangedListenerTest {

  @Test
  public void onTrackingModeChanged_delegateIsUpdated() {
    MapFpsDelegate delegate = mock(MapFpsDelegate.class);
    FpsDelegateTrackingChangedListener listener = new FpsDelegateTrackingChangedListener(delegate);
    int trackingMode = NavigationCamera.NAVIGATION_TRACKING_MODE_GPS;

    listener.onFinishedTransitionTo(trackingMode);

    verify(delegate).updateCameraTracking(trackingMode);
  }
}
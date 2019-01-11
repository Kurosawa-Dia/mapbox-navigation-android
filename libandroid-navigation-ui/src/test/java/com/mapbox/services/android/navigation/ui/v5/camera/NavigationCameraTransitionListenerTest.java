package com.mapbox.services.android.navigation.ui.v5.camera;

import com.mapbox.mapboxsdk.location.modes.CameraMode;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NavigationCameraTransitionListenerTest {

  @Test
  public void onLocationCameraTransitionFinished() {
    NavigationCamera camera = mock(NavigationCamera.class);
    int trackingMode = NavigationCamera.NAVIGATION_TRACKING_MODE_GPS;
    NavigationCameraTransitionListener listener = new NavigationCameraTransitionListener(camera, trackingMode);

    listener.onLocationCameraTransitionFinished(CameraMode.TRACKING_GPS);

    verify(camera).updateTrackingModeListenersWith(trackingMode);
  }

  @Test
  public void onLocationCameraTransitionCanceled() {
    NavigationCamera camera = mock(NavigationCamera.class);
    int trackingMode = NavigationCamera.NAVIGATION_TRACKING_MODE_GPS;
    NavigationCameraTransitionListener listener = new NavigationCameraTransitionListener(camera, trackingMode);

    listener.onLocationCameraTransitionCanceled(CameraMode.TRACKING_GPS);

    verify(camera).updateTrackingModeListenersWith(trackingMode);
  }
}
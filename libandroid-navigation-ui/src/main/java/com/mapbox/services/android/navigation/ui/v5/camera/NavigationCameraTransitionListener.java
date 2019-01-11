package com.mapbox.services.android.navigation.ui.v5.camera;

import com.mapbox.mapboxsdk.location.OnLocationCameraTransitionListener;

class NavigationCameraTransitionListener implements OnLocationCameraTransitionListener {

  private final NavigationCamera camera;
  private final int trackingMode;

  NavigationCameraTransitionListener(NavigationCamera camera, @NavigationCamera.TrackingMode int trackingMode) {
    this.camera = camera;
    this.trackingMode = trackingMode;
  }

  @Override
  public void onLocationCameraTransitionFinished(int cameraMode) {
    camera.updateTrackingModeListenersWith(trackingMode);
  }

  @Override
  public void onLocationCameraTransitionCanceled(int cameraMode) {
    camera.updateTrackingModeListenersWith(trackingMode);
  }
}

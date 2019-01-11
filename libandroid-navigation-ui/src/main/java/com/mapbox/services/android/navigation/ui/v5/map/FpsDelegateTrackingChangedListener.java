package com.mapbox.services.android.navigation.ui.v5.map;

import com.mapbox.services.android.navigation.ui.v5.camera.TrackingModeTransitionListener;

class FpsDelegateTrackingChangedListener implements TrackingModeTransitionListener {

  private final MapFpsDelegate delegate;

  FpsDelegateTrackingChangedListener(MapFpsDelegate delegate) {
    this.delegate = delegate;
  }

  @Override
  public void onFinishedTransitionTo(int trackingMode) {
    delegate.updateCameraTracking(trackingMode);
  }
}

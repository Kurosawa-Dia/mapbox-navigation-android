package com.mapbox.services.android.navigation.ui.v5.camera;

/**
 * Listener that gets invoked when the navigation camera finishes a transition
 * to a new tracking mode.
 */
public interface TrackingModeTransitionListener {

  /**
   * Invoked when the camera has finished a transition to a new tracking mode.
   *
   * @param trackingMode the current tracking mode being used by the camera
   */
  void onFinishedTransitionTo(@NavigationCamera.TrackingMode int trackingMode);
}

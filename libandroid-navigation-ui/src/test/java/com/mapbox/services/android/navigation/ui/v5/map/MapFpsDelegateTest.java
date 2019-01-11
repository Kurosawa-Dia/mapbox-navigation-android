package com.mapbox.services.android.navigation.ui.v5.map;

import android.content.Context;

import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.directions.v5.models.StepManeuver;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.services.android.navigation.ui.v5.camera.NavigationCamera;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteLegProgress;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteStepProgress;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapFpsDelegateTest {

  @Test
  public void addProgressChangeListener_navigationReceivesListener() {
    MapboxNavigation navigation = mock(MapboxNavigation.class);
    MapFpsDelegate delegate = new MapFpsDelegate(mock(MapView.class), mock(MapBatteryMonitor.class));

    delegate.addProgressChangeListener(navigation);

    verify(navigation).addProgressChangeListener(any(FpsDelegateProgressChangeListener.class));
  }

  @Test
  public void addTrackingChangedListener_cameraReceivesListener() {
    NavigationCamera camera = mock(NavigationCamera.class);
    MapFpsDelegate delegate = new MapFpsDelegate(mock(MapView.class), mock(MapBatteryMonitor.class));

    delegate.addTrackingChangedListener(camera);

    verify(camera).addTrackingModeTransitionListener(any(FpsDelegateTrackingChangedListener.class));
  }

  @Test
  public void onStart_cameraListenerRemoved() {
    NavigationCamera camera = mock(NavigationCamera.class);
    MapFpsDelegate delegate = new MapFpsDelegate(mock(MapView.class), mock(MapBatteryMonitor.class));
    delegate.addTrackingChangedListener(camera);

    delegate.onStart();

    verify(camera, times(2)).addTrackingModeTransitionListener(any(FpsDelegateTrackingChangedListener.class));
  }

  @Test
  public void onStop_navigationListenerRemoved() {
    MapboxNavigation navigation = mock(MapboxNavigation.class);
    MapFpsDelegate delegate = new MapFpsDelegate(mock(MapView.class), mock(MapBatteryMonitor.class));
    delegate.addProgressChangeListener(navigation);

    delegate.onStop();

    verify(navigation).removeProgressChangeListener(any(FpsDelegateProgressChangeListener.class));
  }

  @Test
  public void updateEnabledFalse_maxFpsReset() {
    MapView mapView = mock(MapView.class);
    MapFpsDelegate delegate = new MapFpsDelegate(mapView, mock(MapBatteryMonitor.class));

    delegate.updateEnabled(false);

    mapView.setMaximumFps(anyInt());
  }

  @Test
  public void isEnabled_returnsCurrentEnabledState() {
    MapFpsDelegate delegate = new MapFpsDelegate(mock(MapView.class), mock(MapBatteryMonitor.class));

    delegate.updateEnabled(false);

    assertFalse(delegate.isEnabled());
  }

  @Test
  public void updateMaxFpsThreshold_isCorrectlySet() {
    MapFpsDelegate delegate = new MapFpsDelegate(mock(MapView.class), mock(MapBatteryMonitor.class));
    int maxFpsThreshold = 5;

    delegate.updateMaxFpsThreshold(maxFpsThreshold);

    assertEquals(maxFpsThreshold, delegate.retrieveMaxFpsThreshold());
  }

  @Test
  public void updateCameraTracking_resetsWhenNotTracking() {
    MapView mapView = mock(MapView.class);
    MapFpsDelegate delegate = new MapFpsDelegate(mapView, mock(MapBatteryMonitor.class));
    int trackingMode = NavigationCamera.NAVIGATION_TRACKING_MODE_NONE;

    delegate.updateCameraTracking(trackingMode);

    mapView.setMaximumFps(anyInt());
  }

  @Test
  public void adjustFpsFor_thresholdSetWithCorrectManeuver() {
    MapView mapView = mock(MapView.class);
    MapBatteryMonitor batteryMonitor = mock(MapBatteryMonitor.class);
    when(batteryMonitor.isPluggedIn(any(Context.class))).thenReturn(false);
    MapFpsDelegate delegate = new MapFpsDelegate(mapView, batteryMonitor);
    RouteProgress routeProgress = buildRouteProgressWith("straight");
    int maxFps = 5;
    delegate.updateMaxFpsThreshold(maxFps);

    delegate.adjustFpsFor(routeProgress);

    verify(mapView).setMaximumFps(eq(maxFps));
  }

  @Test
  public void adjustFpsFor_thresholdSetWithCorrectDuration() {
    MapView mapView = mock(MapView.class);
    MapBatteryMonitor batteryMonitor = mock(MapBatteryMonitor.class);
    when(batteryMonitor.isPluggedIn(any(Context.class))).thenReturn(false);
    MapFpsDelegate delegate = new MapFpsDelegate(mapView, batteryMonitor);
    RouteProgress routeProgress = buildRouteProgressWith(100d, 20d);
    int maxFps = 5;
    delegate.updateMaxFpsThreshold(maxFps);

    delegate.adjustFpsFor(routeProgress);

    verify(mapView).setMaximumFps(eq(maxFps));
  }

  private RouteProgress buildRouteProgressWith(String maneuverModifier) {
    RouteProgress routeProgress = mock(RouteProgress.class);
    RouteLegProgress routeLegProgress = mock(RouteLegProgress.class);
    LegStep currentStep = mock(LegStep.class);
    StepManeuver currentManeuver = mock(StepManeuver.class);
    when(currentManeuver.modifier()).thenReturn(maneuverModifier);
    when(currentStep.maneuver()).thenReturn(currentManeuver);
    when(routeLegProgress.currentStep()).thenReturn(currentStep);
    when(routeProgress.currentLegProgress()).thenReturn(routeLegProgress);
    return routeProgress;
  }

  private RouteProgress buildRouteProgressWith(double totalDuration, double durationRemaining) {
    RouteProgress routeProgress = mock(RouteProgress.class);
    RouteLegProgress routeLegProgress = mock(RouteLegProgress.class);
    RouteStepProgress routeStepProgress = mock(RouteStepProgress.class);
    StepManeuver currentManeuver = mock(StepManeuver.class);
    when(currentManeuver.modifier()).thenReturn("left");
    LegStep currentStep = mock(LegStep.class);
    when(currentStep.duration()).thenReturn(totalDuration);
    when(routeStepProgress.durationRemaining()).thenReturn(durationRemaining);
    when(routeLegProgress.currentStepProgress()).thenReturn(routeStepProgress);
    when(routeProgress.currentLegProgress()).thenReturn(routeLegProgress);
    when(currentStep.maneuver()).thenReturn(currentManeuver);
    when(routeLegProgress.currentStep()).thenReturn(currentStep);
    return routeProgress;
  }
}
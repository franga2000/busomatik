package com.VegaSolutions.lpptransit.ui.custommaps;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

class MarkerAnimator {

    private final Handler handler;
    private Runnable current;
    private final List<Object> animated = new ArrayList<>();

    MarkerAnimator() {
        handler = new Handler(Looper.myLooper());
    }

    /**
     * Animate marker from point A to B.
     * @param marker marker to animate
     * @param finalPosition point B
     * @param finalCardinalDirection final rotation
     * @param latLngInterpolator interpolator to interpolate marker
     */
    void animateMarker(Marker marker, LatLng finalPosition, float finalCardinalDirection, LatLngInterpolator latLngInterpolator) {

        final LatLng startPosition = marker.getPosition();
        float startCardinalDirection = marker.getRotation() % 360;
        final float beginCardinalDirection;
        final float endCardinalDirection;

        // Adjust angle animation (shortest path).
        if (Math.abs(finalCardinalDirection - startCardinalDirection) > 180) {
            if (finalCardinalDirection > startCardinalDirection)
                startCardinalDirection += 360;
            else finalCardinalDirection += 360;
        }

        endCardinalDirection = finalCardinalDirection;
        beginCardinalDirection = startCardinalDirection;

        if (animated.contains(marker))
            handler.removeCallbacks(current);
        else animated.add(marker);

        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 1000;

        current = new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {

                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                // Update marker
                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));
                marker.setRotation(v * (endCardinalDirection - beginCardinalDirection) + beginCardinalDirection);

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else animated.remove(marker);
            }
        };

        handler.post(current);
    }

    void animateMarkerWithCircle(Marker marker, Circle circle, double radius, LatLng finalPosition, LatLngInterpolator latLngInterpolator) {

        final LatLng startPosition = marker.getPosition();
        final double startRad = circle.getRadius();

        if (animated.contains(marker))
            handler.removeCallbacks(current);
        else animated.add(marker);

        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 1000;

        current = new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {

                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                // Update marker and circle
                LatLng pos = latLngInterpolator.interpolate(v, startPosition, finalPosition);
                double rad = (radius - startRad) * v  + startRad;
                marker.setPosition(pos);
                circle.setCenter(pos);
                circle.setRadius(rad);

                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else animated.remove(marker);
            }
        };

        handler.post(current);
    }

}

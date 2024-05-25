package ru.dyachenkoas.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.ArrayList;
import java.util.List;

import ru.dyachenkoas.mireaproject.databinding.MapMainBinding;

public class Maps extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 200;
    private MapMainBinding binding;
    private MapView mapView;
    private List<Establishment> establishments = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = MapMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));

        mapView = binding.mapView;
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(12.0);
        GeoPoint startPoint = new GeoPoint(55.7522, 37.6156);
        mapController.setCenter(startPoint);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        } else {
            setupMapOverlays();
        }

        establishments.add(new Establishment("Central Market", "A bustling marketplace", 55.766749, 37.624211));
        establishments.add(new Establishment("Depo", "A trendy food court", 55.780218, 37.593084));
        establishments.add(new Establishment("MIREA", "A lovely university", 55.794259, 37.701448));

        addMarkersToMap();

        return view;
    }

    private void setupMapOverlays() {
        CompassOverlay compassOverlay = new CompassOverlay(requireContext(),
                new InternalCompassOrientationProvider(requireContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(requireContext(), "Tapped at: " + p.getLatitude() + ", " + p.getLongitude(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean longPressHelper(           GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        mapView.getOverlays().add(0, mapEventsOverlay);
    }

    private void addMarkersToMap() {
        for (Establishment establishment : establishments) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(establishment.getLatitude(), establishment.getLongitude()));
            marker.setTitle(establishment.getName());
            marker.setSnippet(establishment.getDescription());
            marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
            marker.setOnMarkerClickListener((marker1, mapView) -> {
                Toast.makeText(requireContext(), marker1.getTitle() + "\n" + marker1.getSnippet(),
                        Toast.LENGTH_SHORT).show();
                return true;
            });
            mapView.getOverlays().add(marker);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(requireContext(),
                PreferenceManager.getDefaultSharedPreferences(requireContext()));
        mapView.onPause();
    }

    static class Establishment {
        private final String name;
        private final String description;
        private final double latitude;
        private final double longitude;

        public Establishment(String name, String description, double latitude, double longitude) {
            this.name = name;
            this.description = description;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
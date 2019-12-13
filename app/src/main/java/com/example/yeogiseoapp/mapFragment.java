package com.example.yeogiseoapp;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yeogiseoapp.data.ScheduleData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class mapFragment extends Fragment
        implements OnMapReadyCallback {
    private MapView mapView = null;
    private GoogleMap mMap;
    ArrayList<Polyline> polylines = new ArrayList<Polyline>();
    ArrayList<LatLng> markers = new ArrayList<LatLng>();
    Button allowBtn;

    public mapFragment()
    {
        // required
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_map, container, false);

        allowBtn = layout.findViewById(R.id.togetherBtn);

        allowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((roomActivity)getActivity()).sendAllow();
                ((roomActivity)getActivity()).openOverlay(2);
            }
        });
        mapView = (MapView)layout.findViewById(R.id.map);
        mapView.getMapAsync(this);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수

        if(mapView != null)
        {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng INHA = new LatLng(37.450606, 126.657225);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(INHA));

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        ((roomActivity)getActivity()).initSchedule(new ScheduleData(((roomActivity)getActivity()).getGid()));
    }

    public void makeMarker(float latitude, float longitude, Bitmap img)
    {
        LatLng pic = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(pic);

        markerOptions.title("제목");

        markerOptions.snippet("무슨 사진일까?");

        if(img != null)
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(img));
        else
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(pic));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        markers.add(pic);
    }

    public void drawPath(){        //polyline을 그려주는 메소드
        LatLng start, end;
        removeLines();
        for(int i = 1; i<markers.size(); i++) {
            start = markers.get(i-1);
            end = markers.get(i);
            PolylineOptions options = new PolylineOptions().add(start).add(end).width(15).color(Color.BLACK).geodesic(true);
            polylines.add(mMap.addPolyline(options));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 18));
        }
    }

    public void removeLines(){
        for(Polyline line : polylines)
            line.remove();
        polylines.clear();
    }

    public void moveCamera(LatLng dest){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }

}
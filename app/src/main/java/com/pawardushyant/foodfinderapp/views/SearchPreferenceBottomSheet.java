package com.pawardushyant.foodfinderapp.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pawardushyant.foodfinderapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class SearchPreferenceBottomSheet extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "SearchPreferenceBottomSheet";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1003;
    private static final int GPS_PERMISSION = 1005;

    TextView tvRadius;

    GetSearchCallback callback;

    private String radius, selected_choice;

    final String[] choices = {"lodging", "restaurant", "food", "point_of_interest", "establishment"};
    private Location location = null;

    public static SearchPreferenceBottomSheet newInstance() {
        return new SearchPreferenceBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SeekBar seekBar = view.findViewById(R.id.seek_radius);
        seekBar.setMin(1);
        seekBar.setMax(2500);
        Spinner spinner = view.findViewById(R.id.spinner_type);
        CheckBox chk = view.findViewById(R.id.chk_location);
        Button btn = view.findViewById(R.id.btn_search);
        tvRadius = view.findViewById(R.id.tv_radius);
        updateProgress(1);
        ArrayAdapter<String> a = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, choices);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(0);
        spinner.setAdapter(a);
        spinner.setOnItemSelectedListener(this);
        btn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        chk.setOnCheckedChangeListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (GetSearchCallback) requireActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        updateProgress(i);
    }

    private void updateProgress(int i) {
        radius = String.valueOf(i);
        tvRadius.setText(i + "Km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onClick(View view) {
        callback.searchCallback(selected_choice, radius, location);
        dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selected_choice = choices[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            getLocaion();
        }
    }

    private void getLocaion() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationManager locationManager = (LocationManager) requireActivity()
                    .getSystemService(Context.LOCATION_SERVICE);
            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    updateLocation(location);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        } else {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startGPS();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == GPS_PERMISSION){
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000L);
            locationRequest.setFastestInterval(5000L);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);


            SettingsClient client = LocationServices.getSettingsClient(requireActivity());
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(locationSettingsResponse -> getLocaion());
        }
    }

    private void startGPS() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_PERMISSION);
    }

    private void updateLocation(Location location) {
        this.location = location;
    }

    public interface GetSearchCallback{
        void searchCallback(String type, String radius, Location location);
    }
}

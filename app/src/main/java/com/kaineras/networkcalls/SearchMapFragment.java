package com.kaineras.networkcalls;

import android.app.Fragment;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchMapFragment extends Fragment {

    EditText etPlace;
    Button btSearch;
    Geocoder geoCoder;
    GoogleMap gmSearch;

    public SearchMapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        etPlace=(EditText)rootView.findViewById(R.id.edit_text_place);
        btSearch=(Button)rootView.findViewById(R.id.button_search);
        gmSearch = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        prepareMap();
        prepareText();
        prepareButton();
        return rootView;
    }

    private void prepareMap() {

    }

    private void prepareButton() {
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchFor = etPlace.getText().toString();
                new GetLocationTask().execute(searchFor);

            }
        });
    }

    private void prepareText() {
        etPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()<=0)
                    btSearch.setEnabled(false);
                else
                    btSearch.setEnabled(true);
            }
        });
    }

    class GetLocationTask extends AsyncTask<String,Void,String>{


        List<android.location.Address> addresses = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                addresses = geoCoder.getFromLocationName(params[0], 5);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(addresses!=null)
                if (addresses.size() > 0) {
                    LatLng latLng = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                    CameraPosition cameraPosition=new CameraPosition.Builder()
                            .target(latLng).zoom(10).build();
                    CameraUpdate cameraUpdate= CameraUpdateFactory.newCameraPosition(cameraPosition);
                    gmSearch.animateCamera(cameraUpdate);
                }
            else
                    Toast.makeText(getActivity(),R.string.fail_addres,Toast.LENGTH_LONG).show();
        }
    }
}

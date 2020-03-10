package com.app.mateforpark.UserFragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.mateforpark.R;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Explore_Fragment extends Fragment {

    Button mFilterButton;
    CountryCodePicker mCountryCodeFilter;
    RadioGroup mRadioGroup;



    private String currentUId;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;



    public Explore_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        currentUId = mAuth.getCurrentUser().getUid();

        mFilterButton = getActivity().findViewById(R.id.filter);
        mCountryCodeFilter = getActivity().findViewById(R.id.countryFilter);
        mRadioGroup = getActivity().findViewById(R.id.radioGroupFilter);




        mFilterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectGender = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = getActivity().findViewById(selectGender);


                if(selectGender == -1){

                    Toast.makeText(getActivity(), "No option Selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {

                    final String country = mCountryCodeFilter.getSelectedCountryEnglishName();
                    String gender = radioButton.getText().toString();


                    Bundle bundle = new Bundle();

                    bundle.putString("param_country",country); //send anything you want
                    bundle.putString("param_gender",gender); //send anything you want

                    final Home_Dashboard_Fragment fragment2 = new Home_Dashboard_Fragment();
                    fragment2.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.container,fragment2).commit();
                }


            }
        });








    }
}

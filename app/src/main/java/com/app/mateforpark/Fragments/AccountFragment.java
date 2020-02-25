package com.app.mateforpark.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.mateforpark.LoginActivity;
import com.app.mateforpark.R;
import com.app.mateforpark.UserProfileSettingsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    LinearLayout profileSettings, logoutSettings;
    private FirebaseAuth mAuth;
    private AuthStateListener firebaseAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mUserDatabase;
    private String currentUId, username;
    TextView mAccountName;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase Connections & id retrieval
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();


        //firebase Auth Listener to check if user is already logged in or not
        firebaseAuthStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        profileSettings = getView().findViewById(R.id.profileSettings);
        logoutSettings = getView().findViewById(R.id.signout);
        mAccountName = getView().findViewById(R.id.accountName);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUId);

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    //get the child you want
                    if (map.get("name") != null) {
                        username = map.get("name").toString();
                        mAccountName.setText(username);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserProfileSettingsActivity.class);

                startActivity(intent);
            }
        });

        logoutSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setupGoogleLogin();

                //firebase signout
                mAuth.signOut();
                //gmail signout
                mGoogleSignInClient.signOut();

                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

               getActivity().finish();

            }
        });
    }

    public void setupGoogleLogin(){

        //GoogleSignInOptions is options used to configure the GOOGLE_SIGN_IN_API.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(),gso);

    }


}

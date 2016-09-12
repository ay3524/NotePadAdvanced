package com.ay3524.notepadadvanced;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class GooglePlusSignIn extends Fragment {


    private static final int RC_SIGN_IN = 1234;
    TextView skip;
    OnStartNewFragmentListener mCallback;
    private GoogleApiClient mGoogleApiClient;
    TextView welcomeText;

    public GooglePlusSignIn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //if(((AppCompatActivity)getActivity()).getSupportActionBar() != null)
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_google_plus_sign_in, container, false);
        skip = (TextView) v.findViewById(R.id.skip);

        welcomeText = (TextView) v.findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/mtcorsva.ttf");
        welcomeText.setTypeface(typeface);

        SignInButton b = (SignInButton) v.findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Toast.makeText(getActivity(), "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Utils.isOnline(getActivity())){
                    googleSignIn();
                }else{
                    Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                }


            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.maps.ashishyadav271.sharedpreferencesdemo",Context.MODE_PRIVATE);

                sharedPreferences.edit().putString("SKIPENABLED","Yes").apply();

                mCallback.onStartListFragment(new NoteListFragment());
            }
        });
        return v;
    }

    private void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (OnStartNewFragmentListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must Implement OnStartNewFragmentListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);

        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String userName = acct.getDisplayName();
            String email = acct.getEmail();

            final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.maps.ashishyadav271.sharedpreferencesdemo",Context.MODE_PRIVATE);

            sharedPreferences.edit().putString("UserName", userName).apply();
            sharedPreferences.edit().putString("Email", email).apply();

            //NoteListFragment fragment = NoteListFragment.newInstance(userName,email);
           // mCallback.onStartNewFragment(fragment,"Note");

            mCallback.onStartListFragment(new NoteListFragment());

            //Toast.makeText(getActivity(), userName + " "+email, Toast.LENGTH_SHORT).show();

            //updateUI(true);
        } else {
            Log.d("ERRORTAG", "handleSignInResult:" + result.getStatus());
            /*Snackbar snackbar1 =  Snackbar.make(view, "You must choose an account to Login !", Snackbar.LENGTH_LONG);
            View sbView = snackbar1.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.GREEN);

            snackbar1.show();*/

            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }

    }
}

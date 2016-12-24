package com.ay3524.notepadadvanced;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment implements NoteListAdapter.ClickListener{

    View rootView;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Notes> notes;
    NoteListAdapter adapter;
    OnStartNewFragmentListener mCallback;
    private DbAdapter dbAdapter;
    private LinearLayout emptyView;
    //private String date;

    public NoteListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_note_list, container, false);

        emptyView = (LinearLayout) rootView.findViewById(R.id.emptyView);

        TextView addText = (TextView) rootView.findViewById(R.id.addText);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/mtcorsva.ttf");
        addText.setTypeface(typeface);

        dbAdapter = new DbAdapter(getActivity());

        initView();

      //  getNote();

        //getUserNameEMail();

        return rootView;
    }

   /* private void getUserNameEMail() {
        Bundle args = getArguments();

        if(args != null){
            String userName = args.getString("Content");
            String email = args.getString("Date");

        }

    }*/

    private void initView() {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getNotesFromDatabase();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.onStartNewFragment(new PlainNoteEditorFragment(),"Note Editor");
                //openFragment(new PlainNoteEditorFragment(),"Note Editor");
            }
        });
    }

    private void getNotesFromDatabase() {

        notes = dbAdapter.getAllData();

        if(notes.isEmpty()){

            emptyView.setVisibility(View.VISIBLE);

        }else{
            emptyView.setVisibility(View.INVISIBLE);
            adapter = new NoteListAdapter(notes, getActivity());
            adapter.setClickListener(NoteListFragment.this);
            //adapter.setClickListener(DealsFragment.this);
            recyclerView.setAdapter(adapter);
        }




        //Toast.makeText(getActivity(), notes.toString() , Toast.LENGTH_SHORT).show();

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
    public void onItemClick(View v, int pos) {

        Notes notesObj = notes.get(pos);

        String content = notesObj.getNoteContent();
        String date = notesObj.getNoteReadableDate();

        PlainNoteEditorFragment fragment = PlainNoteEditorFragment.newInstance(content,date);
        mCallback.onStartNewFragment(fragment,"NotePad");
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.about_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.about){

            final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.maps.ashishyadav271.sharedpreferencesdemo",Context.MODE_PRIVATE);

            //String skipPressed = sharedPreferences.getString("SKIPENABLED", "");

            String userName = sharedPreferences.getString("UserName", "");

            String userEmail = sharedPreferences.getString("Email", "");

            /*if(!skipPressed.equals("")){
                Toast.makeText(getActivity(), "SKIPPED", Toast.LENGTH_SHORT).show();
            }else
            Toast.makeText(getActivity(), userName + " " +userEmail, Toast.LENGTH_SHORT).show();*/
           showLogin(userName, userEmail);
        }
        return true;
    }

    /*public static NoteListFragment newInstance(String userName, String email) {

        NoteListFragment fragment = new NoteListFragment();

        if(!userName.isEmpty()){
            Bundle args = new Bundle();

            args.putString("UserName",userName);
            args.putString("Email",email);
            fragment.setArguments(args);
        }

        return null;
    }*/

    private void showLogin(final String userName, String userEmail) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle("Welcome to NotePad");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final TextView userNameTextView = (TextView) dialogView.findViewById(R.id.userName);
        TextView emailTextView = (TextView) dialogView.findViewById(R.id.email);
        final Button loginButton = (Button) dialogView.findViewById(R.id.loginButton);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        if(!userName.equalsIgnoreCase("")&&!userEmail.equalsIgnoreCase("")) {

            userNameTextView.setText(userName);
            emailTextView.setText(userEmail);
            loginButton.setText(getResources().getString(R.string.logoutText));
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), loginButton.getText().toString(), Toast.LENGTH_SHORT).show();
                    final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.maps.ashishyadav271.sharedpreferencesdemo",Context.MODE_PRIVATE);

                    sharedPreferences.edit().putString("UserName","").apply();
                    sharedPreferences.edit().putString("Email","").apply();
                    sharedPreferences.edit().putString("SKIPENABLED", "").apply();

                    GooglePlusSignIn fragment = new GooglePlusSignIn();
                    mCallback.onStartListFragment(fragment);
                    //AlertDialog b = dialogBuilder.create();
                    b.dismiss();
                }
            });
        }else{
            loginButton.setText(getResources().getString(R.string.loginText));
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GooglePlusSignIn fragment = new GooglePlusSignIn();
                    mCallback.onStartListFragment(fragment);
                    b.dismiss();
                    //Toast.makeText(getActivity(), loginButton.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }




        /*dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String changedSubName = edt.getText().toString();
                //editItem(position, changedSubName);
                //Log.d("SPINNER1", String.valueOf(sp.getSelectedItem()));
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                //edt.setText(st);
                //Log.d("TAG2", edt.getText().toString());
            }
        });*/

    }
}

package com.ay3524.notepadadvanced;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlainNoteEditorFragment extends Fragment {


    //EditText title;
    EditText content;
    DbAdapter dbAdapter;
    boolean inEditMode;
    private String contentString;
    View v;
    private String date;

    public PlainNoteEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        showKeyboard();

        dbAdapter = new DbAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_plain_note_editor, container, false);
        //title = (EditText) v.findViewById(R.id.title);
        content = (EditText) v.findViewById(R.id.content);
        //content.setSelection(content.getText().toString().length());
        content.requestFocus();
        //String data = dbAdapter.getAllData();
        //Toast.makeText(getActivity(), data , Toast.LENGTH_SHORT).show();

        getNote();

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.editor_menu,menu);

        MenuItem deleteIcon = menu.findItem(R.id.delete);

        if(!inEditMode){
            deleteIcon.setVisible(false);
            getActivity().invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.save){

            if((!content.getText().toString().isEmpty())) {
                if(inEditMode){
                    if(saveNote()){
                        dbAdapter.deleteData(date);
                        Snackbar.make(v,"Note Updated",Snackbar.LENGTH_SHORT)
                                .setAction("Action",null).show();
                    }
                }else{
                    if(saveNote()){
                        Snackbar.make(v,"Note Saved",Snackbar.LENGTH_SHORT)
                                .setAction("Action",null).show();
                    }

                }
                //saveNote();
                return true;
            }
            else
                Toast.makeText(getActivity(), "Empty Field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(item.getItemId() == R.id.delete){

            alert();

        }
        return true;

    }

    private boolean saveNote() {
        //String title1 = title.getText().toString();
        String content1 = content.getText().toString();

        Notes notes = new Notes();
        Calendar calendar = GregorianCalendar.getInstance();
        notes.setNoteModifiedDate(calendar.getTimeInMillis());
        String date1 = notes.getReadableModifiedDate();

        long id = dbAdapter.insertData(content1,date1);

        if(id > 0){
            //Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            closeKeyboard();
            getActivity().getSupportFragmentManager().popBackStack();
            //String data = dbAdapter.getAllData();
            //Toast.makeText(getActivity(), data , Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(getActivity(), "Unable to save try again", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void closeKeyboard() {
        try{
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(),0);
        }catch (Exception e){
            //Log.e("IMM ERROR",e.getMessage());
        }
    }

    private void showKeyboard() {
        try{
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
        }catch (Exception e){
            Log.e("IMM ERROR",e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateNote();
    }

    public static PlainNoteEditorFragment newInstance(String content, String date){
        PlainNoteEditorFragment fragment = new PlainNoteEditorFragment();

        if(!content.isEmpty()){
            Bundle args = new Bundle();

            args.putString("Content",content);
            args.putString("Date",date);
            fragment.setArguments(args);
        }
        return fragment;
    }
    public void getNote(){
        Bundle args = getArguments();

        if(args != null){
            contentString = args.getString("Content");
            date = args.getString("Date");

            populateNote();
            if(contentString != null){
                inEditMode = true;
            }
        }
    }


    private void populateNote() {

        content.setText(contentString);
    }

    private void alert() {
        closeKeyboard();
        new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Do you really want to delete this note ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dbAdapter.deleteData(date);
                        getActivity().getSupportFragmentManager().popBackStack();
                        closeKeyboard();

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        closeKeyboard();
    }

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        closeKeyboard();
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        closeKeyboard();
    }
}
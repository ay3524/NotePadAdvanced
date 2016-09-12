package com.ay3524.notepadadvanced;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by ashishyadav271 on 7/12/2016.
 */
public class Notes {
    //String noteTitle;
    String noteContent;
    //Long noteCreatedDate;
    Long noteModifiedDate;
    String noteReadableDate;
    public String getReadableModifiedDate(){
        Calendar calendar = new GregorianCalendar().getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM, yyyy - h:mm:ss a");
        simpleDateFormat.setTimeZone(calendar.getTimeZone());
        calendar.setTimeInMillis(this.getNoteModifiedDate());

        Date modifiedDate = calendar.getTime();
        return simpleDateFormat.format(modifiedDate);
    }

    /*public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }*/

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
/*
    public Long getNoteCreatedDate() {
        return noteCreatedDate;
    }

    public void setNoteCreatedDate(Long noteCreatedDate) {
        this.noteCreatedDate = noteCreatedDate;
    }*/

    public String getNoteReadableDate() {
        return noteReadableDate;
    }

    public void setNoteReadableDate(String noteReadableDate) {
        this.noteReadableDate = noteReadableDate;
    }

    public Long getNoteModifiedDate() {
        return noteModifiedDate;
    }

    public void setNoteModifiedDate(Long noteModifiedDate) {
        this.noteModifiedDate = noteModifiedDate;
    }
}

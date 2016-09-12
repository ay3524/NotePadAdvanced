package com.ay3524.notepadadvanced;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ashishyadav271 on 7/12/2016.
 */
public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder>{

    List<Notes> mNotes;
    Context mContext;
    ClickListener clickListener;

    public NoteListAdapter(List<Notes> notes,Context context){
        mContext = context;
        mNotes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note_list,parent,false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notes selectedNote = mNotes.get(position);

        String titleFromContent = selectedNote.getNoteContent();
        holder.noteTitle.setText(titleFromContent);
        holder.noteCreatedDate.setText(selectedNote.getNoteReadableDate());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }
    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteTitle;
        TextView noteCreatedDate;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.view = itemView;
            noteTitle = (TextView) itemView.findViewById(R.id.noteTitle);
            noteCreatedDate = (TextView) itemView.findViewById(R.id.noteDate);
            //itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.onItemClick(view , getAdapterPosition());
            }

        }

    }
    public interface ClickListener{
        void onItemClick(View v, int pos);
    }
}
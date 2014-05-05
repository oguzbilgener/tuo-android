package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Note;


/**
 * Created by begum on 05/05/14.
 */
public class NotesAdapter extends ArrayAdapter<Note> {

    ArrayList<Note> noteList;
    LayoutInflater inflater;
    Context context;

    public NotesAdapter(Context context, ArrayList<Note> noteList) {
        super(context, R.layout.item_note_plain, noteList);

        this.context = context;
        this.noteList = noteList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder {
        // Common for every type of note
        protected TextView noteTitle;
        protected TextView date;

        // For everything except to-do note
        protected TextView tag1;
        protected TextView tag2;

        // For plain note
        protected TextView noteDescription;

        // For photo note
        protected ImageView photo;

        // TODO: Figure out what to do with the audio

        // For to-do note
        protected CheckBox checkBox1;
        protected CheckBox checkBox2;
        protected CheckBox checkBox3;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        int id;

        if(getItemViewType(position) == getItem(position).NOTE_PLAIN)
            id = R.layout.item_note_plain;
        else if(getItemViewType(position) == getItem(position).NOTE_PHOTO)
            id = R.layout.item_note_photo;
        else if(getItemViewType(position) == getItem(position).NOTE_AUDIO)
            id = R.layout.item_note_voice;
        else (getItemViewType(position) == getItem(position).NOTE_TODO)
            id = R.layout.item_note_todo;

        if (convertView == null) {
            view = inflater.inflate( id, null);
            final ViewHolder viewHolder = new ViewHolder();

            if (getItemViewType(position) == getItem(position).NOTE_PLAIN) {
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_plain_title);
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getStatus();
    }

    @Override
    public int getViewTypeCount()
    {
        return 4;
    }
}

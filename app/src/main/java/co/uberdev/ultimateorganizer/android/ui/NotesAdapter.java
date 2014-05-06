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
        else
            id = R.layout.item_note_todo;

        if (convertView == null) {
            view = inflater.inflate( id, null);
            final ViewHolder viewHolder = new ViewHolder();

            if (getItemViewType(position) == getItem(position).NOTE_PLAIN) {
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_plain_title);
                viewHolder.date = (TextView) view.findViewById(R.id.note_plain_date);
                viewHolder.tag1 = (TextView) view.findViewById(R.id.plain_tag_1);
                viewHolder.tag2 = (TextView) view.findViewById(R.id.plain_tag_2);
                viewHolder.noteDescription = (TextView) view.findViewById(R.id.note_plain_description);
                view.setTag(R.id.note_item_object,viewHolder);

            }
            else if (getItemViewType(position) == getItem(position).NOTE_PHOTO) {
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_photo_title);
                viewHolder.date = (TextView) view.findViewById(R.id.note_photo_date);
                viewHolder.tag1 = (TextView) view.findViewById(R.id.note_photo_tag_1);
                viewHolder.tag2 = (TextView) view.findViewById(R.id.note_photo_tag_2);
                viewHolder.photo = (ImageView) view.findViewById(R.id.note_photo_photo);
                view.setTag(R.id.note_item_object,viewHolder);
            }
            else if(getItemViewType(position) == getItem(position).NOTE_AUDIO) {
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_voice_title);
                viewHolder.date = (TextView) view.findViewById(R.id.note_voice_date);
                viewHolder.tag1 = (TextView) view.findViewById(R.id.voice_tag_1);
                viewHolder.tag2 = (TextView) view.findViewById(R.id.voice_tag_2);

                // TODO: The audio...
                view.setTag(R.id.note_item_object,viewHolder);
            }
            else if (getItemViewType(position) == getItem(position).NOTE_TODO){
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_todo_title);
                viewHolder.date = (TextView) view.findViewById(R.id.note_todo_date);
                viewHolder.checkBox1 = (CheckBox) view.findViewById(R.id.todo_checkbox_1);
                viewHolder.checkBox2 = (CheckBox) view.findViewById(R.id.todo_checkbox_2);
                viewHolder.checkBox3 = (CheckBox) view.findViewById(R.id.todo_checkbox_3);
                view.setTag(R.id.note_item_object,viewHolder);
            }
        }
        else
            view = convertView;

        final ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.note_item_object);

        Note noteItem = noteList.get(position);

        view.setTag(R.id.note_item_id, noteItem.getId());
        view.setTag(R.id.note_item_index, position);

        viewHolder.noteTitle.setText(noteItem.getNoteTitle());
//        viewHolder.date.setText(noteItem.getLastModified());

        return view;
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

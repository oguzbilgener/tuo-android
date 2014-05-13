package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Note;
import co.uberdev.ultimateorganizer.android.util.Utils;


/**
 * Created by begum on 05/05/14.
 */
public class NotesAdapter extends ArrayAdapter<Note> implements View.OnClickListener
{

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
		protected RelativeLayout innerLayout;
        protected TextView noteTitle;
        protected TextView date;

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
				viewHolder.innerLayout = (RelativeLayout) view.findViewById(R.id.item_note_plain_inner_layout);
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_plain_title);
                viewHolder.date = (TextView) view.findViewById(R.id.note_plain_date);
                viewHolder.noteDescription = (TextView) view.findViewById(R.id.note_plain_description);
                view.setTag(R.id.note_item_object,viewHolder);

            }
            else if (getItemViewType(position) == getItem(position).NOTE_PHOTO) {
				viewHolder.innerLayout = (RelativeLayout) view.findViewById(R.id.item_note_photo_inner_layout);
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_photo_title);
                viewHolder.date = (TextView) view.findViewById(R.id.note_photo_date);
                viewHolder.photo = (ImageView) view.findViewById(R.id.note_photo_photo);
                view.setTag(R.id.note_item_object,viewHolder);
            }
            else if(getItemViewType(position) == getItem(position).NOTE_AUDIO) {
                viewHolder.noteTitle = (TextView) view.findViewById(R.id.note_voice_title);
                viewHolder.date = (TextView) view.findViewById(R.id.note_voice_date);

                // TODO: The audio...
                view.setTag(R.id.note_item_object,viewHolder);
            }
            else if (getItemViewType(position) == getItem(position).NOTE_TODO){
				viewHolder.innerLayout = (RelativeLayout) view.findViewById(R.id.item_note_todo_inner_layout);
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

		if(viewHolder.innerLayout != null)
		{
			viewHolder.innerLayout.setOnClickListener(this);
			viewHolder.innerLayout.setTag(R.id.note_item_index, position);
		}

        viewHolder.noteTitle.setText(noteItem.getNoteTitle());
		viewHolder.noteDescription.setText(noteItem.getContent());
//        viewHolder.date.setText(noteItem.getLastModified());

		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
		Date lastModifiedDate = new Date((long)noteItem.getLastModified()*1000);

		if(Utils.isDateToday(noteItem.getLastModified()))
		{
			viewHolder.date.setText(getContext().
							getString(R.string.today_capital) + "\n" + hourFormat.format(lastModifiedDate)
			);
		}
		else if(Utils.isDateToday(noteItem.getLastModified()))
		{
			viewHolder.date.setText(getContext().
					getString(R.string.yesterday_capital) +"\n"+hourFormat.format(lastModifiedDate)
			);
		}
		else
		{
			Calendar lastModifiedCalendar = Calendar.getInstance();
			lastModifiedCalendar.setTime(lastModifiedDate);

			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
			viewHolder.date.setText(
					dateFormat.format(new Date(((long)noteItem.getLastModified()*1000))));
		}

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

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.item_note_plain_inner_layout)
		{
			long localId = noteList.get((Integer)v.getTag(R.id.note_item_index)).getLocalId();
			Intent editIntent = new Intent(context, ViewNoteActivity.class);
			editIntent.putExtra(context.getString(R.string.INTENT_DETAILS_NOTE_LOCAL_ID), localId);

			context.startActivity(editIntent);
		}
	}
}

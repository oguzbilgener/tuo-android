package co.uberdev.ultimateorganizer.android.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Note;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Created by begum on 10/05/14.
 */
public class AddNoteFragment extends Fragment {

    private TextView notePreview;
    private EditText noteEdit;
    private TextView noteDate;
    public Note shownNote;

    public AddNoteFragment() {  }

    public static AddNoteFragment newInstance(Context context, Note shownNote)
    {
        AddNoteFragment fragment = new AddNoteFragment();
        fragment.shownNote = shownNote;
        String noteJsonStr = shownNote.asJsonString();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.ARGS_NOTE_JSON_OBJECT), noteJsonStr);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance()
    {
        return new AddNoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_note, container, false);

        notePreview = (TextView) rootView.findViewById(R.id.add_note_preview);
        noteEdit = (EditText) rootView.findViewById(R.id.add_note_edit);
        noteDate = (TextView) rootView.findViewById(R.id.add_note_date);

        notePreview.setText(shownNote.getContent());
        noteEdit.setVisibility(View.INVISIBLE);

        if(Utils.isDateToday(shownNote.getLastModified()))
        {
            noteDate.setText(
                    getString(R.string.today_capital)
            );
        }
        else if(Utils.isDateToday(shownNote.getLastModified()))
        {
            noteDate.setText(
                    getString(R.string.yesterday_capital)
            );
        }
        else
        {
            Date lastModifiedDate = new Date((long)shownNote.getLastModified()*1000);
            Calendar lastModifiedCalendar = Calendar.getInstance();
            lastModifiedCalendar.setTime(lastModifiedDate);

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy HH:mm");
                noteDate.setText(
                        dateFormat.format(new Date(((long)shownNote.getLastModified()*1000))));
        }

        return rootView;
    }
}

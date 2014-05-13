package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Note;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by begum on 10/05/14.
 */
public class ViewNoteFragment extends Fragment implements FragmentCommunicator, View.OnClickListener
{
    private TextView notePreview;
    private EditText noteEdit;
    private TextView noteDate;
    private CoreUser user;
    public Note shownNote;

	private ViewSwitcher switcher;

	public int viewType;
	public static final int TYPE_PREVIEW = 0;
	public static final int TYPE_EDIT = 1;

    private ActivityCommunicator activityCommunicator;

    public static final int MESSAGE_REQUEST_NOTE = -99;
    public static final int MESSAGE_RESPONSE_NOTE = -98;

	public static final int MESSAGE_BEGIN_EDITING = -77;
	public static final int MESSAGE_END_EDITING = -78;

	public static final int REQUEST_BEGIN_EDITING = -57;

    public ViewNoteFragment() {  }

    public static ViewNoteFragment newInstance(Context context, Note shownNote)
    {
        ViewNoteFragment fragment = new ViewNoteFragment();
        fragment.shownNote = shownNote;
		if(shownNote != null)
		{
			String noteJsonStr = shownNote.asJsonString();
			Bundle args = new Bundle();
			args.putString(context.getString(R.string.ARGS_NOTE_JSON_OBJECT), noteJsonStr);
			fragment.setArguments(args);
			fragment.viewType = TYPE_PREVIEW;
		}
		else
		{
			fragment.shownNote = new Note();
			fragment.viewType = TYPE_EDIT;
		}
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = ((UltimateApplication) getActivity().getApplication()).getUser();
    }

    public static Fragment newInstance()
    {
        return new ViewNoteFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_note, container, false);

        notePreview = (TextView) rootView.findViewById(R.id.add_note_preview);
//		ViewGroup.LayoutParams maxParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//		noteEdit = new EditText(getActivity());
//		notePreview = new EditText(getActivity());
//		noteEdit.setLayoutParams(maxParams);
//		notePreview.setLayoutParams(maxParams);
//		notePreview.setClickable(true);
//		notePreview.setOnClickListener(this);
//		noteEdit.setId(R.id.note_view_edit);
//		notePreview.setId(R.id.note_view_preview);
//        noteEdit = (EditText) rootView.findViewById(R.id.add_note_edit);
        noteDate = (TextView) rootView.findViewById(R.id.add_note_date);

		switcher = (ViewSwitcher) rootView.findViewById(R.id.switcher);
//		switcher.addView(noteEdit);
//		switcher.addView(notePreview);

		if(viewType == TYPE_PREVIEW)
			switcher.showPrevious();
		else
			switcher.showNext();

        notePreview.setText(shownNote.getContent());

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activityCommunicator = (ActivityCommunicator) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activityCommunicator = null;
    }

    public FragmentActivity getParent()
    {
        return (FragmentActivity) getActivity();
    }

    public Note buildNote()
    {
        Bundle args = getArguments();
        Note note;
        if(shownNote == null && !args.containsKey(getString(R.string.ARGS_NOTE_JSON_OBJECT)))
        {
            note = new Note();
        }
        else if(shownNote != null)
        {
            note = shownNote;
        }
        else
        {
            String noteStr = args.getString(getString(R.string.ARGS_NOTE_JSON_OBJECT));
            note = Note.fromJson(noteStr, Note.class);
            if(note == null)
            {
                note = new Note();
            }
        }

        note.setContent(noteEdit.getText().toString());

        if(user != null)
        {
            note.setOwnerId(user.getId());
        }

        return note;
    }

    public void fillFromNote(Note note)
    {
        notePreview.setText(note.getContent());
        noteEdit.setText(note.getContent());
    }

    @Override
    public void onMessage(int msgType, Object obj) {
        switch(msgType)
        {
            case MESSAGE_REQUEST_NOTE:
                activityCommunicator.onMessage(MESSAGE_RESPONSE_NOTE, buildNote());
                break;
        }
    }

	@Override
	public void onClick(View v)
	{
		if(v.getId() == R.id.add_note_preview)
		{
			activityCommunicator.onMessage(REQUEST_BEGIN_EDITING, null);
//			switcher.showNext();
//			invalidateActionBar();
		}
	}

	public int getViewType()
	{
		return viewType;
	}
}

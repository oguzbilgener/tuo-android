package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Note;

/**
 * Created by oguzbilgener on 15/03/14.
 */
public class NotesFragment extends Fragment
{

    protected ListView noteListView;
    protected ArrayList<Note> noteList;
    protected NotesAdapter noteAdapter;


	public NotesFragment()
	{
        // Required empty constructor
	}

	public static NotesFragment newInstance() {
		NotesFragment fragment = new NotesFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

    public static int getViewId() {
        return R.id.base_fragment_frame;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        noteList = new ArrayList<Note>();
        Note note = new Note();
        note.setNoteTitle("Calculus Time!");
        note.setNoteDescription("stop whatever you're doing and study calculus");
        note.setDateCreated(123123321);
        note.setLastModified( 123123321);
        try{
            note.setStatus( 0);
        }
        catch( Exception e){
            e.printStackTrace();
        }

        noteList.add(note);

        Note note2 = new Note();
        note2.setNoteTitle("Fizik çok boktan");
        note2.setDateCreated(73321);
        note2.setLastModified( 123123321);

        try{
            note2.setStatus( 1);
        }
        catch( Exception e){
            e.printStackTrace();
        }
        noteList.add(note2);

        noteAdapter = new NotesAdapter(getActivity(), noteList);
    }

	@Override
	public void onResume()
	{
		super.onResume();

//		EventBus.getDefault().register(this);
	}

	@Override
	public void onPause()
	{
		super.onPause();

//		EventBus.getDefault().unregister(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        noteListView = (ListView) rootView.findViewById(R.id.notes_list_view);
        noteListView.setAdapter(noteAdapter);
		return rootView;
	}

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public HomeActivity getHomeActivity()
    {
        return (HomeActivity) getActivity();
    }
}



package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.origamilabs.library.views.StaggeredGridView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Note;
import co.uberdev.ultimateorganizer.core.CoreAttachment;

/**
 * Created by oguzbilgener on 15/03/14.
 */
public class NotesFragment extends Fragment
{

    protected StaggeredGridView noteListview;
    protected ArrayList<Note> noteList;
    protected NotesAdapter noteAdapter;

    protected OnFragmentInteractionListener mListener;

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
        note.setDateCreated( 123123321);
        note.setLastModified( 123123321);
        try{
            note.setStatus( 0);
        }
        catch( Exception e){
            e.printStackTrace();
        }

        noteList.add(note);
        Note note2 = new Note();
        note2.setNoteTitle("fizik cok boktan");
        note2.setDateCreated(73321);
        note2.setLastModified( 123123321);
        try{
            note2.setStatus( 1);
        }
        catch( Exception e){
            e.printStackTrace();
        }



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
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);
		TextView textView = (TextView) rootView.findViewById(R.id.section_label);
		textView.setText("Notes");
        noteListview = (StaggeredGridView) rootView.findViewById(R.id.staggered_grid);
        noteListview.setAdapter(noteAdapter);
		return rootView;
	}

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public HomeActivity getHomeActivity()
    {
        return (HomeActivity) getActivity();
    }
}



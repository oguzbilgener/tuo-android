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
import co.uberdev.ultimateorganizer.android.models.Notes;

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

        noteAdapter = new NotesAdapter(getActivity(), noteList);
    }

	@Override
	public void onResume()
	{
		super.onResume();

		noteList.clear();

		Notes fromdb = new Notes(getHomeActivity().getLocalStorage().getDb());

		fromdb.loadFromDb(null, new String[] {}, 0);

		for(int i=0; i<fromdb.size(); i++)
		{
			noteList.add((Note) fromdb.get(i));
		}
		noteAdapter.notifyDataSetChanged();
	}

	@Override
	public void onPause()
	{
		super.onPause();


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


    public HomeActivity getHomeActivity()
    {
        return (HomeActivity) getActivity();
    }
}



package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.network.TaskRemoveTask;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreUser;

/**
 * Created by dunkuCodder 02/05/2014
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverviewCommonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OverviewCommonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class OverviewCommonFragment extends Fragment implements OverviewTaskAdapter.OnTaskRemovedListener
{
    protected OnFragmentInteractionListener mListener;

    protected ListView overviewTaskListview;
    protected ArrayList<Task> overviewTaskList;
    protected OverviewTaskAdapter overviewTaskAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OverviewTaskFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static OverviewCommonFragment newInstance()
    {
        OverviewCommonFragment fragment = new OverviewCommonFragment();
        return fragment;
    }
    public OverviewCommonFragment() {
        // Required empty public constructor
    }

    public static int getViewId() {
        return R.id.base_fragment_frame;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        overviewTaskList = new ArrayList<Task>();

        overviewTaskAdapter = new OverviewTaskAdapter(getActivity(), overviewTaskList);
		overviewTaskAdapter.setLocalStorage(getHomeActivity().getLocalStorage());

		overviewTaskAdapter.setOnTaskRemovedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tasks_home, container, false);

        overviewTaskListview = (ListView) rootView.findViewById(R.id.overview_tasks_list_view);

        overviewTaskListview.setAdapter(overviewTaskAdapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

	@Override
	public void onTaskRemoved(int position, Task task)
	{
		Utils.log.d("onTaskRemoved");
		// remove task from the server as well, if the user is signed in
		CoreUser user = ((UltimateApplication) getActivity().getApplication()).getUser();
		if(user != null)
		{
			new TaskRemoveTask(getActivity(), user, task).execute();
		}
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

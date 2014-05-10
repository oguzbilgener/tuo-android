package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Task;
import co.uberdev.ultimateorganizer.android.network.GetPublicFeedTask;
import co.uberdev.ultimateorganizer.android.network.TaskListener;
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreTask;
import co.uberdev.ultimateorganizer.core.CoreUser;

public class PublicFeedFragment extends Fragment implements TaskListener
{
	private CoreUser user;
	private LocalStorage localStorage;

    private ListView publicFeedListView;
    private ArrayList<Task> publicFeedTasksList;
    private PublicFeedAdapter publicFeedAdapter;

	private ActivityCommunicator activityCommunicator;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PublicFeedFragment.
     */
    public static PublicFeedFragment newInstance()
    {
        PublicFeedFragment fragment = new PublicFeedFragment();
        return fragment;
    }
    public PublicFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        publicFeedTasksList = new ArrayList<Task>();

		user = ((UltimateApplication) getActivity().getApplication()).getUser();

        Task task = new Task();
        task.setCourseCodeCombined( "TURK102-13");
        task.setTaskName("Third Homework");
        task.setTaskDesc("Letter to Papa");
        task.setTaskOwnerNameCombined("Ani Kristo");
        task.setEndDate( 1234567);
        publicFeedTasksList.add(task);

        Task task2 = new Task();
        task2.setCourseCodeCombined( "CS102-13");
        task2.setTaskName("Project");
        task2.setTaskDesc("Finish It");
        task2.setTaskOwnerNameCombined("Begum Ozcan");
        task2.setEndDate( 12345367);
        publicFeedTasksList.add(task2);

        publicFeedAdapter = new PublicFeedAdapter(getActivity(), publicFeedTasksList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_academic_network_public_feed, container, false);

        publicFeedListView = (ListView) rootView.findViewById(R.id.academic_network_public_feed_listview);

        publicFeedListView.setAdapter(publicFeedAdapter);

		new GetPublicFeedTask(getActivity(), user, this).execute();

        return rootView;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            activityCommunicator = (ActivityCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ActivityCommunicator");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        activityCommunicator = null;
    }

	@Override
	public void onPreExecute()
	{
		if(activityCommunicator != null)
		{
			activityCommunicator.onMessage(AcademicNetworkActivity.MESSAGE_LOADING_STARTED, null);
		}
	}

	@Override
	public void onPostExecute(Integer result, Object data)
	{
		if(activityCommunicator != null)
		{
			activityCommunicator.onMessage(AcademicNetworkActivity.MESSAGE_LOADING_ENDED, null);
		}

		if(result == GetPublicFeedTask.SUCCESS && data != null)
		{
			try
			{
				publicFeedTasksList.clear();

				CoreTask[] publicTasks = (CoreTask[]) data;
				for(int i=0; i<publicTasks.length; i++)
				{
					publicFeedTasksList.add((Task)publicTasks[i]);
				}

				publicFeedAdapter.notifyDataSetChanged();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Toast.makeText(getActivity(), getString(R.string.unknown_error_public_feed), Toast.LENGTH_SHORT).show();
			}
		}
		else if(result == GetPublicFeedTask.ERROR_NETWORK)
		{
			Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		}
		else if(result == GetPublicFeedTask.ERROR_UNAUTHORIZED)
		{
			Toast.makeText(getActivity(), getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
		}
		else
		{
			Utils.log.w("Unknown error while loading tasks");
			Toast.makeText(getActivity(), getString(R.string.unknown_error_public_feed), Toast.LENGTH_SHORT).show();
		}
	}
}

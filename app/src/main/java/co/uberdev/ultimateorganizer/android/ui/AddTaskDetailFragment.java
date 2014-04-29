package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.DatePickerFragment;
import co.uberdev.ultimateorganizer.android.util.DatePickerSpinnerAdapter;
import co.uberdev.ultimateorganizer.android.util.SpinnerStateAdapter;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddTaskDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddTaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddTaskDetailFragment extends Fragment  implements DatePickerFragment.DatePickerFragmentCallbacks, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

	private DatePickerSpinnerAdapter fromDateSpinnerAdapter;
	private DatePickerSpinnerAdapter toDateSpinnerAdapter;

	private DateSpinnerStateAdapter fromDateStateSpinnerAdapter;
	private DateSpinnerStateAdapter toDateStateSpinnerAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTaskDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTaskDetailFragment newInstance(String param1, String param2) {
        AddTaskDetailFragment fragment = new AddTaskDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public AddTaskDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
		fromDateSpinnerAdapter = new DatePickerSpinnerAdapter(getActivity());
		toDateSpinnerAdapter = new DatePickerSpinnerAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_add_task_detailed, container, false);

		Spinner fromDateSpinner = (Spinner) rootView.findViewById(R.id.add_task_spinner_from_date);
		Spinner fromTimeSpinner = (Spinner) rootView.findViewById(R.id.add_task_spinner_from_time);

		Spinner toDateSpinner = (Spinner) rootView.findViewById(R.id.add_task_spinner_to_date);
		Spinner toTimeSpinner = (Spinner) rootView.findViewById(R.id.add_task_spinner_to_time);

		fromDateStateSpinnerAdapter = new DateSpinnerStateAdapter(fromDateSpinner);
		toDateStateSpinnerAdapter = new DateSpinnerStateAdapter(toDateSpinner);

		fromDateSpinner.setAdapter(fromDateSpinnerAdapter);
		toDateSpinner.setAdapter(toDateSpinnerAdapter);

		return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

	private class DateSpinnerStateAdapter extends SpinnerStateAdapter
	{
		int id;
		public DateSpinnerStateAdapter(Spinner spinner)
		{
			super(spinner);
			this.id = spinner.getId();
		}

		@Override
		public void onOpen()
		{
			if(id == R.id.add_task_spinner_from_date)
			{
				// show date picker for from
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getFragmentManager(), getString(R.string.DATE_PICKER_FROM));
			}
			else if(id == R.id.add_task_spinner_from_time)
			{
				// show time picker for from
			}
			else if(id == R.id.add_task_spinner_to_date)
			{
				// show date picker for to
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getFragmentManager(), getString(R.string.DATE_PICKER_TO));
			}
		}

		@Override
		public void onClose()
		{

		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day)
	{
		int id = view.getId();

		if(id == R.id.add_task_spinner_from_date)
		{
			// update date picker for from
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);

			fromDateSpinnerAdapter.updateCalendar(cal);
		}
		else if(id == R.id.add_task_spinner_to_time)
		{
			// update time picker for from
		}

	}

	@Override
	public void onClick(View view)
	{
		int id = view.getId();


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
    public static interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

package co.uberdev.ultimateorganizer.android.ui;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.DatePickerFragment;

public class AddTaskActivity extends Activity implements DatePickerFragment.DatePickerFragmentCallbacks, AddTaskDetailFragment.OnFragmentInteractionListener
{
	private DatePickerFragment.DatePickerFragmentCallbacks mOnDatePickResultListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_overview)));

		AddTaskDetailFragment fragment = new AddTaskDetailFragment();
		mOnDatePickResultListener = (DatePickerFragment.DatePickerFragmentCallbacks) fragment;

        setContentView(R.layout.activity_add_task);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
		Bundle extras = getIntent().getExtras();
		if(id == android.R.id.home && extras != null && extras.getString(getString(R.string.INTENT_CALLER_ACTIVITY)) != null) {
			finish();
			return true;
		}

        if (id == R.id.action_add_task) {
			// literally add the task, show a popup and return back to HomeActivity
			finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onDateSet(DatePicker view, int year, int month, int day)
	{
		mOnDatePickResultListener.onDateSet(view, year, month, day);
	}

	@Override
	public void onFragmentInteraction(Uri uri) {
		// TODO: use this
	}
}

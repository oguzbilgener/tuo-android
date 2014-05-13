package co.uberdev.ultimateorganizer.android.ui;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.db.LocalStorage;
import co.uberdev.ultimateorganizer.android.models.Note;
import co.uberdev.ultimateorganizer.android.models.Notes;
import co.uberdev.ultimateorganizer.core.CoreDataRules;

/**
 * Created by begum on 10/05/14.
 */
public class ViewNoteActivity extends FragmentActivity
{
	private LocalStorage localStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);

		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_notes)));

		localStorage = new LocalStorage(this);

		Note noteToShow = null;

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(getResources().getString(R.string.INTENT_DETAILS_NOTE_LOCAL_ID)))
		{
			long localId = getIntent().getExtras().getLong(getResources().getString(R.string.INTENT_DETAILS_NOTE_LOCAL_ID));

			Notes notes = new Notes(localStorage.getDb());
			notes.loadFromDb(CoreDataRules.columns.notes.localId+" = ?", new String[]{String.valueOf(localId)},0);

			if(notes.size() > 0)
			{
				noteToShow = (Note) notes.get(0);
			}
			// if there is no such note, this is an add note activity!
		}

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, ViewNoteFragment.newInstance(this, noteToShow))
					.commit();
		}


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(id == android.R.id.home) {
			finish();
			return true;
		}
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onResume()
	{
		super.onResume();

		if (localStorage != null)
			localStorage.reopen();
	}

	public void onDestroy()
	{
		localStorage.close();
		super.onDestroy();
	}
}

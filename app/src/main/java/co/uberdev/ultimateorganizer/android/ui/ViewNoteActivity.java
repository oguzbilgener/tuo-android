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
import co.uberdev.ultimateorganizer.android.util.ActivityCommunicator;
import co.uberdev.ultimateorganizer.android.util.FragmentCommunicator;
import co.uberdev.ultimateorganizer.android.util.Utils;
import co.uberdev.ultimateorganizer.core.CoreDataRules;

/**
 * Created by begum on 10/05/14.
 */
public class ViewNoteActivity extends FragmentActivity implements ActivityCommunicator
{
	private LocalStorage localStorage;
	private static final String FRAG_TAG = "note_frag";
	private FragmentCommunicator fragmentCommunicator;

	private int viewType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);

		getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_section_notes)));

		localStorage = new LocalStorage(this);

		Note noteToShow = null;

		viewType = 0;

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(getResources().getString(R.string.INTENT_DETAILS_NOTE_LOCAL_ID)))
		{
			long localId = getIntent().getExtras().getLong(getResources().getString(R.string.INTENT_DETAILS_NOTE_LOCAL_ID));

			Notes notes = new Notes(localStorage.getDb());
			notes.loadFromDb(CoreDataRules.columns.notes.localId+" = ?", new String[]{String.valueOf(localId)},0);

			if(notes.size() > 0)
			{
				noteToShow = (Note) notes.get(0);;
			}
			// if there is no such note, this is an add note activity!
		}

		if (savedInstanceState == null) {

			ViewNoteFragment fragment = ViewNoteFragment.newInstance(this, noteToShow);
			fragmentCommunicator = fragment;
			getSupportFragmentManager().beginTransaction()
					.add(R.id.note_fragment_container,  fragment, FRAG_TAG)
					.commit();
		}

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (localStorage != null)
			localStorage.reopen();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		int menuId;
		if(viewType == ViewNoteFragment.TYPE_EDIT)
		{
			Utils.log.d("menu edit");
			menuId = R.menu.view_note_edit;
		}
		else
		{
			Utils.log.d("menu preview");
			menuId = R.menu.view_note_preview;
		}
		getMenuInflater().inflate(menuId, menu);
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
		if (id == R.id.action_edit_note) {
			fragmentCommunicator.onMessage(ViewNoteFragment.MESSAGE_BEGIN_EDITING, null);
			return true;
		}
		if (id == R.id.action_save_note) {
			fragmentCommunicator.onMessage(ViewNoteFragment.MESSAGE_END_EDITING, null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onDestroy()
	{
		localStorage.close();
		super.onDestroy();
	}

	@Override
	public void onMessage(int msgType, Object obj)
	{
		if(msgType == ViewNoteFragment.REQUEST_BEGIN_EDITING)
		{
			invalidateOptionsMenu();
		}

		else if(msgType == ViewNoteFragment.MESSAGE_RESPONSE_NOTE)
		{
			invalidateOptionsMenu();
			if(obj != null && obj instanceof Note)
			{
				Note latestNote = (Note) obj;
				latestNote.setDb(localStorage.getDb());
				latestNote.setLastModified(Utils.getUnixTimestamp());

				if(latestNote.getLocalId() >= 0)
				{
					latestNote.update();
				}
				else
				{
					latestNote.insert();
				}
			}
		}

		else if(msgType == ViewNoteFragment.REQUEST_CHANGE_TYPE)
		{
			if(obj != null)
			{
				this.viewType = (Integer) obj;
				invalidateOptionsMenu();
			}
		}
	}
}

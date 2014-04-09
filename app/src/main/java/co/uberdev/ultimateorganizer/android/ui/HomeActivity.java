package co.uberdev.ultimateorganizer.android.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Locale;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.Utils;


public class HomeActivity extends FragmentActivity
        implements HomeNavigationDrawerFragment.NavigationDrawerCallbacks,
		ViewPager.OnPageChangeListener,
		ActionBar.OnNavigationListener {

    // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private HomeNavigationDrawerFragment mHomeNavigationDrawerFragment;

	// ViewPager that contains the fragments
	public SectionsPagerAdapter mSectionsPagerAdapter;
	public ViewPager mViewPager;

	public SubPageAdapter mSubPageAdapter;

	// Spinner as a sub level navigation element
	private SpinnerAdapter mSpinnerAdapter;

	private boolean firstPageCreated;
	private boolean nextPageCreated;
	private boolean pageJustChanged;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

	/**
	 * Called when the Activity is created.
	 * @param savedInstanceState the instance state from the last creation. It might be null as well.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mSubPageAdapter = new SubPageAdapter();

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.homePager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(this);

		// Initialize the drawer.
        mHomeNavigationDrawerFragment = (HomeNavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mHomeNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

		// Register in-app events
//		EventBus.getDefault().register(this);

		// prepare the ActionBar for the first page
		if(mHomeNavigationDrawerFragment.hasUserLearnedDrawer()) {
			getActionBar().setBackgroundDrawable(mSectionsPagerAdapter.getColorBackground(0));
		}
		else {
			// Apply specific drawer color to the ActionBar
			int drawerColor = getResources().getColor(R.color.title_navigation_drawer);
			getActionBar().setBackgroundDrawable(new ColorDrawable(drawerColor));
		}

		firstPageCreated = false;
		nextPageCreated = false;
		pageJustChanged = false;
    }

	@Override
	public void onResume() {
		super.onResume();
		// Indicate the latest navigation item in the navigation drawer
		mHomeNavigationDrawerFragment.getmNavigationItemAdapter().setSelectedNavigationItem(mViewPager.getCurrentItem());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Utils.log.i("onSaveInstanceState");

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		Utils.log.i("onPause");
		// Remember sub navigation positions!
		mSubPageAdapter.permanantlyStoreSubPositions();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// Unregister in-app events
//		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	/**
	 * The correct way to arbitrarily change the page of the ViewPager
	 * Called by HomeNavigationFragment and onCreate of HomeActivity
	 * @param position the position of the main level navigation
	 */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
		if(position < 3) {
			Utils.log.w("onNavigationDrawerItemSelected " + position);
			// Tell the ViewPager to display the new fragment
			if (mViewPager != null) {
				// Literally set the current item
				mViewPager.setCurrentItem(position, false);

				prepareActionBar(position);
			}
		}
		else {
			// Start those activities with an extra, specifying that they are called from home activity
			Intent startIntent;
			if(position == 3) {
				startIntent = new Intent(this, ScheduleActivity.class);
			}
			else /* if(position == 4)*/ {
				startIntent = new Intent(this, AcademicNetworkActivity.class);
			}
			startIntent.putExtra(getString(R.string.INTENT_CALLER_ACTIVITY),getString(R.string.INTENT_CALLER_HOME));
			startActivity(startIntent);
		}
    }

	public void prepareActionBar(int position)
	{
		// Apply the final ActionBar background color
		getActionBar().setBackgroundDrawable(mSectionsPagerAdapter.getColorBackground(position));
		// Modify the ActionBar to make it relevant
		mSectionsPagerAdapter.applyActionBarRules(position);
	}

	/**
	 * Called when the user changes the sub level navigation item (Spinner), by the ActionBar Spinner.
	 * @param position the position of the sub level navigation
	 * @param id
	 * @return
	 */
	@Override
	public boolean onNavigationItemSelected(int position, long id) {

		Utils.log.i("onNavigationItemSelected "+position+" @ "+getCurrentPageIndex());
		if(getCurrentPageIndex() == 0 && !firstPageCreated && position == 0) {
			getActionBar().setSelectedNavigationItem(mSubPageAdapter.getCurrentSubPosition(0));
			firstPageCreated = true;
			pageJustChanged = false;
		}
		if(!nextPageCreated) {
			nextPageCreated = true;
			if(getCurrentPageIndex() == 1) {
				getActionBar().setSelectedNavigationItem(mSubPageAdapter.getCurrentSubPosition(1));
				return false;
			}
			mSubPageAdapter.changeDisplayedFragment(1, mSubPageAdapter.getCurrentSubPosition(1));
			return true;
		}
		if(pageJustChanged) {
			pageJustChanged = false;
			try {
				Utils.log.w("call sub "+position+" while "+getCurrentPageIndex() +" and "+mSubPageAdapter.getCurrentSubPosition(getCurrentPageIndex()));
				getActionBar().setSelectedNavigationItem(mSubPageAdapter.getCurrentSubPosition(getCurrentPageIndex()));
			}
			catch(Exception e) {
				Utils.log.e(e.toString());
				e.printStackTrace();
			}
			return false;
		}
		// Change the sub navigation item
		Utils.log.e("hmm "+mSubPageAdapter.changeDisplayedFragment(getCurrentPageIndex(), position));
		return true;
	}

	/**
	 * Consecutively called while the user is changing the current page by swiping.
	 * @param position
	 * @param positionOffset
	 * @param positionOffsetPixels
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if(positionOffsetPixels > 0)
		{
			int oldPos = getCurrentPageIndex();
			int newPos = position;

			/*
			 Android's ViewPager behaves weird and it does not change the position parameter if
			 	it is a right to left swipe. it also gives the inverted position offset if
			 	it is a left to right swipe
			 */

			if(newPos != oldPos) {
				positionOffset = 1 - positionOffset;
			}
			else {
				newPos++;
			}
			// now get the colors from resources
			int oldColor = mSectionsPagerAdapter.getIntBackground(oldPos);
			int newColor = mSectionsPagerAdapter.getIntBackground(newPos);
			// mix em
			int mixColor = Utils.mixTwoColors(newColor, oldColor, positionOffset);
			// apply the mixture
			getActionBar().setBackgroundDrawable(new ColorDrawable(mixColor));
		}
	}

	/**
	 * Called when the user changes the pages with a swipe gesture
	 * @param position the position of the main level navigation
	 */
	@Override
	public void onPageSelected(int position) {
		Utils.log.d("onPageSelected "+position);
		// change the selected item in the action bar
		mHomeNavigationDrawerFragment.setItemChecked(position);
		// When a page is selected, apply its 100% opaque Action Bar color
		getActionBar().setBackgroundDrawable(mSectionsPagerAdapter.getColorBackground(position));
		// Modify the ActionBar to make it relevant
		mSectionsPagerAdapter.applyActionBarRules(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
//		getActionBar().setBackgroundDrawable(mSectionsPagerAdapter.getColorBackground(mViewPager.getCurrentItem()));
	}

	public int getCurrentPageIndex()
	{
		return mViewPager.getCurrentItem();
	}

    public void onSectionAttached(int number) {
		mTitle = mSectionsPagerAdapter.getPageTitle(number);
    }

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Creates a top level Fragment page and returns it
		 * Called by system
		 * @param position
		 * @return
		 */
		@Override
		public Fragment getItem(int position) {
			Utils.log.i("SectionsPagerAdapter getItem("+position+")");
			// getItem is called to instantiate the fragment for the given page.
			switch(position) {
				case 0:
					return OverviewBaseFragment.newInstance();
				case 1:
					return CalendarBaseFragment.newInstance();
				case 2:
				default:
					return NotesFragment.newInstance();
			}
		}


		/**
		 * Returns the count of top level navigation items
		 * @return
		 */
		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		/**
		 * Returns the titles of top level navigation items
		 * @param position
		 * @return
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position)
			{
				// Overview
				case 0:
					return getString(R.string.title_section_overview).toUpperCase(l);
				// Calendar
				case 1:
					return getString(R.string.title_section_calendar).toUpperCase(l);
				// Notes
				case 2:
					return getString(R.string.title_section_notes).toUpperCase(l);
			}
			return null;
		}

		public int getIntBackground(int position) {
			switch (position)
			{
				// Overview
				case 0:
					return getResources().getColor(R.color.title_section_overview);
				// Calendar
				case 1:
					return getResources().getColor(R.color.title_section_calendar);
				// Notes
				case 2:
					return getResources().getColor(R.color.title_section_notes);
			}
			return 0;
		}

		public ColorDrawable getColorBackground(int position) {
			return new ColorDrawable(getIntBackground(position));
		}

		/**
		 * Modifies HomeActivity's ActionBar according to the main navigation position
		 * @param position the main navigation position
		 */
		public void applyActionBarRules(int position) {
			Utils.log.d("applyActionBarRules("+position+")");
			final ActionBar actionBar = getActionBar();
			switch(position)
			{
				// Global Context Action Bar
				// To display when Navigation Drawer is open
				case -1:
					showGlobalContextActionBar();
					break;
				// Overview
				case 0:
					pageJustChanged = true;
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
					actionBar.setDisplayShowTitleEnabled(false);

                    String[] overviewSpinnerArray = getResources().getStringArray(R.array.title_section_overview_subs);

                    ArrayList<String> overviewSpinner = new ArrayList<String>();
                    for (int i = 0; i < overviewSpinnerArray.length; i++) {
                        overviewSpinner.add(overviewSpinnerArray[i]);
                    }

                    mSpinnerAdapter  = new OverviewSpinnerAdapter(getHomeActivity(), R.id.overview_spinner_item_text, overviewSpinner);

                    ((ArrayAdapter)mSpinnerAdapter).setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					actionBar.setListNavigationCallbacks(mSpinnerAdapter, getHomeActivity());
					break;
				// Calendar
				case 1:
					pageJustChanged = true;
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
					actionBar.setDisplayShowTitleEnabled(false);
					mSpinnerAdapter = ArrayAdapter.createFromResource(getHomeActivity(), R.array.title_section_calendar_subs,
							android.R.layout.simple_spinner_dropdown_item);
					actionBar.setListNavigationCallbacks(mSpinnerAdapter, getHomeActivity());
					break;
				// Notes
				case 2:
					pageJustChanged = true;
					actionBar.setDisplayShowTitleEnabled(true);
					actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					actionBar.setTitle(getPageTitle(position));
					break;
			}
		}
	}

	/**
	 * Holds the positions of the sub navigation pages, stores them in the SharedPreferences and
	 * displays the sub page in the ViewPager when needed.
	 */
	public class SubPageAdapter {
		public static final String PREF_SUBNAV_OVERVIEW = "sub_navigation_overview_item_position";
		public static final String PREF_SUBNAV_CALENDAR = "sub_navigation_calendar_item_position";

		private int[] positions = new int[]{0, 0, 0};
		private final int[] subPositionCounts = new int[]{2,4,0};


		public SubPageAdapter() {
			// Get the last position from SharedPreferences
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getHomeActivity());
			positions[0] = sp.getInt(PREF_SUBNAV_OVERVIEW, 0);
			positions[1] = sp.getInt(PREF_SUBNAV_CALENDAR, 0);
			Utils.log.d(positions[0]+" "+positions[1]);
		}

		public int getCurrentSubPosition(int position) {
			Utils.log.i("getCurrentSubPosition{"+position+"} = "+positions[position]);
			return positions[position];
		}

		public Fragment getSubItem(int position, int subPosition) {

			Utils.log.d("SubPageAdapter getSubItem("+position+", "+subPosition+")");
			switch(position) {
				case 0:
					return OverviewBaseFragment.newInstance();
				case 1:
					switch(subPosition) {
						case 0:
							return CalendarDailyFragment.newInstance();
						case 1:
							return CalendarWeeklyFragment.newInstance();
						case 2:
							return CalendarMonthlyFragment.newInstance();
						case 3:
							return CalendarYearlyFragment.newInstance();
					}
					break;
			}
			return null;
		}

		public Fragment getSubItem(int position) {
			try {
				return getSubItem(position, positions[position]);
			}
			catch (Exception e) {
				return null;
			}
		}

		public boolean changeDisplayedFragment(int position, int subPosition) {
			if(position < 0 || position >= positions.length) {
				return false;
			}
			Fragment fragToDisplay = getSubItem(position, subPosition);


			if(fragToDisplay == null) {
				return false;
			}
			Utils.log.i("changeDisplayedFragment("+position+", "+subPosition+"");

			// Find the Base Fragment that will contain the sub fragment
			BaseFragment baseFragment = (BaseFragment)Utils.findFragmentByPosition(position, getSupportFragmentManager(),
					mSectionsPagerAdapter, mViewPager);
			if(baseFragment == null) {
				Utils.log.w("baseFrag null");
				return false;
			}

			// Literally change the displayed Fragment
			FragmentManager childManager = baseFragment.getSubFragmentManager();
			FragmentTransaction childTransaction = childManager.beginTransaction();
			// add a nice fade effect
			childTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			childTransaction.replace(BaseFragment.getViewId(), fragToDisplay).commit();

			// Store the new preference
			setStoredPosition(position, subPosition);


			return true;
		}

		private void setStoredPosition(int page, int subPosition) {
			// prevent unwanted indexes
			if(page >= positions.length || subPosition >= subPositionCounts[page]) {
				Utils.log.e("interesting "+page+"  "+subPosition);
				return;
			}
			Utils.log.d("setStored "+page+"  "+subPosition);

			// also save into the memory
			positions[page] = subPosition;

			// IO concern. do not write every time a position is changed.
		}

		public void permanantlyStoreSubPositions() {
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getHomeActivity()).edit();
			editor.putInt(PREF_SUBNAV_OVERVIEW, positions[0]);
			editor.putInt(PREF_SUBNAV_CALENDAR, positions[1]);
			editor.commit();
		}
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to show the global app
	 * 'context', rather than just what's in the current screen.
	 */
	public void showGlobalContextActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}

	/**
	 * Removes the global context action bar, restores the relevant Action Bar.
	 * Passes the action to SectionsPagerAdapter's applyActionBarRules(int position) method
	 * Called by the Navigation Drawer, when the drawer is closed
	 */
	public void restoreActionBar() {
		mSectionsPagerAdapter.applyActionBarRules(getCurrentPageIndex());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mHomeNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.home, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Passes the action to Navigation Drawer's isDrawerOpen() method
	 * Called by children fragments.
	 * @return
	 */
	public boolean isDrawerOpen() {
		return mHomeNavigationDrawerFragment.isDrawerOpen();
	}

	/**
	 * A utility method to use in inner classes.
	 * HomeActivity contains many interfaces, so a base context is not enough most of the times.
	 * @return this activity
	 */
	public HomeActivity getHomeActivity()
	{
		return this;
	}

//	public void onEvent(MyEvent)

}

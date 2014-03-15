package co.uberdev.ultimateorganizer.android.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.util.Utils;


public class HomeActivity extends Activity
        implements HomeNavigationDrawerFragment.NavigationDrawerCallbacks,
		ViewPager.OnPageChangeListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private HomeNavigationDrawerFragment mHomeNavigationDrawerFragment;

	public SectionsPagerAdapter mSectionsPagerAdapter;
	public ViewPager mViewPager;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

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

		// display the first page
		this.onNavigationDrawerItemSelected(0);
    }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// Unregister in-app events
//		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Tell the ViewPager to display the new fragment
		if(mViewPager != null)
		{
			mViewPager.setCurrentItem(position);
			getActionBar().setBackgroundDrawable(mSectionsPagerAdapter.getColorBackground(position));
		}
    }

    public void onSectionAttached(int number) {
		mTitle = mSectionsPagerAdapter.getPageTitle(number);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if(positionOffsetPixels > 0)
		{
			int oldPos = mViewPager.getCurrentItem();
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

	@Override
	public void onPageSelected(int position) {
		// When a page is selected, apply its 100% opaque Action Bar color
		mHomeNavigationDrawerFragment.setItemChecked(position);
		getActionBar().setBackgroundDrawable(mSectionsPagerAdapter.getColorBackground(position));
	}

	@Override
	public void onPageScrollStateChanged(int state) {
//		getActionBar().setBackgroundDrawable(mSectionsPagerAdapter.getColorBackground(mViewPager.getCurrentItem()));
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			// getItem is called to instantiate the fragment for the given page.
			switch(position) {
				case 0:
					return OverviewFragment.newInstance();
				case 1:
					return CalendarFragment.newInstance();
				case 2:
				default:
					return NotesFragment.newInstance();
			}
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position)
			{
				case 0:
					return getString(R.string.title_section_overview).toUpperCase(l);
				case 1:
					return getString(R.string.title_section_calendar).toUpperCase(l);
				case 2:
					return getString(R.string.title_section_notes).toUpperCase(l);
			}
			return null;
		}

		public int getIntBackground(int position) {
			switch (position)
			{
				case 0:
					return getResources().getColor(R.color.title_section_overview);
				case 1:
					return getResources().getColor(R.color.title_section_calendar);
				case 2:
					return getResources().getColor(R.color.title_section_notes);
			}
			return 0;
		}

		public ColorDrawable getColorBackground(int position) {
			return new ColorDrawable(getIntBackground(position));
		}
	}

//	public void onEvent(MyEvent)

}

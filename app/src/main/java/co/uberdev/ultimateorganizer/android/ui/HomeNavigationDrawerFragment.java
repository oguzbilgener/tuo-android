package co.uberdev.ultimateorganizer.android.ui;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import co.uberdev.ultimateorganizer.android.R;
import co.uberdev.ultimateorganizer.android.auth.LoginActivity;
import co.uberdev.ultimateorganizer.android.util.UltimateApplication;
import co.uberdev.ultimateorganizer.android.util.Utils;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class HomeNavigationDrawerFragment extends Fragment implements View.OnClickListener
{

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;

	private ViewGroup headerContainer;
	private LayoutInflater inflater;

    private NavigationItemAdapter mNavigationItemAdapter;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public HomeNavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
			Utils.log.w("nav sis not null");
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
			Utils.log.e("asd"+mCurrentSelectedPosition);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mNavigationItemAdapter = new NavigationItemAdapter(rootView);
		headerContainer = (ViewGroup) rootView.findViewById(R.id.navigation_drawer_header_container);
		this.inflater = inflater;
       	return rootView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

				// display the relevant action bar according to the fragment index
				Utils.log.d("onDrawerClosed restoreActionBar: "+getParent().getCurrentPageIndex());
				getParent().restoreActionBar();

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset)
			{
				int currentColor = getParent().mSectionsPagerAdapter.getIntBackground(getParent().mViewPager.getCurrentItem());
				int drawerColor = getParent().getResources().getColor(R.color.title_navigation_drawer);
				// find the mixture
				int mixColor = Utils.mixTwoColors(drawerColor, currentColor,slideOffset);
				// apply the mixture
				getActionBar().setBackgroundDrawable(new ColorDrawable(mixColor));
			}
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
			// Open the drawer
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

		// TODO: user the user view for logged in users
		int headerLayoutId;
		UltimateApplication app = (UltimateApplication) getActivity().getApplication();
		if(app.getUser() != null)
		{
			headerLayoutId = R.layout.header_user_navigation_drawer;
		}
		else
		{
			headerLayoutId = R.layout.header_guest_navigation_drawer;
		}

		View headerView = inflater.inflate(headerLayoutId, headerContainer, false);
		headerContainer.addView(headerView);
		headerContainer.findViewById(R.id.drawer_auth_expand_button).setOnClickListener(this);

		if(app.getUser() != null)
		{
			((TextView)headerContainer.findViewById(R.id.header_user_school)).setText(app.getUser().getSchoolName());
			((TextView)headerContainer.findViewById(R.id.header_user_realname)).setText(app.getUser().getFirstName()+" "+app.getUser().getLastName());
		}
    }

    private void selectItem(int position) {
        if(mNavigationItemAdapter != null) {
            mNavigationItemAdapter.setSelectedNavigationItem(position);
        }
    }

	public void setItemChecked(int position)
	{
        if(mNavigationItemAdapter != null) {
			mCurrentSelectedPosition = position;
            mNavigationItemAdapter.setSelectedNavigationItem(position);
        }
	}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
		// pass this action to the Parent Activity
		getParent().showGlobalContextActionBar();
	}

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

	private HomeActivity getParent() {
		return (HomeActivity) getActivity();
	}

	/**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

	public boolean hasUserLearnedDrawer() {
		return mUserLearnedDrawer;
	}

    /**
     * Created by dunkuCoder on 03/04/14.
     * Adapter for the items of navigation drawer, to link them with activities
     */
    public class NavigationItemAdapter implements View.OnClickListener
    {
        private ArrayList<View> navigationItems;
		private View rootView;

        public NavigationItemAdapter(View rootView)
        {
            navigationItems = new ArrayList<View>();
			this.rootView = rootView;
            navigationItems.add(rootView.findViewById(R.id.navigation_drawer_item_overview));
            navigationItems.add(rootView.findViewById(R.id.navigation_drawer_item_calendar));
            navigationItems.add(rootView.findViewById(R.id.navigation_drawer_item_notes));
            navigationItems.add(rootView.findViewById(R.id.navigation_drawer_item_schedule));
            navigationItems.add(rootView.findViewById(R.id.navigation_drawer_item_network));

            for(View item : navigationItems)
            {
                item.setOnClickListener(this);
            }

        }

		public View getRootView()
		{
			return rootView;
		}

        @Override
        public void onClick(View view)
        {
            int index;
            switch(view.getId())
            {
                case R.id.navigation_drawer_item_overview:
                    index = 0;
                    break;
                case R.id.navigation_drawer_item_calendar:
                    index = 1;
                    break;
                case R.id.navigation_drawer_item_notes:
                    index = 2;
                    break;
                case R.id.navigation_drawer_item_schedule:
                    index = 3;
                    break;
                case R.id.navigation_drawer_item_network:
                    index = 4;
                    break;
                default:
                    index = 0;
            }

            mCurrentSelectedPosition = index;
            if(mDrawerLayout != null)
            {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            }

			if(index < 3)
			{
				setSelectedNavigationItem(index);
			}

            if (mCallbacks != null) {
                mCallbacks.onNavigationDrawerItemSelected(index);
            }
        }

        public void setSelectedNavigationItem(int position)
        {
            for( int i = 0; i < navigationItems.size(); i++)
            {
                if( i != position)
                {
					// Regenerate the drawable each time. This is necessary for color state lists
					Drawable unselected = getParent().getResources().getDrawable(R.drawable.navigation_item_background);
					// Use the correct API, according to the API level
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                        navigationItems.get(i).setBackgroundDrawable(unselected);
                    else
                        navigationItems.get(i).setBackground(unselected);

					// TODO: Make the text style normal
                }
                else
                {
					// Regenerate the drawable each time. This is necessary for color state lists
					Drawable selected = getParent().getResources().getDrawable(R.drawable.navigation_selected_item_background);
					// Use the correct API, according to the API level
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
                        navigationItems.get(i).setBackgroundDrawable(selected);
                    else
                        navigationItems.get(i).setBackground(selected);

					// TODO:  Make the text style bold. Use TextAppearance or Typeface classes

                }
            }
        }

		/**
		 * A utility method for getting a TextView id from the position of the item in the NavigationDrawer
		 * @param position
		 * @return
		 */
		public int getTextViewId(int position)
		{
			switch(position)
			{
				case 0:
					return R.id.navigation_drawer_item_text_overview;
				case 1:
					return R.id.navigation_drawer_item_text_calendar;
				case 2:
					return R.id.navigation_drawer_item_text_notes;
				case 3:
					return R.id.navigation_drawer_item_text_academic_schedule;
				case 4:
					return R.id.navigation_drawer_item_text_academic_network;
			}
			return 0;
		}

		/**
		 * A utility method for getting the section title String from the position of the item in the Navigation Drawer
		 * @param position
		 * @return
		 */
		public int getNavigationItemString(int position)
		{
			switch(position)
			{
				case 0:
					return R.string.title_section_overview;
				case 1:
					return R.string.title_section_calendar;
				case 2:
					return R.string.title_section_notes;
				case 3:
					return R.string.title_section_academic_schedule;
				case 4:
					return R.string.title_section_academic_network;
			}
			return 0;
		}
    }

	public NavigationItemAdapter getmNavigationItemAdapter()
	{
		return mNavigationItemAdapter;
	}

	@Override
	public void onClick(View view)
	{
		if(view.getId() == R.id.drawer_auth_expand_button)
		{
			// open menu
			int menuID;
			UltimateApplication app = (UltimateApplication) getActivity().getApplication();
			if(app.getUser() != null)
			{
				menuID = R.menu.drawer_user_expand;
			}
			else
			{
				menuID = R.menu.drawer_guest_expand;
			}
			PopupMenu menu = new PopupMenu(getActivity(), view);
			menu.getMenuInflater().inflate(menuID, menu.getMenu());

			menu.show();

			// ugly inline click listener. no other choice
			menu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					switch(item.getItemId())
					{
						case R.id.menu_guest_login:
							// user pressed login
							openLoginScreen();
							return true;

						case R.id.menu_guest_register:
							// user pressed register
							openRegisterScreen();
							return true;

						case R.id.menu_user_logout:
							logout();
							return true;
					}
					return false;
				}
			});
		}
	}

	public void openLoginScreen()
	{
		Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		getActivity().startActivity(loginIntent);
	}

	public void openRegisterScreen()
	{

	}

	public void logout()
	{
		UltimateApplication app = (UltimateApplication) getActivity().getApplication();
		app.logoutUser();

		Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
		homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		getActivity().startActivity(homeIntent);
	}
}

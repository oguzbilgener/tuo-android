package co.uberdev.ultimateorganizer.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.uberdev.ultimateorganizer.android.R;

/**
 * Created by oguzbilgener on 16/03/14.
 */
public abstract class BaseFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(getLayoutId(), container, false);
		return rootView;
	}

	public static int getLayoutId() {
		return R.layout.fragment_base;
	}
	public static int getViewId() {
		return R.id.base_fragment_frame;
	}

	public FragmentManager getSubFragmentManager() {
		return getChildFragmentManager();
	}
}

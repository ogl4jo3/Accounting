package com.ogl4jo3.accounting.useteaching;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogl4jo3.accounting.R;

/**
 * A simple {@link Fragment} subclass.
 */
@Deprecated
public class UseTeachingNewExpensesFragment extends Fragment {

	public UseTeachingNewExpensesFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_use_teaching_new_expenses, container, false);
	}

}

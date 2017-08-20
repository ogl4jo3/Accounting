package ogl4jo3.shaowei.ogl4jo3.accounting.useteaching;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ogl4jo3.shaowei.ogl4jo3.accounting.MainActivity;
import ogl4jo3.shaowei.ogl4jo3.accounting.R;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesHelper;
import ogl4jo3.shaowei.ogl4jo3.utility.sharedpreferences.SharedPreferencesTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class UseTeachingStartFragment extends Fragment {

	public UseTeachingStartFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_use_teaching_start, container, false);
		Button btnStart=(Button)view.findViewById(R.id.btn_start_use);
		btnStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				startUse();
			}
		});
		return view;
	}

	/**
	 * 開始使用
	 */
	private void startUse() {
		//將第一次使用改為否
		new SharedPreferencesHelper(getActivity(), SharedPreferencesTag.prefsData)
				.setBoolean(SharedPreferencesTag.prefsFirstUse, false);
		//開始使用
		Intent intent = new Intent(getActivity(), MainActivity.class);
		startActivity(intent);
		getActivity().finish();
	}

}

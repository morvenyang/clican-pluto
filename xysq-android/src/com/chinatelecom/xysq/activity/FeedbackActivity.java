package com.chinatelecom.xysq.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chinatelecom.xysq.R;
import com.umeng.fb.fragment.FeedbackFragment;

public class FeedbackActivity extends FragmentActivity {

	private FeedbackFragment mFeedbackFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		Button button = (Button) this.findViewById(R.id.feedback_backButton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			String conversation_id = getIntent().getStringExtra(
					FeedbackFragment.BUNDLE_KEY_CONVERSATION_ID);
			mFeedbackFragment = FeedbackFragment.newInstance(conversation_id);

			getSupportFragmentManager().beginTransaction()
					.add(R.id.feedback_layout, mFeedbackFragment).commit();
		}

	}

	@Override
	protected void onNewIntent(android.content.Intent intent) {
		mFeedbackFragment.onRefresh();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}

package eu.trentorise.smartcampus.android.feedback.fragment;

import com.actionbarsherlock.app.SherlockFragment;

import eu.trentorise.smartcampus.android.feedback.R;
import eu.trentorise.smartcampus.android.feedback.model.Feedback;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class FeedbackFragment extends Fragment {
	
	private String mAppId;
	private String mActId;
	
	private TextView mAssingmentTV;
	private SeekBar mDifficultySB;
	private Spinner mTypeSpinner;
	private ImageView mScreenShotIV;
	private CheckBox mAttachScreenshotCB;
	private EditText mNoteEditText;
	private Button mSendBtn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.feedback_fragment_layout, container);
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mAssingmentTV = (TextView) getActivity().
				findViewById(R.id.feedback_assignment_tv);
		mDifficultySB = (SeekBar) getActivity().
				findViewById(R.id.feedback_difficulty_sb);
		mTypeSpinner = (Spinner) getActivity().
				findViewById(R.id.feedback_type_sp);
		mScreenShotIV = (ImageView) getActivity().
				findViewById(R.id.feedback_screenshot_imgv);
		mAttachScreenshotCB = (CheckBox) getActivity().
				findViewById(R.id.feedback_attach_cb);
		mNoteEditText = (EditText) getActivity().
				findViewById(R.id.feedback_notes_et);
		mSendBtn = (Button) getActivity().
				findViewById(R.id.feedback_send_Btn);
		
		mSendBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Feedback feedback = new Feedback();
				feedback.setNote(mNoteEditText.getText().toString());
				feedback.setDifficulty(mDifficultySB.getProgress());
				feedback.setType(mTypeSpinner.getSelectedItem().toString());
				feedback.setAppId(mAppId);
				feedback.setActivityId(mActId);
				//TODO call the post
			}
		});
		
	}
	
	public void refreshImage(Bitmap image){
		this.mScreenShotIV.setImageBitmap(image);
	}
	
}
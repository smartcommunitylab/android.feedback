package eu.trentorise.smartcampus.android.feedback.asynctask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import eu.trentorise.smartcampus.android.common.GlobalConfig;
import eu.trentorise.smartcampus.android.common.Utils;
import eu.trentorise.smartcampus.android.feedback.R;
import eu.trentorise.smartcampus.android.feedback.interfaces.OnFeedbackSent;
import eu.trentorise.smartcampus.android.feedback.model.Feedback;
import eu.trentorise.smartcampus.android.feedback.utils.ScreenShooter;
import eu.trentorise.smartcampus.protocolcarrier.ProtocolCarrier;
import eu.trentorise.smartcampus.protocolcarrier.common.Constants.Method;
import eu.trentorise.smartcampus.protocolcarrier.custom.FileRequestParam;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageRequest;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageResponse;
import eu.trentorise.smartcampus.protocolcarrier.custom.ObjectRequestParam;
import eu.trentorise.smartcampus.protocolcarrier.custom.RequestParam;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.ConnectionException;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.ProtocolException;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

public class SendFeedbackAsyncTask extends AsyncTask<Bitmap, Void, String> {

	private String appToken;
	private String authToken;
	private Fragment mFragment;
	private Feedback mFeedback;
	private boolean mAttachScreenShot;
	private Exception mEx;
	private Context mContext;

	public SendFeedbackAsyncTask(Fragment act, Feedback feedback,
			boolean attach, String appToken, String authToken) {
		super();
		if (!(act instanceof OnFeedbackSent))
			throw new IllegalStateException(
					"The fragment must implement OnFeedbackSent");
		this.mFragment = act;
		this.mFeedback = feedback;
		this.mAttachScreenShot = attach;
		this.appToken = appToken;
		this.authToken = authToken;
		this.mContext = act.getActivity().getApplicationContext();
	}

	@Override
	protected String doInBackground(Bitmap... params) {

		ProtocolCarrier pc = new ProtocolCarrier(mFragment.getActivity(), null);
		String url;
		try {
			url = GlobalConfig.getAppUrl(mFragment.getActivity());
		} catch (ProtocolException e) {
			mEx = e;
			return null;
		}
		MessageRequest mr = new MessageRequest(url, "feedback/feedback");
		mr.setMethod(Method.POST);

		List<RequestParam> l = new ArrayList<RequestParam>();
		
		if (mAttachScreenShot) {
			FileRequestParam frp = new FileRequestParam();
			frp.setContent(ScreenShooter.bitmapAsByteArray(params[0]));
			frp.setParamName("file");
			frp.setContentType("image/png");
			frp.setFilename("filename");
			l.add(frp);
		}

		ObjectRequestParam orr = new ObjectRequestParam();
		orr.setParamName("body");
		Map map = Utils.convertObjectToData(Map.class, mFeedback);

		orr.setVars(map);

		l.add(orr);
		mr.setRequestParams(l);
		try {
			MessageResponse mresp = pc.invokeSync(mr, appToken, authToken);
			return mresp.toString();
		} catch (ConnectionException e) {
			mEx = e;
			return null;
		} catch (ProtocolException e) {
			mEx = e;
			return null;
		} catch (SecurityException e) {
			mEx = e;
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (mEx != null) {
			Log.e(SendFeedbackAsyncTask.class.toString(), mEx.toString());
			((OnFeedbackSent) mFragment).onFeedbackSent(mContext
					.getString(R.string.feedback_sent_failure));
		} else {
			((OnFeedbackSent) mFragment).onFeedbackSent(mContext
					.getString(R.string.feedback_sent_ok));
			Log.i(SendFeedbackAsyncTask.class.toString(), "ok");
		}
	}

}

package ch.fhnw.webfr.sayt.client;

import java.util.List;

import ch.fhnw.webfr.sayt.shared.Address;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SearchAsYouType implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final SaytServiceAsync saytService = GWT.create(SaytService.class);

	private SearchPanel sp;

	// A keeper of the timer instance in case we need to cancel it
	private Timer timeoutTimer = null;

	// An indicator when the computation should quit
	private boolean abortFlag = false;

	// request counter
	private int requestCount = 0;

	static final int TIMEOUT = 5; // 5 second timeout

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		sp = new SearchPanel(this);

		// Use RootPanel.get() to get the entire body element
		RootPanel.get("gwtContent").add(sp);
	}

	public void sayt(String text) {
		// cancel timer that is already running.
		cancelTimer();
		
		// Create a timer to abort if the RPC takes too long
		timeoutTimer = new Timer() {
			public void run() {
				Window.alert("Timeout expired.");
				timeoutTimer = null;
				abortFlag = true;
			}
		};

		// (re)Initialize the abort flag and start the timer.
		abortFlag = false;
		timeoutTimer.schedule(TIMEOUT * 1000); // timeout is in milliseconds

		// Kick off an RPC
		saytService.findAddressMatchingAny(text, new SearchHandler(
				++requestCount));
	}

	class SearchHandler implements AsyncCallback<List<Address>> {
		private int requestNo;

		public SearchHandler(int count) {
			requestNo = count;
		}

		@Override
		public void onFailure(Throwable caught) {
			cancelTimer();
			if (isCurrent()) {
				RootPanel err = RootPanel.get("errors");
				err.clear();
				err.add(new Label(caught.getMessage()));
			}
		}

		@Override
		public void onSuccess(List<Address> result) {
			cancelTimer();
			if (abortFlag || !isCurrent()) {
				// Timeout already occurred. discard result
				return;
			}
			sp.replaceAddresses(result);
		}

		private boolean isCurrent() {
			return requestCount <= requestNo;
		}

	}

	// Stop the timeout timer if it is running
	private void cancelTimer() {
		if (timeoutTimer != null) {
			timeoutTimer.cancel();
			timeoutTimer = null;
		}
	}

}

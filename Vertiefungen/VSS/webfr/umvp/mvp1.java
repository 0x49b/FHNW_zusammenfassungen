package webfr.simple.client;

import webfr.simple.client.presenter.Presenter;
import webfr.simple.client.presenter.WelcomePresenter;
import webfr.simple.client.view.WelcomeView;

import com.google.gwt.user.client.ui.HasWidgets;

public class AppController {

	private Presenter start, current;
	private HasWidgets container;

	public AppController() {
		start = new WelcomePresenter(new WelcomeView(), this);
	}

	public void start(final HasWidgets container) {
		if (container != null) {
			this.container = container;
		}
		showViewOf(start);
	}
	
	public void showViewOf(Presenter p) {
		current = p;
		current.present(container);
	}

}

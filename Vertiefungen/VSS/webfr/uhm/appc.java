package webfr.simple.client;

import webfr.simple.client.presenter.OnePresenter;
import webfr.simple.client.presenter.Presenter;
import webfr.simple.client.presenter.WelcomePresenter;
import webfr.simple.client.view.OneView;
import webfr.simple.client.view.WelcomeView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class AppController implements ValueChangeHandler<String> {
  public static final String WELCOME_PAGE = "welcome";
  public static final String ONE_PAGE = "one";

	private Presenter welcome, one, current;
	private int clicks = 0;
	private EventBus eventbus = new SimpleEventBus();
	private HasWidgets container;

	public AppController(HasWidgets container) {
		eventbus = new SimpleEventBus();
		this.container = container;
		bind();
	}

	private void bind() {
		welcome = new WelcomePresenter(new WelcomeView(), this);
		one = new OnePresenter(new OneView(), this);
		current = welcome;
		History.addValueChangeHandler(this);
	}

	public void present() {
		current.present(container);
	}
	
	public int getClicks() {
		return clicks;
	}
	
	public int incrementClicks() {
		return ++clicks;
	}
	
	public Presenter getCurrentPresenter() {
		return current;
	}
	
	public Presenter getWelcomePresenter() {
		return welcome;
	}
	
	public Presenter getOnePresenter() {
		return one;
	}
	
	public HasWidgets getContainer() {
		return container;
	}
	
	public EventBus getEventBus() {
		return eventbus;
	}

  @Override
  public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();
    int qm = token.indexOf('?');
    String parameter = (qm < 0) ? "" : token.substring(qm+1);
    if (qm >= 0) {
      token = token.substring(0, qm); 
    }
    Presenter p = null;
    if (token == null || token.isEmpty() || WELCOME_PAGE.equals(token)) {
      p = welcome;
    } else if (ONE_PAGE.equals(token)) {
      p = one;
      try {
        clicks = Integer.parseInt(parameter);
      } catch (NumberFormatException e) {}
    }
    if (p != null) {
      current = p;
      present();
    }
  }
	
}

package webfr.simple.client.presenter;

import webfr.simple.client.AppController;
import webfr.simple.client.view.OneDisplay;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasWidgets;

public class OnePresenter implements Presenter {
	
	OneDisplay display;
	int counter = 0;
	AppController controller;
	
	public OnePresenter(OneDisplay d, AppController ac) {
		display = d;
		controller = ac;
	}

	@Override
	public void bind() {
		// set handler of counter button
		display.setClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				counter++;
				display.setNofClicks("" + counter);
			}
			
		});
		
		// set handler of back button
		display.setBackHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				controller.start(null);
			}
			
		});
	}

	@Override
	public void present(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());		
	}

}

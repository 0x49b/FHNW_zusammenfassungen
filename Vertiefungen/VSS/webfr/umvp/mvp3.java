package webfr.simple.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class OneView extends Composite implements OneDisplay {

	private static OneViewUiBinder uiBinder = GWT.create(OneViewUiBinder.class);

	interface OneViewUiBinder extends UiBinder<Widget, OneView> {
	}

	public OneView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField Button button;
	@UiField Button back;

	public OneView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		button.setText(firstName);
	}

	@Override
	public void setNofClicks(String s) {
		button.setText(s);
	}

	@Override
	public void setClickHandler(ClickHandler h) {
		button.addClickHandler(h);
	}

	@Override
	public void setBackHandler(ClickHandler h) {
		back.addClickHandler(h);
	}

}

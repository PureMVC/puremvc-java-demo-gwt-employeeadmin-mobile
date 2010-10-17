/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.view;

import org.java.multicore.demos.gwt.employeeadmin.client.ApplicationFacade;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndEvent;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndHandler;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel.Direction;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User list mediator.
 */
public class PureMVCMediator extends Mediator {

	/**
	 * The declarative UI.
	 */
	@UiTemplate("PureMVC.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, PureMVCMediator> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/**
	 * Mediator name.
	 */
	public static final String NAME = "PureMVCMediator";

	/**
	 * Widgets.
	 */
	@UiField PagePanel hpanel;
	@UiField Image ipuremvc;

	@UiHandler({"ipuremvc"})
	void onClick(ClickEvent event) {
		if(event.getSource().equals(ipuremvc)) {
			this.getFacade().registerMediator(new UserListMediator());
			hpanel.setScroll(Direction.CENTER_TO_LEFT);
			hpanel.addTransitionEndHandler(new TransitionEndHandler() {
				@Override
				public void onTransitionEnd(TransitionEndEvent event) {
					getFacade().removeMediator(PureMVCMediator.NAME);
				}
			});
			getFacade().sendNotification(ApplicationFacade.INIT_USERS);
		}
	}

	public PureMVCMediator() {
		super(NAME, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onRegister() {
		initView();
		super.onRegister();
	}

	/* (non-Javadoc)
	 * @see org.puremvc.java.multicore.patterns.mediator.Mediator#onRemove()
	 */
	@Override
	public void onRemove() {
		super.onRemove();
		RootPanel.get().remove(hpanel);
	}

	/**
	 * Initialize the user list view.
	 */
	private void initView() {
		uiBinder.createAndBindUi(this);
		RootPanel.get().add(hpanel);

	    // Point the image at a real URL.
		ipuremvc.setUrl(GWT.getHostPageBaseURL() + "images/puremvc.png");

		hpanel.setScroll(Direction.CENTER_TO_LEFT);

		// Hook up a load listener, so that we can be informed if the image fails
	    // to load.
		ipuremvc.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
		  		hpanel.setScroll(Direction.LEFT_TO_CENTER);
			}
		});
	}

}

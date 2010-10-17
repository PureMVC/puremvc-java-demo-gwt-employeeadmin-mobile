/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.view;

import org.java.multicore.demos.gwt.employeeadmin.client.ApplicationFacade;
import org.java.multicore.demos.gwt.employeeadmin.client.model.UserProxy;
import org.java.multicore.demos.gwt.employeeadmin.client.model.vo.RoleVO;
import org.java.multicore.demos.gwt.employeeadmin.client.model.vo.UserVO;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndEvent;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndHandler;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.ULPanel;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel.Direction;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User list mediator.
 */
public class UserListMediator extends Mediator {

	/**
	 * The declarative UI.
	 */
	@UiTemplate("UserList.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, UserListMediator> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/**
	 * Mediator name.
	 */
	public static final String NAME = "UserListMediator";

	/**
	 * Reference to the user proxy.
	 */
	private UserProxy userProxy = null;

	/**
	 * Mode.
	 */
	public static final int MODE_EDIT = 0;
	public static final int MODE_DELETE = 1;
	private int mode = MODE_EDIT;

	
	/**
	 * Widgets.
	 */
	@UiField PagePanel hpanel;
	@UiField ULPanel ulpanel;

	@UiField Button bplus;
	@UiField Button bmodify;

	private int lastTableRow = 0;
	private int selectedRow = -1;
	
	@UiHandler({"bplus", "bmodify"})
	void onClick(ClickEvent event) {
		if(hpanel.getInScroll())
			return;

		
		if(event.getSource().equals(bplus)) {
			onNew();
		} else if(event.getSource().equals(bmodify)) {
			if(mode==MODE_EDIT) {
				mode = MODE_DELETE;
			} else {
				mode = MODE_EDIT;
			}

			updateList();
		}
	}

	public UserListMediator() {
		super(NAME, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onRegister() {
		userProxy = (UserProxy) getFacade().retrieveProxy(UserProxy.NAME);
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
		hpanel.setScroll(Direction.LEFT_TO_CENTER);
	}

	/**
	 * Clear list.
	 */
	private void clearList() {
		ulpanel.clear();
		
	}
	
	private void updateList() {
		clearList();
		//for (int i = 0; i < userProxy.users().size(); i++) {
		for (UserVO userVO :  userProxy.users()) {	
			//UserVO user = userProxy.users().get(i);
			insertInList(userVO);
		}  
			
		if(mode==MODE_DELETE) {
			for(int i=0; i<ulpanel.getWidgetCount(); i++) {
				Widget li = ulpanel.getWidget(i);
				li.getElement().setAttribute("selected", "delete");
			}
		} else {
			for(int i=0; i<ulpanel.getWidgetCount(); i++) {
				Widget li = ulpanel.getWidget(i);
				li.getElement().removeAttribute("selected");
			}
		}

	}

	/**
	 * Insert in list.
	 * @param col1 param1

	 */
	private void insertInList(final UserVO user) {

		Anchor anchor = new Anchor();
		final String txt = user.fname + " " + user.lname;
		anchor.setText(txt);

		anchor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if(hpanel.getInScroll())
					return;

				for (int i = 0; i < userProxy.users().size(); i++) {
					String name = userProxy.users().get(i).fname + " " + userProxy.users().get(i).lname;
					if(txt.equals(name)) {
						selectedRow = i;
						break;
					}
				}
				if(mode==MODE_DELETE) {
					onDelete();
				} else {
					onSelect();	
				}
			}});
		ulpanel.add(anchor);
	}

	/**
	 * Create a new userVO and send a notification.
	 */
	private void onNew() {

		this.getFacade().registerMediator(new UserFormMediator());
		hpanel.setScroll(Direction.CENTER_TO_LEFT);
		hpanel.addTransitionEndHandler(new TransitionEndHandler() {
			@Override
			public void onTransitionEnd(TransitionEndEvent event) {
				getFacade().removeMediator(UserListMediator.NAME);
			}
		});

		UserVO user = new UserVO();
		sendNotification(ApplicationFacade.NEW_USER, user);
	}

	/**
	 * Remove the user and send a notification.
	 */
	private void onDelete() {
		sendNotification(ApplicationFacade.DELETE_USER, userProxy.users().get(selectedRow));
	}

	/**
	 * Send USER_SELECTED notification.
	 */
	private void onSelect() {
		this.getFacade().registerMediator(new UserFormMediator());
		hpanel.setScroll(Direction.CENTER_TO_LEFT);
		hpanel.addTransitionEndHandler(new TransitionEndHandler() {
			@Override
			public void onTransitionEnd(TransitionEndEvent event) {
				getFacade().removeMediator(UserListMediator.NAME);
			}
		});

		sendNotification(ApplicationFacade.USER_SELECTED, userProxy.users().get(selectedRow));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String[] listNotificationInterests() {
		return new String[] { ApplicationFacade.CANCEL_SELECTED,
				ApplicationFacade.USER_UPDATED,
				ApplicationFacade.USER_DELETED,
				ApplicationFacade.USER_ADDED,
				ApplicationFacade.INIT_USERS
				};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void handleNotification(final INotification notification) {
		if (notification.getName().equals(ApplicationFacade.CANCEL_SELECTED)) {
		} else if (notification.getName().equals(ApplicationFacade.USER_UPDATED)) {
			updateList();
		} else if (notification.getName().equals(ApplicationFacade.USER_DELETED)) {
			updateList();
		} else if (notification.getName().equals(ApplicationFacade.INIT_USERS)) {
			updateList();
		} else if (notification.getName().equals(ApplicationFacade.USER_ADDED)) {
			UserVO user = (UserVO) notification.getBody();
			insertInList(user);
		}
		super.handleNotification(notification);
	}
}

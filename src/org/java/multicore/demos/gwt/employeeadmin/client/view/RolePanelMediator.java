/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.view;

import java.util.HashMap;
import java.util.List;

import org.java.multicore.demos.gwt.employeeadmin.client.ApplicationFacade;
import org.java.multicore.demos.gwt.employeeadmin.client.model.RoleProxy;
import org.java.multicore.demos.gwt.employeeadmin.client.model.enumerator.RoleEnum;
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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Role panel mediator.
 */
public class RolePanelMediator extends Mediator {

	/**
	 * The declarative UI.
	 */
	@UiTemplate("RolePanel.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, RolePanelMediator> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/**
	 * Reference to the Controller.
	 */
	private RoleProxy roleProxy = null;

	/**
	 * Reference to the current user.
	 */
	private UserVO user;

	/**
	 * Reference all roles.
	 */
	private List<RoleEnum> allRolesVO = RoleEnum.comboList();

	/**
	 * The selected role.
	 */
	private HashMap<RoleEnum,Boolean> selectedRole = new HashMap<RoleEnum,Boolean>();

	/**
	 * The widgets.
	 */
	@UiField PagePanel hpanel;
	@UiField Button buserprofile;
	@UiField Button bsave;
	@UiField ULPanel ulpanel;

	/**
	 * The handler.
	 */
	@UiHandler({"buserprofile", "bsave"})
	void onClick(ClickEvent event) {
		if(hpanel.getInScroll())
			return;

		if(event.getSource().equals(buserprofile)) {
			this.getFacade().registerMediator(new UserFormMediator());
			UserFormMediator ufm = (UserFormMediator)getFacade().retrieveMediator(UserFormMediator.NAME);
			PagePanel pp = (PagePanel)ufm.getViewComponent();
			pp.setScroll(Direction.LEFT_TO_CENTER);
			hpanel.setScroll(Direction.CENTER_TO_RIGHT);
			hpanel.addTransitionEndHandler(new TransitionEndHandler() {
				@Override
				public void onTransitionEnd(TransitionEndEvent event) {
					getFacade().removeMediator(RolePanelMediator.NAME);
				}
			});

			sendNotification(ApplicationFacade.USER_SELECTED, user);
		} else if(event.getSource().equals(bsave)) {
			onSave();
			showSave(false);
		}
	}

	/**
	 * Mediator name.
	 */
	public static final String NAME = "RolePanelMediator";

	/**
	 * Constructor.
	 */
	public RolePanelMediator() {
		super(NAME, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onRegister() {
		roleProxy = (RoleProxy) getFacade().retrieveProxy(RoleProxy.NAME);
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
	 * Initialize the role panel view.
	 */
	private void initView() {
		uiBinder.createAndBindUi(this);
		RootPanel.get().add(hpanel);

		for (int i = 0; i < allRolesVO.size(); i++) {
			insertInList(allRolesVO.get(i).value);
		}

		hpanel.setScroll(Direction.RIGHT_TO_CENTER);
		showSave(false);
	}

	private void showSave(final boolean show) {

		DeferredCommand.addCommand(new Command() {
			public void execute() {
				if (show) {
					bsave.removeStyleName("fadeout");
					bsave.addStyleName("fadein");
				} else {
					bsave.removeStyleName("fadein");
					bsave.addStyleName("fadeout");
				}
            }
        });
	}

	private void setItemChecked(int index, boolean value) {
		if(index > ulpanel.getWidgetCount())
			return;
		Widget li = ulpanel.getWidget(index);
		if (value) {
			li.getElement().setAttribute("selected", "checked");
		}
		else {
			li.getElement().setAttribute("selected", "unchecked");
		}
	}

	private boolean getItemChecked(int index) {
		if(index > ulpanel.getWidgetCount())
			return false;
		Widget li = ulpanel.getWidget(index);
		if(li.getElement().getAttribute("selected").equals("checked"))
			return true;

		return false;
	}

	private void setAllItemsChecked(boolean flag) {
		for(int i=0; i<ulpanel.getWidgetCount(); i++) {
			Widget li = ulpanel.getWidget(i);
			if(flag) {
				li.getElement().setAttribute("selected", "checked");
			}
			else {
				li.getElement().setAttribute("selected", "unchecked");
			}
		}
	}

	/**
	 * Add the selected role to the user.
	 */
	private void onSave() {
		for (int i=0; i<allRolesVO.size(); i++) {
			roleProxy.removeRoleFromUser(user, allRolesVO.get(i));
		}

		for (RoleEnum mapKey : selectedRole.keySet()) {
			if(selectedRole.get(mapKey))
				roleProxy.addRoleToUser(user, mapKey);
		}
	}

	/**
	 * Clear list.
	 */
/*	private void clearList() {
		for (int i = 0; i < allRolesVO.size(); i++) {
			setItemChecked(i, false);
		}
	}
*/
	private void updateList() {
		for (int i = 0; i < allRolesVO.size(); i++) {
			Boolean value = selectedRole.get(allRolesVO.get(i));
			setItemChecked(i, value);
		}
	}

	/**
	 * Insert in list.
	 * @param role the role
	 */
	private void insertInList(final String role) {
		Anchor anchor = new Anchor();
		anchor.setText(role);
		ulpanel.add(anchor);

		final int index = ulpanel.getWidgetCount() - 1;
		anchor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Boolean previousValue = getItemChecked(index);
				selectedRole.put(allRolesVO.get(index), !previousValue);
				updateList();
				showSave(true);
			}});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String[] listNotificationInterests() {
		return new String[] {
				ApplicationFacade.NEW_USER,
				ApplicationFacade.USER_ADDED,
				ApplicationFacade.USER_UPDATED,
				ApplicationFacade.USER_DELETED,
				ApplicationFacade.CANCEL_SELECTED,
				ApplicationFacade.USER_SELECTED,
				ApplicationFacade.ADD_ROLE_RESULT
				};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void handleNotification(final INotification notification) {
		if (notification.getName().equals(ApplicationFacade.NEW_USER)) {

		}
		if (notification.getName().equals(ApplicationFacade.USER_SELECTED)) {
			user = (UserVO) notification.getBody();
			List<RoleEnum> userRolesVO = roleProxy.getUserRoles(user.username);
			for (int i = 0; i < allRolesVO.size(); i++) {
				// The user by default hasn't got this role
				selectedRole.put(allRolesVO.get(i), false);
				for (int j = 0; j < userRolesVO.size(); j++) {
					if (allRolesVO.get(i).value.equals(userRolesVO.get(j).value)) {
						// The user has this role
						selectedRole.put(allRolesVO.get(i), true);
					}
				}
			}
			updateList();
		}
		super.handleNotification(notification);
	}
}
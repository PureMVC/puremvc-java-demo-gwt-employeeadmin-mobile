/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.view;

import org.java.multicore.demos.gwt.employeeadmin.client.ApplicationFacade;
import org.java.multicore.demos.gwt.employeeadmin.client.model.UserProxy;
import org.java.multicore.demos.gwt.employeeadmin.client.model.enumerator.DeptEnum;
import org.java.multicore.demos.gwt.employeeadmin.client.model.vo.UserVO;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndEvent;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndHandler;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel.Direction;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.mediator.Mediator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Message mediator.
 */
public class UserFormMediator extends Mediator {

	/**
	 * The declarative UI.
	 */
	@UiTemplate("UserForm.ui.xml")
	interface MyUiBinder extends UiBinder<Widget, UserFormMediator> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/**
	 * Reference to the user proxy.
	 */
	private UserProxy userProxy = null;

	/**
	 * Reference to the user vo.
	 */
	private UserVO user;

	/**
	 * Widgets.
	 */
	@UiField PagePanel hpanel;
	@UiField TextBox firstname;
	@UiField TextBox lastname;
	@UiField TextBox email;
	@UiField TextBox username;
	@UiField TextBox password;
	@UiField TextBox confirm;
	@UiField ListBox lbdepartment;
	@UiField Button busers;
	@UiField Button bsave;
	@UiField Anchor userroles;

	static boolean state = true;

	/**
	 * The handlers.
	 */
	@UiHandler({"busers", "bsave" })
	void onClick(ClickEvent event) {
		if(hpanel.getInScroll())
			return;

		if(event.getSource().equals(busers)) {
			this.getFacade().registerMediator(new UserListMediator());
			hpanel.setScroll(Direction.CENTER_TO_RIGHT);			
			hpanel.addTransitionEndHandler(new TransitionEndHandler() {
				@Override
				public void onTransitionEnd(TransitionEndEvent event) {
					getFacade().removeMediator(UserFormMediator.NAME);
				}
			});

			sendNotification(ApplicationFacade.INIT_USERS);
		} else if(event.getSource().equals(bsave)) {
			if(mode==MODE_ADD) {
				onAdd();
				setMode(MODE_EDIT);
			} else if(mode==MODE_EDIT) {
				onUpdate();
			}
			showSave(false);

		}
	}

	@UiHandler({"firstname", "lastname", "email", "username", "password", "confirm"})
	public void onValueChange(ValueChangeEvent<String> event) {
		showSave(true);
	}

	@UiHandler({"lbdepartment"})
	public void onChange(ChangeEvent event){ 
		showSave(true);
	}

	@UiHandler({"userroles"})
	void handleClick(ClickEvent event) {
		if(hpanel.getInScroll())
			return;

		if(event.getSource().equals(userroles)) {
			if(mode==MODE_ADD) {
				Window.alert("You must save first.");
			} else if(mode==MODE_EDIT) {
				this.getFacade().registerMediator(new RolePanelMediator());
				sendNotification(ApplicationFacade.USER_SELECTED, user);

				hpanel.setScroll(Direction.CENTER_TO_LEFT);
				hpanel.addTransitionEndHandler(new TransitionEndHandler() {
					@Override
					public void onTransitionEnd(TransitionEndEvent event) {
						getFacade().removeMediator(UserFormMediator.NAME);
					}
				});
			}
		}
	}

	/**
	 * Mediator name.
	 */
	public static final String NAME = "UserFormMediator";

	/**
	 * Mode.
	 */
	public static final int MODE_ADD = 0;
	public static final int MODE_EDIT = 1;
	private int mode = MODE_ADD;

	/**
	 * Constructor.
	 */
	public UserFormMediator() {
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

	/**
	 * Initialize the message view.
	 */
	private void initView() {
		uiBinder.createAndBindUi(this);

		password.getElement().setAttribute("type", "password");
		confirm.getElement().setAttribute("type", "password");
		email.getElement().setAttribute("type", "email");

		RootPanel.get().add(hpanel);
		hpanel.setScroll(Direction.RIGHT_TO_CENTER);
		setViewComponent(hpanel);
		showSave(false);
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
	 * Add a user and send a notification.
	 */
	private void onAdd() {
		populateUserVO();
		userProxy.addItem(user);
		sendNotification(ApplicationFacade.USER_ADDED, user);
	}

	/**
	 * Update the user and send a notification.
	 */
	private void onUpdate()	{
		populateUserVO();
		userProxy.updateItem(user);
		sendNotification(ApplicationFacade.USER_UPDATED, user);
	}

	/**
	 * Cancel the action.
	 */
	private void onCancel()	{
		sendNotification(ApplicationFacade.CANCEL_SELECTED);
	}

	/**
	 * Populate the user vo from the form.
	 */
	private void populateUserVO() {
		String value = lbdepartment.getItemText(lbdepartment.getSelectedIndex());
		for (int i = 0; i < DeptEnum.list().size(); i++) {
			if (value.equals(DeptEnum.list().get(i).value)) {
				user.department = DeptEnum.list().get(i);
				break;
			}
		}

		user.fname = firstname.getText();
		user.lname = lastname.getText();
		user.email = email.getText();
		user.username = username.getText();
		user.password = password.getText();
	}

	/**
	 * Insert in form.
	 * @param user UserVO
	 */
	private void insertInForm(final UserVO user) {
		lbdepartment.clear();

		for (int i = 0; i < DeptEnum.list().size(); i++) {
			lbdepartment.addItem(DeptEnum.list().get(i).value);
		}
		for (int i = 0; i < lbdepartment.getItemCount(); i++) {
			if (user.department.value.equals(lbdepartment.getItemText(i))) {
				lbdepartment.setSelectedIndex(i);
				break;
			}
		}

		//Window.alert(user.fname);
		firstname.setText(user.fname);
		lastname.setText(user.lname);
		email.setText(user.email);
		username.setText(user.username);
		password.setText(user.password);
		confirm.setText(user.password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String[] listNotificationInterests() {
		return new String[] { ApplicationFacade.NEW_USER,
				ApplicationFacade.USER_DELETED,
				ApplicationFacade.USER_SELECTED
				};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void handleNotification(final INotification notification) {
		if (notification.getName().equals(ApplicationFacade.NEW_USER)) {
			setMode(MODE_ADD);
			user = (UserVO) notification.getBody();
			insertInForm(user);
		} else if (notification.getName().equals(ApplicationFacade.USER_DELETED)) {

		} else if (notification.getName().equals(ApplicationFacade.USER_SELECTED)) {
			setMode(MODE_EDIT);
			user = (UserVO) notification.getBody();
			insertInForm(user);
		}
		super.handleNotification(notification);
	}

	/**
	 * Set mode.
	 * @param pmode the mode
	 */
	private void setMode(final int pmode) {
		mode = pmode;
	}

	/**
	 * Show toolbar save button.
	 * @param show
	 */
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
}
/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client;

import org.java.multicore.demos.gwt.employeeadmin.client.controller.AddRoleResultCommand;
import org.java.multicore.demos.gwt.employeeadmin.client.controller.DeleteUserCommand;
import org.java.multicore.demos.gwt.employeeadmin.client.controller.StartupCommand;
import org.puremvc.java.multicore.patterns.facade.Facade;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Application facade. <i>Singleton</i>
 */
public class ApplicationFacade extends Facade {

	/**
	 * Startup. <br>
	 * Body : null
	 */
	public static final String STARTUP = "startup";

	/**
	 * Key of this facade.
	 */
	public static final String NAME = "ApplicationFacade";

	/**
	 * Unique instance.
	 */
	private static ApplicationFacade instance = null;

	/**
	 * NewUser. <br>
	 * Body : UserVO
	 */
	public static final String NEW_USER = "newUser";

	/**
	 * DeleteUser. <br>
	 * Body : UserVO
	 */
	public static final String DELETE_USER = "deleteUser";

	/**
	 * CancelSelected. <br>
	 * Body : UserVO
	 */
	public static final String CANCEL_SELECTED = "cancelSelected";

	/**
	 * UserSelected. <br>
	 * Body : UserVO
	 */
	public static final String USER_SELECTED = "userSelected";

	/**
	 * UserAdded. <br>
	 * Body : UserVO
	 */
	public static final String USER_ADDED = "userAdded";

	/**
	 * UserUpdated. <br>
	 * Body : UserVO
	 */
	public static final String USER_UPDATED = "userUpdated";

	/**
	 * UserDeleted. <br>
	 * Body : UserVO
	 */
	public static final String USER_DELETED = "userDeleted";

	/**
	 * InitUsers. <br>
	 * Body : Null
	 */
	public static final String INIT_USERS = "initUsers";

	/**
	 * AddRole. <br>
	 * Body : RoleVO
	 */
	public static final String ADD_ROLE = "addRole";

	/**
	 * AddRoleResult. <br>
	 * Body : Boolean
	 */
	public static final String ADD_ROLE_RESULT = "addRoleResult";

	/**
	 * Constructor.
	 * @param key the key
	 */
	protected ApplicationFacade(final String key) {
		super(NAME);
	}

	/**
	 * get the instance.
	 * @param key the key
	 * @return the singleton
	 */
	public static ApplicationFacade getInstance(final String key) {
		if (instance == null) {
			instance = new ApplicationFacade(key);
		}
		return instance;
	}

    /**
     * Start the application.
     */
    public final void startup() {
         sendNotification(STARTUP);
     }

	/**
	 * Initialize controller. Register the commands.
	 */
	@Override
	protected final void initializeController() {
		super.initializeController();
		preloadImages();
		registerCommand(STARTUP, new StartupCommand());
		registerCommand(DELETE_USER, new DeleteUserCommand());
		registerCommand(ADD_ROLE_RESULT, new AddRoleResultCommand());
	}

    /**
     * Preload all the images.
     */
	public final void preloadImages() {
        // create a DOM element <li>
        Element itemDiv = DOM.createDiv();
        itemDiv.setId("preloader");
        // attach the div to the Root Element
        DOM.appendChild(RootPanel.get().getElement(), itemDiv);
    }

}

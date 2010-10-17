/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.controller;

import org.java.multicore.demos.gwt.employeeadmin.client.ApplicationFacade;
import org.java.multicore.demos.gwt.employeeadmin.client.model.RoleProxy;
import org.java.multicore.demos.gwt.employeeadmin.client.model.UserProxy;
import org.java.multicore.demos.gwt.employeeadmin.client.view.PureMVCMediator;
import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

/**
 * initialization of the view.
 * Register mediator and proxy
 */
public class StartupCommand extends SimpleCommand {

	/**
	 * Register mediator and proxy.
	 * @param notification notification
	 */
	@Override
	public final void execute(final INotification notification) {
		getFacade().registerProxy(new UserProxy());
		getFacade().registerProxy(new RoleProxy());

		getFacade().registerMediator(new PureMVCMediator());

		// Remove the command because it never be called more than once
		getFacade().removeCommand(ApplicationFacade.STARTUP);
	}
}

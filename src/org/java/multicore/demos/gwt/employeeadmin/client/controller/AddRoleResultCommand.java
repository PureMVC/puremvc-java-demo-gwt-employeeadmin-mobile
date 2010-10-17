/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.controller;

import org.puremvc.java.multicore.interfaces.INotification;
import org.puremvc.java.multicore.patterns.command.SimpleCommand;

/**
 * Add role result command.
 */
public class AddRoleResultCommand extends SimpleCommand {

	/**
	 * Check if the the role already exists.
	 * and if it exists send an alert message
	 * @param notification notification
	 */
	@Override
	public final void execute(final INotification notification) {
		/*
		Boolean result = (Boolean) notification.getBody();
		if (result == Boolean.FALSE) {
			// Do not execute alert for the android/iphone/ipad version
			//Window.alert("Role already exists for this user!");
		}*/
	}
}
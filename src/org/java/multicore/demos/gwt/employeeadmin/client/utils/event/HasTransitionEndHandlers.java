/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.utils.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasTransitionEndHandlers extends HasHandlers {
	public HandlerRegistration addTransitionEndHandler(TransitionEndHandler handler);
}



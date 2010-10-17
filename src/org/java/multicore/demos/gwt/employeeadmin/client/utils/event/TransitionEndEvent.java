/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.utils.event;

import com.google.gwt.event.shared.GwtEvent;


public class TransitionEndEvent extends GwtEvent<TransitionEndHandler> {
	public String content;
	private static final GwtEvent.Type<TransitionEndHandler> TYPE = new GwtEvent.Type<TransitionEndHandler>();
	
	public TransitionEndEvent(String content) {
		this.content = content;
	}
	
	@Override
	protected void dispatch(TransitionEndHandler handler) {
		handler.onTransitionEnd(this);
	}

	@Override
	public GwtEvent.Type<TransitionEndHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static Type<TransitionEndHandler> getType() {
		return TYPE;
	}
}



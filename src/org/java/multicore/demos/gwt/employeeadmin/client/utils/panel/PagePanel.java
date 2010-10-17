/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.utils.panel;

import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.HasTransitionEndHandlers;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndEvent;
import org.java.multicore.demos.gwt.employeeadmin.client.utils.event.TransitionEndHandler;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.HTMLPanel;


public class PagePanel extends HTMLPanel implements HasTransitionEndHandlers {

	public enum Direction {
		   LEFT_TO_CENTER,
		   CENTER_TO_RIGHT,
		   RIGHT_TO_CENTER,
		   CENTER_TO_LEFT,
		   LEFT_TO_RIGHT,
		   RIGHT_TO_LEFT
		};
	
	private Direction scroll;
	private Boolean inScroll = false;
	
    public PagePanel(String html) {
		super(html);
				
		registerTransitionEndEvent();
	}

    public Boolean getInScroll() {
		return inScroll;
	}

	private void setInScroll(Boolean inScroll) {
		this.inScroll = inScroll;
	}

	public Direction getScroll() {
    	return scroll;
    }

	public void setScroll(Direction direction) {
		setInScroll(true);

		this.scroll = direction;
		switch(direction) {
			case LEFT_TO_CENTER:
				setStyleName("left");
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						setStyleName("center");
		            }
		        });
			break;

			case CENTER_TO_RIGHT:
				setStyleName("center");
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						setStyleName("right");
		            }
		        });
			break;

			case RIGHT_TO_CENTER:
				setStyleName("right");
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						setStyleName("center");
		            }
		        });
			break;

			case CENTER_TO_LEFT:
				setStyleName("center");
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						setStyleName("left");
		            }
		        });
			break;

			case LEFT_TO_RIGHT:
				setStyleName("left");
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						setStyleName("right");
		            }
		        });
			break;

			case RIGHT_TO_LEFT:
				setStyleName("right");
				DeferredCommand.addCommand(new Command() {
					public void execute() {
						setStyleName("left");
		            }
		        });
			break;

			default:
		}
	}

	/**
	 * Registers a TransitionEnd event for a given element. The event
	 * is triggered when an animation is finished.
	 * The only way to access the Webkit-specific events is to do it the hard way
	 * through JSNI. 
	 * 
	 * TODO: see if a better way with less risk of memory leaks exists
	 * 
	 * @param element Element to register event for. 
	 */
	private native void registerTransitionEndEvent() /*-{
		try {
			var that = this;
			var callBack = function(e){
				that.@org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel::onTransitionEnd()();
			};
			var element = that.@org.java.multicore.demos.gwt.employeeadmin.client.utils.panel.PagePanel::getElement()();
			element.addEventListener('webkitTransitionEnd', callBack, false);
		}
		catch (err) {
		}
	}-*/;

	/**
	 * Removes animation classes and clears out old content.
	 */
	public void onTransitionEnd() {
		fireEvent(new TransitionEndEvent("inTransitionEnd"));
		setInScroll(false);
	}

	@Override
	public HandlerRegistration addTransitionEndHandler(TransitionEndHandler handler) {
		return addHandler(handler, TransitionEndEvent.getType());
	}

}
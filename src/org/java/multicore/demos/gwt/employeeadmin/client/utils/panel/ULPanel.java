/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.utils.panel;

import java.util.Iterator;

import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class ULPanel extends ComplexPanel {

	private Element list;

    @UiConstructor public ULPanel() {
        this.list = DOM.createElement("ul");
        setElement(list);
    }

	@UiFactory public void setSelected(String selected) {
    	list.setAttribute("selected", selected);
    }

    /**
     * adds a Widget to the List as a new List Item (li)
     *
     * please make absolutely sure that the Widget w is not child of any other
     * Widget (DOM Element)
     *
     * @param w
     *            the Widget to add
     */
    public void add(Widget w) {
            // create a DOM element <li>
            Element item = DOM.createElement("li");
            // wrap the Widget w into the <li> Element
            DOM.appendChild(item, w.getElement());
            // wrap the <li> Element into the UnorderedList
            DOM.appendChild(this.list, item);
            // let ComplexPanel save this widget in a WidgetCollection
            super.add(w, item);
    }

    /**
     * removes a Widget from the list, including it's list item.
     *
     * @param w the Widget to remove
     * @return if the operation succeeded
     */
    public boolean remove(Widget w) {
    	if (getChildren().contains(w)) {
    		DOM.removeChild(this.list, DOM.getParent(w.getElement()));
    			return super.remove(w);
            }
    	return false;
    }

    public void clear() {
    	Iterator<Widget> it = getChildren().iterator();
    	while (it.hasNext()) {
    		remove(it.next());
        }
        super.clear();
    }

}
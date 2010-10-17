/*
 PureMVC Java MultiCore Demo - GWT Employee Admin Mobile by Anthony Quinault <anthony.quinault@puremvc.org>
 Based upon PureMVC AS3 Demo - Flex Employee Admin - Copyright(c) 2007-08 Cliff Hall <clifford.hall@puremvc.org>
 Your reuse is governed by the Creative Commons Attribution 3.0 License
 */

package org.java.multicore.demos.gwt.employeeadmin.client.model.vo;

import java.util.ArrayList;
import java.util.List;

import org.java.multicore.demos.gwt.employeeadmin.client.model.enumerator.RoleEnum;

/**
 * RoleVO.
 */
public class RoleVO {

	/**
	 * Username.
	 */
	public String username = new String();

	/**
	 * Roles enumeration.
	 */
	public List<RoleEnum> roles = new ArrayList<RoleEnum>();

	/**
	 * Constructor.
	 */
	public RoleVO(final String username, final List<RoleEnum> roles) {
		if (username != null) {
			this.username = username;
		}
		if (roles != null) {
			this.roles = new ArrayList<RoleEnum>(roles);
		}
	}

}

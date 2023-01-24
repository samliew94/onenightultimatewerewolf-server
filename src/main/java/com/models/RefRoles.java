/**
 * 
 *
 * @author Sam Liew 2 Jan 2023 10:41:11 PM
 */
package com.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Sam Liew 2 Jan 2023 10:41:11 PM
 *
 */
@Entity
@Data
public class RefRoles 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer refRoleId;
	private String roleName;
	private Integer count = 0; // how many of these roles will appear in game?
	private Integer tuple = 1; // when adding/removing this role, totalRoles += or -= (count * tuple)
	private Integer max = 1; // some roles like seer, can only have one.
	private String color;
	
	/**
	 * wake order.<br/>
	 * Lower has higher priority.<br/>
	 * if -1 (default), the player does not get to do anything 
	 *
	 * @author Sam Liew 15 Jan 2023 10:01:55 PM
	 */
	private Integer priority = -1; 
	
	
}


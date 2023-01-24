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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * All players' performed actions are logged here.<br/>
 * If 
 * 
 * @author Sam Liew 2 Jan 2023 10:41:11 PM
 *
 */
@Entity
@Data
public class ActionHistory 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer actionHistoryId;
	
	/**
	 * Which role performed this action?
	 *
	 * @author Sam Liew 15 Jan 2023 10:37:40 PM
	 */
	@JoinColumn(name = "username")
	@ManyToOne
	private Roles role;	

	/**
	 * The first selected player.<br/>
	 * Seer selects another player (views).<br/>
	 * Robber selects 1 player (swaps and view).<br/>
	 * Troublemaker selects 1 of 2 player (swaps).<br/>
	 *
	 * @author Sam Liew 15 Jan 2023 10:33:07 PM
	 */
	@JoinColumn(name = "target_role_one")
	@ManyToOne
	private Roles targetRoleOne;
	
	/**
	 * The first selected player.<br/>
	 * Troublemaker selects 2 of 2 player (swaps).<br/>
	 *
	 * @author Sam Liew 15 Jan 2023 10:42:37 PM
	 */
	@JoinColumn(name = "target_role_two")
	@ManyToOne
	private Roles targetRoleTwo;
	
	/**
	 * The index of the 1st selected unassigned role.<br/>
	 * Lone Werewolf views 1 unassigned role.<br/>
	 * Seer views 1 of 2 unassigned role.<br/>
	 *
	 * @author Sam Liew 15 Jan 2023 10:36:18 PM
	 */
	private int unassignedTarget1 = -1;
	
	
	/**
	 * The index of the 2nd selected unassigned role.<br/> 
	 * Seer views 2 of 2 assigned role.<br/>
	 *
	 * @author Sam Liew 15 Jan 2023 10:36:39 PM
	 */
	private int unassignedTarget2 = -1;
	
	private String remark;
	
}


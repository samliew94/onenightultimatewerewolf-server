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
 * @author Sam Liew 2 Jan 2023 10:41:11 PM
 *
 */
@Entity
@Data
public class Roles 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rolesId;
	
	@JoinColumn(name = "username")
	@ManyToOne
	private Players player;
	
	@JoinColumn(name = "ref_role_id")
	@ManyToOne
	private RefRoles refRole;
	
	/**
	 * At the end of the game, what's this player's real role?
	 *
	 * @author Sam Liew 19 Jan 2023 11:05:06 PM
	 */
	@JoinColumn(name = "final_ref_role_id")
	@ManyToOne
	private RefRoles finalRefRole;
}


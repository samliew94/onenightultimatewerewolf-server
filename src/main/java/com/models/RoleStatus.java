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
public class RoleStatus 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleStatusId;
	
	@JoinColumn(name = "role_id")
	@ManyToOne
	private Roles role;
	
	private String route;
}


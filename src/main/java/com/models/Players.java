/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:13:01 AM
 */
package com.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author Sam Liew 27 Dec 2022 11:13:01 AM
 *
 */
@Entity
@Data
public class Players {

	@Id
	private String username;
	
	private Integer isAdmin = 0;
	private String ipAddress;	
	
}


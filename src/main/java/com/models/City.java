/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:13:01 AM
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
 * @author Sam Liew 27 Dec 2022 11:13:01 AM
 *
 */
@Entity
@Data
public class City {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer cityId;
	private String cityName;
	private String cityDescription;
	
	@JoinColumn(name = "state_id")
	@ManyToOne
	private State state;
	
}


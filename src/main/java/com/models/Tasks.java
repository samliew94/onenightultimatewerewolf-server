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

import org.hibernate.annotations.ColumnDefault;

import lombok.Data;

/**
 * @author Sam Liew 2 Jan 2023 10:41:11 PM
 *
 */
@Entity
@Data
public class Tasks 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer taskId;
	
	@JoinColumn(name = "username")
	@ManyToOne
	private Players player;
	private Integer night;
}


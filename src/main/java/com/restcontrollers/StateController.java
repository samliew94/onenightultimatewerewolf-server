/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 */
package com.restcontrollers;

import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.State;

/**
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/state")
@SuppressWarnings({"unchecked", "rawtypes", "serial"})
public class StateController 
{
	@Autowired
	private SessionFactory sessionFactory;
	
	@GetMapping("{stateId}")
	public ResponseEntity getState(@PathVariable int stateId) {
		
		try (Session session = sessionFactory.openSession())
		{
			State state = session.get(State.class, stateId);
			
			if (state == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("State not found");
			
			return ResponseEntity.ok(state);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.internalServerError().build();
	}
	
	@PostMapping("edit")
	public ResponseEntity editState(@RequestBody Map body)
	{
		int stateId = (int) body.get("stateId");
		String desc = (String) body.get("stateDescription");
		
		try (Session session = sessionFactory.openSession())
		{
			State state = session.get(State.class, stateId);
			
			if (state == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("State not found");
			
			state.setStateDescription(desc);
			
			Transaction transaction = session.beginTransaction();
			try 
			{	
				session.saveOrUpdate(state);
				transaction.commit();
				
				return ResponseEntity.ok(null);
				
			} catch (Exception e) {
				transaction.rollback();
				throw e;
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ResponseEntity.internalServerError().build();
	}
	
	
	
	
	
}


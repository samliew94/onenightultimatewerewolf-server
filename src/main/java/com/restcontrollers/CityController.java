/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 */
package com.restcontrollers;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.models.City;

/**
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/city")
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class CityController {
	@Autowired
	private SessionFactory sessionFactory;

	@GetMapping("{cityId}")
	public ResponseEntity getCity(@PathVariable int cityId) {

		try (Session session = sessionFactory.openSession()) {
			City city = session.get(City.class, cityId);

			if (city == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found");

			return ResponseEntity.ok(city);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.internalServerError().build();
	}

	@GetMapping("state/{stateId}")
	public ResponseEntity getCityByState(@PathVariable int stateId) {

		try (Session session = sessionFactory.openSession()) {
			String query = "FROM City WHERE state.stateId = :stateId";

			List<City> cities = session.createQuery(query).setParameter("stateId", stateId).getResultList();

			return ResponseEntity.ok(cities);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.internalServerError().build();
	}
	
	@DeleteMapping("delete/{cityId}")
	public ResponseEntity deleteState(@PathVariable int cityId)
	{
		try (Session session = sessionFactory.openSession())
		{
			City city = session.get(City.class, cityId);
			
			if (city == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City not found");
			
			Transaction transaction = session.beginTransaction();
			try 
			{	
				session.delete(city);
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

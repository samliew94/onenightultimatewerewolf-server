/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 */
package com.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.services.RolesService;

/**
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/roles")
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class RolesController 
{
	@Lazy
	@Autowired
	private RolesService rolesService;
		
	@GetMapping("all")
	public ResponseEntity getAll() {
		
		System.out.println("GET /roles/all");
		
		try {
			return rolesService.getAll();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e);
		}
	}
	
	
	@GetMapping("finalrole/{username}")
	public ResponseEntity getFinalRole(@PathVariable String username) {
		
		System.out.println("GET /roles/finalrole/" + username);
		
		try {
			return rolesService.getFinalRole(username);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e);
		}
	}
	
	
	
}

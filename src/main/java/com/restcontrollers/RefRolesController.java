/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 */
package com.restcontrollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.services.RefRolesService;

/**
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/refroles")
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class RefRolesController {
	
	@Lazy
	@Autowired
	private RefRolesService refRolesService;
	
	@PutMapping("edit")
	public synchronized ResponseEntity edit(@RequestBody Map body) 
	{
		System.out.println("PUT /refroles/edit " + body);
		
		try {
			return refRolesService.edit(body);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
	}

	@GetMapping("all")
	public synchronized ResponseEntity all() 
	{
		System.out.println("GET /refroles/all");
		
		try {
			return refRolesService.all();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
	}
	
}

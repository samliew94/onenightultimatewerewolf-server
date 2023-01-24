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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.services.ActionHistoryService;

/**
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/actionhistory")
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class ActionHistoryController 
{
	@Lazy
	@Autowired
	private ActionHistoryService actionHistoryService;
	
	/**
	 * 
	 *
	 * @author Sam Liew 16 Jan 2023 9:37:25 AM
	 */
	@PostMapping("completeaction")
	public ResponseEntity completeAction(@RequestBody Map body) {
		
		System.out.println("POST actionhistory/completeaction " + body);
		
		try {
			return actionHistoryService.completeAction(body);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
		
		
	}
	
	@GetMapping("viewaction/{username}")
	public ResponseEntity viewaction(@PathVariable String username) {
		
		System.out.println("GET actionhistory/viewaction/" + username);
		
		try {
			return actionHistoryService.viewAction(username);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
		
	}
	
	@GetMapping("all")
	public ResponseEntity all() {
		
		System.out.println("GET actionhistory/all/");
		
		try {
			return actionHistoryService.all();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
		
	}
	
	@PostMapping("fill")
	public ResponseEntity fill() {
		
		System.out.println("POST actionhistory/fill");
		
		try {
			return actionHistoryService.fill();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
		
	}
	
	
	
}

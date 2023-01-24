/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 */
package com.restcontrollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.services.GameService;

/**
 * @author Sam Liew 27 Dec 2022 11:13:11 AM
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/game")
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class GameController 
{
	@Autowired
	private GameService gameService;
	
	@PostMapping("start")
	public synchronized ResponseEntity start(@RequestBody Map body)
	{
		System.out.println("POST /game/start " + body);
		
		try {
			return gameService.startGame(body);
//			return gameService.startGameDebug();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
		
	}
	
	@PostMapping("end")
	public synchronized ResponseEntity end(@RequestBody Map body)
	{
		System.out.println("POST /game/end " + body);
		
		try {
			return gameService.endGame(body);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
		
	}
	
	@GetMapping("center")
	public synchronized ResponseEntity center()
	{
		System.out.println("GET /game/center");
		
		try {
			return gameService.center();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getClass().getSimpleName());
		}
		
	}
	
}

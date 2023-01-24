/**
 * 
 *
 * @author Sam Liew 19 Jan 2023 8:26:50 PM
 */
package com.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.models.ActionHistory;
import com.models.Players;
import com.models.Roles;

/**
 * @author Sam Liew 19 Jan 2023 8:26:50 PM
 *
 */
@Service
@Transactional
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class PlayerService {

	private Session session;

	@Autowired
	public void setSession(EntityManager entityManager) {
		session = entityManager.unwrap(Session.class);
	}
	
	@Lazy
	@Autowired
	private RolesService rolesService;
	
	@Lazy
	@Autowired
	private GameService gameService;
	
	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 8:28:08 PM
	 * @return 
	 */
	public synchronized ResponseEntity addPlayer(Map body, HttpServletRequest request) throws Exception{
		
		String username = ((String) body.get("username")).toLowerCase();
		
		Players player = session.get(Players.class, username);
		
		String msg = "";
		if (player == null) {
			player = new Players();
			player.setUsername(username);
			player.setIpAddress(request.getRemoteAddr());
			player.setIsAdmin(session.createQuery("FROM Players").getResultList().size() == 0 ? 1 : 0);
			msg = "Successfully registered " + username + "\n\n";
			session.saveOrUpdate(player);
		}
		
		Map result = new HashMap();
		
		if (player.getIsAdmin() == 1) 
			result.put("route", "lobby");
		else 
		{
			Roles role = rolesService.getRole(player);
			
			if (role == null)
				return ResponseEntity.badRequest().body(msg + "Game Not Started!");
			
			return gameService.getRoute(role);
		}
		
		return ResponseEntity.ok(result);
	}

	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 10:55:46 PM
	 */
	public ResponseEntity getLobbyPlayers() throws Exception{
		List<Players> players = session.createQuery("FROM Players", Players.class).getResultList();		
		return ResponseEntity.ok(players);
	}

	/**
	 * Delete leaf dependencies ...down to root (Players.class)
	 *
	 * @author Sam Liew 19 Jan 2023 10:57:48 PM
	 */
	public ResponseEntity delPlayer(Map body) throws Exception{
		
		String targetUsername = ((String) body.get("targetUsername")).toLowerCase();
		session.createQuery("FROM ActionHistory WHERE role.player.username = :targetUsername", ActionHistory.class)
				.setParameter("targetUsername", targetUsername)
				.getResultList().forEach(x->session.delete(x));
				;
				
		session.createQuery("FROM Roles WHERE player.username = :targetUsername", Roles.class)
				.setParameter("targetUsername", targetUsername)
				.getResultList().forEach(x->session.delete(x));
				;
				
		session.delete(session.get(Players.class, targetUsername));
				
		return ResponseEntity.ok(null);
		
	}
	
	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 5:03:42 PM
	 */
	public ResponseEntity allExceptRequestor(String username) throws Exception{
		List<String> usernames = session.createQuery("SELECT username FROM Players WHERE username != :username")
				.setParameter("username", username)
				.getResultList();
		
		return ResponseEntity.ok(usernames);
	}
	
	
}


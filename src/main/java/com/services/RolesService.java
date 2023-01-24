/**
 * 
 *
 * @author Sam Liew 19 Jan 2023 8:26:50 PM
 */
package com.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.models.Players;
import com.models.RefRoles;
import com.models.Roles;

/**
 * @author Sam Liew 19 Jan 2023 8:26:50 PM
 *
 */
@Service
@Transactional
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class RolesService {

	private Session session;

	@Autowired
	public void setSession(EntityManager entityManager) {
		session = entityManager.unwrap(Session.class);
	}
	
	@Lazy
	@Autowired
	private GameService gameService;	
	
	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 8:53:00 PM
	 */
	public Roles getRole(Players player) throws Exception{
		
		Roles role = session.createQuery("FROM Roles WHERE player = :player", Roles.class).setParameter("player", player)
				.getResultStream().findFirst().orElse(null);
		
		return role;
	}

	/**
	 * DEBUG ONLY
	 *
	 * @author Sam Liew 20 Jan 2023 11:34:24 AM
	 */
	public ResponseEntity getAll() throws Exception{
		
		List<Roles> roles = session.createQuery("FROM Roles", Roles.class).getResultList();
		
		List<Map> results = new ArrayList();
		
		roles.forEach(x->{
			Players player = x.getPlayer();
			RefRoles refRole = x.getRefRole();
			
			results.add(new HashMap() {{
				put("username", player.getUsername());
				put("roleName", refRole.getRoleName());
				put("finalRoleName", x.getFinalRefRole() == null ? null : x.getFinalRefRole().getRoleName());
			}});
		});
		
		return ResponseEntity.ok(results);
		
	}

	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 9:22:35 PM
	 */
	public ResponseEntity getFinalRole(String username) throws Exception {
		
		username = username.toLowerCase();
		
		if (gameService.getGameStarted())
			return ResponseEntity.badRequest().body("Can't see Final Card until game ends");
		
		Roles role = session.createQuery("FROM Roles WHERE player.username = :username", Roles.class).setParameter("username", username).getSingleResult();
		
		RefRoles finalRefRole = role.getFinalRefRole();
		
		String roleName = finalRefRole.getRoleName();
		String color = finalRefRole.getColor();
		
		Map result = new HashMap();
		result.put("roleName", roleName);
		result.put("color", color);
		
		return ResponseEntity.ok(result);
	}
	
	
}


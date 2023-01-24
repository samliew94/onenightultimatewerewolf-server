/**
 * 
 *
 * @author Sam Liew 19 Jan 2023 8:26:50 PM
 */
package com.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.models.ActionHistory;
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
public class GameService {

	private Session session;
	
	private Random random = new SecureRandom();
	
	private int gameId;
	
	private boolean gameStarted;
	
	private List<Integer> unassignedRefRolesId = new ArrayList();

	@Autowired
	public void setSession(EntityManager entityManager) {
		session = entityManager.unwrap(Session.class);
	}
	
	@Lazy
	@Autowired
	private ActionHistoryService actionHistoryService;

	/**
	 * Determine WHAT screen the user should see.<br/>
	 *
	 * @author Sam Liew 19 Jan 2023 9:09:45 PM
	 */
	public ResponseEntity getRoute(Roles role) {

		Map result = new HashMap();
		
		String query = "";
		
		query = "FROM ActionHistory WHERE role = :role";
		
		ActionHistory actionHistory = session.createQuery(query, ActionHistory.class).setParameter("role", role).getResultStream().findFirst().orElse(null);
		
		if (actionHistory != null) {
			
//			String roleName = role.getRefRole().getRoleName();
//			
//			if (roleName.equalsIgnoreCase("werewolf")) {
//				
//				query = "FROM Roles WHERE refRole.roleName = 'werewolf' AND player != :player";
//				
//				boolean isLoneWerewolf = session.createQuery(query).setParameter("player", actionHistory.getRole().getPlayer()).getResultList().size() == 0;
//			
//				if (isLoneWerewolf)
//					result.put("route", "loneWerewolfDone");
//				else
//					result.put("route", "werewolvesDone");
//				
//			} else if (roleName.equalsIgnoreCase("seer")) {
//				
//				result.put("route", "seerDone");
//				
//			} else if (roleName.equalsIgnoreCase("robber")) {
//				
//				result.put("route", "robberDone");
//				
//			} else if (roleName.equalsIgnoreCase("troublemaker")) {
//				
//				result.put("route", "troublemakerDone");
//				
//			} else if (roleName.equalsIgnoreCase("villager")) {
//			
//				result.put("route", "villagerDone");
//				
//			} else if (roleName.equalsIgnoreCase("tanner")) {
//				
//				result.put("route", "tannerDone");
//				
//			} else if (roleName.equalsIgnoreCase("drunk")) {
//				
//				result.put("route", "drunkDone");
//				
//			} else if (roleName.equalsIgnoreCase("hunter")) {
//				
//				result.put("route", "hunterDone");
//				
//			} else if (roleName.equalsIgnoreCase("mason")) {
//				
//				result.put("route", "masonDone");
//				
//			} else if (roleName.equalsIgnoreCase("minion")) {
//				
//				result.put("route", "minionDone");
//				
//			}
//			
//			if (result.containsKey("route"))
//				return ResponseEntity.ok(result);
			
			result.put("route", "genericDone");
			
			return ResponseEntity.ok(result);
		}
		
		Players player = role.getPlayer();
		RefRoles refRole = role.getRefRole();
		String roleName = refRole.getRoleName();
		
		if (roleName.equalsIgnoreCase("werewolf")) {
			
			query = "SELECT player.username FROM Roles WHERE refRole.roleName = 'werewolf' AND player != :player";
			
			List<String> otherWerewolves = session.createQuery(query).setParameter("player", player).getResultList();
			
			if (actionHistory == null) {
				if (otherWerewolves.isEmpty())
					result.put("route", "loneWerewolfAction");
				else
					result.put("route", "quiz"); // 2P werewolves must do quiz
			}
			
		} else if (roleName.equalsIgnoreCase("seer")) {
			
			result.put("route", "seerAction");
			
		} else if (roleName.equalsIgnoreCase("robber")) {
			
			result.put("route", "robberAction");
			
		} else if (roleName.equalsIgnoreCase("troublemaker")) {
			
			result.put("route", "troublemakerAction");
			
		} else if (roleName.equalsIgnoreCase("drunk")) {
			
			result.put("route", "drunkAction");
			
		} else if (roleName.equalsIgnoreCase("villager") || roleName.equalsIgnoreCase("tanner")
				|| roleName.equalsIgnoreCase("hunter") || roleName.equalsIgnoreCase("mason") 
				|| roleName.equalsIgnoreCase("minion") ) {
			
			result.put("route", "quiz");
		} 
		
		return ResponseEntity.ok(result);
	}
	
	public List<Integer> getUnassignedRefRoles(){
		return unassignedRefRolesId;
	}
	
	public ResponseEntity startGameDebug() throws Exception{
		
		List<Roles> roles = new ArrayList();
		
		List<Players> players = session.createQuery("FROM Players", Players.class).getResultList();
		
		Roles p0 = new Roles();
		p0.setRefRole(session.createQuery("FROM RefRoles WHERE roleName = 'drunk'", RefRoles.class).getSingleResult());
		p0.setPlayer(players.get(0));
		roles.add(p0);
		
		Roles p1 = new Roles();
		p1.setRefRole(session.createQuery("FROM RefRoles WHERE roleName = 'werewolf'", RefRoles.class).getSingleResult());
		p1.setPlayer(players.get(1));
		roles.add(p1);
		
		Roles p2 = new Roles();
		p2.setRefRole(session.createQuery("FROM RefRoles WHERE roleName = 'villager'", RefRoles.class).getSingleResult());
		p2.setPlayer(players.get(2));
		roles.add(p2);
		
		Roles p3 = new Roles();
		p3.setRefRole(session.createQuery("FROM RefRoles WHERE roleName = 'mason'", RefRoles.class).getSingleResult());
		p3.setPlayer(players.get(3));
		roles.add(p3);
		
		session.createQuery("FROM ActionHistory").getResultList().forEach(x->session.delete(x));
		session.createQuery("FROM Roles").getResultList().forEach(x->session.delete(x));
		roles.forEach(x->session.saveOrUpdate(x));
		
		unassignedRefRolesId.clear();
		unassignedRefRolesId.add(session.createQuery("SELECT refRoleId FROM RefRoles WHERE roleName = 'seer'", Integer.class).getSingleResult());
		unassignedRefRolesId.add(session.createQuery("SELECT refRoleId FROM RefRoles WHERE roleName = 'robber'", Integer.class).getSingleResult());
		unassignedRefRolesId.add(session.createQuery("SELECT refRoleId FROM RefRoles WHERE roleName = 'troublemaker'", Integer.class).getSingleResult());
		
		gameStarted = true;
		
		return getRoute(p0);
	}
	
	public ResponseEntity startGame(Map body) throws Exception{
		
		String query = "";
		
		query = "FROM RefRoles rr WHERE rr.count > 0";
		
		List<RefRoles> refRoles = session.createQuery(query, RefRoles.class).getResultList();
		
		Map<RefRoles, Integer> refRoleCount = new HashMap();
		
		int totalRoles = 0;
		for (int i = 0; i < refRoles.size(); i++) {
			RefRoles rr = refRoles.get(i);
			totalRoles += rr.getCount();
			refRoleCount.put(rr, rr.getCount());
		}
		
		query = "FROM Players";
		
		List<Players> players = session.createQuery(query, Players.class).getResultList();
		List<Players> copyPlayers = new ArrayList();
		players.forEach(x->copyPlayers.add(x));
		
		if (players.size() + 3 != totalRoles) {
			String msg = "";
			msg = "Total Roles = " + totalRoles;
			msg += "\nTotal Players = " + players.size();
			msg += "\n\nTotal Roles MUST be 3 greater than total players!";
			return ResponseEntity.badRequest().body(msg);
		}
		
		query = "FROM Roles";
		
		List<Roles> newRoles = new ArrayList();
		
		Collections.shuffle(players, random);
		
		while(!players.isEmpty()) {

			Collections.shuffle(refRoles, random);
			
			RefRoles refRole = refRoles.get(0);
			
			refRoleCount.put(refRole, refRoleCount.get(refRole)-1);
			
			if (refRoleCount.get(refRole) == 0)
				refRoles.remove(refRole);
			
			Players player = players.remove(0);
			
			Roles role = new Roles();
			role.setPlayer(player);
			role.setRefRole(refRole);
			newRoles.add(role);
		}
		
		unassignedRefRolesId.clear();
		refRoleCount.forEach((k,v) ->{
			
			while (v > 0) {
				v -= 1;
				unassignedRefRolesId.add(k.getRefRoleId());
			}
			
		});
		
		Collections.shuffle(unassignedRefRolesId, random);
		
		session.createQuery("FROM ActionHistory").getResultList().forEach(x->session.delete(x));
		session.createQuery("FROM Roles").getResultList().forEach(x->session.delete(x));
		newRoles.forEach(x->session.saveOrUpdate(x));
		
		gameId += 1;
		gameStarted = true;
		
		String username = ((String) body.get("username")).toLowerCase();
		Roles role = newRoles.stream().filter(x->x.getPlayer().getUsername().equals(username)).findFirst().get();
		
		return getRoute(role);
	}
	
	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 8:40:10 PM
	 * @param body 
	 */
	public ResponseEntity endGame(Map body) throws Exception{
		
		Map result = new HashMap() {{
			put("route", "finalRole");
		}};
		
		String username = ((String) body.get("username")).toLowerCase();
		
		Players player = session.get(Players.class, username);
	
		if (player.getIsAdmin() != 1)
			return ResponseEntity.ok(result);
		
		List<ActionHistory> actionHistories = session.createQuery("FROM ActionHistory", ActionHistory.class).getResultList();
		List<Players> players = session.createQuery("FROM Players", Players.class).getResultList();
		
		if (actionHistories.size() < players.size()) {
			
			Set<String> allUsernames = players.stream().map(x->x.getUsername()).collect(Collectors.toSet());
			Set<String> historyUsernames = actionHistories.stream().map(x->x.getRole().getPlayer().getUsername()).collect(Collectors.toSet());
			
			allUsernames.removeAll(historyUsernames);
			
			String msg = "Can't End Game.\n\n";
			msg += String.join("\n", allUsernames) + "\n\nhave not completed action";
			
			return ResponseEntity.badRequest().body(msg);
		}
			
		actionHistoryService.executeActions();
		
		gameStarted = false; // synonym for game end
		
		return ResponseEntity.ok(result);
	}
	
	public int getGameId() {
		return gameId;
	}

	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 9:23:08 PM
	 */
	public boolean getGameStarted() {
		return gameStarted;
	}

	/**
	 * 
	 *
	 * @author Sam Liew 24 Jan 2023 5:50:29 PM
	 */
	public ResponseEntity center() throws Exception{
		
		int counter = 0;
		String msg = "";
		for (int i = 0; i < unassignedRefRolesId.size(); i++) {
			int x = unassignedRefRolesId.get(i);
			
			String roleName = session.get(RefRoles.class, x).getRoleName();
			msg += "Card " + (i+1) + " = " + roleName + "\n";
			
		}
		
		return ResponseEntity.ok(msg);
		
	}

	
	
}


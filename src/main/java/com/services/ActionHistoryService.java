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

import com.models.ActionHistory;
import com.models.RefRoles;
import com.models.Roles;

/**
 * @author Sam Liew 19 Jan 2023 8:26:50 PM
 *
 */
@Service
@Transactional
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class ActionHistoryService {

	private Session session;

	@Autowired
	public void setSession(EntityManager entityManager) {
		session = entityManager.unwrap(Session.class);
	}
	
	@Lazy
	@Autowired
	private GameService gameService;
	
	public ActionHistory getActionHistory(Roles role) throws Exception{
		return session.createQuery("FROM ActionHistory WHERE role = :role", ActionHistory.class)
			.setParameter("role", role).getResultStream().findFirst().orElse(null);
	}

	/**
	 * branches into completActionXYZ where XYZ = <br/>
	 * werewolf<br/>
	 * seer<br/>
	 * robber<br/>
	 * villager<br/>
	 *
	 * @author Sam Liew 19 Jan 2023 9:24:23 PM
	 */
	public ResponseEntity completeAction(Map body) throws Exception{
		
		String username = ((String) body.get("username")).toLowerCase();
		
		String query = "";
		
		query = "FROM Roles WHERE player.username = :username";
		
		Roles role = session.createQuery(query,Roles.class).setParameter("username", username).getSingleResult();
		
		if (role.getRefRole().getRoleName().equalsIgnoreCase("werewolf"))
			return completeActionWerewolf(body,role);
		else if (role.getRefRole().getRoleName().equalsIgnoreCase("seer"))
			return completeActionSeer(body,role);
		else if (role.getRefRole().getRoleName().equalsIgnoreCase("robber"))
			return completeActionRobber(body,role);
		else if (role.getRefRole().getRoleName().equalsIgnoreCase("troublemaker"))
			return completeActionTroublemaker(body,role);
		else if (role.getRefRole().getRoleName().equalsIgnoreCase("drunk"))
			return completeActionDrunk(body,role);
		else
			return completeActionGeneric(body,role);
		
//		throw new Exception("Can't find role by username " + username);
	}

	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 10:34:30 PM
	 */
	private ResponseEntity completeActionGeneric(Map body, Roles role) {
		
		Map result = new HashMap();
		
		ActionHistory actionHistory = new ActionHistory();
		actionHistory.setRole(role);
		actionHistory.setRemark(role.getPlayer().getUsername() + " " + role.getRefRole().getRoleName() +" finished generic mission");
		
		session.saveOrUpdate(actionHistory);
		
		result.put("route", "genericDone");
		return ResponseEntity.ok(result);
		
	}

	/**
	 * 
	 *
	 * @author Sam Liew 23 Jan 2023 10:41:01 PM
	 */
	private ResponseEntity completeActionDrunk(Map body, Roles role) throws Exception{
		
		Map result = new HashMap();
		
		ActionHistory actionHistory = new ActionHistory();
		actionHistory.setRole(role);
		
		int targetIndex = (int) body.get("targetIndex");
		actionHistory.setUnassignedTarget1(targetIndex);
		actionHistory.setRemark(role.getPlayer().getUsername() + " chose card " + (targetIndex + 1));	
		
		session.persist(actionHistory);
		
		result.put("route", "genericDone");		
		return ResponseEntity.ok(result);
	}
	
	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 10:00:14 PM
	 */
	private ResponseEntity completeActionTroublemaker(Map body, Roles role) {
		
		Map result = new HashMap();
		
		String targetUsername1 = (String) body.get("targetUsername1");
		String targetUsername2 = (String) body.get("targetUsername2");
		
		ActionHistory actionHistory = new ActionHistory();
		actionHistory.setRole(role);
		
		String query = "";
		
		query = "FROM Roles WHERE player.username IN (:targetUsername1, :targetUsername2)";
		
		List<Roles> roles = session.createQuery(query, Roles.class)
				.setParameter("targetUsername1", targetUsername1)
				.setParameter("targetUsername2", targetUsername2)
				.getResultList();
		
		actionHistory.setTargetRoleOne(roles.get(0));
		actionHistory.setTargetRoleTwo(roles.get(1));
		actionHistory.setRemark(role.getPlayer().getUsername() + " swapped " + targetUsername1 + " and " + targetUsername2);
		
		session.saveOrUpdate(actionHistory);
		
		result.put("route", "genericDone");
		return ResponseEntity.ok(result);
	}

	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 9:57:37 PM
	 */
	private ResponseEntity completeActionRobber(Map body, Roles role) {
		
		Map result = new HashMap();
		
		String targetUsername = (String) body.get("targetUsername");
		
		ActionHistory actionHistory = new ActionHistory();
		actionHistory.setRole(role);
		
		String query = "";
		
		query = "FROM Roles WHERE player.username = :username";
		
		Roles targetRole = session.createQuery(query, Roles.class).setParameter("username", targetUsername).getSingleResult();
		
		actionHistory.setTargetRoleOne(targetRole);
		actionHistory.setRemark(role.getPlayer().getUsername() + " chose to rob " + targetUsername);
		
		session.saveOrUpdate(actionHistory);
		
		result.put("route", "genericDone");		
		return ResponseEntity.ok(result);
	}

	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 9:43:29 PM
	 */
	private ResponseEntity completeActionSeer(Map body, Roles role) {
		
		Map result = new HashMap();
		
		String targetUsername = (String) body.get("targetUsername");
		
		ActionHistory actionHistory = new ActionHistory();
		actionHistory.setRole(role);
		
		if (targetUsername != null) {
			
			String query = "";
			
			query = "FROM Roles WHERE player.username = :username";
			
			Roles targetRole = session.createQuery(query, Roles.class).setParameter("username", targetUsername).getSingleResult();
			
			actionHistory.setTargetRoleOne(targetRole);
			actionHistory.setRemark(role.getPlayer().getUsername() + " chose to see " + targetUsername + "'s card");
						
		}
		else {
			int targetIndex1 = (int) body.get("targetIndex1");
			int targetIndex2 = (int) body.get("targetIndex2");
			
			actionHistory.setUnassignedTarget1(targetIndex1);
			actionHistory.setUnassignedTarget2(targetIndex2);
			actionHistory.setRemark(role.getPlayer().getUsername() + " chose to see 2 center cards (" + targetIndex1 + "," + targetIndex2 + ")");
			
		}
		
		session.saveOrUpdate(actionHistory);
		
		result.put("route", "genericDone");		
		return ResponseEntity.ok(result);
	}

	/**
	 * 
	 *
	 * @author Sam Liew 19 Jan 2023 9:26:17 PM
	 * @param body 
	 * @param role 
	 */
	private ResponseEntity completeActionWerewolf(Map body, Roles role) throws Exception{
		
		Map result = new HashMap();
		
		String query = "SELECT player.username FROM Roles WHERE refRole.roleName = 'werewolf' AND player != :player";
		
		List<String> otherWerewolves = session.createQuery(query).setParameter("player", role.getPlayer()).getResultList();
		
		ActionHistory actionHistory = new ActionHistory();
		actionHistory.setRole(role);
		
		if (otherWerewolves.size() == 0) {
			int targetIndex = (int) body.get("targetIndex");
			RefRoles refRole = session.get(RefRoles.class, gameService.getUnassignedRefRoles().get(targetIndex));
			
			actionHistory.setUnassignedTarget1(targetIndex);
			actionHistory.setRemark(role.getPlayer().getUsername() + " chose to view card " + (targetIndex + 1) + " = " + refRole.getRoleName());
			
		}
		else
			actionHistory.setRemark(role.getPlayer().getUsername() + " completed 2P werewolves' task");		
		
		session.persist(actionHistory);
		
		result.put("route", "genericDone");		
		return ResponseEntity.ok(result);
	}
		
	public ResponseEntity viewAction(String username) throws Exception{
		
		username = username.toLowerCase();
		
		ActionHistory actionHistory = session.createQuery("FROM ActionHistory WHERE role.player.username = :username", ActionHistory.class)
				.setParameter("username", username).getResultStream().findFirst().orElse(null);
		
		if (actionHistory == null)
			return ResponseEntity.badRequest().body("Can't find actionHistory of " + username);
		
		String roleName = actionHistory.getRole().getRefRole().getRoleName();
		
		if (roleName.equalsIgnoreCase("werewolf"))
			return viewActionWerewolf(actionHistory);
		else if (roleName.equalsIgnoreCase("seer"))
			return viewActionSeer(actionHistory);
		else if (roleName.equalsIgnoreCase("robber"))
			return viewActionRobber(actionHistory);
		else if (roleName.equalsIgnoreCase("troublemaker"))
			return viewActionTroublemaker(actionHistory);
		else if (roleName.equalsIgnoreCase("villager") || roleName.equalsIgnoreCase("tanner") || roleName.equalsIgnoreCase("hunter"))
			return viewActionGeneric(actionHistory);
		else if (roleName.equalsIgnoreCase("mason"))
			return viewActionMason(actionHistory);
		else if (roleName.equalsIgnoreCase("minion"))
			return viewActionMinion(actionHistory);
		else if (roleName.equalsIgnoreCase("drunk"))
			return viewActionDrunk(actionHistory);
		
		return ResponseEntity.internalServerError().body("Can't find rolename = " + roleName);
	}
	
	private ResponseEntity viewActionDrunk(ActionHistory actionHistory) {
				
		List<Map> results = new ArrayList();
		
		results.add(new HashMap() {{
			put("title", actionHistory.getRole().getRefRole().getRoleName());
			put("color", actionHistory.getRole().getRefRole().getColor());
		}});
		
		int targetIndex = actionHistory.getUnassignedTarget1();
		
		results.add(new HashMap() {{
			put("top", 10.0);
			put("title", "Swapped with Card " + (targetIndex + 1));
			put("size", 5);
		}});
		
		return ResponseEntity.ok(results);
	}
	
	private ResponseEntity viewActionMinion(ActionHistory actionHistory) {
		
		List<Roles> roles = session.createQuery("FROM Roles WHERE refRole.roleName = 'werewolf'", Roles.class).getResultList();
				
		List<Map> results = new ArrayList();
		
		results.add(new HashMap() {{
			put("title", actionHistory.getRole().getRefRole().getRoleName());
			put("color", actionHistory.getRole().getRefRole().getColor());
		}});
		
		if (roles.isEmpty()) {
			
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", "There are no Werewolves. You lose if you're killed");
				put("size", 5);
			}});
			
		}
		else {
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", "Other Werewolves");
				put("size", 4);
			}});
			
			for (int i = 0; i < roles.size(); i++) {
				Roles role = roles.get(i);
				
				results.add(new HashMap() {{
					put("title", role.getPlayer().getUsername());
					put("color", role.getRefRole().getColor());
				}});
			}
		}
		
		return ResponseEntity.ok(results);
	}
	
	private ResponseEntity viewActionMason(ActionHistory actionHistory) {
		
		Roles role = session.createQuery("FROM Roles WHERE refRole.roleName = 'mason' AND player != :player", Roles.class)
				.setParameter("player", actionHistory.getRole().getPlayer())
				.getResultStream().findFirst().orElse(null);
		
		List<Map> results = new ArrayList();
		
		results.add(new HashMap() {{
			put("title", actionHistory.getRole().getRefRole().getRoleName());
			put("color", actionHistory.getRole().getRefRole().getColor());
		}});
	
		
		if (role != null) {
			
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", "other mason");
				put("size", 4);
			}});
			
			results.add(new HashMap() {{
				put("title", role.getPlayer().getUsername());
				put("color", role.getRefRole().getColor());
			}});
			
		} else {
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", "you are the only Mason");
				put("size", 5);
			}});
		}
		
		return ResponseEntity.ok(results);
	}
	
	private ResponseEntity viewActionGeneric(ActionHistory actionHistory) {
		
		List<Map> results = new ArrayList();
		
		results.add(new HashMap() {{
			put("title", actionHistory.getRole().getRefRole().getRoleName());
			put("color", actionHistory.getRole().getRefRole().getColor());
		}});
		
		return ResponseEntity.ok(results);
	}
	
	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 6:35:47 PM
	 */
	private ResponseEntity viewActionTroublemaker(ActionHistory actionHistory) {
		
		String u1 = actionHistory.getTargetRoleOne().getPlayer().getUsername();
		String u2 = actionHistory.getTargetRoleTwo().getPlayer().getUsername();
		
		List<Map> results = new ArrayList();
		
		results.add(new HashMap() {{
			put("title", "troublemaker");
			put("color", "6495ED");
			put("size", 3);
		}});
		
		results.add(new HashMap() {{
			put("title", "swapped " + u1 + " and " + u2);
			put("size", 5);
		}});
		
		
		return ResponseEntity.ok(results);
	}

	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 6:35:47 PM
	 */
	private ResponseEntity viewActionRobber(ActionHistory actionHistory) {
		
		Map result = new HashMap();
		
		Roles targetRole = actionHistory.getTargetRoleOne();
		
		result.put("targetUsername", targetRole.getPlayer().getUsername());
		result.put("roleName", targetRole.getRefRole().getRoleName());
		result.put("color", targetRole.getRefRole().getColor());
		
		List<Map> results = new ArrayList();
		
		results.add(new HashMap() {{
			put("title", targetRole.getRefRole().getRoleName());
			put("color", targetRole.getRefRole().getColor());
		}});
		
		results.add(new HashMap() {{
			put("title", "swapped with " + targetRole.getPlayer().getUsername());
			put("size", 5);
		}});
		
		return ResponseEntity.ok(results);
	}
	
	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 6:01:17 PM
	 */
	private ResponseEntity viewActionSeer(ActionHistory actionHistory) {
		
		Roles targetRole = actionHistory.getTargetRoleOne();
		
		List<Map> results = new ArrayList();
		
		results.add(new HashMap() {{
			put("title", actionHistory.getRole().getRefRole().getRoleName());
			put("color", actionHistory.getRole().getRefRole().getColor());
		}});
		
		if (targetRole != null) {
			
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", targetRole.getPlayer().getUsername() + " is ");
			}});
			
			results.add(new HashMap() {{
				put("title", targetRole.getRefRole().getRoleName());
				put("color", targetRole.getRefRole().getColor());						
			}});
			
		}else {
			int t1 = actionHistory.getUnassignedTarget1();
			int t2 = actionHistory.getUnassignedTarget2();
			
			RefRoles refRole1 = session.get(RefRoles.class, gameService.getUnassignedRefRoles().get(t1));
			RefRoles refRole2 = session.get(RefRoles.class, gameService.getUnassignedRefRoles().get(t2));
			
//			result.put("index1", t1);
//			result.put("index2", t2);
//			result.put("roleName1", refRole1.getRoleName());
//			result.put("color1", refRole1.getColor());
//			result.put("roleName2", refRole2.getRoleName());
//			result.put("color2", refRole2.getColor());
			
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", "Card " + (t1+1) + " is ");
				put("size", 5);
			}});
			
			results.add(new HashMap() {{
				put("title", refRole1.getRoleName());
				put("color", refRole1.getColor());
			}});
			
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", "Card " + (t2+1) + " is ");
				put("size", 5);
			}});
			
			results.add(new HashMap() {{
				put("title", refRole2.getRoleName());
				put("color", refRole2.getColor());
			}});
		}
		
		return ResponseEntity.ok(results);
	}

	
	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 1:32:26 PM
	 */
	private ResponseEntity viewActionWerewolf(ActionHistory actionHistory) {
		
		List<String> otherWerewolves = session.createQuery("SELECT player.username FROM Roles WHERE refRole.roleName = 'werewolf' AND player != :player")
				.setParameter("player", actionHistory.getRole().getPlayer())
				.getResultList();
		
		List<Map> results = new ArrayList();
		
		if (otherWerewolves.isEmpty()) {
			
			int targetIndex = actionHistory.getUnassignedTarget1();
			Integer refRoleId = gameService.getUnassignedRefRoles().get(targetIndex);
			RefRoles targetRole = session.get(RefRoles.class, refRoleId);
			
			results.add(new HashMap() {{
				put("title", "lone werewolf");
				put("color", actionHistory.getRole().getRefRole().getColor());
			}});
			
			results.add(new HashMap() {{
				put("top", 10.0);
				put("title", "Card " + (targetIndex+1) + " is ");
				put("size", 5);
			}});
			
			results.add(new HashMap() {{
				put("title", targetRole.getRoleName());
				put("color", targetRole.getColor());
			}});
			
		}else {
			
			results.add(new HashMap() {{
				put("title", "werewolf");
				put("color", actionHistory.getRole().getRefRole().getColor());
			}});
			
			results.add(new HashMap() {{
				put("top", 25.0);
				put("title", "Other Werewolf");
				put("size", 5);
			}});
			
			results.add(new HashMap() {{
				put("title", otherWerewolves.get(0));
				put("color", actionHistory.getRole().getRefRole().getColor());
			}});
		}
		
		return ResponseEntity.ok(results);
	}

	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 1:20:27 PM
	 */
	public ResponseEntity all() {
		
		List<ActionHistory> actionHistories = session.createQuery("FROM ActionHistory", ActionHistory.class).getResultList();
		
		return ResponseEntity.ok(actionHistories);
	}

	/**
	 * Execute Robber and Troublemaker's power.<br/>
	 * Set final role.<br/>
	 *
	 * @author Sam Liew 20 Jan 2023 8:41:49 PM
	 */
	public void executeActions() throws Exception {
		
		ActionHistory ahRobber = session.createQuery("FROM ActionHistory WHERE role.refRole.roleName = 'robber'", ActionHistory.class)
				.getResultStream().findFirst().orElse(null);
		
		if (ahRobber != null) {
			Roles robber = ahRobber.getRole();
			Roles robberTarget = ahRobber.getTargetRoleOne();
			
			int robberId = robber.getRefRole().getRefRoleId();
			int robberTargetId = robberTarget.getRefRole().getRefRoleId();
			
			robber.setFinalRefRole(session.get(RefRoles.class, robberTargetId));
			robberTarget.setFinalRefRole(session.get(RefRoles.class, robberId));
			
			session.saveOrUpdate(robber);
			session.saveOrUpdate(robberTarget);
		}
		
		ActionHistory ahTroublemaker = session.createQuery("FROM ActionHistory WHERE role.refRole.roleName = 'troublemaker'", ActionHistory.class)
				.getResultStream().findFirst().orElse(null);
		
		if (ahTroublemaker != null) {
			
			Roles target1 = ahTroublemaker.getTargetRoleOne();			
			Roles target2 = ahTroublemaker.getTargetRoleTwo();
			
			int targetId1 = target1.getRefRole().getRefRoleId();
			int targetId2 = target2.getRefRole().getRefRoleId();
			
			RefRoles finalRefRole1 = target1.getFinalRefRole();
			RefRoles finalRefRole2 = target2.getFinalRefRole();
			
			if (finalRefRole1 != null)
				targetId1 = finalRefRole1.getRefRoleId();
			
			if (finalRefRole2 != null)
				targetId2 = finalRefRole2.getRefRoleId();
			
			target1.setFinalRefRole(session.get(RefRoles.class, targetId2));
			target2.setFinalRefRole(session.get(RefRoles.class, targetId1));
			
			session.saveOrUpdate(target1);
			session.saveOrUpdate(target2);
			
		}

		ActionHistory ahDrunk = session.createQuery("FROM ActionHistory WHERE role.refRole.roleName = 'drunk'", ActionHistory.class)
				.getResultStream().findFirst().orElse(null);
		
		if (ahDrunk != null) {
			Integer refRoleId = gameService.getUnassignedRefRoles().get(ahDrunk.getUnassignedTarget1());
			RefRoles finalRefRole = session.get(RefRoles.class, refRoleId);
			
			Roles role = ahDrunk.getRole();
			role.setFinalRefRole(finalRefRole);
			
			session.saveOrUpdate(role);
		}
		
		
		List<Roles> otherRoles = session.createQuery("FROM Roles WHERE finalRefRole IS NULL", Roles.class).getResultList();
		
		otherRoles.forEach(x->{
			RefRoles curRefRole = x.getRefRole();
			x.setFinalRefRole(curRefRole);
			session.saveOrUpdate(x);
		});
	}

	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 9:58:46 PM
	 */
	public ResponseEntity fill() throws Exception {
		
		List<Roles> roles = session.createQuery("FROM Roles", Roles.class).getResultList();
		
		roles.forEach(x->{
			ActionHistory ah = new ActionHistory();
			ah.setRole(x);
			session.saveOrUpdate(ah);
		});
		
		return ResponseEntity.ok(null);
		
	}
	
}


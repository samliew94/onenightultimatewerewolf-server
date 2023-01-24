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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.models.RefRoles;

/**
 * @author Sam Liew 19 Jan 2023 8:26:50 PM
 *
 */
@Service
@Transactional
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class RefRolesService {

	private Session session;

	@Autowired
	public void setSession(EntityManager entityManager) {
		session = entityManager.unwrap(Session.class);
	}
	
	public ResponseEntity edit(Map body) throws Exception
	{
		int refRoleId = (int) body.get("refRoleId");
		int operation = (int) body.get("operation");
		

		RefRoles refRole = session.get(RefRoles.class, refRoleId);
		if(refRole == null)
			return ResponseEntity.badRequest().body("Can't find role to edit!");
		
		Integer max = refRole.getMax();
		Integer tuple = refRole.getTuple();
		
		int count = refRole.getCount();
		
		count += (tuple * operation);
		
		if (count > max)
			 return ResponseEntity.badRequest().body("Role count exceeds max possible value (" + max + ")");
		else if (count <= 0)
			count = 0;
		
		refRole.setCount(count);
		
		session.saveOrUpdate(refRole);

		return ResponseEntity.ok(null);
	
	}

	/**
	 * 
	 *
	 * @author Sam Liew 20 Jan 2023 11:52:35 AM
	 */
	public ResponseEntity all() {
		
		List<RefRoles> refRoles = session.createQuery("FROM RefRoles", RefRoles.class).getResultList();
		
		List<Map> results = new ArrayList();
		
		int totalRoles = 0;
		for (int i = 0; i < refRoles.size(); i++) {
			RefRoles x = refRoles.get(i);
			
			totalRoles += x.getCount();
			results.add(new HashMap() {{
				put("refRoleId", x.getRefRoleId());
				put("roleName", x.getRoleName());
				put("count", x.getCount());
				put("color", x.getColor());
			}});
		}
		
		int totalPlayers = session.createQuery("FROM Players").getResultList().size();

		
		Map result = new HashMap();
		result.put("totalPlayers", totalPlayers);
		result.put("totalRoles", totalRoles);
		result.put("results", results);
		
		return ResponseEntity.ok(result);
	}	
}


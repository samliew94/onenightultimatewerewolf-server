/**
 * 
 *
 * @author Sam Liew 2 Jan 2023 11:22:07 PM
 */
package com.init;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.models.RefRoles;

/**
 * @author Sam Liew 2 Jan 2023 11:22:07 PM
 *
 */
@Component
@Transactional
@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class Init {

	private Session session;
	
	private final String good = "6495ED";
	private final String bad = "FA5F55";

	@Autowired
	public void setSession(EntityManager entityManager) {
		session = entityManager.unwrap(Session.class);
	}
	
	/**
	 * 
	 *
	 * @author Sam Liew 2 Jan 2023 11:23:03 PM
	 */
	public void execute() throws Exception
	{
		refRoles();
		System.out.println("Completed Init");
	}

	/**
	 * 
	 *
	 * @author Sam Liew 2 Jan 2023 11:23:39 PM
	 * @param session 
	 */
	private void refRoles() {
		
		RefRoles werewolf = new RefRoles();
		werewolf.setMax(2);;
		werewolf.setCount(2);
		werewolf.setRoleName("werewolf");
		werewolf.setColor(bad);
		session.saveOrUpdate(werewolf);
		
		RefRoles seer = new RefRoles();
		seer.setCount(1);
		seer.setRoleName("seer");
		seer.setColor(good);
		session.saveOrUpdate(seer);
		
		RefRoles robber = new RefRoles();
		robber.setCount(1);
		robber.setRoleName("robber");
		robber.setColor(good);
		session.saveOrUpdate(robber);
		
		RefRoles troublemaker = new RefRoles();
		troublemaker.setCount(1);
		troublemaker.setRoleName("troublemaker");
		troublemaker.setColor(good);
		session.saveOrUpdate(troublemaker);
		
		RefRoles villager = new RefRoles();
		villager.setCount(1);
		villager.setMax(10);
		villager.setRoleName("villager");
		villager.setColor(good);
		session.saveOrUpdate(villager);
		
		RefRoles tanner = new RefRoles();
		tanner.setRoleName("tanner");
		tanner.setColor("AEF359");
		session.saveOrUpdate(tanner);
		
		RefRoles drunk = new RefRoles();
		drunk.setRoleName("drunk");
		drunk.setColor(good);
		session.saveOrUpdate(drunk);
		
		RefRoles hunter = new RefRoles();
		hunter.setRoleName("hunter");
		hunter.setColor(good);
		session.saveOrUpdate(hunter);		
		
		RefRoles mason = new RefRoles();
		mason.setTuple(2);
		mason.setMax(2);
		mason.setRoleName("mason");
		mason.setColor(good);
		session.saveOrUpdate(mason);

		RefRoles minion = new RefRoles();
		minion.setRoleName("minion");
		minion.setColor(bad);
		session.saveOrUpdate(minion);
		
		int totalCount = 0;
		List<RefRoles> refRoles = session.createQuery("FROM RefRoles rr WHERE rr.count > 0",RefRoles.class).getResultList();
		
		for (int i = 0; i < refRoles.size(); i++) {
			RefRoles x = refRoles.get(i);
			System.out.println(x.getRoleName() + " = " + x.getCount());
			totalCount += x.getCount(); 			
		}
				
		System.out.println("Total Roles = " + totalCount);
		
	}

}


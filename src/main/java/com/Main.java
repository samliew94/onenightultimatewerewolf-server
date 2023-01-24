/**
 * 
 *
 * @author Sam Liew 27 Dec 2022 11:11:42 AM
 */
package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.SpringVersion;

import com.init.Init;


/**
 * @author Sam Liew 27 Dec 2022 11:12:07 AM
 *
 */
@SpringBootApplication
public class Main implements ApplicationRunner{
	
	@Lazy
	@Autowired
	private Init init; 
	
	public static void main(String[] args) {
        try {
			SpringApplication.run(Main.class, args);
			System.out.println(SpringVersion.getVersion());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
    }
	
	/**
	 * 
	 *
	 * @author Sam Liew 2 Jan 2023 11:21:33 PM
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		init.execute();
		
	}
	
	
	
}

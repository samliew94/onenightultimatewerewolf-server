/**
 * 
 *
 * @author Sam Liew 20 Dec 2022 11:04:47 PM
 */
package com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Sam Liew 20 Dec 2022 11:04:47 PM
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer
{
    
    /**
     * 
     *
     * @author Sam Liew 27 Dec 2022 11:17:51 AM
     */
	@Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**").allowedOrigins("*");
    }
	
}


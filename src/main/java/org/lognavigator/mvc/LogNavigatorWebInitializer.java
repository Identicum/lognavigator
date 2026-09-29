package org.lognavigator.mvc;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogNavigatorWebInitializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogNavigatorWebInitializer.class);

	private static final String APP_VERSION_ATTRIBUTE_NAME = "appVersion";

	@Autowired
	private ServletContext servletContext;

	@Value("${Implementation-Version}")
	private String appVersion;
	
	
	@PostConstruct
	public void initWebAppConfig() {
		LOGGER.info("Starting LogNavigator version {}.", appVersion);
		servletContext.setAttribute(APP_VERSION_ATTRIBUTE_NAME, appVersion);
	}
}

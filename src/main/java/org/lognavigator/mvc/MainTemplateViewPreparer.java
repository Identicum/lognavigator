package org.lognavigator.mvc;

import static org.lognavigator.util.Constants.LOG_ACCESS_CONFIG_IDS_BY_DISPLAY_GROUP_KEY;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import jakarta.servlet.http.HttpServletRequest;

import org.lognavigator.bean.LogAccessConfig;
import org.lognavigator.exception.ConfigException;
import org.lognavigator.service.AuthorizationService;
import org.lognavigator.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * View preparer launched just before main template display, 
 * which loads authorized log access configs for current user and bind it as a request attribute
 * so that main template can use it in log-access-configs combobox
 */
@Component
public class MainTemplateViewPreparer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainTemplateViewPreparer.class);

	@Autowired
	private ConfigService configService;

	@Autowired
	private AuthorizationService authorizationService;

	
	public void prepare(HttpServletRequest request) {
		
		try {
			// Get authorized log access configs for current user 
			Authentication authorizedUser = SecurityContextHolder.getContext().getAuthentication();
			Set<LogAccessConfig> allLogAccessConfigs = configService.getLogAccessConfigs();
			Set<LogAccessConfig> authorizedLogAccessConfigs = authorizationService.getAuthorizedLogAccessConfigs(allLogAccessConfigs, authorizedUser);
			
			// Create map <displayGroup> -> <logAccessConfig>
			Map<String, Set<LogAccessConfig>> logAccessConfigsMap = new TreeMap<String, Set<LogAccessConfig>>();
			for (LogAccessConfig logAccessConfig : authorizedLogAccessConfigs) {
				String displayGroup = logAccessConfig.getDisplayGroup() != null ? logAccessConfig.getDisplayGroup() : "";
				Set<LogAccessConfig> logAccessConfigIds = logAccessConfigsMap.get(displayGroup);
				if (logAccessConfigIds == null) {
					logAccessConfigIds = new TreeSet<LogAccessConfig>();
					logAccessConfigsMap.put(displayGroup, logAccessConfigIds);
				}
				logAccessConfigIds.add(logAccessConfig);
			}
			
			// Inject logAccessConfigIds map into request scope
			request.setAttribute(LOG_ACCESS_CONFIG_IDS_BY_DISPLAY_GROUP_KEY, logAccessConfigsMap);
		}
		catch (ConfigException e) {
			LOGGER.error("Error while loading configuration", e);
		}
	}

}

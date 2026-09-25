package org.lognavigator.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * View resolver which renders every view inside main template JSP.
 * View name is exposed as 'viewName' request attribute, so that main template includes '/WEB-INF/jsp/{viewName}.jsp' as body.
 * 'redirect:' and 'forward:' prefixes are supported.
 */
@Component
public class MainTemplateViewResolver extends UrlBasedViewResolver {

	private static final String MAIN_TEMPLATE_URL = "/WEB-INF/jsp/main-template.jsp";
	private static final String VIEW_NAME_ATTRIBUTE_NAME = "viewName";

	@Autowired
	private MainTemplateViewPreparer viewPreparer;

	public MainTemplateViewResolver() {
		setViewClass(MainTemplateView.class);
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		MainTemplateView view = (MainTemplateView) super.buildView(viewName);
		view.setUrl(MAIN_TEMPLATE_URL);
		view.addStaticAttribute(VIEW_NAME_ATTRIBUTE_NAME, viewName);
		view.setViewPreparer(viewPreparer);
		return view;
	}

}

package org.lognavigator.mvc;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.InternalResourceView;

/**
 * View which renders main template JSP, after main template view preparer is executed
 */
public class MainTemplateView extends InternalResourceView {

	private MainTemplateViewPreparer viewPreparer;

	public void setViewPreparer(MainTemplateViewPreparer viewPreparer) {
		this.viewPreparer = viewPreparer;
	}

	@Override
	protected void exposeHelpers(HttpServletRequest request) throws Exception {
		super.exposeHelpers(request);
		if (viewPreparer != null) {
			viewPreparer.prepare(request);
		}
	}

}

package org.eclipselabs.tapiji.translator.rap.views.widgets.filter;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipselabs.tapiji.translator.rap.model.Term;
import org.eclipselabs.tapiji.translator.rap.model.Translation;


public class ExactMatcher extends ViewerFilter {

	protected final StructuredViewer viewer;
	protected String pattern = "";
	protected StringMatcher matcher;
	
	public ExactMatcher (StructuredViewer viewer) {
		this.viewer = viewer;
	}
	
	public String getPattern () {
		return pattern;
	}
	
	public void setPattern (String p) {
		boolean filtering = matcher != null;
		if (p != null && p.trim().length() > 0) {
			pattern = p;
			matcher = new StringMatcher ("*" + pattern + "*", true, false);
			if (!filtering)
				viewer.addFilter(this);
			else
				viewer.refresh();
		} else {
			pattern = "";
			matcher = null;
			if (filtering) {
				viewer.removeFilter(this);
			}
		}
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		Term term = (Term) element;
		FilterInfo filterInfo = new FilterInfo();
		boolean selected = false;
		
		// Iterate translations
		for (Translation translation : term.getAllTranslations()) {
			String value = translation.value;
			String locale = translation.id;
			if (matcher.match(value)) {
				filterInfo.addFoundInTranslation(locale);
				filterInfo.addSimilarity(locale, 1d);
				int start = -1;
				while ((start = value.toLowerCase().indexOf(pattern.toLowerCase(), start+1)) >= 0) {
					//TODO: filterInfo.addFoundInTranslationRange(locale, start, pattern.length());
				}
				selected = true;
			}
		} 
		
		term.setInfo(filterInfo);
		return selected;
	}


}
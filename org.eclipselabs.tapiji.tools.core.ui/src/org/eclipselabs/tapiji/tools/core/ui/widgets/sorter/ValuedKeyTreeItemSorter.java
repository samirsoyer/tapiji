package org.eclipselabs.tapiji.tools.core.ui.widgets.sorter;

import java.util.Locale;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipselabs.tapiji.tools.core.model.view.SortInfo;
import org.eclipselabs.tapiji.translator.rbe.babel.bundle.IValuedKeyTreeNode;

public class ValuedKeyTreeItemSorter extends ViewerSorter {

	private StructuredViewer 	viewer;
	private SortInfo 			sortInfo;
	
	public ValuedKeyTreeItemSorter (StructuredViewer viewer,
									SortInfo sortInfo) {
		this.viewer = viewer;
		this.sortInfo = sortInfo;
	}

	public StructuredViewer getViewer() {
		return viewer;
	}

	public void setViewer(StructuredViewer viewer) {
		this.viewer = viewer;
	}

	public SortInfo getSortInfo() {
		return sortInfo;
	}

	public void setSortInfo(SortInfo sortInfo) {
		this.sortInfo = sortInfo;
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		try {
			if (!(e1 instanceof IValuedKeyTreeNode && e2 instanceof IValuedKeyTreeNode))
				return super.compare(viewer, e1, e2);
			IValuedKeyTreeNode comp1 = (IValuedKeyTreeNode) e1;
			IValuedKeyTreeNode comp2 = (IValuedKeyTreeNode) e2;
			
			int result = 0;
			
			if (sortInfo == null)
				return 0;
			
			if (sortInfo.getColIdx() == 0)
				result = comp1.getMessageKey().compareTo(comp2.getMessageKey());
			else {
				Locale loc = sortInfo.getVisibleLocales().get(sortInfo.getColIdx()-1);
				result = (comp1.getValue(loc) == null ? "" : comp1.getValue(loc))
					.compareTo((comp2.getValue(loc) == null ? "" : comp2.getValue(loc)));
			}
			
			return result * (sortInfo.isDESC() ? -1 : 1);
		} catch (Exception e) {
			return 0;
		}
	}
	
}

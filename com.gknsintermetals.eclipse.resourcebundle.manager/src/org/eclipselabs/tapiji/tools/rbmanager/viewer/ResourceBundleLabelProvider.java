package org.eclipselabs.tapiji.tools.rbmanager.viewer;

import java.util.Locale;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.IDescriptionProvider;
import org.eclipselabs.tapiji.tools.core.model.manager.ResourceBundleManager;
import org.eclipselabs.tapiji.tools.core.util.RBFileUtils;
import org.eclipselabs.tapiji.tools.rbmanager.ImageUtils;
import org.eclipselabs.tapiji.tools.rbmanager.model.VirtualContainer;
import org.eclipselabs.tapiji.tools.rbmanager.model.VirtualContentManager;
import org.eclipselabs.tapiji.tools.rbmanager.model.VirtualProject;
import org.eclipselabs.tapiji.tools.rbmanager.model.VirtualResourceBundle;



public class ResourceBundleLabelProvider extends LabelProvider implements ILabelProvider, IDescriptionProvider{
	VirtualContentManager vcManager;
	
	public ResourceBundleLabelProvider(){
		super();
		vcManager = VirtualContentManager.getVirtualContentManager();
	}
	
	@Override
	public Image getImage(Object element) {
		Image returnImage = null;
		if (element instanceof IProject){	
			VirtualProject p = (VirtualProject) vcManager.getContainer((IProject) element);
			if (p!=null && p.isFragment())
				return returnImage = ImageUtils.getBaseImage(ImageUtils.FRAGMENT_PROJECT_IMAGE);
			else
				returnImage = PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_PROJECT);
		}
		if ((element instanceof IContainer)&&(returnImage ==  null))
			returnImage = PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_FOLDER);
		if (element instanceof VirtualResourceBundle)
			returnImage = ImageUtils.getBaseImage(ImageUtils.RESOURCEBUNDLE_IMAGE);
		if (element instanceof IFile){
			if (RBFileUtils.isResourceBundleFile((IFile)element)){
				Locale l = RBFileUtils.getLocale((IFile)element);
				returnImage = ImageUtils.getLocalIcon(l);
				
				VirtualProject p = ((VirtualProject)vcManager.getContainer(((IFile) element).getProject()));
				if (p!=null && p.isFragment())
					returnImage = ImageUtils.getImageWithFragment(returnImage);
			}
		}
		
		if (returnImage != null) {
			if (checkMarkers(element))
				//Add a Warning Image
				returnImage = ImageUtils.getImageWithWarning(returnImage);
		}
		return returnImage;
	}
	
	@Override
	public String getText(Object element) {
		
		StringBuilder text = new StringBuilder();
		if (element instanceof IContainer) {
			IContainer container = (IContainer) element;
			text.append(container.getName());
			
			if (element instanceof IProject){
				VirtualContainer vproject =  vcManager.getContainer((IProject) element);
//				if (vproject != null && vproject instanceof VirtualFragment) text.append("�");
			}
			
			VirtualContainer vContainer = vcManager.getContainer(container);
			if (vContainer != null && vContainer.getRbCount() != 0)
				text.append("_["+vContainer.getRbCount()+"]");
			
		}
		if (element instanceof VirtualResourceBundle){
			text.append(((VirtualResourceBundle)element).getName());
		}
		if (element instanceof IFile){
			if (RBFileUtils.isResourceBundleFile((IFile)element)){
				Locale locale = RBFileUtils.getLocale((IFile)element);
				text.append("     ");
				if (!locale.toString().equals("")) text.append(locale);
				else text.append("default");
				
				VirtualProject vproject = (VirtualProject) vcManager.getContainer(((IFile) element).getProject());
				if (vproject!= null && vproject.isFragment()) text.append("�");
			}
		}
		if(element instanceof String){
			text.append(element);
		}
		return text.toString();
	}
	
	@Override
	public String getDescription(Object anElement) {
		if (anElement instanceof IResource)
			return ((IResource)anElement).getName();
		if (anElement instanceof VirtualResourceBundle)
			return ((VirtualResourceBundle)anElement).getName();
		return null;
	}
	
	private boolean checkMarkers(Object element){
		if (element instanceof IResource)
			if (RBFileUtils.hasResourceBundleMarker((IResource)element)) return true;
		
		if (element instanceof VirtualResourceBundle){
			ResourceBundleManager rbmanager = ((VirtualResourceBundle)element).getResourceBundleManager();
			String id = ((VirtualResourceBundle)element).getResourceBundleId();
			for (IResource r : rbmanager.getResourceBundles(id)){
				if (RBFileUtils.hasResourceBundleMarker(r)) return true;
			}
		}
		
		return false;
	}

}
package com.gknsintermetals.eclipse.resourcebundle.manager.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipselabs.tapiji.tools.core.model.manager.ResourceBundleManager;

public class VirtualResourceBundle{
	private String  resourcebundlename;
	private String  resourcebundleId;
	private ResourceBundleManager rbmanager;

	
	public VirtualResourceBundle(String rbname, String rbId, ResourceBundleManager rbmanager) {
		this.rbmanager=rbmanager;
		resourcebundlename=rbname;
		resourcebundleId=rbId;
	}

	public ResourceBundleManager getResourceBundleManager() {
		return rbmanager;
	}

	public String getResourceBundleId(){
		return resourcebundleId;
	}
	
	
	@Override
	public String toString(){
		return resourcebundleId;
	}

	public IPath getFullPath() {
		return rbmanager.getRandomFile(resourcebundleId).getFullPath(); 
	}


	public String getName() {
		return resourcebundlename;
	}

	public Collection<IResource> getFiles() {
		return rbmanager.getResourceBundles(resourcebundleId);
	}

	public IFile getRandomFile(){
		return rbmanager.getRandomFile(resourcebundleId);
	}
}
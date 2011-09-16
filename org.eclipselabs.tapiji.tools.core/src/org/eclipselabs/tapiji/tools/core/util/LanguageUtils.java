package org.eclipselabs.tapiji.tools.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipselabs.tapiji.tools.core.Logger;
import org.eclipselabs.tapiji.tools.core.model.manager.ResourceBundleManager;

import com.essiembre.eclipse.rbe.api.PropertiesGenerator;


public class LanguageUtils {
	private static final String INITIALISATION_STRING = PropertiesGenerator.GENERATED_BY;
	
	
	private static IFile createFile(IContainer container, String fileName, IProgressMonitor monitor) throws CoreException, IOException {
		if (!container.exists()){
			if (container instanceof IFolder){
				((IFolder) container).create(false, false, monitor);
			}
		}
		
		IFile file = container.getFile(new Path(fileName));
		if (!file.exists()){
			InputStream s = new StringBufferInputStream(INITIALISATION_STRING);
			file.create(s, true, monitor);
			s.close();
		}
		
		return file;
    }
	
	/**
	 * Checks if ResourceBundle provides a given locale.
	 * If the locale is not provided, creates a new properties-file with the resourcebundle-basename
	 * and given the locale.
	 * @param project
	 * @param rbId
	 * @param locale
	 */
	public static void addLanguageToResourceBundle(IProject project, String rbId, final Locale locale){
		ResourceBundleManager rbManager = ResourceBundleManager.getManager(project);
		
		if (rbManager.getProvidedLocales(rbId).contains(locale)) return;
		
//		Iterator<IResource> it = rbManager.getResourceBundles(rbId).iterator();
//		IResource f = null;
//        while (it.hasNext() && !(f=it.next()).getProject().equals(project))		/*?*/
//        	/*no statement*/;
//        
//		final IResource file = f;
//		final IContainer c = f.getParent();
        
		final IResource file = rbManager.getRandomFile(rbId);
		final IContainer c = ResourceUtils.getCorrespondingFolders(file.getParent(), project);
		
        new Job("create new propertfile") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					String newFilename = ResourceBundleManager.getResourceBundleName(file);
					if (locale.getLanguage() != null && !locale.getLanguage().equalsIgnoreCase("[default]") && !locale.getLanguage().equals(""))
						newFilename += "_"+locale.getLanguage();
					if (locale.getCountry() != null && !locale.getCountry().equals(""))
						newFilename += "_"+locale.getCountry();
					if (locale.getVariant() != null && !locale.getCountry().equals(""))
						newFilename += "_"+locale.getVariant();
					newFilename += ".properties";
					
					createFile(c, newFilename, monitor);
				} catch (CoreException e) {
					Logger.logError(e);
				} catch (IOException e) {
					Logger.logError(e);
				}
            	monitor.done();
				return Status.OK_STATUS;
			}
		}.schedule();
	}
	
	
	/**
	 * Adds new properties-file for a given locale for all ResourceBundle of a project.
	 * If a ResourceBundle just contains the language, happe ns nothing.
	 * @param project
	 * @param locale
	 */
	public static void addLanguageToProject(IProject project, Locale locale){
		ResourceBundleManager rbManager = ResourceBundleManager.getManager(project);
		
		//Audit if all resourecbundles provide this locale. if not - add new file
		for (String rbId : rbManager.getResourceBundleIdentifiers()){
			addLanguageToResourceBundle(project, rbId, locale);
		}
	}
	
	
	private static void deleteFile(IFile file, boolean force, IProgressMonitor monitor) throws CoreException{
		EditorUtils.deleteAuditMarkersForResource(file);
		file.delete(force, monitor);
	}
	
	/**
	 * 
	 * @param project
	 * @param rbId
	 * @param locale
	 */
	public static void removeFileFromResourceBundle(IProject project, String rbId, Locale locale) {
		ResourceBundleManager rbManager = ResourceBundleManager.getManager(project);
		
		if (!rbManager.getProvidedLocales(rbId).contains(locale)) return;
		
		final IFile file = rbManager.getResourceBundleFile(rbId, locale);
		
		new Job("remove properties-file") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					deleteFile(file, false, monitor);
				} catch (CoreException e) {
					Logger.logError(e);
				}
				return Status.OK_STATUS;
			}
		 }.schedule();
	}
	
	/**
	 * 
	 * @param rbManager
	 * @param locale
	 * @return
	 */
	public static void removeLanguageFromProject(IProject project, Locale locale){
		ResourceBundleManager rbManager = ResourceBundleManager.getManager(project);
		
		for (String rbId : rbManager.getResourceBundleIdentifiers()){
			removeFileFromResourceBundle(project, rbId, locale);
		}

	}

	
}
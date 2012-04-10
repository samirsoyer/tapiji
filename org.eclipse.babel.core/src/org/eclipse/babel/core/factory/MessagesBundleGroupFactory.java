package org.eclipse.babel.core.factory;

import java.io.File;

import org.eclipse.babel.core.configuration.ConfigurationManager;
import org.eclipse.babel.core.message.MessagesBundleGroup;
import org.eclipse.babel.core.message.strategy.PropertiesFileGroupStrategy;
import org.eclipse.babel.tapiji.translator.rbe.babel.bundle.IMessagesBundleGroup;
import org.eclipse.core.resources.IResource;

/**
 * Factory class for creating a {@link MessagesBundleGroup} with a {@link PropertiesFileGroupStrategy}.
 * This is in use when we work with TapiJI only and not with <code>EclipsePropertiesEditorResource</code>.
 * <br><br>
 * 
 * @author Alexej Strelzow
 */
public class MessagesBundleGroupFactory {
	
    public static IMessagesBundleGroup createBundleGroup(IResource resource) {
    	
            File ioFile = new File(resource.getRawLocation().toFile().getPath());
            
            return new MessagesBundleGroup(new PropertiesFileGroupStrategy(ioFile, 
            		ConfigurationManager.getInstance().getSerializerConfig(), 
            		ConfigurationManager.getInstance().getDeserializerConfig()));
    }
}

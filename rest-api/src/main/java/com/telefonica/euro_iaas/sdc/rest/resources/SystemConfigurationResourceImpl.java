package com.telefonica.euro_iaas.sdc.rest.resources;

import java.util.Properties;

import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.dto.Attributes;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.multipart.BodyPartEntity;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;

/**
 * Default SystemConfigurationResource implementation
 * 
 * @author Sergio Arroyo
 * 
 */
@Path("/crm/configuration/properties")
@Component
@Scope("request")
public class SystemConfigurationResourceImpl implements
		SystemConfigurationResource {

	@InjectParam("systemPropertiesProvider")
	SystemPropertiesProvider propertiesProvider;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attributes findAttributes() {
		Properties configuration = propertiesProvider.loadProperties();
		Attributes attributes = new Attributes();
		for (Object key : configuration.keySet()) {
			String strKey = (String) key;
			attributes.add(new Attribute(strKey, configuration
					.getProperty(strKey)));
		}
		return attributes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAttributes(Attributes attributes) {
		Properties configuration = new Properties();
		for (Attribute attribute : attributes) {
			configuration.put(attribute.getKey(), attribute.getValue());
		}
		propertiesProvider.setProperties(configuration);
	}

}
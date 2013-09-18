package com.telefonica.euro_iaas.sdc.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.MultiPart;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.dto.ProductReleaseDto;
 

public class ProductResourceImplTest {

	 @Test
	 public void testInsert() throws Exception {
		 /*
		 
		 final String BASE_URI = "http://localhost:8080/sdc/rest/catalog/product";
		 
		 Client c = Client.create();
		 WebResource service = c.resource(BASE_URI);
		 
		 ProductReleaseDto productReleaseDto = new ProductReleaseDto();
		 productReleaseDto.setProductName("def");
		 productReleaseDto.setProductDescription("DEF 0.1.0 description");
		 productReleaseDto.setVersion("0.1.0");
		 productReleaseDto.setReleaseNotes("prueba ReelaseNotes");
		 
		 OS os = new OS("Debian def 5.2", "Debian def 5.2","5.2");
		 List<OS> supportedOS =  Arrays.asList(os);
		 productReleaseDto.setSupportedOS(supportedOS);
		 
			 
		 Attribute privateAttribute = new Attribute("ssl_port", 
			"8443", "The ssl listen port");
		 Attribute privateAttributeII = new Attribute("port", 
			 "8080", "The listen port");
		 
		 List<Attribute> privateAttributes = 
			 Arrays.asList(privateAttribute, privateAttributeII);
		 productReleaseDto.setPrivateAttributes(privateAttributes);
			 
		 //Second and third Multipart
		 File recipes = 
			 	new File ("E:\\TID\\desarrollo\\doc\\files\\tomcat.tar");
		 File installable = 
			 	//new File ("E:\\TID\\desarrollo\\doc\\files\\install_chef_client_package.sh");
		 		new File ("E:\\TID\\desarrollo\\doc\\files\\sdc-0.4.0.tar");
		 
		 byte[] bytesRecipes = getByteFromnFile(recipes);
		 byte[] bytesInstallable = getByteFromnFile (installable);
				 
		 // Construct a MultiPart with three body parts
		 MultiPart multiPart = new MultiPart().
		 	bodyPart(new BodyPart(productReleaseDto, MediaType.APPLICATION_XML_TYPE)).
		    bodyPart(new BodyPart(bytesRecipes, MediaType.APPLICATION_OCTET_STREAM_TYPE)).
		    bodyPart(new BodyPart(bytesInstallable, MediaType.APPLICATION_OCTET_STREAM_TYPE));
		 
		 // POST the request
		 ClientResponse response = service.path("/").
		 	type("multipart/mixed").post(ClientResponse.class, multiPart);
		 System.out.println("Response Status : " + response.getEntity(String.class));
		 */
	 }

	 @Test
	 public void testDelete() throws Exception {
	 /*	 
		 final String BASE_URI = "http://localhost:8080/sdc/rest/catalog/product";
		 		 
		 Client c = Client.create();
		 WebResource service = c.resource(BASE_URI + "/def/release/0.1.0");
		 
		 System.out.println("Before invoking service");	 
			 
		// Delete the resource
		 service.path("/").
		 	type(MediaType.APPLICATION_XML).delete();
	*/
	  }
	 
	 private  byte[] getByteFromnFile (File file) 
	 	throws FileNotFoundException, IOException
	 {
		 InputStream is = new FileInputStream(file);

		    // Get the size of the file
		    long length = file.length();

		    // You cannot create an array using a long type.
		    // It needs to be an int type.
		    // Before converting to an int type, check
		    // to ensure that file is not larger than Integer.MAX_VALUE.
		    if (length > Integer.MAX_VALUE) {
		        // File is too large
		    }

		    // Create the byte array to hold the data
		    byte[] bytes = new byte[(int)length];

		    // Read in the bytes
		    int offset = 0;
		    int numRead = 0;
		    while (offset < bytes.length
		           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
		        offset += numRead;
		    }

		    // Ensure all the bytes have been read in
		    if (offset < bytes.length) {
		        throw new IOException("Could not completely read file "+file.getName());
		    }

		    // Close the input stream and return bytes
		    is.close();
		    return bytes;
	 }
}
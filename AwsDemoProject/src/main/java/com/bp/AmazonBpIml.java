package com.bp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.dao.AmazonDao;
import com.property.PropertyHolder;
import com.utils.SystemVariables;

@Component
public class AmazonBpIml implements AmazonBp{
	
	private static Logger LOGGER = Logger
			.getLogger(AmazonBpIml.class);
	@Autowired
	SystemVariables systemVar;
	
	@Autowired
	AmazonDao amazonDao;
	
	
	@Autowired
	PropertyHolder propertyHolder;
	
	public String saveJson() throws Exception,AmazonClientException {
		// TODO Auto-generated method stub
		String s3PropertiesUrl=systemVar.getS3Url();
		 String outputText =null;
		 S3ObjectInputStream  s3ObjectInputStream=null;
		 if (StringUtils.isNotBlank(s3PropertiesUrl)) {
	            // S3 properties file path is specified
	            try {
	            	 AmazonS3 s3Client = new AmazonS3Client();
	                GetObjectRequest getObjectRequest = toS3GetObjectRequestFromS3Url(s3PropertiesUrl);
	                S3Object s3Object = s3Client.getObject(getObjectRequest);
	                if(s3Object!=null){
	                	s3ObjectInputStream=s3Object.getObjectContent();
	                	if(s3ObjectInputStream!=null){
	    	                System.out.println("s3ObjectInputStream is not null");
	    	                outputText = IOUtils.toString(s3ObjectInputStream);
	                		amazonDao.saveJson(outputText);
	                	}
	                }
	                
	                
	            } catch (URISyntaxException e) {

	                throw new Exception(
	                        "Incorrect S3 properties URL specified. Make sure the property "
	                                + "\"s3.properties.url\" points to a "
	                                + "properties file in S3. If you do not want to load secure properties "
	                                + "then do NOT specify \"s3" + ".properties.url\" or keep it empty");
	            } catch (IOException e) {
	                throw new Exception("Error loading S3 properties file from " + s3PropertiesUrl + ".");
	            } catch (AmazonServiceException ase) {
	                // Throw request reached to S3 but was rejected for some reason.

	               throw new Exception("Error loading S3 properties file from " + s3PropertiesUrl + ". The "
	                                + "request made it to S3 but was rejected for some reason.");
	            } catch (AmazonClientException ace) {
	                throw new Exception("Error loading S3 properties file from " + s3PropertiesUrl + ".");
	            }
	            finally{
	            	if(s3ObjectInputStream!=null)
	            	s3ObjectInputStream.close();
	            }
	        }
		 return "object "+outputText +"saved succesfully";
	}
	 protected GetObjectRequest toS3GetObjectRequestFromS3Url(String s3Url) throws URISyntaxException, Exception {
	        String bucketName = null;
	        String key = null;
	        URI uri = new URI(s3Url);
	        //https://s3.amazonaws.com/testamazons3bucket/sample.json
	        if (s3Url.startsWith("s3")) {
	            // The s3:// urls have the format
	            // s3://bucket-name/s3-object-path

	            // Parse uri to extract bucket name and s3 object path to create
	            // GetObjectRequest
	            bucketName = uri.getHost();
	            key = StringUtils.substringAfter(uri.getPath(), "/");
	        } else if (s3Url.startsWith("http")) {
	            // The http:// or https:// s3 urls have the format
	            // http(s)://s3.amazonaws.com/bucket-name/s3-object-path

	            // Parse uri to extract bucket name and s3 object path to create
	            // GetObjectRequest
	            // uri.getPath() = /bucket-name/s3-object-path
	            //
	            bucketName = StringUtils.substringBefore(StringUtils.substringAfter(uri.getPath(), "/"), "/");
	            key = StringUtils.substringAfter(StringUtils.substringAfter(uri.getPath(), "/"), "/");
	        }

	        if (StringUtils.isBlank(bucketName)) {
	           /* throw new PaloginException(
	                    ErrorCode.EMPTY_BUCKET_NAME,
	                    new Exception(
	                            "Could not derive S3 bucket name from the URL specified by \"s3.properties.url\" property. Make sure the property \"s3.properties.url\" points to a properties file in S3. If you do not want to load properties from S3 then do NOT specify \"s3.properties.url\" or keep it empty."));*/
	        }
	        if (StringUtils.isBlank(key)) {
	           /* throw new PaloginException(
	                    ErrorCode.BLANK_KEY,
	                    new Exception(
	                            "Could not derive S3 object key from the URL specified by \"s3.properties.url\" property. Make sure the property \"s3.properties.url\" points to a properties file in S3. If you do not want to load properties from S3 then do NOT specify \"s3.properties.url\" or keep it empty."));*/
	        }
	        return new GetObjectRequest(bucketName, key);
	    }

	
}

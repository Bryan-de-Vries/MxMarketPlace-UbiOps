// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package awsservices.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import awsservices.impl.AWSClients;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

public class S3_DeleteObject extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject __credentials;
	private awsservices.proxies.Credentials credentials;
	private java.lang.String bucket;
	private java.lang.String key;

	public S3_DeleteObject(IContext context, IMendixObject credentials, java.lang.String bucket, java.lang.String key)
	{
		super(context);
		this.__credentials = credentials;
		this.bucket = bucket;
		this.key = key;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		this.credentials = __credentials == null ? null : awsservices.proxies.Credentials.initialize(getContext(), __credentials);

		// BEGIN USER CODE
		S3Client client = AWSClients.getS3Client(getContext(), credentials);
		DeleteObjectRequest req = DeleteObjectRequest.builder()
				.bucket(bucket)
				.key(key)
				.build();
		client.deleteObject(req);
		return true;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "S3_DeleteObject";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
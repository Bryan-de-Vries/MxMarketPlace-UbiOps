// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package awsservices.actions;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import awsservices.impl.AWSClients;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public class S3_ListBuckets extends CustomJavaAction<java.util.List<IMendixObject>>
{
	private IMendixObject __credentials;
	private awsservices.proxies.Credentials credentials;

	public S3_ListBuckets(IContext context, IMendixObject credentials)
	{
		super(context);
		this.__credentials = credentials;
	}

	@java.lang.Override
	public java.util.List<IMendixObject> executeAction() throws Exception
	{
		this.credentials = __credentials == null ? null : awsservices.proxies.Credentials.initialize(getContext(), __credentials);

		// BEGIN USER CODE
		List<IMendixObject> result = new LinkedList<>();
		S3Client client = AWSClients.getS3Client(getContext(), credentials);
		ListBucketsResponse buckets = client.listBuckets();
		for (Bucket bucket : buckets.buckets()) {
			awsservices.proxies.Bucket mxBucket = new awsservices.proxies.Bucket(getContext());
			mxBucket.setName(bucket.name());
			mxBucket.setCreationDate(new Date(bucket.creationDate().toEpochMilli()));
			result.add(mxBucket.getMendixObject());
		}
		return result;		
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "S3_ListBuckets";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}

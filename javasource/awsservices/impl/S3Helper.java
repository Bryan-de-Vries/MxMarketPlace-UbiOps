package awsservices.impl;

import java.util.Date;

import com.mendix.systemwideinterfaces.core.IContext;

import awsservices.proxies.S3Object;

public class S3Helper {
	public static S3Object getMxS3Object(IContext context, software.amazon.awssdk.services.s3.model.S3Object obj, String bucket) {
		S3Object mxObj = new S3Object(context);
		mxObj.setKey(obj.key());
		mxObj.setLastChanged(new Date(obj.lastModified().toEpochMilli()));
		mxObj.setStorageClass(obj.storageClass().toString());
		mxObj.setSize(obj.size());
		mxObj.setBucket(bucket);
		return mxObj;
	}
	
	public static String getFilenameFromKey(String key) {
		int lastIndex = key.lastIndexOf('/');
		if (lastIndex != -1) {
			return key.substring(key.lastIndexOf('/'));
		} else {
			return key;
		}
	}
}

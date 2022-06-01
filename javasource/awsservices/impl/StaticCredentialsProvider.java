package awsservices.impl;

import java.time.Instant;

import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;

import awsservices.proxies.Credentials;
import awsservices.proxies.StaticCredentials;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class StaticCredentialsProvider extends CredentialsProvider {

	public StaticCredentialsProvider(IContext context, Credentials credentials) {
		super(context, credentials);
	}

	@Override
	public AwsCredentialsProvider getAwsCredentialsProvider() throws CoreException {
		StaticCredentials staticCreds = credentials.getCredentials_StaticCredentials();
		
		AwsBasicCredentials awsBasicCreds = AwsBasicCredentials.create(
				staticCreds.getAccessKey(), 
				encryption.proxies.microflows.Microflows.decrypt(context, staticCreds.getSecretKey()));
		
		return software.amazon.awssdk.auth.credentials.StaticCredentialsProvider.create(awsBasicCreds);
	}

	@Override
	public Instant getExpiration() throws CoreException {
		return null;
	}

	
	
}

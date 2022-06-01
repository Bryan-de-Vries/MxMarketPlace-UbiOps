package awsservices.impl;

import java.net.URI;
import java.net.URISyntaxException;

import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;

import awsservices.impl.clientcache.ClientCache;
import awsservices.proxies.Credentials;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.StsClientBuilder;

public class AWSClients {
	
	public static synchronized SqsClient getSqsClient(IContext context, Credentials credentials) throws CoreException {
		SdkClient client = ClientCache.getClient(context, SqsClient.SERVICE_NAME, credentials);
		if (client == null) {
			AWSHelper.LOGGER.info("Creating client for " + SqsClient.SERVICE_NAME + "(" + credentials.getIdentifier() + ").");
			Region region = Region.of(credentials.getRegionName());
			CredentialsProvider credentialsProvider = getCredentialsProvider(context, credentials);
			SqsClientBuilder builder = SqsClient.builder().credentialsProvider(
					credentialsProvider.getAwsCredentialsProvider())
					.httpClientBuilder(ApacheHttpClient.builder())
					.region(region);
			
			if (credentials.getEndpoint() != null && !credentials.getEndpoint().isEmpty())
				try {
					builder.endpointOverride(new URI(credentials.getEndpoint()));
				} catch (URISyntaxException e) {
					AWSHelper.LOGGER.error("Unable to construct URI: " + e.getMessage(), e);
				}
			
			SqsClient newClient = builder.build();
			ClientCache.addClient(context, SqsClient.SERVICE_NAME, credentials, 
					credentialsProvider.getExpiration(), newClient);
			return newClient;
		} else {
			return (SqsClient) client;
		}
	}
	
	public static synchronized S3Client getS3Client(IContext context, Credentials credentials) throws CoreException {
		SdkClient client = ClientCache.getClient(context, S3Client.SERVICE_NAME, credentials);
		if (client == null) {
			AWSHelper.LOGGER.info("Creating client for " + S3Client.SERVICE_NAME + "(" + credentials.getIdentifier() + ").");
			Region region = Region.of(credentials.getRegionName());
			CredentialsProvider credentialsProvider = getCredentialsProvider(context, credentials);
			S3ClientBuilder builder = S3Client.builder().credentialsProvider(
					credentialsProvider.getAwsCredentialsProvider())
					.httpClientBuilder(ApacheHttpClient.builder())
					.region(region);
			
			if (credentials.getEndpoint() != null && !credentials.getEndpoint().isEmpty())
				try {
					builder.endpointOverride(new URI(credentials.getEndpoint()));
				} catch (URISyntaxException e) {
					AWSHelper.LOGGER.error("Unable to construct URI: " + e.getMessage(), e);
				}
			
			S3Client newClient = builder.build();
			ClientCache.addClient(context, S3Client.SERVICE_NAME, credentials, 
					credentialsProvider.getExpiration(), newClient);
			return newClient;
		} else {
			return (S3Client) client;
		}
	}
	
	public static synchronized StsClient getStsClient(IContext context, Credentials credentials) throws CoreException {
		SdkClient client = ClientCache.getClient(context, StsClient.SERVICE_NAME, credentials);
		if (client == null) {
			AWSHelper.LOGGER.info("Creating client for " + StsClient.SERVICE_NAME + "(" + credentials.getIdentifier() + ").");
			Region region = Region.of(credentials.getRegionName());
			CredentialsProvider credentialsProvider = getCredentialsProvider(context, credentials);
			StsClientBuilder builder = StsClient.builder().credentialsProvider(
					credentialsProvider.getAwsCredentialsProvider())
					.httpClientBuilder(ApacheHttpClient.builder())
					.region(region);
			
			if (credentials.getEndpoint() != null && !credentials.getEndpoint().isEmpty())
				try {
					builder.endpointOverride(new URI(credentials.getEndpoint()));
				} catch (URISyntaxException e) {
					AWSHelper.LOGGER.error("Unable to construct URI: " + e.getMessage(), e);
				}
					
			StsClient newClient = builder.build();
			ClientCache.addClient(context, StsClient.SERVICE_NAME, credentials, 
					credentialsProvider.getExpiration(), newClient);
			return newClient;
		} else {
			return (StsClient) client;
		}
	}

	public static synchronized SnsClient getSnsClient(IContext context, Credentials credentials) throws CoreException {
		SdkClient client = ClientCache.getClient(context, SnsClient.SERVICE_NAME, credentials);
		if (client == null) {
			AWSHelper.LOGGER.info("Creating client for " + SnsClient.SERVICE_NAME + "(" + credentials.getIdentifier() + ").");
			Region region = Region.of(credentials.getRegionName());
			CredentialsProvider credentialsProvider = getCredentialsProvider(context, credentials);
			SnsClientBuilder builder = SnsClient.builder().credentialsProvider(
					credentialsProvider.getAwsCredentialsProvider())
					.httpClientBuilder(ApacheHttpClient.builder())
					.region(region);
			
			if (credentials.getEndpoint() != null && !credentials.getEndpoint().isEmpty())
				try {
					builder.endpointOverride(new URI(credentials.getEndpoint()));
				} catch (URISyntaxException e) {
					AWSHelper.LOGGER.error("Unable to construct URI: " + e.getMessage(), e);
				}
			
			SnsClient newClient = builder.build();
			ClientCache.addClient(context, SnsClient.SERVICE_NAME, credentials,
					credentialsProvider.getExpiration(), newClient);
			return newClient;
		} else {
			return (SnsClient) client;
		}
	}

	private static CredentialsProvider getCredentialsProvider(IContext context, Credentials credentials) {
		switch (credentials.getProvider()) {
			case _STATIC: return new StaticCredentialsProvider(context, credentials);
			case COGNITO: return new CognitoCredentialsProvider(context, credentials);
		}

		throw new IllegalStateException("Unimplemented credentials provider:" + credentials.getProvider().getCaption());
	}
}

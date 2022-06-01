package awsservices.impl;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.thirdparty.org.json.JSONObject;

import awsservices.proxies.Credentials;
import encryption.proxies.microflows.Microflows;
import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.CognitoIdentityClient;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetCredentialsForIdentityResponse;
import software.amazon.awssdk.services.cognitoidentity.model.GetIdRequest;
import software.amazon.awssdk.services.cognitoidentity.model.GetIdResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InitiateAuthResponse;

public class CognitoCredentialsProvider extends CredentialsProvider {

	private Instant expiration;
	
	public CognitoCredentialsProvider(IContext context, Credentials credentials) {
		super(context, credentials);
	}

	@Override
	public AwsCredentialsProvider getAwsCredentialsProvider() throws CoreException {
		var region = Region.of(credentials.getRegionName());
		var cognitoCredentials = credentials.getCredentials_CognitoCredentials();

		var creds = getCredentialsThroughCognito(cognitoCredentials.getUsername(),
				Microflows.decrypt(context, cognitoCredentials.getPassword()), cognitoCredentials.getAccessKey(),
				Microflows.decrypt(context, cognitoCredentials.getSecretKey()), cognitoCredentials.getIdentityPoolId(),
				region);

		var awsCreds = AwsSessionCredentials.create(creds.accessKeyId(), creds.secretKey(), creds.sessionToken());
		expiration = creds.expiration().minusSeconds(60);
		return software.amazon.awssdk.auth.credentials.StaticCredentialsProvider.create(awsCreds);
	}

	/**
	 * Calculate the secret hash to be sent along with the authentication request.
	 *
	 * @param userPoolClientId     : The client id of the app.
	 * @param userPoolClientSecret : The secret for the userpool client id.
	 * @param userName             : The username of the user trying to
	 *                             authenticate.
	 * @return Calculated secret hash.
	 */
	private static String calculateSecretHash(String userPoolClientId, String userPoolClientSecret, String userName) {
		final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

		SecretKeySpec signingKey = new SecretKeySpec(userPoolClientSecret.getBytes(StandardCharsets.UTF_8),
				HMAC_SHA256_ALGORITHM);
		try {
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);
			mac.update(userName.getBytes(StandardCharsets.UTF_8));
			byte[] rawHmac = mac.doFinal(userPoolClientId.getBytes(StandardCharsets.UTF_8));
			return java.util.Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			throw new RuntimeException("Error while calculating ");
		}
	}

	private static String getIdToken(String username, String password, String clientId, String clientSecret,
			Region region) {
		try (var provider = CognitoIdentityProviderClient.builder().region(region)
				.credentialsProvider(AnonymousCredentialsProvider.create()).build()) {

			var secretHash = calculateSecretHash(clientId, clientSecret, username);

			var params = new HashMap<String, String>();
			params.put("USERNAME", username);
			params.put("PASSWORD", password);
			params.put("SECRET_HASH", secretHash);
			var req = InitiateAuthRequest.builder().clientId(clientId).authFlow(AuthFlowType.USER_PASSWORD_AUTH)
					.authParameters(params).build();

			final InitiateAuthResponse response = provider.initiateAuth(req);

			AWSHelper.LOGGER.info("Successfully logged in using Cognito");
			
			if (response.authenticationResult() == null) {
				throw new RuntimeException("Unable to login: " + response.toString());
			}

			return response.authenticationResult().idToken();
		}
	}

	private static software.amazon.awssdk.services.cognitoidentity.model.Credentials 
		getCredentialsThroughCognito(String username, String password, String clientId,
			String clientSecret, String identityPoolId, Region region) {

		String token = getIdToken(username, password, clientId, clientSecret, region);

		var payload = token.split("\\.")[1];
		var decoded = Base64.getDecoder().decode(payload);
		var jwtSection = new String(decoded, StandardCharsets.UTF_8);
		var jwt = new JSONObject(jwtSection);

		var providerId = jwt.getString("iss").replace("https://", "");

		try (var identity = CognitoIdentityClient.builder().region(region)
				.credentialsProvider(AnonymousCredentialsProvider.create()).build()) {

			final Map<String, String> logins = Collections.singletonMap(providerId, token);
			var idReq = GetIdRequest.builder().identityPoolId(identityPoolId).logins(logins).build();

			final GetIdResponse idResponse = identity.getId(idReq);
			final String identityId = idResponse.identityId();

			var credReq = GetCredentialsForIdentityRequest.builder().identityId(identityId).logins(logins).build();

			final GetCredentialsForIdentityResponse credsResp = identity.getCredentialsForIdentity(credReq);

			AWSHelper.LOGGER.info("Successfully acquired AWS credentials through Cognito");
			
			return credsResp.credentials();
		}
	}

	@Override
	public Instant getExpiration() throws CoreException {
		return expiration;
	}
}

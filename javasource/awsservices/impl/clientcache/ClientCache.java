package awsservices.impl.clientcache;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;

import awsservices.impl.AWSHelper;
import awsservices.proxies.Credentials;
import software.amazon.awssdk.core.SdkClient;

public class ClientCache {
	private static final Map<String, ClientHolder> cache = 
			new HashMap<>();
	private static final Timer timer = new Timer(true);
	
	public static SdkClient getClient(
			IContext context, 
			String serviceName,
			Credentials config) throws CoreException {
		String key = serviceName + "_" + config.getMendixObject().getId().toLong();
		if (cache.containsKey(key)) {
            ClientHolder holder = cache.get(key);
            
            final Boolean clientExpired = holder.getExpires()
                    .map(exp -> exp.isBefore(Instant.now()))
                    .orElse(false);
            final Boolean isChanged = !holder.getChangedDate().equals(
            		config.getMendixObject().getChangedDate(context));
            
            if (!clientExpired && !isChanged) {
                return holder.getClient();
            }
            AWSHelper.LOGGER.info("Client " + config.getIdentifier() + " is expired or changed.");
        }
		return null;
	}
	
	public static void addClient(
			IContext context, 
			String serviceName, 
			Credentials config,
			Instant expiration,
			SdkClient client) throws CoreException {
		String key = serviceName + "_" + config.getMendixObject().getId().toLong();
		
		ClientHolder newHolder = new ClientHolder(client, 
				config.getMendixObject().getChangedDate(context),
				expiration);
		final ClientHolder oldClient = cache.put(key, newHolder);
        if (oldClient != null && oldClient.getClient() != null) {
            // Clean up the connections for the old client after 5 minutes
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    oldClient.getClient().close();
                }
            }, Duration.ofMinutes(5).toMillis());
        }
        
        if (expiration == null) {
        	AWSHelper.LOGGER.info("Added client for " + serviceName);
        } else {
        	AWSHelper.LOGGER.info("Added client for " + serviceName + " (expires at " + expiration.toString() + ").");
        }
	}
}

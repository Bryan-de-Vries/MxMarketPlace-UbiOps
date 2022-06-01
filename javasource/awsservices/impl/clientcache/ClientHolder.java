package awsservices.impl.clientcache;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import software.amazon.awssdk.core.SdkClient;

public class ClientHolder {
	private final SdkClient client;
    private final Instant expires;
    private final Date changedDate;

    ClientHolder(SdkClient client, Date changedDate, Instant expires) {
        this.client = client;
        this.expires = expires;
        this.changedDate = changedDate;
    }

    ClientHolder(SdkClient client, Date changedDate) {
        this.client = client;
        this.expires = null;
        this.changedDate = changedDate;
    }

    public SdkClient getClient() {
        return client;
    }
    
    public Date getChangedDate() {
    	return changedDate;
    }

    public Optional<Instant> getExpires() {
        return Optional.ofNullable(expires);
    }
}

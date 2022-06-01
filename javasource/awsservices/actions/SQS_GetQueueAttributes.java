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
import java.util.Map.Entry;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import awsservices.impl.AWSClients;
import awsservices.proxies.QueueAttributes;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

/**
 * Retrieves the queue attributes and applies them on the argument Queue.
 */
public class SQS_GetQueueAttributes extends CustomJavaAction<IMendixObject>
{
	private IMendixObject __credentials;
	private awsservices.proxies.Credentials credentials;
	private java.lang.String queueUrl;

	public SQS_GetQueueAttributes(IContext context, IMendixObject credentials, java.lang.String queueUrl)
	{
		super(context);
		this.__credentials = credentials;
		this.queueUrl = queueUrl;
	}

	@java.lang.Override
	public IMendixObject executeAction() throws Exception
	{
		this.credentials = __credentials == null ? null : awsservices.proxies.Credentials.initialize(getContext(), __credentials);

		// BEGIN USER CODE
		SqsClient sqs = AWSClients.getSqsClient(getContext(), credentials);
		
		GetQueueAttributesRequest attrReq = GetQueueAttributesRequest.builder()
				.queueUrl(queueUrl)
				.attributeNames(QueueAttributeName.ALL)
				.build();
		
		GetQueueAttributesResponse attrRes = sqs.getQueueAttributes(attrReq);
		QueueAttributes result = new QueueAttributes(getContext());
		
		
		for (Entry<QueueAttributeName, String> attr : attrRes.attributes().entrySet()) {
			if (attr.getValue() == null || attr.getValue().isEmpty() 
					|| attr.getValue().isBlank())
				continue;
			
			switch (attr.getKey()) {
			case APPROXIMATE_NUMBER_OF_MESSAGES:
				result.setApproximateNumberOfMessages(Integer.parseInt(attr.getValue()));
				break;
			case APPROXIMATE_NUMBER_OF_MESSAGES_DELAYED:
				result.setApproximateNumberOfMessagesDelayed(Integer.parseInt(attr.getValue()));
				break;
			case APPROXIMATE_NUMBER_OF_MESSAGES_NOT_VISIBLE:
				result.setApproximateNumberOfMessagesNotVisible(Integer.parseInt(attr.getValue()));
				break;
			case CREATED_TIMESTAMP:
				result.setCreatedTimestamp(new Date(Long.parseLong(attr.getValue()) * 1000));
				break;
			case DELAY_SECONDS:
				result.setDelaySeconds(Integer.parseInt(attr.getValue()));
				break;
			case FIFO_QUEUE:
				result.setFIFOQueue(Boolean.parseBoolean(attr.getValue()));
				break;
			case LAST_MODIFIED_TIMESTAMP:
				result.setLastModifiedTimestamp(new Date(Long.parseLong(attr.getValue()) * 1000));
				break;
			case MAXIMUM_MESSAGE_SIZE:
				result.setMaximumMessageSize(Integer.parseInt(attr.getValue()));
				break;
			case MESSAGE_RETENTION_PERIOD:
				result.setMessageRetentionPeriod(Integer.parseInt(attr.getValue()));
				break;
			case QUEUE_ARN:
				result.setARN(attr.getValue());
				break;
			case VISIBILITY_TIMEOUT:
				result.setVisibilityTimeout(Integer.parseInt(attr.getValue()));
				break;
			case RECEIVE_MESSAGE_WAIT_TIME_SECONDS:
				result.setReceiveMessageWaitTimeSeconds(Integer.parseInt(attr.getValue()));
				break;
			default:
			}
		}
		
		return result.getMendixObject();
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "SQS_GetQueueAttributes";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}

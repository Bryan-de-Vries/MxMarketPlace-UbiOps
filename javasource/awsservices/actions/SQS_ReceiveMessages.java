// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package awsservices.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import awsservices.impl.AWSClients;
import awsservices.impl.SQSHelper;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

public class SQS_ReceiveMessages extends CustomJavaAction<java.util.List<IMendixObject>>
{
	private IMendixObject __credentials;
	private awsservices.proxies.Credentials credentials;
	private java.lang.String queueUrl;
	private java.lang.Long maxNumberOfMessages;
	private java.lang.Long waitTimeSeconds;
	private java.lang.Boolean includeAttributes;

	public SQS_ReceiveMessages(IContext context, IMendixObject credentials, java.lang.String queueUrl, java.lang.Long maxNumberOfMessages, java.lang.Long waitTimeSeconds, java.lang.Boolean includeAttributes)
	{
		super(context);
		this.__credentials = credentials;
		this.queueUrl = queueUrl;
		this.maxNumberOfMessages = maxNumberOfMessages;
		this.waitTimeSeconds = waitTimeSeconds;
		this.includeAttributes = includeAttributes;
	}

	@java.lang.Override
	public java.util.List<IMendixObject> executeAction() throws Exception
	{
		this.credentials = __credentials == null ? null : awsservices.proxies.Credentials.initialize(getContext(), __credentials);

		// BEGIN USER CODE
		SqsClient sqs = AWSClients.getSqsClient(getContext(), credentials);
		
		List<QueueAttributeName> attributes = new LinkedList<>();
		if (includeAttributes) {
			attributes.add(QueueAttributeName.ALL);
		}
		
		ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
				.queueUrl(queueUrl)
				.attributeNames(attributes)
				.messageAttributeNames(MessageSystemAttributeName.SENDER_ID.name(), 
						MessageSystemAttributeName.SENT_TIMESTAMP.name())
				.maxNumberOfMessages(maxNumberOfMessages.intValue())
				.waitTimeSeconds(waitTimeSeconds.intValue())
				.build();

		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).messages();
		List<IMendixObject> result = new ArrayList<>();
		
		 for (Message message : messages) {
			 result.add(SQSHelper.getMendixMessageFromSQSMessage(getContext(), message)
					 .getMendixObject());
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
		return "SQS_ReceiveMessages";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}

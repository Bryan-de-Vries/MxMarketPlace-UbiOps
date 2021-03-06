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
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class SQS_SendMessage extends CustomJavaAction<java.lang.Void>
{
	private IMendixObject __credentials;
	private awsservices.proxies.Credentials credentials;
	private java.lang.String body;
	private java.lang.String queueUrl;

	public SQS_SendMessage(IContext context, IMendixObject credentials, java.lang.String body, java.lang.String queueUrl)
	{
		super(context);
		this.__credentials = credentials;
		this.body = body;
		this.queueUrl = queueUrl;
	}

	@java.lang.Override
	public java.lang.Void executeAction() throws Exception
	{
		this.credentials = __credentials == null ? null : awsservices.proxies.Credentials.initialize(getContext(), __credentials);

		// BEGIN USER CODE
		SqsClient sqs = AWSClients.getSqsClient(getContext(), credentials);
		
		SendMessageRequest req = SendMessageRequest.builder()
				.queueUrl(queueUrl)
				.messageBody(body)
				.build();
		sqs.sendMessage(req);
		return null;
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "SQS_SendMessage";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}

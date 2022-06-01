// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package awsservices.actions;

import awsservices.impl.AWSClients;
import awsservices.proxies.MessageAttribute;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import java.util.Map;
import java.util.stream.Collectors;

public class SNS_PublishMessage extends CustomJavaAction<java.lang.String>
{
	private IMendixObject __credentials;
	private awsservices.proxies.Credentials credentials;
	private java.lang.String subject;
	private java.lang.String body;
	private java.lang.String topic;
	private java.util.List<IMendixObject> __attributes;
	private java.util.List<awsservices.proxies.MessageAttribute> attributes;

	public SNS_PublishMessage(IContext context, IMendixObject credentials, java.lang.String subject, java.lang.String body, java.lang.String topic, java.util.List<IMendixObject> attributes)
	{
		super(context);
		this.__credentials = credentials;
		this.subject = subject;
		this.body = body;
		this.topic = topic;
		this.__attributes = attributes;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		this.credentials = __credentials == null ? null : awsservices.proxies.Credentials.initialize(getContext(), __credentials);

		this.attributes = new java.util.ArrayList<awsservices.proxies.MessageAttribute>();
		if (__attributes != null)
			for (IMendixObject __attributesElement : __attributes)
				this.attributes.add(awsservices.proxies.MessageAttribute.initialize(getContext(), __attributesElement));

		// BEGIN USER CODE
		final SnsClient client = AWSClients.getSnsClient(context(), credentials);

		final Map<String, MessageAttributeValue> attrs = attributes.stream().collect(Collectors.toMap(
				MessageAttribute::getKey,
				x -> MessageAttributeValue.builder()
						.dataType("String")
						.stringValue(x.getValue())
						.build()
		));

		final PublishRequest request = PublishRequest.builder()
				.messageAttributes(attrs)
				.message(body)
				.subject(subject)
				.topicArn(topic)
				.build();

		final PublishResponse response = client.publish(request);

		return response.messageId();
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "SNS_PublishMessage";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}

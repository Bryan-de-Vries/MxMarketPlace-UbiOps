package awsservices.impl;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;

import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;

public class SQSHelper {
	public static awsservices.proxies.Message getMendixMessageFromSQSMessage(
			IContext context, Message awsMsg) throws CoreException {
		Map<MessageSystemAttributeName, String> sysAttrs = awsMsg.attributes();
		
		awsservices.proxies.Message result = new awsservices.proxies.Message(context);
		result.setMessageId(awsMsg.messageId());
		result.setReceiptHandle(awsMsg.receiptHandle());
		result.setMD5OfBody(awsMsg.md5OfBody());
		result.setBody(awsMsg.body());
		result.setSenderId(sysAttrs.get(MessageSystemAttributeName.SENDER_ID));
		
		if (sysAttrs.containsKey(MessageSystemAttributeName.SENT_TIMESTAMP)) {
			try {
				result.setSentTimestamp(new Date(Long.parseLong(
						sysAttrs.get(MessageSystemAttributeName.SENT_TIMESTAMP))));
			} catch (Exception e) {}
		}

		for (Entry<String, MessageAttributeValue> entry : awsMsg.messageAttributes().entrySet()) {
			awsservices.proxies.MessageAttribute newAttribute = 
					new awsservices.proxies.MessageAttribute(context);
			newAttribute.setKey(entry.getKey());
			newAttribute.setValue(entry.getValue().stringValue());
			newAttribute.setMessageAttribute_Message(result);
		}
		
		return result;
	}
}

package co.elastic.apm.mule.listeners;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mule.api.context.notification.MessageProcessorNotificationListener;
import org.mule.context.notification.MessageProcessorNotification;
import org.springframework.beans.factory.annotation.Autowired;

import co.elastic.apm.mule.utils.SpanUtils;

/**
 * @author michaelhyatt
 * 
 *         Listener for MessageProcessor notifications, corresponds to start and
 *         finish of flow step execution.
 *
 */
public class TraceMessageProcessorNotificationListener
		implements MessageProcessorNotificationListener<MessageProcessorNotification> {

	@Override
	public void onNotification(MessageProcessorNotification notification) {

		logger.debug("Received " + notification.getActionName());
		
		switch (notification.getAction()) {
		case MessageProcessorNotification.MESSAGE_PROCESSOR_PRE_INVOKE:
//			break;
// This enum was introduced in Mule 3.9 and is not available in 3.8
// Also, the meaning and values of MESSAGE_PROCESSOR_PRE_INVOKE and MESSAGE_PROCESSOR_POST_INVOKE changed between 3.8 and 3.9. Sigh!
//		case MessageProcessorNotification.MESSAGE_PROCESSOR_PRE_INVOKE_ORIGINAL_EVENT:
			spanUtils.startSpan(notification);
			break;

		case MessageProcessorNotification.MESSAGE_PROCESSOR_POST_INVOKE:
			spanUtils.endSpan(notification);
			break;
		}
	}

	@Autowired
	private SpanUtils spanUtils;

	private Logger logger = LogManager.getLogger(TraceMessageProcessorNotificationListener.class);
}

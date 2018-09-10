package org.pcsoft.framework.jremote.ext.up.impl.queued;

import org.pcsoft.framework.jremote.api.type.PushItemUpdate;
import org.pcsoft.framework.jremote.commons.type.MethodKey;
import org.pcsoft.framework.jremote.ext.up.api.UpdatePolicy;
import org.pcsoft.framework.jremote.ext.up.impl.queued.internal.InvocationProcessor;
import org.pcsoft.framework.jremote.ext.up.impl.queued.internal.ProcessorType;
import org.pcsoft.framework.jremote.ext.up.impl.queued.internal.UpdateKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represent a queued update policy. This logic runs a model update or an event call in a queue system to limit updates.<br/>
 * To configure this class, you need to create file named {@value org.pcsoft.framework.jremote.ext.up.impl.queued.internal.InvocationConfiguration#PROP_FILE}
 * within this property values (all are integers):<br/>
 * <ul>
 *     <li><b><code>xxx.count</code></b> - Count of elements to run in one processor iteration, default is <code>1</code></li>
 *     <li><b><code>xxx.delay</code></b> - Milliseconds to wait until processor timer run next invocation iteration, default is <code>10</code></li>
 *     <li><b><code>xxx.buffer-size</code></b> - Size of invocation buffer, default is <code>256</code>. If buffer overflows the oldest invocations are removed</li>
 * </ul>
 * Please note, replace <code>xxx</code> with <code>model</code> or <code>event</code>, like <code>model.delay</code> or <code>event.count</code>.<br/>
 * You can change the used file via system property {@value org.pcsoft.framework.jremote.ext.up.impl.queued.internal.InvocationConfiguration#SYS_PROP_FILE}
 */
public class QueuedUpdatePolicy implements UpdatePolicy {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueuedUpdatePolicy.class);

    private final InvocationProcessor modelUpdateProcessor = new InvocationProcessor(ProcessorType.ModelObserver);
    private final InvocationProcessor eventProcessor = new InvocationProcessor(ProcessorType.Event);

    public QueuedUpdatePolicy() {
        modelUpdateProcessor.start();
        eventProcessor.start();
    }

    @Override
    public void runModelUpdateAndObserverInvocation(MethodKey methodKey, PushItemUpdate update, Object value, Runnable updateCallback) {
        final UpdateKey updateKey = new UpdateKey(methodKey, update, value);

        LOGGER.trace("Insert new model update call for " + updateKey);
        modelUpdateProcessor.addInvocation(updateKey, updateCallback);
    }

    @Override
    public void callReceiver(MethodKey methodKey, Runnable eventCallback) {
        final UpdateKey updateKey = new UpdateKey(methodKey, null, null);

        LOGGER.trace("Insert new receiver call for " + updateKey);
        eventProcessor.addInvocation(updateKey, eventCallback);
    }
}

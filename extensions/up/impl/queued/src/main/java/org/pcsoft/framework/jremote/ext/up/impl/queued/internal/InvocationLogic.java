package org.pcsoft.framework.jremote.ext.up.impl.queued.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.SortedMap;

/**
 * Contains logic for invocation of queued runnables
 */
public final class InvocationLogic {
    public static void runInvocation(final SortedMap<UpdateKey, Runnable> map, ProcessorType processorType) {
        runInvocation(processorType.getCount(), map);
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static void runInvocation(int count, final SortedMap<UpdateKey, Runnable> map) {
        if (count <= 0) {
            final List<Runnable> invocationList;
            synchronized (map) {
                invocationList = new ArrayList<>(map.values());
                map.clear();
            }

            for (final Runnable invocation : invocationList) {
                invocation.run();
            }
        } else {
            final List<Runnable> invocationList = new ArrayList<>();
            synchronized (map) {
                for (int i=0; i<count; i++) {
                    final UpdateKey updateKey;
                    try {
                        updateKey = map.firstKey();
                    } catch (NoSuchElementException ignore) {
                        break;
                    }
                    invocationList.add(map.get(updateKey));
                    map.remove(updateKey);
                }
            }

            for (final Runnable invocation : invocationList) {
                invocation.run();
            }
        }
    }

    private InvocationLogic() {
    }
}

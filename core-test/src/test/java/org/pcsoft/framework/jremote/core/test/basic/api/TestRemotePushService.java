package org.pcsoft.framework.jremote.core.test.basic.api;

import org.pcsoft.framework.jremote.api.Push;
import org.pcsoft.framework.jremote.api.RemotePushService;
import org.pcsoft.framework.jremote.api.type.PushItemUpdate;
import org.pcsoft.framework.jremote.api.type.PushMethodType;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

@RemotePushService
public interface TestRemotePushService extends Remote {
    @Push(modelClass = TestRemotePushModel.class, property = "name")
    void pushName(String name) throws RemoteException;

    @Push(modelClass = TestRemotePushModel.class, property = "value")
    void pushValue(int value) throws RemoteException;

    @Push(modelClass = TestRemotePushModel.class, property = "words")
    void pushAllWords(List<String> words) throws RemoteException;

    @Push(modelClass = TestRemotePushModel.class, property = "words", type = PushMethodType.SingleListItem)
    void pushWord(String word, PushItemUpdate update) throws RemoteException;

    @Push(modelClass = TestRemotePushModel.class, property = "sizes")
    void pushAllSizes(int[] sizes) throws RemoteException;

    @Push(modelClass = TestRemotePushModel.class, property = "sizes", type = PushMethodType.SingleListItem)
    void pushSize(int size, PushItemUpdate update) throws RemoteException;

    //Default test only
    default int calc(int x, int y) throws RemoteException {
        return x - y;
    }
}

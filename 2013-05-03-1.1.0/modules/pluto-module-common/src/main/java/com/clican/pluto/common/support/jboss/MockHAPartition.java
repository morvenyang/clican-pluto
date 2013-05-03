/**
 * The Clican-Pluto software suit is Copyright 2009, Clican Company and individual contributors, and is licensed under the GNU LGPL.
 *
 * @author wei.zhang
 *
 */
package com.clican.pluto.common.support.jboss;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.jboss.ha.framework.interfaces.ClusterNode;
import org.jboss.ha.framework.interfaces.DistributedReplicantManager;
import org.jboss.ha.framework.interfaces.DistributedState;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.ha.framework.interfaces.ResponseFilter;

@SuppressWarnings({ "deprecation", "rawtypes" })
public class MockHAPartition implements HAPartition {

    private Map<String, Object> handlers = new HashMap<String, Object>();

    public void callAsyncMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types,
            long methodTimeout, ClusterNode targetNode) throws Throwable {
        throw new UnsupportedOperationException("not implemented");
    }

    public void callAsynchMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types,
            boolean excludeSelf) throws Exception {
        throw new UnsupportedOperationException("not implemented");
    }

    public ArrayList callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types,
            boolean excludeSelf) throws Exception {
        throw new UnsupportedOperationException("not implemented");
    }

    public ArrayList callMethodOnCluster(String serviceName, String methodName, Object[] args, Class[] types,
            boolean excludeSelf, ResponseFilter filter) throws Exception {
        throw new UnsupportedOperationException("not implemented");
    }

    public ArrayList callMethodOnCoordinatorNode(String serviceName, String methodName, Object[] args, Class[] types,
            boolean excludeSelf) throws Exception {
        throw new UnsupportedOperationException("not implemented");
    }

    public Object callMethodOnNode(String serviceName, String methodName, Object[] args, Class[] types,
            long methodTimeout, ClusterNode targetNode) throws Throwable {
        Object handler = handlers.get(serviceName);
        Method method = handler.getClass().getMethod(methodName, types);
        return method.invoke(handler, args);
    }

    public boolean getAllowSynchronousMembershipNotifications() {
        return false;
    }

    public ClusterNode getClusterNode() {
        throw new UnsupportedOperationException("not implemented");
    }

    public ClusterNode[] getClusterNodes() {
        return new ClusterNode[1];
    }

    public Vector getCurrentView() {
        throw new UnsupportedOperationException("not implemented");
    }

    public long getCurrentViewId() {
        throw new UnsupportedOperationException("not implemented");
    }

    public DistributedReplicantManager getDistributedReplicantManager() {
        throw new UnsupportedOperationException("not implemented");
    }

    public DistributedState getDistributedStateService() {
        throw new UnsupportedOperationException("not implemented");
    }

    public String getNodeName() {
        throw new UnsupportedOperationException("not implemented");
    }

    public String getPartitionName() {
        throw new UnsupportedOperationException("not implemented");
    }

    public void registerMembershipListener(HAMembershipListener listener) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void registerRPCHandler(String serviceName, Object handler) {
        handlers.put(serviceName, handler);
    }

    public void registerRPCHandler(String serviceName, Object handler, ClassLoader classloader) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void setAllowSynchronousMembershipNotifications(boolean allowSync) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void subscribeToStateTransferEvents(String serviceName, HAPartitionStateTransfer subscriber) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void unregisterMembershipListener(HAMembershipListener listener) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void unregisterRPCHandler(String serviceName, Object subscriber) {
        handlers.remove(serviceName);
    }

    public void unsubscribeFromStateTransferEvents(String serviceName, HAPartitionStateTransfer subscriber) {
        throw new UnsupportedOperationException("not implemented");
    }

}

// $Id$
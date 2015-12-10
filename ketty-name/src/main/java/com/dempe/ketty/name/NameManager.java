package com.dempe.ketty.name;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/12/10
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class NameManager implements Runnable {

    private Map<String, List<NodeInfo>> nameMap = new ConcurrentHashMap<String, List<NodeInfo>>();

    private Map<String, List<NodeInfo>> unAvailableNameMap = new ConcurrentHashMap<String, List<NodeInfo>>();

    private Set<String> nodeIDSet = new HashSet<String>();

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public NameManager() {
        scheduledExecutorService.scheduleAtFixedRate(this, 0L, 0L, TimeUnit.SECONDS);
    }


    public boolean isRegister(String nodeId) {
        return nodeIDSet.contains(nodeId);
    }

    public StatusCode registerServer(String name, String host, int port) {
        String nodeId = IDMaker.buildID(name, host, port);
        if (nodeIDSet.contains(nodeId)) {
            return StatusCode.EXIST_ERR;
        } else {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setNodeId(nodeId);
            nodeInfo.setName(name);
            nodeInfo.setHost(host);
            nodeInfo.setPort(port);
            List<NodeInfo> nodeInfoList = nameMap.get(name);
            if (nodeInfoList == null) {
                nodeInfoList = new ArrayList<NodeInfo>();
            }
            nodeInfoList.add(nodeInfo);
            nameMap.put(name, nodeInfoList);
        }
        return StatusCode.SUCCESS;
    }

    public StatusCode deRegisterServer(String name, String host, int port) {
        String nodeId = IDMaker.buildID(name, host, port);
        if (!nodeIDSet.contains(nodeId)) {
            return StatusCode.UN_EXIST_ERR;
        } else {
            List<NodeInfo> nodeInfoList = nameMap.get(name);
            for (NodeInfo nodeInfo : nodeInfoList) {
                if (StringUtils.equals(nodeInfo.getHost(), host)
                        && StringUtils.equals(nodeInfo.getName(), name)
                        && nodeInfo.getPort() == port) {
                    nodeInfoList.remove(nodeInfo);
                }
            }
        }
        return StatusCode.SUCCESS;
    }


    public List<NodeInfo> listServerByName(String name) {
        return nameMap.get(name);
    }

    public void checkAvailable() {
        Set<Map.Entry<String, List<NodeInfo>>> entries = nameMap.entrySet();
        Iterator<Map.Entry<String, List<NodeInfo>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<NodeInfo>> next = iterator.next();
            String key = next.getKey();
            List<NodeInfo> nodeList = next.getValue();
            for (NodeInfo nodeInfo : nodeList) {
                boolean available = nodeInfo.isAvailable();
                if (!available) {
                    nodeList.remove(nodeInfo);
                    List<NodeInfo> unNodeInfoList = unAvailableNameMap.get(key);
                    if (unNodeInfoList == null) {
                        unNodeInfoList = new ArrayList<NodeInfo>();
                    }
                    unNodeInfoList.add(nodeInfo);
                }
            }
        }
    }

    public void checkUnAvailable() {
        Set<Map.Entry<String, List<NodeInfo>>> entries = unAvailableNameMap.entrySet();
        Iterator<Map.Entry<String, List<NodeInfo>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<NodeInfo>> next = iterator.next();
            String key = next.getKey();
            List<NodeInfo> nodeList = next.getValue();
            for (NodeInfo nodeInfo : nodeList) {
                boolean available = nodeInfo.isAvailable();
                if (available) {
                    nodeList.remove(nodeInfo);
                    List<NodeInfo> NodeInfoList = nameMap.get(key);
                    if (NodeInfoList == null) {
                        NodeInfoList = new ArrayList<NodeInfo>();
                    }
                    NodeInfoList.add(nodeInfo);
                }
            }
        }
    }


    @Override
    public void run() {
        checkAvailable();
        checkUnAvailable();

    }
}

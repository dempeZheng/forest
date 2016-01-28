package com.dempe.ketty.ha.zoo;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.Participant;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2016/1/28
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class KettyLeaderLatch {
    private CuratorFramework client;
    private String latchPath;
    private String id;
    private LeaderLatch leaderLatch;

    public KettyLeaderLatch(String connString, String latchPath, String id) {
        client = CuratorFrameworkFactory.newClient(connString, new ExponentialBackoffRetry(100, Integer.MAX_VALUE));
        this.id = id;
        this.latchPath = latchPath;
    }

    public void start() throws Exception {
        client.start();
        client.getZookeeperClient().blockUntilConnectedOrTimedOut();
        leaderLatch = new LeaderLatch(client, latchPath, id);
        leaderLatch.start();
    }

    public boolean isLeader() {
        return leaderLatch.hasLeadership();
    }

    public Participant currentLeader() throws Exception {
        return leaderLatch.getLeader();
    }

    public void close() throws IOException {
        leaderLatch.close();
        client.close();
    }


    public static void main(String[] args) throws Exception {
        String latchPath = "/examples/latch";
        String connStr = "127.0.0.1:2181";
        KettyLeaderLatch node1 = new KettyLeaderLatch(connStr, latchPath, "node-1");
        KettyLeaderLatch node2 = new KettyLeaderLatch(connStr, latchPath, "node-2");
        KettyLeaderLatch node3 = new KettyLeaderLatch(connStr, latchPath, "node-3");
        KettyLeaderLatch node4 = new KettyLeaderLatch(connStr, latchPath, "node-4");
        KettyLeaderLatch node5 = new KettyLeaderLatch(connStr, latchPath, "node-5");
        node1.start();
        node2.start();
        node3.start();
        node4.start();
        node5.start();

        Thread.sleep(5000);
        System.out.println("----------------------------------------");
        for (int i = 0; i < 100; i++) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>i:" + i);
            System.out.println("node-1 think the leader is " + node2.currentLeader());
            System.out.println("node-2 think the leader is " + node2.currentLeader());
            Thread.sleep(10000);
        }


        node1.close();

        System.out.println("now node-2 think the leader is " + node2.currentLeader());

        node2.close();
        System.out.println("now node-2 think the leader is " + node3.currentLeader());

        node3.close();
        System.out.println("now node-2 think the leader is " + node4.currentLeader());

        node4.close();
        System.out.println("now node-2 think the leader is " + node5.currentLeader());

        node5.close();
    }

}
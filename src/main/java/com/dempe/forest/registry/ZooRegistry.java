package com.dempe.forest.registry;

import com.dempe.forest.transport.ServerConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.regex.Pattern;


public class ZooRegistry {

    private final Logger logger = LoggerFactory.getLogger(ZooRegistry.class);

    private CuratorFramework client = null;
    private ServiceDiscovery<InstanceDetails> serviceDiscovery = null;

    private static String basePath;
    static ZooRegistry instance = null;

    public static ZooRegistry getInstance(ServerConfig config) {
        if (instance == null) {
            synchronized (ZooRegistry.class) {
                if (instance == null) {
                    instance = new ZooRegistry(config);
                }
            }
        }
        return instance;
    }

    ZooRegistry(ServerConfig config) {
        basePath = config.zkBasePath();
        client = CuratorFrameworkFactory.newClient(config.zkConnectStr(), new ExponentialBackoffRetry(1000, 3));
        client.start();

        JsonInstanceSerializer<InstanceDetails> serializer = new JsonInstanceSerializer<InstanceDetails>(InstanceDetails.class);
        serviceDiscovery = ServiceDiscoveryBuilder.builder(InstanceDetails.class).client(client).basePath(basePath).serializer(serializer).build();
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            logger.error("Failed to create ZooRegistry!msg=" + e.getMessage(), e);
        }
    }

    public void registerService(String serviceName, int port) throws Exception {
        String localIp = getInnerHostIp();
        String id = serviceName.substring(serviceName.lastIndexOf(".") + 1) + ":" + localIp + ":" + port;
        ServiceInstance<InstanceDetails> service = ServiceInstance.<InstanceDetails>builder()
                .name(serviceName)
                .address(getInnerHostIp())
                .port(port)
                .id(id)
                .serviceType(ServiceType.DYNAMIC)
                .payload(new InstanceDetails(id)).build();

        serviceDiscovery.registerService(service);
        logger.info("registerService|serviceName=" + serviceName + "|port=" + port);
    }

    public void unregisterService(String serviceName, int port) throws Exception {

        String localIp = getInnerHostIp();
        String id = serviceName.substring(serviceName.lastIndexOf(".") + 1) + ":" + localIp + ":" + port;
        ServiceInstance<InstanceDetails> service = ServiceInstance.<InstanceDetails>builder()
                .name(serviceName)
                .address(getInnerHostIp())
                .port(port)
                .id(id)
                .serviceType(ServiceType.DYNAMIC)
                .payload(new InstanceDetails(id)).build();

        serviceDiscovery.unregisterService(service);
        logger.info("unregisterService|serviceName=" + serviceName + "|port=" + port);
    }

    public void registerService(ServiceInstance<InstanceDetails> service) throws Exception {
        serviceDiscovery.registerService(service);
    }

    public void unregisterService(ServiceInstance<InstanceDetails> service) throws Exception {
        serviceDiscovery.unregisterService(service);
    }

    public void updateService(ServiceInstance<InstanceDetails> service) throws Exception {
        serviceDiscovery.updateService(service);
    }

    public Collection<ServiceInstance<InstanceDetails>> queryForInstances(String serviceName) throws Exception {
        return serviceDiscovery.queryForInstances(serviceName);
    }

    //todo..... 
    public void addWatcher(String path, Watcher watcher) {
        try {
            client.getChildren().usingWatcher(watcher).forPath(path);
        } catch (KeeperException.NoNodeException e) {
            try {
                client.create().creatingParentsIfNeeded().forPath(path);
                client.getChildren().usingWatcher(watcher).forPath(path);
            } catch (Exception e1) {
                new RuntimeException("failed to add watcher for path:" + path, e1);
            }
        } catch (Exception e) {
            new RuntimeException("failed to add watcher for path:" + path, e);
        }
    }

    public void close() {
        CloseableUtils.closeQuietly(serviceDiscovery);
        CloseableUtils.closeQuietly(client);
    }


    private static String innerHostIp = null;
    private static Pattern ipPattern = Pattern.compile("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");
    private static Pattern privateIpPattern = Pattern.compile("(^127\\.0\\.0\\.1)|(^10(\\.[0-9]{1,3}){3}$)|(^172\\.1[6-9](\\.[0-9]{1,3}){2}$)|(^172\\.2[0-9](\\.[0-9]{1,3}){2}$)|(^172\\.3[0-1](\\.[0-9]{1,3}){2}$)|(^192\\.168(\\.[0-9]{1,3}){2}$)");

    private static String getInnerHostIp() {
        if (innerHostIp == null) {
            synchronized (ZooRegistry.class) {
                if (innerHostIp == null) {
                    String ip = null;
                    try {
                        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                        while (interfaces.hasMoreElements()) {
                            NetworkInterface iface = interfaces.nextElement();
                            // filters out 127.0.0.1 and inactive if
                            if (iface.isLoopback() || !iface.isUp())
                                continue;

                            Enumeration<InetAddress> addresses = iface.getInetAddresses();
                            while (addresses.hasMoreElements()) {
                                InetAddress addr = addresses.nextElement();
                                String _tempIp = addr.getHostAddress();
                                // find private ip.
                                if (ZooRegistry.ipPattern.matcher(_tempIp).matches()
                                        && ZooRegistry.privateIpPattern.matcher(_tempIp).matches()) {
                                    ip = _tempIp;
                                    break;
                                }
                            }
                        }
                    } catch (SocketException e) {
                        throw new RuntimeException(e);
                    }
                    innerHostIp = ip;
                }
            }
        }
        return innerHostIp;
    }

}

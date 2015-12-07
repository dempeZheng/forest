package com.dempe.ketty.ha;

import com.dempe.ketty.ha.listener.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Dempe
 * Date: 2015/10/23
 * Time: 20:27
 * To change this template use File | Settings | File Templates.
 */
public abstract class HAProxy<T> extends TimerTask {

    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Timer timer;

    /**
     * 负载均衡策略
     * 默认 default
     * 轮叫调度Round Robin
     * 加权轮叫Weighted Round Robin
     * 哈希调度Hash
     */
    public enum Strategy {
        DEFAULT,
        RR,
        WRR,
        HASH
    }

    /**
     * 负载策略
     */
    private Strategy strategy;

    /**
     * 负载均衡工具
     */
    private LoadBalance lb;


    /**
     * 可用的服务
     */
    private List<ServerInfo> availServers = new CopyOnWriteArrayList<ServerInfo>();

    /**
     * 不可用的服务
     */
    private List<ServerInfo> unAvailServers = new CopyOnWriteArrayList<ServerInfo>();

    /**
     * 构造方法
     */
    public HAProxy() {
        this.strategy = Strategy.RR;
    }

    /**
     * 构造方法
     *
     * @param strategy 策略
     */
    public HAProxy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 构造方法
     *
     * @param period   定时检查服务可用性
     * @param strategy 策略
     */
    public HAProxy(Strategy strategy, long period) {
        this(strategy);
        this.timer = new Timer();
        LOGGER.info("HALBProxy timer check started, period:{}", period);
        this.timer.schedule(this, 1000, period);
    }

    /**
     * 构造方法
     *
     * @param period   定时检查服务可用性
     * @param strategy 策略
     * @param conf     服务器链接配置
     * @throws Exception
     */
    public HAProxy(Strategy strategy, long period, String conf) throws Exception {
        this(strategy, period);
        this.initServer(conf);
    }

    /**
     * 构造方法
     *
     * @param period 定时检查服务可用性
     */
    public HAProxy(long period) {
        this(Strategy.RR, period);
    }

    /**
     * 获得位置
     *
     * @return
     */
    public int getIndex() {
        switch (this.strategy) {
            case DEFAULT:
                return 0;
            case RR:
                return this.lb.roundIndex();
            case WRR:
                return this.lb.roundIndexByWeight();
            default:
                break;
        }
        return 0;
    }

    /**
     * hash key获得位置
     *
     * @param key
     * @return
     */
    public int getIndex(String key) {
        return this.lb.hashIndex(key);
    }

    /**
     * 可用转不可用
     *
     * @param index
     */
    private void availToUnAvail(int index) {
        this.unAvailServers.add(this.availServers.remove(index));
        this.notifyHAListener(new ToAvailEvent(this));
    }

    /**
     * 不可用转可用
     *
     * @param serverInfo
     */
    private void unavailToAvail(ServerInfo serverInfo) {
        LOGGER.info("unAvail：", serverInfo.toString());
        if (this.availServers.size() == 0) //如果可用节点的列表已空,则不需考虑负载的策略
        {
            this.availServers.add(serverInfo);
        } else {
            if (this.strategy.equals(Strategy.DEFAULT)) {
                //如果待加入的unavail服务节点is_default = true,则添加到队首; 否则,加入到队尾
                if (!serverInfo.isDefault()) //非缺省节点,直接加到队尾
                {
                    this.availServers.add(serverInfo);
                } else //“缺省服务节点”,从不可用---->可用
                {
                    if (availServers.contains(serverInfo)) {
                        availServers.remove(serverInfo);
                        availServers.add(0, serverInfo);
                    } else {
                        availServers.add(0, serverInfo);
                    }
                }
            } else {
                availServers.add(serverInfo);
            }
        }

        String allAvailServerInfo = this.getAllAvailServerInfo();
        LOGGER.info("avail:{}", allAvailServerInfo);

        this.notifyHAListener(new ToUnAvailEvent(this));
    }

    /**
     * 获得可用的服务器信息
     *
     * @return 服务器信息
     */
    public ServerInfo getAvailServerInfo() {
        ServerInfo server = this.availServers.get(getIndex());
        server.setHits(server.getHits() + 1);
        return server;
    }

    /**
     * 获得可用的服务器信息
     *
     * @param key hash值
     * @return 服务器信息
     */
    public ServerInfo getAvailServerInfo(String key) {
        ServerInfo server = this.availServers.get(getIndex(key));
        server.setHits(server.getHits() + 1);
        return server;
    }

    public int getAvailServerSize() {
        return this.availServers.size();
    }

    public int getUnavailServerSize() {
        return this.unAvailServers.size();
    }

    /**
     * 初始化负载均衡器
     */
    private void initLoadBalance() {
        int[] weights = new int[this.availServers.size()];
        for (int i = 0; i < this.availServers.size(); i++) {
            ServerInfo serverInfo = this.availServers.get(i);
            weights[i] = serverInfo.getWeight();
            serverInfo.setIndex(i);
        }
        if (Strategy.WRR == strategy) {
            this.lb = new LoadBalance(weights);
        } else {
            this.lb = new LoadBalance(this.availServers.size());
        }
    }

    /**
     * 初始化服务器配置，使用者调用
     *
     * @param conf
     * @throws Exception
     */
    public List<ServerInfo> initServer(String conf) throws Exception {
        CopyOnWriteArrayList<ServerInfo> availServer = new CopyOnWriteArrayList<ServerInfo>();
        List<ServerInfo> infos = this.initServerInfo(conf);
        availServer.addAll(infos);
        this.availServers = availServer;
        this.unAvailServers = new CopyOnWriteArrayList<ServerInfo>();
        this.initLoadBalance();
        return infos;
    }

    /**
     * 初始化服务器配置，使用者调用
     *
     * @throws Exception
     */
    public List<ServerInfo> initServer(List<ServerInfo> availServer) {
        CopyOnWriteArrayList<ServerInfo> list = new CopyOnWriteArrayList<ServerInfo>();
        if (availServer != null) {
            this.availServers.clear();
            list.addAll(availServer);
        }

        this.availServers = list;
        this.initLoadBalance();
        return this.availServers;
    }

    /**
     * 获得可用服务的client
     *
     * @param serverInfo
     * @return
     * @throws Exception
     */
    public T getClient(ServerInfo<T> serverInfo) {
        LOGGER.debug("getClient serverInfo:" + serverInfo);
        T server = null;
        try {
            if (serverInfo.getClient() != null) {
                server = serverInfo.getClient();
            } else {
                server = this.createClient(serverInfo);
                serverInfo.setClient(server);
                LOGGER.info("getClient null so createClient:" + server);
            }

        } catch (Exception e) {
            LOGGER.error("getClient error serverInfo:" + serverInfo, e);
        }
        if (server == null) {
            if (availServers.size() > 1) {
                this.availToUnAvail(serverInfo.getIndex());
                this.initLoadBalance();
                return getClient(getAvailServerInfo());
            } else {
                this.checkUnAvailToAvail();
            }
        }
        return server;
    }

    /**
     * 直接更换可用节点的client
     *
     * @param serverInfo
     * @return
     */
    public T changeClient(ServerInfo serverInfo) throws Exception {

        LOGGER.info("changeClient serverInfo:" + serverInfo + " availSize:" + availServers.size() + ",unavailSize:" + unAvailServers.size());
        synchronized (serverInfo) {
            ServerInfo originalInfo = geAvailServerInfo(serverInfo);
            if (null != originalInfo) {
                if (availServers.size() > 1) {
                    LOGGER.info("changeClient availToUnAvail, serverInfo:" + originalInfo + " availSize:" + availServers.size() + ",unavailSize:" + unAvailServers.size());
                    this.availToUnAvail(originalInfo.getIndex());
                    this.initLoadBalance();
                    for (ServerInfo info : this.availServers) {
                        return getClient(info);
                    }
                } else {
                    LOGGER.info("changeClient checkUnAvailToAvail, availServers is empty,serverInfo:" + originalInfo);
                    checkUnAvailToAvail();
                    for (ServerInfo info : availServers) {
                        LOGGER.info("changeClient checkUnAvailToAvail serverInfo list elem:" + info);
                    }
                    return getClient(originalInfo);
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            for (ServerInfo info : availServers) {
                LOGGER.debug("changeClient serverInfo list elem:" + info);
            }
        }
        ServerInfo s = getAvailServerInfo();
        LOGGER.info("changeClient  not check, serverInfo:" + s);
        return getClient(s);
    }

    /**
     * 获得原始的可用ServerInfo
     * 由于传入的 ServerInfo可能是Client,而不是ServerInfo,所以要做内容对比
     *
     * @param serverInfo
     * @return
     */
    private ServerInfo geAvailServerInfo(ServerInfo serverInfo) {
        if (serverInfo == null) {
            return null;
        }
        if (availServers != null) {
            int index = availServers.indexOf(serverInfo);
            if (index != -1) {
                return availServers.get(index);
            }
        }
        return null;
    }

    /**
     * 轮询获得可用服务的client
     *
     * @return
     * @throws Exception
     */
    public T getClient() {
        ServerInfo serverInfo = this.getAvailServerInfo();
        return this.getClient(serverInfo);
    }

    /**
     * hash获得可用服务的client
     *
     * @param key hash值
     * @return
     */
    public T getClient(String key) {
        ServerInfo serverInfo = this.getAvailServerInfo(key);
        return this.getClient(serverInfo);
    }


    /**
     * 定时检测服务是否可用
     */
    @Override
    public void run() {
        checkUnAvailToAvail();
    }

    private void checkUnAvailToAvail() {
        if (null != unAvailServers) {
            int availSize = availServers == null ? 0 : availServers.size();
            LOGGER.debug("HA check availSize:" + availSize + ",unavailSize:" + unAvailServers.size());
            for (ServerInfo serverInfo : unAvailServers) {
                T server = null;
                try {
                    server = this.getClient(serverInfo);
                } catch (Exception e) {
                    LOGGER.error("HA check error:" + serverInfo + " server:" + server, e);
                }
                LOGGER.info("HA check serverInfo:" + serverInfo + " server:" + server);
                if (server != null) {
                    unAvailServers.remove(serverInfo);
                    this.unavailToAvail(serverInfo);
                    this.initLoadBalance();
                }
            }
        }
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 取所有可用服务节点的信息(每个节点信息一行)
     *
     * @return
     */
    public String getAllAvailServerInfo() {
        StringBuffer infoBuf = new StringBuffer();
        for (int i = 0; i < this.availServers.size(); i++) {
            ServerInfo serverInfo = this.availServers.get(i);
            if (i == 0) {
                infoBuf.append(serverInfo.toString());
            } else {
                infoBuf.append("\r\n");
                infoBuf.append(serverInfo.toString());
            }
        }
        return infoBuf.toString();
    }

    /**
     * 个性初始化服务器信息，由子类实现
     *
     * @param conf 服务器配置
     * @return
     * @throws Exception
     */
    protected abstract List<ServerInfo> initServerInfo(String conf) throws Exception;

    /**
     * 创建可用的服务器的client，由子类实现
     *
     * @param serverInfo
     * @return
     * @throws Exception
     */
    protected abstract T createClient(ServerInfo serverInfo) throws Exception;


    private Set<HAListener> listeners = new HashSet<HAListener>();

    public void addHAListener(HAListener listener) {
        listeners.add(listener);
    }

    public void removeHAListener(HAListener listener) {
        listeners.remove(listener);
    }

    public void notifyHAListener(HAEvent event) {
        for (HAListener listener : listeners) {
            try {
                if (event instanceof ToAvailEvent) {
                    if (listener instanceof ToAvailListener) {
                        listener.handleEvent(event);
                    } else {
                        continue;
                    }
                } else if (event instanceof ToUnAvailEvent) {
                    if (listener instanceof ToUnAvailListener) {
                        listener.handleEvent(event);
                    } else {
                        continue;
                    }
                } else if (event instanceof HAEvent) {
                    if (listener instanceof HAListener) {
                        listener.handleEvent(event);
                    } else {
                        continue;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("notifyHAListener handleEvent error!", e);
            }
        }
    }

    /**
     * 直接更换可用节点的client
     *
     * @param client
     * @return
     */
    public T changeClient(T client) throws Exception {
        if (client == null) {
            throw new IllegalArgumentException("client can't be null");
        }
        for (ServerInfo info : this.availServers) {
            if (client.equals(info.getClient())) {
                LOGGER.info("goto changeClient serverInfo:" + info);
                return changeClient(info);

            }
        }
        return getClient();
    }

    public void setAvailServers(CopyOnWriteArrayList<ServerInfo> availServers) {
        this.availServers = availServers;
    }

    public void setUnAvailServers(CopyOnWriteArrayList<ServerInfo> unAvailServers) {
        this.unAvailServers = unAvailServers;
    }

    public List<ServerInfo> getAvailServers() {
        return availServers;
    }

    public List<ServerInfo> getUnAvailServers() {
        return unAvailServers;
    }

    public void addServer(ServerInfo serverInfo) {
        if (!availServers.contains(serverInfo)) {
            availServers.add(serverInfo);
            initLoadBalance();
        }
    }

    public void removeServer(ServerInfo serverInfo) {
        if (availServers.contains(serverInfo)) {
            availServers.remove(serverInfo);
            initLoadBalance();
        } else if (unAvailServers.contains(serverInfo)) {
            unAvailServers.remove(serverInfo);
        }

    }


}

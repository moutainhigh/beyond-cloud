package com.beyond.cloud.framework.leader.impl;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.beyond.cloud.framework.leader.ConsulService;
import com.beyond.cloud.framework.leader.LeaderElection;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.session.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * @author lucifer
 * @date 2020/9/16 13:52
 */
public class ConsulLeaderElectionImpl implements LeaderElection, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsulLeaderElectionImpl.class);

    private volatile boolean leadership;
    private volatile String sessionId;
    private static final String INSTANCE_ID = UUID.randomUUID().toString();

    private final String key;
    private final ConsulService consulService;

    public ConsulLeaderElectionImpl(final String key, final ConsulService consulService) {
        this.key = key;
        this.consulService = consulService;
    }

    @Override
    public String getInstanceId() {
        return INSTANCE_ID;
    }

    @Override
    public boolean leadership() {
        return leadership;
    }

    @Override
    public void release() {
        if (leadership) {
            LOGGER.info("leader down voluntarily");
        }
        leadership = false;
        consulService.deleteSession(sessionId);
    }

    @Override
    public String discoverLeader() {
        return consulService.getValue(key).getDecodedValue();
    }

    @Override
    public void afterPropertiesSet() {
        LOGGER.debug("Start leader election");
        elect();
        WatchLeaderScheduler scheduler = new WatchLeaderScheduler(key, consulService, this::elect);
        scheduler.watchTask();
    }

    @Override
    public void destroy() {
        release();
    }

    private void elect() {
        if (StringUtils.isEmpty(sessionId)) {
            sessionId = consulService.createSession();
        } else {
            Session session = consulService.getSession(sessionId);
            if (Objects.isNull(session)) {
                sessionId = consulService.createSession();
            }
        }

        // 谁先捕获到KV，谁就是leader
        // 注意：如果程序关闭后很快启动，session关联的健康检查可能不会失败，所以session不会失效
        // 这时候可以把程序创建的sessionId保存起来，重启后首先尝试用上次的sessionId，
        leadership = consulService.acquireKV(key, INSTANCE_ID, sessionId);
        LOGGER.debug("elect leader result[{}]", leadership);
    }

    private static class WatchLeaderScheduler {

        private static final Logger LOGGER = LoggerFactory.getLogger(WatchLeaderScheduler.class);
        private static final ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);

        private final String key;
        private final ConsulService consulService;
        private final Runnable electRunner;

        WatchLeaderScheduler(final String key, final ConsulService consulService, final Runnable electRunner) {
            this.key = key;
            this.consulService = consulService;
            this.electRunner = electRunner;
        }

        private void watchTask() {
            executor.scheduleAtFixedRate(() -> watch(electRunner), 10, 10, TimeUnit.SECONDS);
        }

        private void watch(Runnable runnable) {
            GetValue value = consulService.getValue(key);
            if (Objects.isNull(value) || StringUtils.isEmpty(value.getSession())) {
                LOGGER.info("no leader, start elect");
                runnable.run();
            } else {
                LOGGER.trace("has leader");
            }
        }

    }

}


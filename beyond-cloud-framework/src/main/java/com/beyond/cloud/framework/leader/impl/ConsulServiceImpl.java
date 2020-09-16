package com.beyond.cloud.framework.leader.impl;

import com.beyond.cloud.framework.leader.ConsulService;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.ecwid.consul.v1.session.model.Session;

/**
 * @author lucifer
 * @date 2020/9/16 14:32
 */
public final class ConsulServiceImpl implements ConsulService {

    /**
     * 默认延迟的时间
     */
    private static final long DEFAULT_DELAY = 10;

    private final ConsulClient client;

    public ConsulServiceImpl(final ConsulClient client) {
        this.client = client;
    }


    @Override
    public String createSession(final long delay) {
        NewSession session = new NewSession();
        session.setLockDelay(delay);
        return client.sessionCreate(session, QueryParams.DEFAULT).getValue();
    }

    @Override
    public String createSession() {
        return createSession(DEFAULT_DELAY);
    }

    @Override
    public Session getSession(final String sessionId) {
        return client.getSessionInfo(sessionId, QueryParams.DEFAULT).getValue();
    }

    @Override
    public GetValue getValue(final String key) {
        return client.getKVValue(key).getValue();
    }

    /**
     * 使用 Session 捕获KV
     */
    @Override
    public Boolean acquireKV(final String key, final String value, final String sessionId) {
        PutParams putParams = new PutParams();
        putParams.setAcquireSession(sessionId);
        return client.setKVValue(key, value, putParams).getValue();
    }

    @Override
    public void deleteSession(final String sessionId) {
        client.sessionDestroy(sessionId, QueryParams.DEFAULT);
    }

}

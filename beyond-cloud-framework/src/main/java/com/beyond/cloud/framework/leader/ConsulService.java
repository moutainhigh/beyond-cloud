package com.beyond.cloud.framework.leader;

import com.ecwid.consul.v1.kv.model.GetValue;
import com.ecwid.consul.v1.session.model.Session;

/**
 * @author lucifer
 * @date 2020/9/16 15:24
 */
public interface ConsulService {

    String createSession(long delay);

    String createSession();

    Session getSession(String sessionId);

    GetValue getValue(String key);

    Boolean acquireKV(String key, String value, String sessionId);

    void deleteSession(String sessionId);

}

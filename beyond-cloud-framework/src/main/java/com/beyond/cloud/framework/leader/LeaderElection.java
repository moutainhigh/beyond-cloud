package com.beyond.cloud.framework.leader;

/**
 * @author lucifer
 * @date 2020/9/16 13:47
 */
public interface LeaderElection {

    String getInstanceId();

    boolean leadership();

    void release();

    String discoverLeader();

}

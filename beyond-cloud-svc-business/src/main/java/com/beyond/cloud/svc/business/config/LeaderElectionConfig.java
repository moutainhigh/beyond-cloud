package com.beyond.cloud.svc.business.config;

import com.beyond.cloud.framework.leader.ConsulService;
import com.beyond.cloud.framework.leader.LeaderElection;
import com.beyond.cloud.framework.leader.impl.ConsulLeaderElectionImpl;
import com.beyond.cloud.framework.leader.impl.ConsulServiceImpl;
import com.ecwid.consul.v1.ConsulClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @author lucifer
 * @date 2020/9/16 15:11
 */
@Configuration
public class LeaderElectionConfig {

    private static final String ELECT_KEY = "service/beyond-cloud-svc-business/leader";

    @Bean
    @Lazy
    public ConsulService consulService(final ConsulClient consulClient) {
        return new ConsulServiceImpl(consulClient);
    }

    @Bean
    @Lazy
    public LeaderElection leaderElection(final ConsulService consulService) {
        return new ConsulLeaderElectionImpl(ELECT_KEY, consulService);
    }

}

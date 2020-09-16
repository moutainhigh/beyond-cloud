package com.beyond.cloud.svc.business.controller;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.framework.leader.LeaderElection;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucifer
 * @date 2020/9/16 15:35
 */
@RestController
@RequestMapping("/api/leader")
public class LeaderElectionController {

    private final LeaderElection leaderElection;

    public LeaderElectionController(final LeaderElection leaderElection) {this.leaderElection = leaderElection;}

    @GetMapping("/instance-id")
    public ApiResult<String> getInstanceId() {
        return ApiResult.data(leaderElection.getInstanceId());
    }

    @GetMapping("/leadership")
    public ApiResult<Boolean> leadership() {
        return ApiResult.ok(leaderElection.leadership());
    }

    @DeleteMapping("/release")
    public ApiResult release() {
        leaderElection.release();
        return ApiResult.ok();
    }

}

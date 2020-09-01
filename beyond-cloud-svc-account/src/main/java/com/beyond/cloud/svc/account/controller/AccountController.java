package com.beyond.cloud.svc.account.controller;

import com.beyond.cloud.common.ApiResult;
import com.beyond.cloud.svc.account.serivce.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lucifer
 * @date 2020/9/1 14:26
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {this.accountService = accountService;}

    @PostMapping("/debit")
    public ApiResult debit(@RequestParam(value = "userId") String userId,
                           @RequestParam(value = "money") int money) {
        accountService.debit(userId, money);
        return ApiResult.ok();
    }

}

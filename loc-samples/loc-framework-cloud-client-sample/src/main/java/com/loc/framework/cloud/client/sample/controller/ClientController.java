package com.loc.framework.cloud.client.sample.controller;

import com.loc.framework.cloud.client.sample.feign.FeignClientSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

/**
 * Created on 2018/4/17.
 */
@RestController
public class ClientController {

  @Autowired
  private FeignClientSample feignClientSample;

  @GetMapping("/client")
  public Problem client() {
    return feignClientSample.server();
  }
}

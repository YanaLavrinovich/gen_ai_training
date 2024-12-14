package com.lavr.training.gen.ai.service;

import com.lavr.training.gen.ai.dto.OpenAiDeploymentList;

public interface DialService {

  /**
   * Get deployment names and ids
   *
   * @return {@link OpenAiDeploymentList} with list of deployments
   */
  OpenAiDeploymentList getDeployments();
}

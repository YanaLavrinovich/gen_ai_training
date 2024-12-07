package com.lavr.training.gen.ai.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class OpenAiDeploymentList {

  private List<OpenAiDeployment> data;
}

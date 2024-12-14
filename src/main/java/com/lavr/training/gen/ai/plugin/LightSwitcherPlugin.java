package com.lavr.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LightSwitcherPlugin {

  private boolean isTurnOnLight = false;

  @DefineKernelFunction(name = "changeState", description = "Changes the state of the light")
  public String changeState(
      @KernelFunctionParameter(description = "The new state of the light", name = "isOn")
          boolean isOn) {
    log.info("Light Changer Plugin was called with lightState: {}", isOn);
    isTurnOnLight = isOn;
    return isTurnOnLight ? "Light is turned on" : "Light is turned off";
  }

  @DefineKernelFunction(name = "getLightState", description = "Provide a state of a light switcher")
  public String getLightState() {
    log.info("Light Changer Plugin was called to get light state");
    return isTurnOnLight ? "Light is turned on" : "Light is turned off";
  }
}

package com.lavr.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeatherForecastPlugin {

  @DefineKernelFunction(
      name = "getWeather",
      description = "Get a weather forecast for a given location")
  public String getWeatherForecast(
      @KernelFunctionParameter(
              description = "Location is used for weather forecast",
              name = "location")
          String location) {
    log.info("Weather Forecast Plugin was called with location: {}", location);

    return "Weather forecast for " + location + " is 7°C";
  }
}

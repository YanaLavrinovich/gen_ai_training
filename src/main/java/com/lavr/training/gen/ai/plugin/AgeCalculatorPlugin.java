package com.lavr.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgeCalculatorPlugin {

  @DefineKernelFunction(
      name = "calculateAge",
      description = "calculate age in years based on the given birthday")
  public long calculateAge(
      @KernelFunctionParameter(
              description = "The birth date in ISO-8601 format (yyyy-MM-dd)",
              name = "birthDay")
          String birthDate) {
    log.info("Age Calculator Plugin was called with the birth date - {}", birthDate);
    try {
      LocalDate localBirthDate = LocalDate.parse(birthDate);
      LocalDate currentDate = LocalDate.now();

      long age = ChronoUnit.YEARS.between(localBirthDate, currentDate);
      if (age < 0) {
        throw new IllegalArgumentException("The user hasn't born yet");
      }

      return age;
    } catch (DateTimeParseException ex) {
      throw new IllegalArgumentException("Birthday is in the wrong pattern");
    }
  }
}

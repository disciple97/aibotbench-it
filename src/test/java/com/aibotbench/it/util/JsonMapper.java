package com.aibotbench.it.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.openunirest.http.ObjectMapper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class JsonMapper implements ObjectMapper {

  private static final com.fasterxml.jackson.databind.ObjectMapper INSTANCE;

  static {
    INSTANCE = new com.fasterxml.jackson.databind.ObjectMapper();
  }

  @Override
  @Nullable
  public <T> T readValue(String value, Class<T> valueType) {
    try {
      return INSTANCE.readValue(value, valueType);
    } catch (IOException e) {
      throw new JsonMapperException(e);
    }
  }

  @Override
  @Nullable
  public String writeValue(Object value) {
    try {
      return INSTANCE.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new JsonMapperException(e);
    }
  }
}

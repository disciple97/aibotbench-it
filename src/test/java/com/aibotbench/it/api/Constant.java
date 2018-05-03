package com.aibotbench.it.api;

import lombok.experimental.UtilityClass;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@UtilityClass
public class Constant {

  public static final String AIBOTBENCH_URL = "http://ai2.aibotbench.com/ai";

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  public static final Locale DEFAULT_LOCALE = new Locale("en", "US");
}

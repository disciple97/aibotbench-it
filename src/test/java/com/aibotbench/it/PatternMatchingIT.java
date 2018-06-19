package com.aibotbench.it;

import com.aibotbench.it.api.AiRequest;
import com.aibotbench.it.api.AiResponse;
import com.aibotbench.it.util.JsonMapper;
import io.github.openunirest.http.HttpResponse;
import io.github.openunirest.http.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aibotbench.it.api.Constant.AIBOTBENCH_URL;
import static com.aibotbench.it.api.Constant.DEFAULT_CHARSET;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;

@Slf4j
public class PatternMatchingIT {

  private static final String FILE_FORMAT = "/pattern-matching-it_%s.txt";

  private static final Pattern PATTERN = compile("(.*?);(.*)");

  private SoftAssertions softAssertions;

  @BeforeClass
  public static void setUpOnce() {
    Unirest.setObjectMapper(new JsonMapper());
  }

  @Before
  public void setUp() {
    softAssertions = new SoftAssertions();
  }

  @After
  public void tearDown() {
    softAssertions.assertAll();
  }

  @Test
  public void testEnglishPatternMatching() throws IOException {
    test(Locale.ENGLISH);
  }

  @Test
  public void testRussianPatternMatching() throws IOException {
    test(new Locale("ru", "RU", "Cyrl"));
  }

  private void test(Locale locale) throws IOException {
    try (
        InputStream in = getClass().getResourceAsStream(format(FILE_FORMAT, locale.getLanguage()));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET))
    ) {
      List<Matcher> matchers = br.lines()
          .map(l -> new ImmutablePair<>(l, PATTERN.matcher(l)))
          .filter(p -> {
            if (!p.getRight().matches()) {
              log.error("Invalid line in the input file: {}", p.getLeft());
              return false;
            }
            return true;
          })
          .map(ImmutablePair::getRight)
          .collect(toList());

      int n = matchers.size();
      log.info("Total number of patters to be matched: {}", n);

      String url = format("%s?lang=%s", AIBOTBENCH_URL, locale.getLanguage());

      matchers.forEach(m -> check(locale, url, m.group(1), m.group(2)));

      int m = softAssertions.errorsCollected().size();
      log.info("Number of collected errors - {}, success rate - {}", m, ((double) (n - m)) / n);
    }
  }

  private void check(Locale locale, String url, String message, String pattern) {

    log.info("Checking... {}", message);

    HttpResponse<AiResponse> httpResponse = Unirest.post(url)
        .header("accept", "application/json")
        .header("Content-Type", "application/json")
        .body(new AiRequest(message))
        .asObject(AiResponse.class);

    softAssertions.assertThat(httpResponse.getStatus()).isEqualTo(200);
    softAssertions
        .assertThat(httpResponse.getBody().getText().toLowerCase(locale))
        .matches(compile(pattern.toLowerCase(locale)));
  }
}

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aibotbench.it.api.Constant.AIBOTBENCH_URL;
import static com.aibotbench.it.api.Constant.DEFAULT_CHARSET;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.MULTILINE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;

@Slf4j
public class PatternMatchingITrus {
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
    public void testPatternMatching() throws IOException {
        try (
                InputStream in = getClass().getResourceAsStream("/pattern-matching-it-rus.txt");
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

            matchers.forEach(m -> check(m.group(1), m.group(2)));

            int m = softAssertions.errorsCollected().size();
            log.info("Number of collected errors - {}, success rate - {}", m, ((double) (n - m)) / n);
        }
    }

    private void check(String message, String pattern) {

        log.info("Checking... {}", message);

        HttpResponse<AiResponse> httpResponse = Unirest.post(AIBOTBENCH_URL + "?lang=ru")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new AiRequest(message))
                .asObject(AiResponse.class);

        softAssertions.assertThat(httpResponse.getStatus()).isEqualTo(200);
        softAssertions
                .assertThat(httpResponse.getBody().getText())
                .matches(compile(pattern, MULTILINE | CASE_INSENSITIVE));
    }
}

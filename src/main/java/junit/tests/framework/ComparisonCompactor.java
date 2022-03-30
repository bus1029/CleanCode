package junit.tests.framework;

public class ComparisonCompactor {
  private static final String ELLIPSES = "...";
  private static final String DELTA_END = "]";
  private static final String DELTA_START = "[";

  private final int contextLength;
  private final String expected;
  private final String actual;
  private int prefix;
  private int suffix;

  public ComparisonCompactor(int contextLength, String expected, String actual) {
    this.contextLength = contextLength;
    this.expected = expected;
    this.actual = actual;
  }

  public String compact(String message) {
    if (shouldNotCompact()) {
      return Assert.format(message, expected, actual);
    }

    findCommonPrefix();
    findCommonSuffix();
    String compactExpected = compactString(this.expected);
    String compactActual = compactString(this.actual);
    return Assert.format(message, compactExpected, compactActual);
  }

  private boolean shouldNotCompact() {
    // 무조건 만족해야 하는 조건이기 때문에 메소드 이름에 should 를 붙였다.
    return expected == null || actual == null || areStringsEqual();
  }

  private boolean areStringsEqual() {
    return expected.equals(actual);
  }

  private void findCommonPrefix() {
    prefix = 0;
    int end = Math.min(expected.length(), actual.length());
    for (; prefix < end; prefix++) {
      if (expected.charAt(prefix) != actual.charAt(prefix)) {
        break;
      }
    }
  }

  private void findCommonSuffix() {
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;
    for (;
         actualSuffix >= prefix && expectedSuffix >= prefix;
         actualSuffix--, expectedSuffix--) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
        break;
      }
    }
    suffix = expected.length() - expectedSuffix;
  }

  private String compactString(String source) {
    String result =
            DELTA_START + source.substring(prefix, source.length() - suffix + 1) + DELTA_END;
    if (prefix > 0) {
      result = computeCommonPrefix() + result;
    }
    if (suffix > 0) {
      result = result + computeCommonSuffix();
    }
    return result;
  }

  private String computeCommonPrefix() {
    return (prefix > contextLength ? ELLIPSES : "") +
            expected.substring(Math.max(0, prefix - contextLength), prefix);
  }

  private String computeCommonSuffix() {
    int end = Math.min(expected.length() - suffix + 1 + contextLength, expected.length());
    return expected.substring(expected.length() - suffix + 1, end) +
            (expected.length() - suffix + 1 < expected.length() - contextLength ? ELLIPSES : "");
  }
}

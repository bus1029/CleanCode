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
  private String compactExpected;
  private String compactActual;

  public ComparisonCompactor(int contextLength, String expected, String actual) {
    this.contextLength = contextLength;
    this.expected = expected;
    this.actual = actual;
  }

  public String formatCompactedComparison(String message) {
    if (canBeCompacted()) {
      compactExpectedAndActual();
      return Assert.format(message, compactExpected, compactActual);
    } else {
      return Assert.format(message, expected, actual);
    }
  }

  private void compactExpectedAndActual() {
    int prefixIndex = findCommonPrefix();
    int suffixIndex = findCommonSuffix(prefixIndex);
    compactExpected = compactString(this.expected);
    compactActual = compactString(this.actual);
  }

  private boolean canBeCompacted() {
    return expected != null && actual != null && !areStringsEqual();
  }

  private boolean areStringsEqual() {
    return expected.equals(actual);
  }

  private int findCommonPrefix() {
    int prefixIndex = 0;
    int end = Math.min(expected.length(), actual.length());
    for (; prefixIndex < end; prefixIndex++) {
      if (expected.charAt(prefixIndex) != actual.charAt(prefixIndex)) {
        break;
      }
    }
    return prefixIndex;
  }

  private int findCommonSuffix(int prefixIndex) {
    int expectedSuffix = expected.length() - 1;
    int actualSuffix = actual.length() - 1;
    for (;
         actualSuffix >= prefixIndex && expectedSuffix >= prefixIndex;
         actualSuffix--, expectedSuffix--) {
      if (expected.charAt(expectedSuffix) != actual.charAt(actualSuffix)) {
        break;
      }
    }
    return expected.length() - expectedSuffix;
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

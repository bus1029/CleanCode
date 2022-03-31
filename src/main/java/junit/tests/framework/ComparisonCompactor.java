package junit.tests.framework;

public class ComparisonCompactor {
  private static final String ELLIPSIS = "...";
  private static final String DELTA_END = "]";
  private static final String DELTA_START = "[";

  private final int contextLength;
  private final String expected;
  private final String actual;
  private int prefixLength;
  private int suffixLength;
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

  private boolean canBeCompacted() {
    return expected != null && actual != null && !areStringsEqual();
  }

  private boolean areStringsEqual() {
    return expected.equals(actual);
  }

  private void compactExpectedAndActual() {
    findCommonPrefixAndSuffix();
    compactExpected = compact(this.expected);
    compactActual = compact(this.actual);
  }

  private void findCommonPrefixAndSuffix() {
    findCommonPrefix();
    suffixLength = 0;
    for (; !suffixOverlapsPrefix(suffixLength); suffixLength++) {
      if (charFromEnd(expected, suffixLength) != charFromEnd(actual, suffixLength)) {
        break;
      }
    }
  }

  private void findCommonPrefix() {
    prefixLength = 0;
    int end = Math.min(expected.length(), actual.length());
    for (; prefixLength < end; prefixLength++) {
      if (expected.charAt(prefixLength) != actual.charAt(prefixLength)) {
        break;
      }
    }
  }

  private boolean suffixOverlapsPrefix(int suffixLength) {
    return actual.length() - suffixLength <= prefixLength ||
            expected.length() - suffixLength <= prefixLength;
  }

  private char charFromEnd(String s, int i) {
    return s.charAt(s.length() - i - 1);
  }

  private String compact(String source) {
    return startingEllipsis() +
            startingContext() +
            DELTA_START +
            delta(source) +
            DELTA_END +
            endingContext() +
            endingEllipsis();
  }

  private String startingEllipsis() {
    return prefixLength > contextLength ? ELLIPSIS : "";
  }

  private String startingContext() {
    int contextStart = Math.max(0, prefixLength - contextLength);
    int contextEnd = this.prefixLength;
    return expected.substring(contextStart, contextEnd);
  }

  private String delta(String s) {
    int deltaStart = this.prefixLength;
    int deltaEnd = s.length() - suffixLength;
    return s.substring(deltaStart, deltaEnd);
  }

  private String endingContext() {
    int contextStart = expected.length() - suffixLength;
    int contextEnd = Math.min(contextStart + contextLength, expected.length());
    return expected.substring(contextStart, contextEnd);
  }

  private String endingEllipsis() {
    return suffixLength > contextLength ? ELLIPSIS : "";
  }
}

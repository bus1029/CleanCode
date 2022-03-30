package junit.tests.framework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComparisonCompactorTest {
  @Test
  void testMessage() {
    String failure = new ComparisonCompactor(0, "b", "c").compact("a");
    assertEquals("a expected:<[b]> but was:<[c]>", failure);
  }

  @Test
  void testStartSame() {
    String failure = new ComparisonCompactor(1, "ba", "bc").compact(null);
    assertEquals("expected:<b[a]> but was:<b[c]>", failure);
  }

  @Test
  void testEndSame() {
    String failure = new ComparisonCompactor(1, "ab", "cb").compact(null);
    assertEquals("expected:<[a]b> but was:<[c]b>", failure);
  }

  @Test
  void testSame() {
    String failure = new ComparisonCompactor(1, "ab", "ab").compact(null);
    assertEquals("expected:<ab> but was:<ab>", failure);
  }

  @Test
  void testNoContextStartAndEndSame() {
    String failure = new ComparisonCompactor(0, "abc", "adc").compact(null);
    assertEquals("expected:<...[b]...> but was:<...[d]...>", failure);
  }

  @Test
  void testStartAndEndContext() {
    String failure = new ComparisonCompactor(1, "abc", "adc").compact(null);
    assertEquals("expected:<a[b]c> but was:<a[d]c>", failure);
  }

  @Test
  void testStartAndEndContextWithEllipses() {
    String failure = new ComparisonCompactor(1, "abcde", "abfde").compact(null);
    assertEquals("expected:<...b[c]d...> but was:<...b[f]d...>", failure);
  }

  @Test
  void testComparisonErrorStartSameComplete() {
    String failure = new ComparisonCompactor(2, "ab", "abc").compact(null);
    assertEquals("expected:<ab[]> but was:<ab[c]>", failure);
  }

  @Test
  void testComparisonErrorEndSameComplete() {
    String failure = new ComparisonCompactor(0, "bc", "abc").compact(null);
    assertEquals("expected:<[]...> but was:<[a]...>", failure);
  }

  @Test
  void testComparisonErrorEndSameCompleteContext() {
    String failure = new ComparisonCompactor(2, "bc", "abc").compact(null);
    assertEquals("expected:<[]bc> but was:<[a]bc>", failure);
  }

  @Test
  void testComparisonErrorOverlappingMatches() {
    String failure = new ComparisonCompactor(0, "abc", "abbc").compact(null);
    assertEquals("expected:<...[]...> but was:<...[b]...>", failure);
  }

  @Test
  void testComparisonErrorOverlappingMatchesContext() {
    String failure = new ComparisonCompactor(2, "abc", "abbc").compact(null);
    assertEquals("expected:<ab[]c> but was:<ab[b]c>", failure);
  }

  @Test
  void testComparisonErrorOverlappingMatches2() {
    String failure = new ComparisonCompactor(0, "abcdde", "abcde").compact(null);
    assertEquals("expected:<...[d]...> but was:<...[]...>", failure);
  }

  @Test
  void testComparisonErrorOverlappingMatches2Context() {
    String failure = new ComparisonCompactor(2, "abcdde", "abcde").compact(null);
    assertEquals("expected:<...cd[d]e> but was:<...cd[]e>", failure);
  }

  @Test
  void testComparisonErrorWithActualNull() {
    String failure = new ComparisonCompactor(0, "a", null).compact(null);
    assertEquals("expected:<a> but was:<null>", failure);
  }

  @Test
  void testComparisonErrorWithActualNullContext() {
    String failure = new ComparisonCompactor(2, "a", null).compact(null);
    assertEquals("expected:<a> but was:<null>", failure);
  }

  @Test
  void testComparisonErrorWithExpectedNull() {
    String failure = new ComparisonCompactor(0, null, "a").compact(null);
    assertEquals("expected:<null> but was:<a>", failure);
  }

  @Test
  void testComparisonErrorWithExpectedNullContext() {
    String failure = new ComparisonCompactor(2, null, "a").compact(null);
    assertEquals("expected:<null> but was:<a>", failure);
  }

  @Test
  void testBug609972() {
    String failure = new ComparisonCompactor(10, "S&P500", "0").compact(null);
    assertEquals("expected:<[S&P50]0> but was:<[]0>", failure);
  }
}

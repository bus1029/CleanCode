package generator;

/**
 * 이 클래스는 사용자가 지정한 최대 값까지 소수를 구한다.
 * 알고리즘은 에라스토테네스의 체다.
 * 2에서 시작하는 정수 배열을 대상으로 작업한다.
 * 최대 값까지 정수를 찾아 그에 해당하는 배수를 모두 제거한다.
 * 배열에 더 이상 배수가 없을 때까지 반복한다.
 */
public class PrimeGenerator {
  private static boolean[] crossedOut;
  private static int[] result;

  private PrimeGenerator() {
  }

  public static int[] generatePrimes(int maxValue) {
    if (maxValue < 2) {
      return new int[0];
    }

    uncrossIntegersUpTo(maxValue);
    crossOutMultiples();
    putUncrossedIntegersIntoResult();
    return result;
  }

  private static void uncrossIntegersUpTo(int maxValue) {
    crossedOut = new boolean[maxValue + 1];
  }

  private static void crossOutMultiples() {
    int limit = determineIterationLimit();
    for (int i = 2; i <= limit; i++) {
      if (isNotCrossed(i)) {
        crossOutMultiplesOf(i);
      }
    }
  }

  private static int determineIterationLimit() {
    // 배열에 있는 모든 배수는 배열 크기의 제곱근보다 작은 소수의 인수이다.
    // 따라서 이 제곱근보다 더 큰 숫자의 배수는 제거할 필요가 없다.
    double iterationLimit = Math.sqrt(crossedOut.length);
    return (int) iterationLimit;
  }

  private static boolean isNotCrossed(int i) {
    return !crossedOut[i];
  }

  private static void crossOutMultiplesOf(int i) {
    for (int multiple = 2*i; multiple < crossedOut.length; multiple += i) {
      crossedOut[multiple] = true;
    }
  }

  private static void putUncrossedIntegersIntoResult() {
    result = new int[numberOfUncrossedIntegers()];
    for (int j = 0, i = 2; i < crossedOut.length; i++) {
      if (isNotCrossed(i)) {
        result[j++] = i;
      }
    }
  }

  private static int numberOfUncrossedIntegers() {
    int count = 0;
    for (int i = 2; i < crossedOut.length; i++) {
      if (isNotCrossed(i)) {
        count++;
      }
    }
    return count;
  }
}

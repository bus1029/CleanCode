package generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimeGeneratorTest {
  @Test
  void testPrimeGenerator_success() {
    int[] primes = PrimeGenerator.generatePrimes(10);
    assertEquals(4, primes.length);
  }

  @Test
  void testPrimeGenerator_empty() {
    int[] primes = PrimeGenerator.generatePrimes(1);
    assertEquals(0, primes.length);
  }
}
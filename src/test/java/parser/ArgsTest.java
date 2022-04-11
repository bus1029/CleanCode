package parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArgsTest {
  @Test
  void testBooleanArgs_success() {
    String[] args = {"-t", "-f", "-a", "-s"};
    Args parser = new Args("t,f,a,s", args);

    assertThat(parser.isValid()).isTrue();
    assertThat(parser.cardinality()).isEqualTo(4);
    assertThat(parser.errorMessage()).isEmpty();
    assertThat(parser.usage()).isNotEmpty();
    assertThat(parser.getBoolean('t')).isTrue();
    assertThat(parser.getBoolean('f')).isTrue();
    assertThat(parser.getBoolean('a')).isTrue();
    assertThat(parser.getBoolean('s')).isTrue();
  }

  @Test
  void testBooleanArgs_failForEmptyArgs() {
    String[] args = {};
    Args parser = new Args("t,f,a,s", args);

    assertThat(parser.isValid()).isTrue();
    assertThat(parser.cardinality()).isZero();
    assertThat(parser.getBoolean('t')).isFalse();
    assertThat(parser.getBoolean('f')).isFalse();
    assertThat(parser.getBoolean('a')).isFalse();
    assertThat(parser.getBoolean('s')).isFalse();
  }

  @Test
  void testBooleanArgs_partialSuccess() {
    String[] args = {"-t", "-as", "#"};
    Args parser = new Args("t,a,#,$", args);

    assertThat(parser.isValid()).isFalse();
    assertThat(parser.errorMessage()).isNotEmpty();
    assertThat(parser.cardinality()).isEqualTo(2);
    assertThat(parser.getBoolean('t')).isTrue();
    assertThat(parser.getBoolean('a')).isTrue();
  }

  @Test
  void testBooleanArgs_wrongFormatSchema() {
    String[] args = {"-t", "-f", "-a", "-s"};
    Args parser = new Args("tf,-a, -s", args);

    assertThat(parser.isValid()).isFalse();
    assertThat(parser.errorMessage()).isNotEmpty();
    assertThat(parser.cardinality()).isZero();
  }

  @Test
  void testBooleanArgs_emptySchemaAndArguments() {
    String[] args = {};
    Args parser = new Args("", args);

    assertThat(parser.usage()).isEmpty();
    assertThat(parser.isValid()).isTrue();
  }
}
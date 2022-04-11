package junit.tests.framework;

public class Assert {
  private Assert() {
  }

  public static String format(String msg, String s1, String s2) {
    if (msg == null) {
      return "expected:<" + s1 + ">" + " but was:<" + s2 + ">";
    }

    return msg + " expected:<" + s1 + ">" + " but was:<" + s2 + ">";
  }
}

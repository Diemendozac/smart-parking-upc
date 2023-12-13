package com.smartparkingupc.utils;

import java.util.regex.Pattern;

public class EmailValidator {

  private static final Pattern PATTERN = Pattern.compile("^[a-z0-9.-]+@unicesar.edu.co$");

  public static boolean isValid(String domain) {
    return PATTERN.matcher(domain).matches();
  }

}

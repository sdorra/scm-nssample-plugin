package com.cloudogu.scm.nssample;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import sonia.scm.user.User;

final class Permissions {

  private Permissions() {}

  static boolean isPermitted(User user) {
    return isPermitted(user.getName());
  }

  static boolean isPermitted(String username) {
    Subject subject = SecurityUtils.getSubject();
    return subject.isPermitted(permission(username));
  }

  static void checkPermission(String username) {
    Subject subject = SecurityUtils.getSubject();
    subject.checkPermission(permission(username));
  }

  private static String permission(String username) {
    return "user:configurableNamespace:".concat(username);
  }
}

package com.cloudogu.scm.nssample;

import org.apache.shiro.SecurityUtils;
import sonia.scm.plugin.Extension;
import sonia.scm.repository.NamespaceStrategy;
import sonia.scm.repository.Repository;

import javax.inject.Inject;

@Extension
public class ConfigurableNamespaceStrategy implements NamespaceStrategy {

  private final NamespaceStore namespaceStore;

  @Inject
  public ConfigurableNamespaceStrategy(NamespaceStore namespaceStore) {
    this.namespaceStore = namespaceStore;
  }

  @Override
  public String createNamespace(Repository repository) {
    String principal = SecurityUtils.getSubject().getPrincipal().toString();
    return namespaceStore.getNamespace(principal);
  }

}

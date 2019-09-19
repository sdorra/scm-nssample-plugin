package com.cloudogu.scm.nssample;

import com.google.common.base.MoreObjects;
import sonia.scm.store.ConfigurationEntryStore;
import sonia.scm.store.ConfigurationEntryStoreFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NamespaceStore {

  private static final String NAME = "configurable-namespace";

  private final ConfigurationEntryStore<String> store;

  @Inject
  public NamespaceStore(ConfigurationEntryStoreFactory storeFactory) {
    this.store = storeFactory.withType(String.class).withName(NAME).build();
  }

  String getNamespace(String username) {
    return MoreObjects.firstNonNull(store.get(username), username);
  }

  void setNamespace(String username, String namespace) {
    Permissions.checkPermission(username);
    this.store.put(username, namespace);
  }
}

package com.cloudogu.scm.nssample;

import sonia.scm.api.v2.resources.Enrich;
import sonia.scm.api.v2.resources.HalAppender;
import sonia.scm.api.v2.resources.HalEnricher;
import sonia.scm.api.v2.resources.HalEnricherContext;
import sonia.scm.api.v2.resources.LinkBuilder;
import sonia.scm.api.v2.resources.ScmPathInfoStore;
import sonia.scm.plugin.Extension;
import sonia.scm.user.User;

import javax.inject.Inject;
import javax.inject.Provider;

@Extension
@Enrich(User.class)
public class NamespaceUserLinkEnricher implements HalEnricher {

  private final Provider<ScmPathInfoStore> scmPathInfoStore;

  @Inject
  public NamespaceUserLinkEnricher(Provider<ScmPathInfoStore> scmPathInfoStore) {
    this.scmPathInfoStore = scmPathInfoStore;
  }

  @Override
  public void enrich(HalEnricherContext context, HalAppender appender) {
    User user = context.oneRequireByType(User.class);
    if (Permissions.isPermitted(user)) {
      appendLink(appender, user);
    }
  }

  private void appendLink(HalAppender appender, User user) {
    String href = new LinkBuilder(scmPathInfoStore.get().get(), NamespaceUserResource.class)
            .method("getNamespace")
            .parameters(user.getName())
            .href();

    appender.appendLink("namespace", href);
  }
}

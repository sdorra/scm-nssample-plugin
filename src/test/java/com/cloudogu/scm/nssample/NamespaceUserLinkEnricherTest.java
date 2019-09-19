package com.cloudogu.scm.nssample;

import com.google.inject.util.Providers;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sonia.scm.api.v2.resources.HalAppender;
import sonia.scm.api.v2.resources.HalEnricherContext;
import sonia.scm.api.v2.resources.ScmPathInfoStore;
import sonia.scm.user.User;
import sonia.scm.user.UserTestData;

import javax.inject.Provider;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NamespaceUserLinkEnricherTest {

  @Mock
  private HalEnricherContext context;

  @Mock
  private HalAppender appender;

  private NamespaceUserLinkEnricher enricher;

  @Mock
  private Subject subject;

  @BeforeEach
  void setupSubject() {
    ThreadContext.bind(subject);
  }

  @AfterEach
  void tearDownSubject() {
    ThreadContext.unbindSubject();
  }

  @BeforeEach
  void setupEnricher() {
    ScmPathInfoStore pathInfoStore = new ScmPathInfoStore();
    pathInfoStore.set(() -> URI.create("/"));
    Provider<ScmPathInfoStore> pathInfoStoreProvider = Providers.of(pathInfoStore);
    enricher = new NamespaceUserLinkEnricher(pathInfoStoreProvider);
  }

  @Test
  void shouldNotAppendNamespaceLinkWithoutPermission() {
    User trillian = UserTestData.createTrillian();
    when(context.oneRequireByType(User.class)).thenReturn(trillian);

    enricher.enrich(context, appender);
    verify(appender, never()).appendLink(any(), any());
  }

  @Test
  void shouldAppendNamespaceLink() {
    User trillian = UserTestData.createTrillian();
    when(context.oneRequireByType(User.class)).thenReturn(trillian);
    when(subject.isPermitted("user:configurableNamespace:trillian")).thenReturn(true);

    enricher.enrich(context, appender);

    verify(appender).appendLink("namespace", "/v2/namespaces/user/trillian");
  }

}

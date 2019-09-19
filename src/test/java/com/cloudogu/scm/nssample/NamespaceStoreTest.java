package com.cloudogu.scm.nssample;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sonia.scm.store.InMemoryConfigurationEntryStoreFactory;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;


@ExtendWith(MockitoExtension.class)
class NamespaceStoreTest {

  private NamespaceStore store;

  @BeforeEach
  void setupStore() {
    InMemoryConfigurationEntryStoreFactory factory = InMemoryConfigurationEntryStoreFactory.create();
    this.store = new NamespaceStore(factory);
  }

  @Test
  void shouldReturnUsernameWithoutNamespace() {
    String namespace = store.getNamespace("trillian");
    assertThat(namespace).isEqualTo("trillian");
  }

  @Nested
  class WithSubject {

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

    @Test
    void shouldGetAndSetNamespace() {
      store.setNamespace("trillian", "hitchhiker");
      String namespace = store.getNamespace("trillian");
      assertThat(namespace).isEqualTo("hitchhiker");
    }

    @Test
    void shouldThrowAuthorizationException() {
      doThrow(AuthorizationException.class).when(subject).checkPermission("user:configurableNamespace:trillian");
      assertThrows(
        AuthorizationException.class,
        () -> store.setNamespace("trillian", "hitchhiker")
      );
    }

  }

}

package com.cloudogu.scm.nssample;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sonia.scm.repository.RepositoryTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigurableNamespaceStrategyTest {

  @Mock
  private NamespaceStore store;

  @InjectMocks
  private ConfigurableNamespaceStrategy strategy;

  @Mock
  private Subject subject;

  @BeforeEach
  void setUpSubject() {
    ThreadContext.bind(subject);
  }

  @AfterEach
  void tearDownSubject() {
    ThreadContext.unbindSubject();
  }

  @Test
  void shouldCreateNamespaceWithPrincipal() {
    when(subject.getPrincipal()).thenReturn("trillian");
    when(store.getNamespace("trillian")).thenReturn("hitchhiker");

    String namespace = strategy.createNamespace(RepositoryTestData.createHeartOfGold());
    assertThat(namespace).isEqualTo("hitchhiker");
  }
}

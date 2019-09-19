package com.cloudogu.scm.nssample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NamespaceUserResourceTest {

  @Mock
  private NamespaceStore store;

  @InjectMocks
  private NamespaceUserResource resource;

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
  void shouldThrowAuthorizationExceptionForGet() {
    doThrow(AuthorizationException.class).when(subject).checkPermission("user:configurableNamespace:trillian");

    assertThrows(AuthorizationException.class, () -> resource.getNamespace(null, "trillian"));
  }

  @Test
  void shouldThrowAuthorizationExceptionForSet() {
    doThrow(AuthorizationException.class).when(subject).checkPermission("user:configurableNamespace:trillian");

    NamespaceDto dto = new NamespaceDto("/v2", "hitchhiker");
    assertThrows(AuthorizationException.class, () -> resource.setNamespace("trillian", dto));
  }

  @Nested
  class WithDispatcher {

    private final ObjectMapper mapper = new ObjectMapper();

    private Dispatcher dispatcher;

    @BeforeEach
    void setupDispatcher() {
      dispatcher = MockDispatcherFactory.createDispatcher();
      dispatcher.getRegistry().addSingletonResource(resource);
    }

    @Test
    void shouldReturnNamespaceDto() throws URISyntaxException, IOException {
      when(store.getNamespace("trillian")).thenReturn("hitchhiker");

      MockHttpRequest request = MockHttpRequest.get("/v2/namespaces/user/trillian");
      MockHttpResponse response = new MockHttpResponse();

      dispatcher.invoke(request, response);

      assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
      assertThat(response.getOutputHeaders().getFirst("Content-Type")).isEqualTo(NamespaceUserResource.MEDIA_TYPE);

      NamespaceDto namespaceDto = mapper.readValue(response.getOutput(), NamespaceDto.class);
      assertThat(namespaceDto.getNamespace()).isEqualTo("hitchhiker");
      assertThat(namespaceDto.getLinks().getLinkBy("self").get().getHref()).isEqualTo("/v2/namespaces/user/trillian");
      assertThat(namespaceDto.getLinks().getLinkBy("update").get().getHref()).isEqualTo("/v2/namespaces/user/trillian");
    }

    @Test
    void shouldUpdateNamespaceFromDto() throws URISyntaxException, JsonProcessingException {
      NamespaceDto dto = new NamespaceDto("/v2/namespaces/user/trillian", "hitchhiker");

      MockHttpRequest request = MockHttpRequest.put("/v2/namespaces/user/trillian");
      request.contentType(NamespaceUserResource.MEDIA_TYPE);
      request.content(mapper.writeValueAsBytes(dto));
      MockHttpResponse response = new MockHttpResponse();

      dispatcher.invoke(request, response);

      assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NO_CONTENT);

      verify(store).setNamespace("trillian", "hitchhiker");
    }

  }
}

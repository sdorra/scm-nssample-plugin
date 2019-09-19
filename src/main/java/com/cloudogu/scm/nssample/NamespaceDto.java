package com.cloudogu.scm.nssample;

import de.otto.edison.hal.HalRepresentation;
import de.otto.edison.hal.Link;
import de.otto.edison.hal.Links;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NamespaceDto extends HalRepresentation {

  private String namespace;

  public NamespaceDto(String uri, String namespace) {
    super(Links.linkingTo().self(uri).single(Link.link("update", uri)).build());
    this.namespace = namespace;
  }
}

package com.cloudogu.scm.nssample;

import com.google.common.annotations.VisibleForTesting;
import sonia.scm.web.VndMediaType;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("v2/namespaces/user/{username}")
public class NamespaceUserResource {

  @VisibleForTesting
  static final String MEDIA_TYPE = VndMediaType.PREFIX + "configurablenamespace" + VndMediaType.SUFFIX;

  private final NamespaceStore namespaceStore;

  @Inject
  public NamespaceUserResource(NamespaceStore namespaceStore) {
    this.namespaceStore = namespaceStore;
  }

  @GET
  @Path("")
  @Produces(MEDIA_TYPE)
  public NamespaceDto getNamespace(@Context UriInfo uriInfo, @PathParam("username") String username) {
    Permissions.checkPermission(username);
    return new NamespaceDto(uriInfo.getAbsolutePath().toASCIIString(), namespaceStore.getNamespace(username));
  }

  @PUT
  @Path("")
  @Consumes(MEDIA_TYPE)
  public Response setNamespace(@PathParam("username") String username, NamespaceDto dto) {
    Permissions.checkPermission(username);
    namespaceStore.setNamespace(username, dto.getNamespace());
    return Response.noContent().build();
  }
}

package com.psira;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
      //  responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200");
      //  responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
      //  responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, Authorization, Content-Type");
      //  responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}

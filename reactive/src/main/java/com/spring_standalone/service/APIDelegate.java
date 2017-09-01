package com.spring_standalone.service;

import com.spring_standalone.exception.AppException;

import java.util.concurrent.CompletableFuture;

public class APIDelegate {

  private APILookup lookupService;
  private ProviderService providerService;
  private APIType serviceType;

  public void setLookupService(APILookup lookupService) {
    this.lookupService = lookupService;
  }

  public void setServiceType(APIType serviceType) {
    this.serviceType = serviceType;
  }

  public <T> CompletableFuture<T> doSearch(String name) {
    providerService = lookupService.getService(serviceType);
    if(providerService == null)
      throw new AppException("200", "Unable to find service provider");

    return providerService.search(name);
  }
}

@Bean @ConditionalOnMissingBean public CloudFoundryApplicationFactory applicationFactory(InstanceProperties instance,ManagementServerProperties management,ServerProperties server,PathMappedEndpoints pathMappedEndpoints,WebEndpointProperties webEndpoint,ObjectProvider<List<MetadataContributor>> metadataContributors,CloudFoundryApplicationProperties cfApplicationProperties){
  return new CloudFoundryApplicationFactory(instance,management,server,pathMappedEndpoints,webEndpoint,new CompositeMetadataContributor(metadataContributors.getIfAvailable(Collections::emptyList)),cfApplicationProperties);
}
public void compare(){
  Triple<Set<String>,Set<String>,Set<String>> result=getDiff(current.getClusters().keySet(),future.getClusters().keySet());
  Set<String> addedClusterIds=result.getFirst();
  Set<String> intersectionClusterIds=result.getMiddle();
  Set<String> deletedClusterIds=result.getLast();
  for (  String clusterId : addedClusterIds) {
    added.add(future.findCluster(clusterId));
  }
  for (  String clusterId : deletedClusterIds) {
    removed.add(current.findCluster(clusterId));
  }
  for (  String clusterId : intersectionClusterIds) {
    ClusterMeta currentMeta=current.findCluster(clusterId);
    ClusterMeta futureMeta=future.findCluster(clusterId);
    if (!reflectionEquals(currentMeta,futureMeta)) {
      ClusterMetaComparator clusterMetaComparator=new ClusterMetaComparator(currentMeta,futureMeta);
      clusterMetaComparator.compare();
      modified.add(clusterMetaComparator);
    }
  }
}

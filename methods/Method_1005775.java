public static ImmutableList<Rule> compose(JvmTarget target,RuleType ruleType){
  List<String> deps=ImmutableList.<String>builder().addAll(external(target.getExternalDeps(false))).addAll(targets(target.getTargetDeps(false))).build();
  Set<String> aptDeps=ImmutableSet.<String>builder().addAll(externalApt(target.getExternalAptDeps(false))).addAll(targetsApt(target.getTargetAptDeps(false))).build();
  Set<String> providedDeps=ImmutableSet.<String>builder().addAll(external(target.getExternalProvidedDeps(false))).addAll(targets(target.getTargetProvidedDeps(false))).build();
  Set<String> exportedDeps=ImmutableSet.<String>builder().addAll(external(target.getExternalExportedDeps(false))).addAll(targets(target.getTargetExportedDeps(false))).build();
  List<String> testTargets=!target.getTest().getSources().isEmpty() ? ImmutableList.of(":" + test(target)) : ImmutableList.of();
  ImmutableList.Builder<Rule> rulesBuilder=new ImmutableList.Builder<>();
  rulesBuilder.add(new JvmRule().srcs(target.getMain().getSources()).exts(ruleType.getProperties()).apPlugins(getApPlugins(target.getApPlugins())).aptDeps(aptDeps).providedDeps(providedDeps).exportedDeps(exportedDeps).resources(target.getMain().getJavaResources()).sourceCompatibility(target.getSourceCompatibility()).targetCompatibility(target.getTargetCompatibility()).mavenCoords(target.getMavenCoords()).testTargets(testTargets).options(target.getMain().getCustomOptions()).ruleType(ruleType.getBuckName()).defaultVisibility().deps(deps).name(src(target)).extraBuckOpts(target.getExtraOpts(ruleType)));
  if (target.hasApplication()) {
    rulesBuilder.add(new JvmBinaryRule().mainClassName(target.getMainClass()).excludes(target.getExcludes()).defaultVisibility().name(bin(target)).deps(ImmutableSet.of(":" + src(target))).ruleType(RuleType.JAVA_BINARY.getBuckName()).extraBuckOpts(target.getExtraOpts(RuleType.JAVA_BINARY)));
  }
  return rulesBuilder.build();
}

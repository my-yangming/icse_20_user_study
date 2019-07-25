public static Rule compose(AndroidLibTarget target,String manifestRule,List<String> deps,List<String> aidlRuleNames,@Nullable String appClass){
  Set<String> libraryDeps=new HashSet<>(deps);
  libraryDeps.addAll(external(target.getExternalDeps(false)));
  libraryDeps.addAll(targets(target.getTargetDeps(false)));
  libraryDeps.addAll(resources(target.getTargetDeps(false)));
  libraryDeps.addAll(resources(target.getTargetExportedDeps(false)));
  List<String> libraryAptDeps=new ArrayList<>();
  libraryAptDeps.addAll(externalApt(target.getExternalAptDeps(false)));
  libraryAptDeps.addAll(targetsApt(target.getTargetAptDeps(false)));
  Set<String> providedDeps=new HashSet<>();
  providedDeps.addAll(external(target.getExternalProvidedDeps(false)));
  providedDeps.addAll(targets(target.getTargetProvidedDeps(false)));
  providedDeps.add(D8Util.RT_STUB_JAR_RULE);
  Set<String> libraryExportedDeps=new HashSet<>();
  libraryExportedDeps.addAll(external(target.getExternalExportedDeps(false)));
  libraryExportedDeps.addAll(targets(target.getTargetExportedDeps(false)));
  libraryExportedDeps.addAll(aidlRuleNames);
  List<String> testTargets=new ArrayList<>();
  if (target.getRobolectricEnabled() && !target.getTest().getSources().isEmpty()) {
    testTargets.add(":" + test(target));
  }
  if (target.getLibInstrumentationTarget() != null && !target.getLibInstrumentationTarget().getMain().getSources().isEmpty()) {
    testTargets.add(":" + AndroidBuckRuleComposer.bin(target.getLibInstrumentationTarget()));
  }
  AndroidRule androidRule=new AndroidRule().srcs(target.getMain().getSources()).exts(target.getRuleType().getProperties()).manifest(manifestRule).proguardConfig(target.getConsumerProguardConfig()).apPlugins(getApPlugins(target.getApPlugins())).aptDeps(libraryAptDeps).providedDeps(providedDeps).exportedDeps(libraryExportedDeps).resources(target.getMain().getJavaResources()).resDirs(target.getResDirs()).sourceCompatibility(target.getSourceCompatibility()).targetCompatibility(target.getTargetCompatibility()).testTargets(testTargets).excludes(appClass != null ? ImmutableSet.of(appClass) : ImmutableSet.of()).generateR2(target.getGenerateR2()).options(target.getMain().getCustomOptions());
  if (target.getRuleType().equals(RuleType.KOTLIN_ANDROID_LIBRARY)) {
    androidRule.language("kotlin");
  }
  if (target.getLintEnabled()) {
    String lintConfigPath;
    if (target.getLintOptions() != null && target.getLintOptions().getLintConfig() != null && target.getLintOptions().getLintConfig().exists()) {
      lintConfigPath=FileUtil.getRelativePath(target.getRootProject().getProjectDir(),target.getLintOptions().getLintConfig());
      ProjectUtil.getPlugin(target.getRootProject()).exportedPaths.add(lintConfigPath);
    }
 else {
      lintConfigPath=null;
    }
    Set<String> customLintTargets=target.getLint().getTargetDeps().stream().filter(t -> (t instanceof JvmTarget)).map(BuckRuleComposer::binTargets).collect(Collectors.toSet());
    if (lintConfigPath != null) {
      androidRule.lintConfigXml(fileRule(lintConfigPath));
    }
    androidRule.customLints(customLintTargets).lintOptions(target.getLintOptions());
  }
 else {
    androidRule.disableLint(true);
  }
  return androidRule.ruleType(target.getRuleType().getBuckName()).defaultVisibility().deps(libraryDeps).name(src(target)).extraBuckOpts(target.getExtraOpts(RuleType.ANDROID_LIBRARY));
}

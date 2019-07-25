@Override protected void __associate(RockerTemplate context){
  super.__associate(context);
  if (context instanceof RuleTemplate) {
    RuleTemplate ninjaContext=(RuleTemplate)context;
    this.ruleType=ninjaContext.ruleType;
    this.name=ninjaContext.name;
    this.visibility=ninjaContext.visibility;
    this.fileConfiguredVisibility=ninjaContext.fileConfiguredVisibility;
    this.deps=ninjaContext.deps;
    this.labels=ninjaContext.labels;
    this.extraBuckOpts=ninjaContext.extraBuckOpts;
  }
 else {
    throw new IllegalArgumentException("Unable to associate (context was not an instance of " + RuleTemplate.class.getCanonicalName() + ")");
  }
}

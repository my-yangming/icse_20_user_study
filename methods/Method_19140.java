@Override public Set<Element> extract(RoundEnvironment roundEnvironment){
  return (Set<Element>)roundEnvironment.getElementsAnnotatedWith(GroupSectionSpec.class);
}

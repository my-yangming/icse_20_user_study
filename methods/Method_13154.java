@Override public void index(API api,Container.Entry entry,Indexes indexes){
  super.index(api,entry,indexes);
  new EjbJarXmlPathFinder(entry,indexes).find(TextReader.getText(entry.getInputStream()));
}

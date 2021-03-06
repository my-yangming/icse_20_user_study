public static Collection<Container.Entry> removeInnerTypeEntries(Collection<Container.Entry> entries){
  HashSet<String> potentialOuterTypePaths=new HashSet<>();
  Collection<Container.Entry> filtredSubEntries;
  for (  Container.Entry e : entries) {
    if (!e.isDirectory()) {
      String p=e.getPath();
      if (p.toLowerCase().endsWith(".class")) {
        int lastSeparatorIndex=p.lastIndexOf('/');
        int dollarIndex=p.substring(lastSeparatorIndex + 1).indexOf('$');
        if (dollarIndex != -1) {
          potentialOuterTypePaths.add(p.substring(0,lastSeparatorIndex + 1 + dollarIndex) + ".class");
        }
      }
    }
  }
  if (potentialOuterTypePaths.size() == 0) {
    filtredSubEntries=entries;
  }
 else {
    HashSet<String> innerTypePaths=new HashSet<>();
    for (    Container.Entry e : entries) {
      if (!e.isDirectory() && potentialOuterTypePaths.contains(e.getPath())) {
        populateInnerTypePaths(innerTypePaths,e);
      }
    }
    filtredSubEntries=new ArrayList<>();
    for (    Container.Entry e : entries) {
      if (!e.isDirectory()) {
        String p=e.getPath();
        if (p.toLowerCase().endsWith(".class")) {
          int indexDollar=p.lastIndexOf('$');
          if (indexDollar != -1) {
            int indexSeparator=p.lastIndexOf('/');
            if (indexDollar > indexSeparator) {
              if (innerTypePaths.contains(p)) {
                continue;
              }
 else {
                populateInnerTypePaths(innerTypePaths,e);
                if (innerTypePaths.contains(p)) {
                  continue;
                }
              }
            }
          }
        }
      }
      filtredSubEntries.add(e);
    }
  }
  List<Container.Entry> list=new ArrayList<>(filtredSubEntries);
  list.sort(ContainerEntryComparator.COMPARATOR);
  return list;
}

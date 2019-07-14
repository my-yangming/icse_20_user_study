public static void assign(Node pattern,Value value,Scope env){
  if (pattern instanceof Name) {
    String id=((Name)pattern).id;
    Scope d=env.findDefiningScope(id);
    if (d == null) {
      _.abort(pattern,"assigned name was not defined: " + id);
    }
 else {
      d.putValue(id,value);
    }
  }
 else   if (pattern instanceof Subscript) {
    ((Subscript)pattern).set(value,env);
  }
 else   if (pattern instanceof Attr) {
    ((Attr)pattern).set(value,env);
  }
 else   if (pattern instanceof RecordLiteral) {
    if (value instanceof RecordType) {
      Map<String,Node> elms1=((RecordLiteral)pattern).map;
      Scope elms2=((RecordType)value).properties;
      if (elms1.keySet().equals(elms2.keySet())) {
        for (        String k1 : elms1.keySet()) {
          assign(elms1.get(k1),elms2.lookupLocal(k1),env);
        }
      }
 else {
        _.abort(pattern,"assign with records of different attributes: " + elms1.keySet() + " v.s. " + elms2.keySet());
      }
    }
 else {
      _.abort(pattern,"assign with incompatible types: record and " + value);
    }
  }
 else   if (pattern instanceof VectorLiteral) {
    if (value instanceof Vector) {
      List<Node> elms1=((VectorLiteral)pattern).elements;
      List<Value> elms2=((Vector)value).values;
      if (elms1.size() == elms2.size()) {
        for (int i=0; i < elms1.size(); i++) {
          assign(elms1.get(i),elms2.get(i),env);
        }
      }
 else {
        _.abort(pattern,"assign vectors of different sizes: " + elms1.size() + " v.s. " + elms2.size());
      }
    }
 else {
      _.abort(pattern,"assign incompatible types: vector and " + value);
    }
  }
 else {
    _.abort(pattern,"unsupported pattern of assign: " + pattern);
  }
}

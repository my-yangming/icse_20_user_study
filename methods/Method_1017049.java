default Expression add(Expression other){
  throw getContext().error(String.format("%s: unsupported binary operator: +",this));
}

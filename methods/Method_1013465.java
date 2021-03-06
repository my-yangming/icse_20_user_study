public final int size(){
  try {
    Assert.fail("Attempted to compute the number of elements in the integer " + Values.ppr(this.toString()) + ".");
    return 0;
  }
 catch (  RuntimeException|OutOfMemoryError e) {
    if (hasSource()) {
      throw FingerprintException.getNewHead(this,e);
    }
 else {
      throw e;
    }
  }
}

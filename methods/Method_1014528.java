@Override public Status execute(Target src,int srcIndex,Status status){
  if (status.isSignFlag() == status.isOverflowFlag() && !status.isZeroFlag()) {
    cpu.setIp((char)src.get(srcIndex));
  }
  return status;
}

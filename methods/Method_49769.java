public InetSocketAddress getSocketAddress(){
  InetSocketAddress inetSocketAddress=null;
  try {
    inetSocketAddress=new InetSocketAddress(mHost,mPort);
  }
 catch (  IllegalArgumentException e) {
  }
  return inetSocketAddress;
}

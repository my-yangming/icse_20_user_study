@Override public MultivaluedMap<String,String> update(MultivaluedMap<String,String> incomingHeaders,MultivaluedMap<String,String> clientOutgoingHeaders){
  MultivaluedHashMap<String,String> headers=new MultivaluedHashMap<>();
  String userAgent=incomingHeaders.getFirst("user-agent");
  headers.putSingle("baggage-user-agent",userAgent);
  String authorization=incomingHeaders.getFirst("Authorization");
  if (authorization != null) {
    headers.putSingle("Authorization",authorization);
  }
  String userPreference=incomingHeaders.getFirst("user-preference");
  if (userPreference != null) {
    headers.putSingle("user-preference",userPreference);
  }
  return headers;
}

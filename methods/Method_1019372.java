String generate(String content) throws Exception {
  if (content == null) {
    content="";
  }
  MessageDigest digest=MessageDigest.getInstance(ALGORITHM);
  return DatatypeConverter.printHexBinary(digest.digest(content.getBytes("UTF-8"))).toLowerCase();
}

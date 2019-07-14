private static String extractAddrSpec(String address){
  Matcher match=NAME_ADDR_EMAIL_PATTERN.matcher(address);
  if (match.matches()) {
    return match.group(2);
  }
  return address;
}

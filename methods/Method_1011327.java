@Nullable public static String check(String caption,String character){
  assert character.length() == 1;
  if (!(Character.isLetter(character.charAt(0))) || caption.indexOf(character) == -1) {
    return "mnemonic should be a letter contained in caption";
  }
  return null;
}

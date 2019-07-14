public static CharSequence concat(CharSequence... text){
  if (text.length == 0) {
    return "";
  }
  if (text.length == 1) {
    return text[0];
  }
  boolean spanned=false;
  for (  CharSequence piece : text) {
    if (piece instanceof Spanned) {
      spanned=true;
      break;
    }
  }
  if (spanned) {
    final SpannableStringBuilder ssb=new SpannableStringBuilder();
    for (    CharSequence piece : text) {
      ssb.append(piece == null ? "null" : piece);
    }
    return new SpannedString(ssb);
  }
 else {
    final StringBuilder sb=new StringBuilder();
    for (    CharSequence piece : text) {
      sb.append(piece);
    }
    return sb.toString();
  }
}

static int parseTwoDigits(CharSequence text,int position){
  int value=text.charAt(position) - '0';
  return ((value << 3) + (value << 1)) + text.charAt(position + 1) - '0';
}

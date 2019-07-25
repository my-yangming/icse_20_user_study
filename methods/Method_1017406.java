/** 
 * Repeats a string n times.
 * @param string - The string to repeat.
 * @param repetitions - Number of times to repeat `string`.
 * @return The generated string.
 */
@NotNull public static String repeat(@NotNull String string,int repetitions){
  final StringBuilder builder=new StringBuilder(repetitions * string.length());
  for (int i=0; i < repetitions; i++) {
    builder.append(string);
  }
  return builder.toString();
}

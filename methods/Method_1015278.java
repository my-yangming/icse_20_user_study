/** 
 * ????min?max???????????min?max
 */
public static int random(int min,int max){
  if (min > max) {
    throw new IllegalArgumentException("????????!???????????");
  }
  return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
}

public static void fail(String message,Object... args){
  throw new AssertionError(args.length == 0 ? message : String.format(message,args));
}

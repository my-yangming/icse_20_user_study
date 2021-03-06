/** 
 * Returns all variations of this policy based on the configuration parameters. 
 */
public static Set<Policy> policies(Config config){
  BasicSettings settings=new BasicSettings(config);
  return settings.admission().stream().map(admission -> new S4LruPolicy(admission,config)).collect(toSet());
}

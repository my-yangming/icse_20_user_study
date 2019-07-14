/** 
 * Change to the given scene, using the appropriate transition for this particular scene change (as specified to the TransitionManager, or the default if no such transition exists).
 * @param scene The Scene to change to
 */
public void transitionTo(@NonNull Scene scene){
  changeScene(scene,getTransition(scene));
}

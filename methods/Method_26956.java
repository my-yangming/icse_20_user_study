/** 
 * Returns the View's center y coordinate, relative to the screen, at the time the values were captured.
 * @param values The TransitionValues captured at the start or end of the Transition.
 * @return the View's center y coordinate, relative to the screen, at the time the valueswere captured.
 */
public int getViewY(TransitionValues values){
  return getViewCoordinate(values,1);
}

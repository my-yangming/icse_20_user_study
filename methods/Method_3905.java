/** 
 * Call this method after onLayout method is complete if state is NOT pre-layout. This method records information like layout bounds that might be useful in the next layout calculations.
 */
public void onLayoutComplete(){
  mLastTotalSpace=getTotalSpace();
}

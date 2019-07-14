/** 
 * Returns whether RecyclerView is currently computing a layout. <p> If this method returns true, it means that RecyclerView is in a lockdown state and any attempt to update adapter contents will result in an exception because adapter contents cannot be changed while RecyclerView is trying to compute the layout. <p> It is very unlikely that your code will be running during this state as it is called by the framework when a layout traversal happens or RecyclerView starts to scroll in response to system events (touch, accessibility etc). <p> This case may happen if you have some custom logic to change adapter contents in response to a View callback (e.g. focus change callback) which might be triggered during a layout calculation. In these cases, you should just postpone the change using a Handler or a similar mechanism.
 * @return <code>true</code> if RecyclerView is currently computing a layout, <code>false</code>otherwise
 */
public boolean isComputingLayout(){
  return mLayoutOrScrollCounter > 0;
}

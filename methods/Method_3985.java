/** 
 * Returns true if RecyclerView is currently running some animations. <p> If you want to be notified when animations are finished, use {@link ItemAnimator#isRunning(ItemAnimator.ItemAnimatorFinishedListener)}.
 * @return True if there are some item animations currently running or waiting to be started.
 */
public boolean isAnimating(){
  return mItemAnimator != null && mItemAnimator.isRunning();
}

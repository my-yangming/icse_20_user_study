/** 
 * Helper method to facilitate for snapping triggered by a fling.
 * @param layoutManager The {@link RecyclerView.LayoutManager} associated with the attached{@link RecyclerView}.
 * @param velocityX     Fling velocity on the horizontal axis.
 * @param velocityY     Fling velocity on the vertical axis.
 * @return true if it is handled, false otherwise.
 */
private boolean snapFromFling(@NonNull RecyclerView.LayoutManager layoutManager,int velocityX,int velocityY){
  if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
    return false;
  }
  RecyclerView.SmoothScroller smoothScroller=createScroller(layoutManager);
  if (smoothScroller == null) {
    return false;
  }
  int targetPosition=findTargetSnapPosition(layoutManager,velocityX,velocityY);
  if (targetPosition == RecyclerView.NO_POSITION) {
    return false;
  }
  smoothScroller.setTargetPosition(targetPosition);
  layoutManager.startSmoothScroll(smoothScroller);
  return true;
}

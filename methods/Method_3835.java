/** 
 * <p>Scroll the RecyclerView to make the position visible.</p> <p>RecyclerView will scroll the minimum amount that is necessary to make the target position visible. If you are looking for a similar behavior to {@link android.widget.ListView#setSelection(int)} or{@link android.widget.ListView#setSelectionFromTop(int,int)}, use {@link #scrollToPositionWithOffset(int,int)}.</p> <p>Note that scroll position change will not be reflected until the next layout call.</p>
 * @param position Scroll to this adapter position
 * @see #scrollToPositionWithOffset(int,int)
 */
@Override public void scrollToPosition(int position){
  mPendingScrollPosition=position;
  mPendingScrollPositionOffset=INVALID_OFFSET;
  if (mPendingSavedState != null) {
    mPendingSavedState.invalidateAnchor();
  }
  requestLayout();
}

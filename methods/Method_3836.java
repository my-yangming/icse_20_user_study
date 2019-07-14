/** 
 * Scroll to the specified adapter position with the given offset from resolved layout start. Resolved layout start depends on  {@link #getReverseLayout()}, {@link ViewCompat#getLayoutDirection(android.view.View)} and {@link #getStackFromEnd()}. <p> For example, if layout is  {@link #VERTICAL} and {@link #getStackFromEnd()} is true, calling<code>scrollToPositionWithOffset(10, 20)</code> will layout such that <code>item[10]</code>'s bottom is 20 pixels above the RecyclerView's bottom. <p> Note that scroll position change will not be reflected until the next layout call. <p> If you are just trying to make a position visible, use  {@link #scrollToPosition(int)}.
 * @param position Index (starting at 0) of the reference item.
 * @param offset   The distance (in pixels) between the start edge of the item view andstart edge of the RecyclerView.
 * @see #setReverseLayout(boolean)
 * @see #scrollToPosition(int)
 */
public void scrollToPositionWithOffset(int position,int offset){
  scrollToPositionWithOffset(position,offset,mShouldReverseLayout);
}

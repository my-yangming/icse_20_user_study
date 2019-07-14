/** 
 * Enable or disable layout and scroll.  After <code>setLayoutFrozen(true)</code> is called, Layout requests will be postponed until <code>setLayoutFrozen(false)</code> is called; child views are not updated when RecyclerView is frozen,  {@link #smoothScrollBy(int,int)}, {@link #scrollBy(int,int)},  {@link #scrollToPosition(int)} and{@link #smoothScrollToPosition(int)} are dropped; TouchEvents and GenericMotionEvents aredropped;  {@link LayoutManager#onFocusSearchFailed(View,int,Recycler,State)} will not becalled. <p> <code>setLayoutFrozen(true)</code> does not prevent app from directly calling  {@link LayoutManager#scrollToPosition(int)},  {@link LayoutManager#smoothScrollToPosition(RecyclerView,State,int)}. <p> {@link #setAdapter(Adapter)} and {@link #swapAdapter(Adapter,boolean)} will automaticallystop frozen. <p> Note: Running ItemAnimator is not stopped automatically,  it's caller's responsibility to call ItemAnimator.end().
 * @param frozen   true to freeze layout and scroll, false to re-enable.
 * @deprecated Use {@link #suppressLayout(boolean)}.
 */
@Deprecated public void setLayoutFrozen(boolean frozen){
  suppressLayout(frozen);
}

/** 
 * Swaps the current adapter with the provided one. It is similar to {@link #setAdapter(Adapter)} but assumes existing adapter and the new adapter uses the same{@link ViewHolder} and does not clear the RecycledViewPool.<p> Note that it still calls onAdapterChanged callbacks.
 * @param adapter The new adapter to set, or null to set no adapter.
 * @param removeAndRecycleExistingViews If set to true, RecyclerView will recycle all existingViews. If adapters have stable ids and/or you want to animate the disappearing views, you may prefer to set this to false.
 * @see #setAdapter(Adapter)
 */
public void swapAdapter(@Nullable Adapter adapter,boolean removeAndRecycleExistingViews){
  setLayoutFrozen(false);
  setAdapterInternal(adapter,true,removeAndRecycleExistingViews);
  processDataSetCompletelyChanged(true);
  requestLayout();
}

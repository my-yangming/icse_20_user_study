/** 
 * Creates a vertical OrientationHelper for the given LayoutManager.
 * @param layoutManager The LayoutManager to attach to.
 * @return A new OrientationHelper
 */
public static OrientationHelper createVerticalHelper(RecyclerView.LayoutManager layoutManager){
  return new OrientationHelper(layoutManager){
    @Override public int getEndAfterPadding(){
      return mLayoutManager.getHeight() - mLayoutManager.getPaddingBottom();
    }
    @Override public int getEnd(){
      return mLayoutManager.getHeight();
    }
    @Override public void offsetChildren(    int amount){
      mLayoutManager.offsetChildrenVertical(amount);
    }
    @Override public int getStartAfterPadding(){
      return mLayoutManager.getPaddingTop();
    }
    @Override public int getDecoratedMeasurement(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
    }
    @Override public int getDecoratedMeasurementInOther(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
    }
    @Override public int getDecoratedEnd(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedBottom(view) + params.bottomMargin;
    }
    @Override public int getDecoratedStart(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedTop(view) - params.topMargin;
    }
    @Override public int getTransformedEndWithDecoration(    View view){
      mLayoutManager.getTransformedBoundingBox(view,true,mTmpRect);
      return mTmpRect.bottom;
    }
    @Override public int getTransformedStartWithDecoration(    View view){
      mLayoutManager.getTransformedBoundingBox(view,true,mTmpRect);
      return mTmpRect.top;
    }
    @Override public int getTotalSpace(){
      return mLayoutManager.getHeight() - mLayoutManager.getPaddingTop() - mLayoutManager.getPaddingBottom();
    }
    @Override public void offsetChild(    View view,    int offset){
      view.offsetTopAndBottom(offset);
    }
    @Override public int getEndPadding(){
      return mLayoutManager.getPaddingBottom();
    }
    @Override public int getMode(){
      return mLayoutManager.getHeightMode();
    }
    @Override public int getModeInOther(){
      return mLayoutManager.getWidthMode();
    }
  }
;
}

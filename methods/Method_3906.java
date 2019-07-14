/** 
 * Creates a horizontal OrientationHelper for the given LayoutManager.
 * @param layoutManager The LayoutManager to attach to.
 * @return A new OrientationHelper
 */
public static OrientationHelper createHorizontalHelper(RecyclerView.LayoutManager layoutManager){
  return new OrientationHelper(layoutManager){
    @Override public int getEndAfterPadding(){
      return mLayoutManager.getWidth() - mLayoutManager.getPaddingRight();
    }
    @Override public int getEnd(){
      return mLayoutManager.getWidth();
    }
    @Override public void offsetChildren(    int amount){
      mLayoutManager.offsetChildrenHorizontal(amount);
    }
    @Override public int getStartAfterPadding(){
      return mLayoutManager.getPaddingLeft();
    }
    @Override public int getDecoratedMeasurement(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
    }
    @Override public int getDecoratedMeasurementInOther(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
    }
    @Override public int getDecoratedEnd(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedRight(view) + params.rightMargin;
    }
    @Override public int getDecoratedStart(    View view){
      final RecyclerView.LayoutParams params=(RecyclerView.LayoutParams)view.getLayoutParams();
      return mLayoutManager.getDecoratedLeft(view) - params.leftMargin;
    }
    @Override public int getTransformedEndWithDecoration(    View view){
      mLayoutManager.getTransformedBoundingBox(view,true,mTmpRect);
      return mTmpRect.right;
    }
    @Override public int getTransformedStartWithDecoration(    View view){
      mLayoutManager.getTransformedBoundingBox(view,true,mTmpRect);
      return mTmpRect.left;
    }
    @Override public int getTotalSpace(){
      return mLayoutManager.getWidth() - mLayoutManager.getPaddingLeft() - mLayoutManager.getPaddingRight();
    }
    @Override public void offsetChild(    View view,    int offset){
      view.offsetLeftAndRight(offset);
    }
    @Override public int getEndPadding(){
      return mLayoutManager.getPaddingRight();
    }
    @Override public int getMode(){
      return mLayoutManager.getWidthMode();
    }
    @Override public int getModeInOther(){
      return mLayoutManager.getHeightMode();
    }
  }
;
}

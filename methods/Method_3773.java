@Override public void setMeasuredDimension(Rect childrenBounds,int wSpec,int hSpec){
  if (mCachedBorders == null) {
    super.setMeasuredDimension(childrenBounds,wSpec,hSpec);
  }
  final int width, height;
  final int horizontalPadding=getPaddingLeft() + getPaddingRight();
  final int verticalPadding=getPaddingTop() + getPaddingBottom();
  if (mOrientation == VERTICAL) {
    final int usedHeight=childrenBounds.height() + verticalPadding;
    height=chooseSize(hSpec,usedHeight,getMinimumHeight());
    width=chooseSize(wSpec,mCachedBorders[mCachedBorders.length - 1] + horizontalPadding,getMinimumWidth());
  }
 else {
    final int usedWidth=childrenBounds.width() + horizontalPadding;
    width=chooseSize(wSpec,usedWidth,getMinimumWidth());
    height=chooseSize(hSpec,mCachedBorders[mCachedBorders.length - 1] + verticalPadding,getMinimumHeight());
  }
  setMeasuredDimension(width,height);
}

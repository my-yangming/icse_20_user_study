/** 
 * ?????????????onBottomDragListener.onDragBottom(false)???????????
 * @param v
 * @use layout.xml??????android:onClick="onReturnClick"??
 * @warn ???Activity???contentView layout????*???View setOnClickListener???android:onClick="onReturnClick"??
 */
@Override public void onReturnClick(View v){
  Log.d(TAG,"onReturnClick >>>");
  if (onBottomDragListener != null) {
    onBottomDragListener.onDragBottom(false);
  }
 else {
    onBackPressed();
  }
}

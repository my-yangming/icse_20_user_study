/** 
 * ?????????????onBottomDragListener.onDragBottom(true)???????????
 * @param v
 * @use layout.xml??????android:onClick="onForwardClick"??
 * @warn ???Activity???contentView layout????*???View setOnClickListener???android:onClick="onForwardClick"??
 */
@Override public void onForwardClick(View v){
  Log.d(TAG,"onForwardClick >>>");
  if (onBottomDragListener != null) {
    onBottomDragListener.onDragBottom(true);
  }
}
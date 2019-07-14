/** 
 * Helper method for  {@link #calculateDxToMakeVisible(android.view.View,int)} and{@link #calculateDyToMakeVisible(android.view.View,int)}
 */
public int calculateDtToFit(int viewStart,int viewEnd,int boxStart,int boxEnd,int snapPreference){
switch (snapPreference) {
case SNAP_TO_START:
    return boxStart - viewStart;
case SNAP_TO_END:
  return boxEnd - viewEnd;
case SNAP_TO_ANY:
final int dtStart=boxStart - viewStart;
if (dtStart > 0) {
return dtStart;
}
final int dtEnd=boxEnd - viewEnd;
if (dtEnd < 0) {
return dtEnd;
}
break;
default :
throw new IllegalArgumentException("snap preference should be one of the" + " constants defined in SmoothScroller, starting with SNAP_");
}
return 0;
}

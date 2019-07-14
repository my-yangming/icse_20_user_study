/** 
 * When this flag is set, the scroll offset and scroll range calculations will take account of span information. <p>This is will increase the accuracy of the scroll bar's size and offset but will require more calls to  {@link SpanSizeLookup#getSpanGroupIndex(int,int)}". <p>This additional accuracy may or may not be needed, depending on the characteristics of your layout.  You will likely benefit from this accuracy when: <ul> <li>The variation in item span sizes is large. <li>The size of your data set is small (if your data set is large, the scrollbar will likely be very small anyway, and thus the increased accuracy has less impact). <li>Calls to  {@link SpanSizeLookup#getSpanGroupIndex(int,int)} are fast.</ul> <p>If you decide to enable this feature, you should be sure that calls to {@link SpanSizeLookup#getSpanGroupIndex(int,int)} are fast, that set span group indexcaching is set to true via a call to {@link SpanSizeLookup#setSpanGroupIndexCacheEnabled(boolean),and span index caching is also enabled via a call to}{ {@link SpanSizeLookup#setSpanIndexCacheEnabled(boolean)}}.
 */
public void setUsingSpansToEstimateScrollbarDimensions(boolean useSpansToEstimateScrollBarDimensions){
  mUsingSpansToEstimateScrollBarDimensions=useSpansToEstimateScrollBarDimensions;
}
/** 
 * Sets the visiblity of a view to  {@link View#VISIBLE} or {@link View#GONE}. Setting the view to GONE removes it from the layout so that it no longer takes up any space.
 */
public static void setGone(final @NonNull View view,final boolean gone){
  if (gone) {
    view.setVisibility(View.GONE);
  }
 else {
    view.setVisibility(View.VISIBLE);
  }
}

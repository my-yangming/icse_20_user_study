/** 
 * Returns true if the RecyclerView should attempt to preserve currently focused Adapter Item's focus even if the View representing the Item is replaced during a layout calculation. <p> By default, this value is  {@code true}.
 * @return True if the RecyclerView will try to preserve focused Item after a layout if it losesfocus.
 * @see #setPreserveFocusAfterLayout(boolean)
 */
public boolean getPreserveFocusAfterLayout(){
  return mPreserveFocusAfterLayout;
}

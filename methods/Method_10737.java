/** 
 * ??????? <p>???????Activity????android:screenOrientation="landscape"</p> <p>???Activity?android:configChanges???????????????????????????????????</p> <p>??Activity?android:configChanges="orientation"??????????????????????????????</p> <p>??Activity?android:configChanges="orientation|keyboardHidden|screenSize"?4.0????????????? ???????????????????onConfigurationChanged??</p>
 * @param activity activity
 */
public static void setLandscape(Activity activity){
  activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
}
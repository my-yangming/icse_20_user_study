/** 
 * ??WIFI????
 */
public static boolean isWifiEnabled(Context context){
  ConnectivityManager mgrConn=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
  TelephonyManager mgrTel=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
  return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
}

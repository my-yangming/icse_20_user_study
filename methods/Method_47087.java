public static void launchSftp(final HybridFileParcelable baseFile,final MainActivity activity){
  final CloudStreamer streamer=CloudStreamer.getInstance();
  new Thread(() -> {
    try {
      streamer.setStreamSrc(baseFile.getInputStream(activity),baseFile.getName(),baseFile.length(activity));
      activity.runOnUiThread(() -> {
        try {
          File file=new File(SshClientUtils.extractRemotePathFrom(baseFile.getPath()));
          Uri uri=Uri.parse(CloudStreamer.URL + Uri.fromFile(file).getEncodedPath());
          Intent i=new Intent(Intent.ACTION_VIEW);
          i.setDataAndType(uri,MimeTypes.getMimeType(baseFile.getPath(),baseFile.isDirectory()));
          PackageManager packageManager=activity.getPackageManager();
          List<ResolveInfo> resInfos=packageManager.queryIntentActivities(i,0);
          if (resInfos != null && resInfos.size() > 0)           activity.startActivity(i);
 else           Toast.makeText(activity,activity.getResources().getString(R.string.smb_launch_error),Toast.LENGTH_SHORT).show();
        }
 catch (        ActivityNotFoundException e) {
          e.printStackTrace();
        }
      }
);
    }
 catch (    Exception e) {
      e.printStackTrace();
    }
  }
).start();
}

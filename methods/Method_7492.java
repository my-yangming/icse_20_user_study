public static void setLangCode(String langCode){
  langCode=langCode.replace('_','-').toLowerCase();
  for (int a=0; a < UserConfig.MAX_ACCOUNT_COUNT; a++) {
    native_setLangCode(a,langCode);
  }
}

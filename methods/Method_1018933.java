/** 
 * Prepare new media (set it, do not play it).
 * @param callbackMedia callback media
 * @param options zero or more options to attach to the new media
 * @return <code>true</code> if successful; <code>false</code> on error
 */
public boolean prepare(CallbackMedia callbackMedia,String... options){
  return changeMedia(MediaFactory.newMedia(libvlcInstance,callbackMedia,options));
}

/** 
 * Visits replacement code for  {@link ProxyTarget#info()}.
 */
public static void info(final MethodVisitor mv,final MethodInfo methodInfo,final int argsOff){
  mv.visitTypeInsn(Opcodes.NEW,PROXY_TARGET_INFO);
  mv.visitInsn(DUP);
  mv.visitMethodInsn(Opcodes.INVOKESPECIAL,PROXY_TARGET_INFO,"<init>","()V",false);
  mv.visitVarInsn(Opcodes.ASTORE,argsOff);
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  argumentsCount(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"argumentCount","I");
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  createArgumentsClassArray(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"argumentsClasses","[Ljava/lang/Class;");
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  createArgumentsArray(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"arguments","[Ljava/lang/Object;");
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  returnType(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"returnType",AsmUtil.L_SIGNATURE_JAVA_LANG_CLASS);
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  targetMethodName(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"targetMethodName",AsmUtil.L_SIGNATURE_JAVA_LANG_STRING);
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  targetMethodDescription(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"targetMethodDescription",AsmUtil.L_SIGNATURE_JAVA_LANG_STRING);
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  targetMethodSignature(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"targetMethodSignature",AsmUtil.L_SIGNATURE_JAVA_LANG_STRING);
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
  targetClass(mv,methodInfo);
  mv.visitFieldInsn(Opcodes.PUTFIELD,PROXY_TARGET_INFO,"targetClass",AsmUtil.L_SIGNATURE_JAVA_LANG_CLASS);
  mv.visitVarInsn(Opcodes.ALOAD,argsOff);
}
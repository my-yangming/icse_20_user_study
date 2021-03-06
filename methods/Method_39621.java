/** 
 * Returns the size of the method_info JVMS structure generated by this MethodWriter. Also add the names of the attributes of this method in the constant pool.
 * @return the size in bytes of the method_info JVMS structure.
 */
int computeMethodInfoSize(){
  if (sourceOffset != 0) {
    return 6 + sourceLength;
  }
  int size=8;
  if (code.length > 0) {
    if (code.length > 65535) {
      throw new MethodTooLargeException(symbolTable.getClassName(),name,descriptor,code.length);
    }
    symbolTable.addConstantUtf8(Constants.CODE);
    size+=16 + code.length + Handler.getExceptionTableSize(firstHandler);
    if (stackMapTableEntries != null) {
      boolean useStackMapTable=symbolTable.getMajorVersion() >= Opcodes.V1_6;
      symbolTable.addConstantUtf8(useStackMapTable ? Constants.STACK_MAP_TABLE : "StackMap");
      size+=8 + stackMapTableEntries.length;
    }
    if (lineNumberTable != null) {
      symbolTable.addConstantUtf8(Constants.LINE_NUMBER_TABLE);
      size+=8 + lineNumberTable.length;
    }
    if (localVariableTable != null) {
      symbolTable.addConstantUtf8(Constants.LOCAL_VARIABLE_TABLE);
      size+=8 + localVariableTable.length;
    }
    if (localVariableTypeTable != null) {
      symbolTable.addConstantUtf8(Constants.LOCAL_VARIABLE_TYPE_TABLE);
      size+=8 + localVariableTypeTable.length;
    }
    if (lastCodeRuntimeVisibleTypeAnnotation != null) {
      size+=lastCodeRuntimeVisibleTypeAnnotation.computeAnnotationsSize(Constants.RUNTIME_VISIBLE_TYPE_ANNOTATIONS);
    }
    if (lastCodeRuntimeInvisibleTypeAnnotation != null) {
      size+=lastCodeRuntimeInvisibleTypeAnnotation.computeAnnotationsSize(Constants.RUNTIME_INVISIBLE_TYPE_ANNOTATIONS);
    }
    if (firstCodeAttribute != null) {
      size+=firstCodeAttribute.computeAttributesSize(symbolTable,code.data,code.length,maxStack,maxLocals);
    }
  }
  if (numberOfExceptions > 0) {
    symbolTable.addConstantUtf8(Constants.EXCEPTIONS);
    size+=8 + 2 * numberOfExceptions;
  }
  size+=Attribute.computeAttributesSize(symbolTable,accessFlags,signatureIndex);
  size+=AnnotationWriter.computeAnnotationsSize(lastRuntimeVisibleAnnotation,lastRuntimeInvisibleAnnotation,lastRuntimeVisibleTypeAnnotation,lastRuntimeInvisibleTypeAnnotation);
  if (lastRuntimeVisibleParameterAnnotations != null) {
    size+=AnnotationWriter.computeParameterAnnotationsSize(Constants.RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS,lastRuntimeVisibleParameterAnnotations,visibleAnnotableParameterCount == 0 ? lastRuntimeVisibleParameterAnnotations.length : visibleAnnotableParameterCount);
  }
  if (lastRuntimeInvisibleParameterAnnotations != null) {
    size+=AnnotationWriter.computeParameterAnnotationsSize(Constants.RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS,lastRuntimeInvisibleParameterAnnotations,invisibleAnnotableParameterCount == 0 ? lastRuntimeInvisibleParameterAnnotations.length : invisibleAnnotableParameterCount);
  }
  if (defaultValue != null) {
    symbolTable.addConstantUtf8(Constants.ANNOTATION_DEFAULT);
    size+=6 + defaultValue.length;
  }
  if (parameters != null) {
    symbolTable.addConstantUtf8(Constants.METHOD_PARAMETERS);
    size+=7 + parameters.length;
  }
  if (firstAttribute != null) {
    size+=firstAttribute.computeAttributesSize(symbolTable);
  }
  return size;
}

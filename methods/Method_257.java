private void parseListenerAnnotation(Class<? extends Annotation> annotationClass,Element element,Map<TypeElement,BindingSet.Builder> builderMap,Set<TypeElement> erasedTargetNames) throws Exception {
  if (!(element instanceof ExecutableElement) || element.getKind() != METHOD) {
    throw new IllegalStateException(String.format("@%s annotation must be on a method.",annotationClass.getSimpleName()));
  }
  ExecutableElement executableElement=(ExecutableElement)element;
  TypeElement enclosingElement=(TypeElement)element.getEnclosingElement();
  Annotation annotation=element.getAnnotation(annotationClass);
  Method annotationValue=annotationClass.getDeclaredMethod("value");
  if (annotationValue.getReturnType() != int[].class) {
    throw new IllegalStateException(String.format("@%s annotation value() type not int[].",annotationClass));
  }
  int[] ids=(int[])annotationValue.invoke(annotation);
  String name=executableElement.getSimpleName().toString();
  boolean required=isListenerRequired(executableElement);
  boolean hasError=isInaccessibleViaGeneratedCode(annotationClass,"methods",element);
  hasError|=isBindingInWrongPackage(annotationClass,element);
  Integer duplicateId=findDuplicate(ids);
  if (duplicateId != null) {
    error(element,"@%s annotation for method contains duplicate ID %d. (%s.%s)",annotationClass.getSimpleName(),duplicateId,enclosingElement.getQualifiedName(),element.getSimpleName());
    hasError=true;
  }
  ListenerClass listener=annotationClass.getAnnotation(ListenerClass.class);
  if (listener == null) {
    throw new IllegalStateException(String.format("No @%s defined on @%s.",ListenerClass.class.getSimpleName(),annotationClass.getSimpleName()));
  }
  for (  int id : ids) {
    if (id == NO_ID.value) {
      if (ids.length == 1) {
        if (!required) {
          error(element,"ID-free binding must not be annotated with @Optional. (%s.%s)",enclosingElement.getQualifiedName(),element.getSimpleName());
          hasError=true;
        }
      }
 else {
        error(element,"@%s annotation contains invalid ID %d. (%s.%s)",annotationClass.getSimpleName(),id,enclosingElement.getQualifiedName(),element.getSimpleName());
        hasError=true;
      }
    }
  }
  ListenerMethod method;
  ListenerMethod[] methods=listener.method();
  if (methods.length > 1) {
    throw new IllegalStateException(String.format("Multiple listener methods specified on @%s.",annotationClass.getSimpleName()));
  }
 else   if (methods.length == 1) {
    if (listener.callbacks() != ListenerClass.NONE.class) {
      throw new IllegalStateException(String.format("Both method() and callback() defined on @%s.",annotationClass.getSimpleName()));
    }
    method=methods[0];
  }
 else {
    Method annotationCallback=annotationClass.getDeclaredMethod("callback");
    Enum<?> callback=(Enum<?>)annotationCallback.invoke(annotation);
    Field callbackField=callback.getDeclaringClass().getField(callback.name());
    method=callbackField.getAnnotation(ListenerMethod.class);
    if (method == null) {
      throw new IllegalStateException(String.format("No @%s defined on @%s's %s.%s.",ListenerMethod.class.getSimpleName(),annotationClass.getSimpleName(),callback.getDeclaringClass().getSimpleName(),callback.name()));
    }
  }
  List<? extends VariableElement> methodParameters=executableElement.getParameters();
  if (methodParameters.size() > method.parameters().length) {
    error(element,"@%s methods can have at most %s parameter(s). (%s.%s)",annotationClass.getSimpleName(),method.parameters().length,enclosingElement.getQualifiedName(),element.getSimpleName());
    hasError=true;
  }
  TypeMirror returnType=executableElement.getReturnType();
  if (returnType instanceof TypeVariable) {
    TypeVariable typeVariable=(TypeVariable)returnType;
    returnType=typeVariable.getUpperBound();
  }
  String returnTypeString=returnType.toString();
  boolean hasReturnValue=!"void".equals(returnTypeString);
  if (!returnTypeString.equals(method.returnType()) && hasReturnValue) {
    error(element,"@%s methods must have a '%s' return type. (%s.%s)",annotationClass.getSimpleName(),method.returnType(),enclosingElement.getQualifiedName(),element.getSimpleName());
    hasError=true;
  }
  if (hasError) {
    return;
  }
  Parameter[] parameters=Parameter.NONE;
  if (!methodParameters.isEmpty()) {
    parameters=new Parameter[methodParameters.size()];
    BitSet methodParameterUsed=new BitSet(methodParameters.size());
    String[] parameterTypes=method.parameters();
    for (int i=0; i < methodParameters.size(); i++) {
      VariableElement methodParameter=methodParameters.get(i);
      TypeMirror methodParameterType=methodParameter.asType();
      if (methodParameterType instanceof TypeVariable) {
        TypeVariable typeVariable=(TypeVariable)methodParameterType;
        methodParameterType=typeVariable.getUpperBound();
      }
      for (int j=0; j < parameterTypes.length; j++) {
        if (methodParameterUsed.get(j)) {
          continue;
        }
        if ((isSubtypeOfType(methodParameterType,parameterTypes[j]) && isSubtypeOfType(methodParameterType,VIEW_TYPE)) || isTypeEqual(methodParameterType,parameterTypes[j]) || isInterface(methodParameterType)) {
          parameters[i]=new Parameter(j,TypeName.get(methodParameterType));
          methodParameterUsed.set(j);
          break;
        }
      }
      if (parameters[i] == null) {
        StringBuilder builder=new StringBuilder();
        builder.append("Unable to match @").append(annotationClass.getSimpleName()).append(" method arguments. (").append(enclosingElement.getQualifiedName()).append('.').append(element.getSimpleName()).append(')');
        for (int j=0; j < parameters.length; j++) {
          Parameter parameter=parameters[j];
          builder.append("\n\n  Parameter #").append(j + 1).append(": ").append(methodParameters.get(j).asType().toString()).append("\n    ");
          if (parameter == null) {
            builder.append("did not match any listener parameters");
          }
 else {
            builder.append("matched listener parameter #").append(parameter.getListenerPosition() + 1).append(": ").append(parameter.getType());
          }
        }
        builder.append("\n\nMethods may have up to ").append(method.parameters().length).append(" parameter(s):\n");
        for (        String parameterType : method.parameters()) {
          builder.append("\n  ").append(parameterType);
        }
        builder.append("\n\nThese may be listed in any order but will be searched for from top to bottom.");
        error(executableElement,builder.toString());
        return;
      }
    }
  }
  MethodViewBinding binding=new MethodViewBinding(name,Arrays.asList(parameters),required,hasReturnValue);
  BindingSet.Builder builder=getOrCreateBindingBuilder(builderMap,enclosingElement);
  Map<Integer,Id> resourceIds=elementToIds(element,annotationClass,ids);
  for (  Map.Entry<Integer,Id> entry : resourceIds.entrySet()) {
    if (!builder.addMethod(entry.getValue(),listener,method,binding)) {
      error(element,"Multiple listener methods with return value specified for ID %d. (%s.%s)",entry.getKey(),enclosingElement.getQualifiedName(),element.getSimpleName());
      return;
    }
  }
  erasedTargetNames.add(enclosingElement);
}

static List<SpecModelValidationError> validate(SpecModel specModel){
  List<SpecModelValidationError> validationErrors=new ArrayList<>();
  final List<SpecMethodModel<DelegateMethod,Void>> onCreateTreePropMethods=SpecModelUtils.getMethodModelsWithAnnotation(specModel,OnCreateTreeProp.class);
  for (  SpecMethodModel<DelegateMethod,Void> onCreateTreePropMethod : onCreateTreePropMethods) {
    if (onCreateTreePropMethod.returnType.equals(TypeName.VOID)) {
      validationErrors.add(new SpecModelValidationError(onCreateTreePropMethod.representedObject,"@OnCreateTreeProp methods cannot return void."));
    }
    if (onCreateTreePropMethod.returnType.isPrimitive() || onCreateTreePropMethod.returnType.toString().startsWith("java.lang.") || onCreateTreePropMethod.returnType.toString().startsWith("java.util.")) {
      validationErrors.add(new SpecModelValidationError(onCreateTreePropMethod.representedObject,"Returning a common JAVA class or a primitive is against the design" + "of tree props, as they will be keyed on their specific types. Consider " + "creating your own wrapper classes instead."));
    }
    if (onCreateTreePropMethod.methodParams.isEmpty() || !onCreateTreePropMethod.methodParams.get(0).getTypeName().equals(specModel.getContextClass())) {
      validationErrors.add(new SpecModelValidationError(onCreateTreePropMethod.representedObject,"The first argument of an @OnCreateTreeProp method should be " + specModel.getComponentClass() + "."));
    }
  }
  return validationErrors;
}

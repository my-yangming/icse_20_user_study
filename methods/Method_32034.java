private DurationField convertField(DurationField field,HashMap<Object,Object> converted){
  if (field == null || !field.isSupported()) {
    return field;
  }
  if (converted.containsKey(field)) {
    return (DurationField)converted.get(field);
  }
  LimitDurationField limitField=new LimitDurationField(field);
  converted.put(field,limitField);
  return limitField;
}

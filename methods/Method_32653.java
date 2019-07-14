/** 
 * Gets a period type that contains the duration types of the array. <p> Only the 8 standard duration field types are supported.
 * @param types  the types to include in the array.
 * @return the period type
 * @since 1.1
 */
public static synchronized PeriodType forFields(DurationFieldType[] types){
  if (types == null || types.length == 0) {
    throw new IllegalArgumentException("Types array must not be null or empty");
  }
  for (int i=0; i < types.length; i++) {
    if (types[i] == null) {
      throw new IllegalArgumentException("Types array must not contain null");
    }
  }
  Map<PeriodType,Object> cache=cTypes;
  if (cache.isEmpty()) {
    cache.put(standard(),standard());
    cache.put(yearMonthDayTime(),yearMonthDayTime());
    cache.put(yearMonthDay(),yearMonthDay());
    cache.put(yearWeekDayTime(),yearWeekDayTime());
    cache.put(yearWeekDay(),yearWeekDay());
    cache.put(yearDayTime(),yearDayTime());
    cache.put(yearDay(),yearDay());
    cache.put(dayTime(),dayTime());
    cache.put(time(),time());
    cache.put(years(),years());
    cache.put(months(),months());
    cache.put(weeks(),weeks());
    cache.put(days(),days());
    cache.put(hours(),hours());
    cache.put(minutes(),minutes());
    cache.put(seconds(),seconds());
    cache.put(millis(),millis());
  }
  PeriodType inPartType=new PeriodType(null,types,null);
  Object cached=cache.get(inPartType);
  if (cached instanceof PeriodType) {
    return (PeriodType)cached;
  }
  if (cached != null) {
    throw new IllegalArgumentException("PeriodType does not support fields: " + cached);
  }
  PeriodType type=standard();
  List<DurationFieldType> list=new ArrayList<DurationFieldType>(Arrays.asList(types));
  if (list.remove(DurationFieldType.years()) == false) {
    type=type.withYearsRemoved();
  }
  if (list.remove(DurationFieldType.months()) == false) {
    type=type.withMonthsRemoved();
  }
  if (list.remove(DurationFieldType.weeks()) == false) {
    type=type.withWeeksRemoved();
  }
  if (list.remove(DurationFieldType.days()) == false) {
    type=type.withDaysRemoved();
  }
  if (list.remove(DurationFieldType.hours()) == false) {
    type=type.withHoursRemoved();
  }
  if (list.remove(DurationFieldType.minutes()) == false) {
    type=type.withMinutesRemoved();
  }
  if (list.remove(DurationFieldType.seconds()) == false) {
    type=type.withSecondsRemoved();
  }
  if (list.remove(DurationFieldType.millis()) == false) {
    type=type.withMillisRemoved();
  }
  if (list.size() > 0) {
    cache.put(inPartType,list);
    throw new IllegalArgumentException("PeriodType does not support fields: " + list);
  }
  PeriodType checkPartType=new PeriodType(null,type.iTypes,null);
  PeriodType checkedType=(PeriodType)cache.get(checkPartType);
  if (checkedType != null) {
    cache.put(checkPartType,checkedType);
    return checkedType;
  }
  cache.put(checkPartType,type);
  return type;
}

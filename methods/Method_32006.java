long setYear(long instant,int year){
  int thisYear=getYear(instant);
  int dayOfYear=getDayOfYear(instant,thisYear);
  int millisOfDay=getMillisOfDay(instant);
  if (dayOfYear > 354 && !isLeapYear(year)) {
    dayOfYear--;
  }
  instant=getYearMonthDayMillis(year,1,dayOfYear);
  instant+=millisOfDay;
  return instant;
}

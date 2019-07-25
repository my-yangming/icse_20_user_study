package com.myimooc.java.design.pattern.observer.weathercondition;

/**
 * @describe 观察者的实现类
 * @author zc
 * @version 1.0 2017-08-29
 */
public class ConcreteObserver implements Observer {
	
	/**
	 * 观察者的�??称，是�?收到了这个信�?�
	 */
	private String observerName;
	
	/**
	 * 天气的内容信�?�，这个消�?�从目标处获�?�
	 */
	private String weatherContent;
	
	/**
	 * �??醒的内容，�?�?�的观察者�??醒�?�?�的内容
	 */
	private String remindThing;
	
	/**
	 * 获�?�目标类的状�?�?�步到观察者的状�?中
	 */
	@Override
	public void update(AbstractWeatherSubject subject) {
		weatherContent = ((ConcreteWeatherSubject)subject).getWeatherContent();
		System.out.println(observerName + " 收到了天气信�?�  " + weatherContent + "，准备去�?� "+remindThing);
	}
	
	@Override
	public String getObserverName() {
		return observerName;
	}
	
	@Override
	public void setObserverName(String observerName) {
		this.observerName = observerName;
	}

	public String getWeatherContent() {
		return weatherContent;
	}

	public void setWeatherContent(String weatherContent) {
		this.weatherContent = weatherContent;
	}

	public String getRemindThing() {
		return remindThing;
	}

	public void setRemindThing(String remindThing) {
		this.remindThing = remindThing;
	}
}

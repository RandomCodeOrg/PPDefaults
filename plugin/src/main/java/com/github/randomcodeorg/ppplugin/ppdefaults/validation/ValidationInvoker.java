package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

public class ValidationInvoker {

	
	//validate(T value, ValidationInformation<? super T> information
	
	@SuppressWarnings("unchecked")
	public  static <T> void invoke(Object value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		validator.validate((T)value, information, stackTrace);
	}
	
	public  static <T> void invoke(int value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
	
	public  static <T> void invoke(float value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
	
	public  static <T> void invoke(double value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
	
	public  static <T> void invoke(byte value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
	
	public  static <T> void invoke(char value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
	
	public  static <T> void invoke(short value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
	
	public  static <T> void invoke(long value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
	
	public  static <T> void invoke(boolean value, Validator<T> validator, ValidationInformation<? super T> information, StackTraceElement[] stackTrace){
		invoke((Object) value, validator, information, stackTrace);
	}
}

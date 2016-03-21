package com.github.randomcodeorg.ppplugin.ppdefaults.validation;

import java.lang.reflect.Array;
import java.util.Iterator;

public class NotEmptyValidator implements Validator<Object> {

	@Override
	public void validate(Object value,
			ValidationInformation<? super Object> information,
			StackTraceElement[] stackTrace) throws RuntimeException {
		if(value == null) return;
		if(value instanceof Iterable<?>){
			validateIterable((Iterable<?>) value, information, stackTrace);
			return;
		}
		if(value instanceof String){
			validateString((String) value, information, stackTrace);
			return;
		}
		if(value.getClass().isArray()){
			validateArray(value, information, stackTrace);
			return;
		}
	}
	
	private void validateString(String value,
			ValidationInformation<? super Object> information,
			StackTraceElement[] stackTrace){
		if(value.isEmpty()){
			throw information.createException(this, value, stackTrace);
		}
	}
	
	private void validateArray(Object value,
			ValidationInformation<? super Object> information,
			StackTraceElement[] stackTrace){
		int length = Array.getLength(value);
		if(length == 0){
			throw information.createException(this, value, stackTrace);
		}
	}
	
	private void validateIterable(Iterable<?> value,
			ValidationInformation<? super Object> information,
			StackTraceElement[] stackTrace){
		Iterator<?> it = value.iterator();
		if(!it.hasNext()){
			throw information.createException(this, value, stackTrace);
		}
	}

	@Override
	public Class<Object> getValueType() {
		return Object.class;
	}


	
	
	
}

package com.github.randomcodeorg.ppplugin.ppdefaults.test.logging;

import com.github.randomcodeorg.ppplugin.ppdefaults.logging.InsertMethodCallLogProcessor;

public class MyInsertMethodCallLogProcessor extends InsertMethodCallLogProcessor {

	public MyInsertMethodCallLogProcessor() {
		setLoggingCodeSource(new LoggingTestCodeSource());
	}

}

package com.github.randomcodeorg.ppplugin.ppdefaults;

import com.github.randomcodeorg.ppplugin.PContext;

public abstract class MultipleExecutionProcessor extends AbstractClassModificationProcessor {

	protected boolean completed = true;

	private int executions = 0;

	private int max_cycles = 100;

	public MultipleExecutionProcessor() {

	}

	@Override
	public void run(PContext context) {
		do {
			context.getLog().info(String.format("This is execution #%d", executions + 1));
			completed = true;
			super.run(context);
			executions++;
			if (!completed) {
				if (executions == max_cycles) {
					context.getLog().warn(String.format("Another cycle was requested but the maximum number (%d) of executions is reached!", max_cycles));
					completed = true;
				} else
					context.getLog().info("Executing one more time because another cycle was requested...");
			}
		} while (!completed);
	}

	protected final void requestCycle() {
		completed = false;
	}
	
	protected final int getMaxCycles(){
		return max_cycles;
	}
	
	protected final void setMaxCycles(int max){
		this.max_cycles = max;
	}
	
}

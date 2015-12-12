package com.github.randomcodeorg.ppplugin.ppdefaults;

import java.io.IOException;

import com.github.randomcodeorg.ppplugin.PContext;
import com.github.randomcodeorg.ppplugin.PProcessor;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public abstract class AbstractClassModificationProcessor implements PProcessor {

	protected PContext context;

	public AbstractClassModificationProcessor() {

	}

	public void init(PContext context) {
		this.context = context;
	}

	public void run(PContext context) {
		try {
			doRun();
		} catch (Throwable th) {
			throw new RuntimeException(th);
		}
	}

	public void doRun() throws NotFoundException {
		ByteCodeHelper bch = new ByteCodeHelper(context);
		try {
			context.getLog().info("Processing compiled classes...");
			ClassPool cp = bch.getClassPool();
			for (Class<?> cl : context.getClasses()) {
				if (cl.getCanonicalName() == null)
					continue;
				CtClass ctClass;
				try {
					ctClass = cp.get(cl.getCanonicalName());
				} catch (NotFoundException e) {
					context.getLog().debug(e);
					context.getLog()
							.warn(String.format(
									"The class '%s' could not be loaded by javassist and was skipped. See the debug log for exception details",
									cl.getCanonicalName()));
					continue;
				}
				try {
					processClass(bch, ctClass, cl);
				} catch (CannotCompileException e) {
					context.getLog().debug(e);
					context.getLog()
							.warn(String.format(
									"The changes in '%s' could not be compiled. See the debug log for exception details",
									cl.getCanonicalName()));
				}
			}
		} finally {
			try {
				bch.commit(true);
			} catch (CannotCompileException e) {
				context.getLog().debug(e);
				context.getLog().warn("Some changes could not be compiled. See the debug log for exception details");
			} catch (IOException e) {
				context.getLog().debug(e);
				context.getLog().warn("Some changes could not be applied. See the debug log for exception details");
			}
			bch.releaseResources();
		}
	}

	protected abstract void processClass(ByteCodeHelper helper, CtClass ctClass, Class<?> clazz)
			throws CannotCompileException;

}

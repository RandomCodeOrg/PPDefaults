package com.github.randomcodeorg.ppplugin.ppdefaults.access;

public interface AccessController {

	/**
	 * Checks if the given access should be verified by another
	 * {@link AccessController}.
	 * 
	 * @param access
	 *            The current access.
	 * @return This method will return an {@link AccessController} that should
	 *         handle the access instead of this instance. The request will be
	 *         handled by this {@link AccessController} if the result is
	 *         {@code null} or this instance.
	 */
	AccessController redirect(AccessInformation access);

	/**
	 * Checks if the given access is allowed.
	 * @param access The access to verify.
	 * @return {@code true} if the given access is allowed. Otherwise {@code false} is returned.
	 */
	boolean check(AccessInformation access);

}

/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.everrest.core;

/**
 * Should not be used by custom services. They have to use
 * {@link javax.ws.rs.WebApplicationException} instead. UnhandledException is
 * used to propagate exception than can't be handled by this framework to top
 * container (e.g. Servlet Container)
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class UnhandledException extends RuntimeException {
    /**
     * @param s
     *         message
     * @param throwable
     *         cause
     */
    public UnhandledException(String s, Throwable throwable) {
        super(s, throwable);
    }

    /**
     * @param throwable
     *         cause
     */
    public UnhandledException(Throwable throwable) {
        super(throwable);
    }
}

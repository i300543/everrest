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
package org.everrest.core.impl.header;

import org.everrest.core.header.AbstractHeaderDelegate;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id$
 */
public class StringHeaderDelegate extends AbstractHeaderDelegate<String> {
    /** {@inheritDoc} */
    @Override
    public Class<String> support() {
        return String.class;
    }

    /** {@inheritDoc} */
    public String fromString(String value) {
        return value;
    }

    /** {@inheritDoc} */
    public String toString(String value) {
        return value;
    }
}

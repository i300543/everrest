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
package org.everrest.core.impl.provider;

import org.everrest.core.BaseObjectModel;
import org.everrest.core.ComponentLifecycleScope;
import org.everrest.core.impl.header.MediaTypeHelper;
import org.everrest.core.provider.ProviderDescriptor;
import org.everrest.core.resource.ResourceDescriptorVisitor;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: ProviderDescriptorImpl.java 292 2009-10-19 07:03:07Z aparfonov
 *          $
 */
public class ProviderDescriptorImpl extends BaseObjectModel implements ProviderDescriptor {
    /**
     * List of media types which this method can consume. See
     * {@link javax.ws.rs.Consumes} .
     */
    private final List<MediaType> consumes;

    /**
     * List of media types which this method can produce. See
     * {@link javax.ws.rs.Produces} .
     */
    private final List<MediaType> produces;

    /**
     * @param providerClass
     *         provider class
     * @param scope
     *         provider scope
     */
    public ProviderDescriptorImpl(Class<?> providerClass, ComponentLifecycleScope scope) {
        super(providerClass, scope);
        this.consumes = MediaTypeHelper.createConsumesList(providerClass.getAnnotation(Consumes.class));
        this.produces = MediaTypeHelper.createProducesList(providerClass.getAnnotation(Produces.class));
    }

    /** {@inheritDoc} */
    public void accept(ResourceDescriptorVisitor visitor) {
        visitor.visitProviderDescriptor(this);
    }

    /** {@inheritDoc} */
    public List<MediaType> consumes() {
        return consumes;
    }

    /** {@inheritDoc} */
    public List<MediaType> produces() {
        return produces;
    }

    /** {@inheritDoc} */
    public String toString() {
        StringBuilder sb = new StringBuilder("[ ProviderDescriptorImpl: ");
        sb.append("provider class: ");
        sb.append(getObjectClass());
        sb.append("; produces media type: ");
        sb.append(produces());
        sb.append("; consumes media type: ");
        sb.append(consumes());
        sb.append("; ");
        sb.append(getConstructorDescriptors());
        sb.append("; ");
        sb.append(getFieldInjectors());
        sb.append(" ]");
        return sb.toString();
    }
}

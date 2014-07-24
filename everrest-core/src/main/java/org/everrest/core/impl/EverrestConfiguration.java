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
package org.everrest.core.impl;

import org.everrest.core.impl.method.MethodInvokerDecoratorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andrew00x
 */
public class EverrestConfiguration {
    public static final String EVERREST_HTTP_METHOD_OVERRIDE = "org.everrest.http.method.override";

    public static final String EVERREST_NORMALIZE_URI = "org.everrest.normalize.uri";

    public static final String EVERREST_CHECK_SECURITY = "org.everrest.security";

    public static final String EVERREST_ASYNCHRONOUS = "org.everrest.asynchronous";

    public static final String EVERREST_ASYNCHRONOUS_SERVICE_PATH = "org.everrest.asynchronous.service.path";

    public static final String EVERREST_ASYNCHRONOUS_POOL_SIZE = "org.everrest.asynchronous.pool.size";

    public static final String EVERREST_ASYNCHRONOUS_QUEUE_SIZE = "org.everrest.asynchronous.queue.size";

    public static final String EVERREST_ASYNCHRONOUS_CACHE_SIZE = "org.everrest.asynchronous.cache.size";

    public static final String EVERREST_ASYNCHRONOUS_JOB_TIMEOUT = "org.everrest.asynchronous.job.timeout";

    public static final String METHOD_INVOKER_DECORATOR_FACTORY = MethodInvokerDecoratorFactory.class.getName();

    /**
     * Max buffer size attribute name. Entities that has size greater then specified will be stored in temporary
     * directory on file system during entity processing.
     */
    public static final String EVERREST_MAX_BUFFER_SIZE = "org.everrest.max.buffer.size";

    public static final boolean defaultCheckSecurity = true;

    public static final boolean defaultHttpMethodOverride = true;

    public static final boolean defaultNormalizeUri = false;

    public static final boolean defaultAsynchronousSupported = true;

    public static final int defaultAsynchronousPoolSize = 10;

    public static final String defaultAsynchronousServicePath = "/async";

    public static final int defaultAsynchronousQueueSize = 100;

    public static final int defaultAsynchronousCacheSize = 512;

    public static final int defaultAsynchronousJobTimeout = 60;

    /** Max buffer size attribute value. See {@link #EVERREST_MAX_BUFFER_SIZE}. */
    public static final int defaultMaxBufferSize = 204800;

    //

    protected boolean checkSecurity = defaultCheckSecurity;

    protected boolean httpMethodOverride = defaultHttpMethodOverride;

    protected boolean normalizeUri = defaultNormalizeUri;

    protected boolean asynchronousSupported = defaultAsynchronousSupported;

    protected int asynchronousPoolSize = defaultAsynchronousPoolSize;

    protected int asynchronousQueueSize = defaultAsynchronousQueueSize;

    protected int asynchronousCacheSize = defaultAsynchronousCacheSize;

    protected int asynchronousJobTimeout = defaultAsynchronousJobTimeout;

    protected int maxBufferSize = defaultMaxBufferSize;

    protected String asynchronousServicePath = defaultAsynchronousServicePath;

    protected final Map<String, Object> properties;

    public EverrestConfiguration() {
        properties = new HashMap<String, Object>();
    }

    public EverrestConfiguration(EverrestConfiguration other) {
        properties = new HashMap<String, Object>(other.properties);
    }

    public boolean isCheckSecurity() {
        return checkSecurity;
    }

    public void setCheckSecurity(boolean checkSecurity) {
        this.checkSecurity = checkSecurity;
    }

    public boolean isHttpMethodOverride() {
        return httpMethodOverride;
    }

    public void setHttpMethodOverride(boolean httpMethodOverride) {
        this.httpMethodOverride = httpMethodOverride;
    }

    public boolean isNormalizeUri() {
        return normalizeUri;
    }

    public void setNormalizeUri(boolean normalizeUri) {
        this.normalizeUri = normalizeUri;
    }

    public boolean isAsynchronousSupported() {
        return asynchronousSupported;
    }

    public void setAsynchronousSupported(boolean asynchronousSupported) {
        this.asynchronousSupported = asynchronousSupported;
    }

    public String getAsynchronousServicePath() {
        return asynchronousServicePath;
    }

    public void setAsynchronousServicePath(String servicePath) {
        this.asynchronousServicePath = servicePath;
    }

    public int getAsynchronousPoolSize() {
        return asynchronousPoolSize;
    }

    public void setAsynchronousPoolSize(int asynchronousPoolSize) {
        this.asynchronousPoolSize = asynchronousPoolSize;
    }

    public int getAsynchronousQueueSize() {
        return asynchronousQueueSize;
    }

    public void setAsynchronousQueueSize(int asynchronousQueueSize) {
        this.asynchronousQueueSize = asynchronousQueueSize;
    }

    public int getAsynchronousCacheSize() {
        return asynchronousCacheSize;
    }

    public void setAsynchronousCacheSize(int asynchronousCacheSize) {
        this.asynchronousCacheSize = asynchronousCacheSize;
    }

    public int getAsynchronousJobTimeout() {
        return asynchronousJobTimeout;
    }

    public void setAsynchronousJobTimeout(int asynchronousJobTimeout) {
        this.asynchronousJobTimeout = asynchronousJobTimeout;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public void setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, Object value) {
        if (value == null) {
            properties.remove(name);
        } else {
            properties.put(name, value);
        }
    }
}

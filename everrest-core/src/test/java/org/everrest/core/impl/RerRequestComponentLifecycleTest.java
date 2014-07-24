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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class RerRequestComponentLifecycleTest extends BaseTest {
    @Path("a")
    public static class Resource1 {
        static AtomicInteger destroyVisit = new AtomicInteger();
        static AtomicInteger initVisit    = new AtomicInteger();

        @SuppressWarnings("unused")
        @PreDestroy
        private void _destroy() // @PreDestroy must be processed even for private methods.
        {
            destroyVisit.incrementAndGet();
        }

        @SuppressWarnings("unused")
        @PostConstruct
        private void _init() // @PostConstruct must be processed even for private methods.
        {
            initVisit.incrementAndGet();
        }

        @GET
        public Entity m(Entity entity) {
            return entity;
        }
    }

    @Provider
    public static class Provider1 implements MessageBodyReader<Entity>, MessageBodyWriter<Entity> {
        static AtomicInteger destroyVisit = new AtomicInteger();
        static AtomicInteger initVisit    = new AtomicInteger();

        @SuppressWarnings("unused")
        @PreDestroy
        private void _destroy() // @PreDestroy must be processed even for private methods.
        {
            destroyVisit.incrementAndGet();
        }

        @SuppressWarnings("unused")
        @PostConstruct
        private void _init() // @PostConstruct must be processed even for private methods.
        {
            initVisit.incrementAndGet();
        }

        @Override
        public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return type == Entity.class;
        }

        @Override
        public long getSize(Entity t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return 0;
        }

        @Override
        public void writeTo(Entity t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
                                                                                                          WebApplicationException {
        }

        @Override
        public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
            return type == Entity.class;
        }

        @Override
        public Entity readFrom(Class<Entity> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                               MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException,
                                                                                                            WebApplicationException {
            return new Entity();
        }
    }

    public static class Entity {
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        providers.addMessageBodyReader(Provider1.class);
        providers.addMessageBodyWriter(Provider1.class);
    }

    public void testResources() throws Exception {
        registry(Resource1.class);
        Resource1.destroyVisit.set(0);
        Resource1.initVisit.set(0);
        ContainerResponse response = launcher.service("GET", "a", "", null, new byte[1], null);
        assertEquals(200, response.getStatus());
        assertEquals(1, Resource1.initVisit.get());
        assertEquals(1, Resource1.destroyVisit.get());
        unregistry(Resource1.class);
    }

    public void testProviders() throws Exception {
        registry(Resource1.class);
        Provider1.destroyVisit.set(0);
        Provider1.initVisit.set(0);
        ContainerResponse response = launcher.service("GET", "a", "", null, new byte[1], null);
        assertEquals(200, response.getStatus());
        // Two instance of Provider1 created since it implements both (MessageBodyReader, MessageBodyWriter) interfaces
        assertEquals(2, Provider1.initVisit.get());
        assertEquals(2, Provider1.destroyVisit.get());
        unregistry(Resource1.class);
    }
}

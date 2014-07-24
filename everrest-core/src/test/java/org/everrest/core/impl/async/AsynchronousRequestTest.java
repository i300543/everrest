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
package org.everrest.core.impl.async;

import org.everrest.core.impl.BaseTest;
import org.everrest.core.impl.ContainerResponse;
import org.everrest.core.impl.MultivaluedMapImpl;
import org.everrest.core.impl.provider.json.JsonParser;
import org.everrest.core.impl.provider.json.ObjectBuilder;
import org.everrest.core.tools.ByteArrayContainerResponseWriter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MultivaluedMap;
import java.io.ByteArrayInputStream;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class AsynchronousRequestTest extends BaseTest {
    @Path("a")
    public static class Resource1 {
        @GET
        public String m() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                return "stopped";
            }
            return "asynchronous response";
        }
    }

    @Path("b")
    public static class Resource2 {
        @GET
        @Path("sub")
        public Msg m() {
            return new Msg();
        }
    }

    @Path("c")
    public static class Resource3 {
        @GET
        public void m() {
            throw new RuntimeException("test process exceptions in asynchronous mode");
        }
    }

    @Path("d")
    public static class Resource4 {
        @Path("sub")
        public SubResource m() {
            return new SubResource1();
        }
    }

    public static interface SubResource {
        @GET
        @Produces("application/json")
        Msg m();
    }

    public static class SubResource1 implements SubResource {
        public Msg m() {
            return new Msg();
        }
    }

    //

    public static class Msg {
        public String getMessage() {
            return "to be or not to be";
        }
    }

    //

    public void testRunJob() throws Exception {
        registry(Resource1.class);
        ContainerResponse response = launcher.service("GET", "/a?async=true", "", null, null, null);
        assertEquals(202, response.getStatus());
        String jobUrl = (String)response.getEntity();
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        response = getAsynchronousResponse(jobUrl, writer);
        assertEquals(200, response.getStatus());
        assertEquals("asynchronous response", new String(writer.getBody()));
        // Try one more time. Job must be removed from pool so expected result is 404.
        response = launcher.service("GET", jobUrl, "", null, null, null);
        assertEquals(404, response.getStatus());
        unregistry(Resource1.class);
    }

    public void testListJobsJson() throws Exception {
        registry(Resource1.class);
        ContainerResponse response = launcher.service("GET", "/a?async=true", "", null, null, null);
        assertEquals(202, response.getStatus());
        MultivaluedMap<String, String> h = new MultivaluedMapImpl();
        h.putSingle("accept", "application/json");
        ByteArrayContainerResponseWriter w = new ByteArrayContainerResponseWriter();
        response = launcher.service("GET", "/async", "", h, null, w, null);
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType().toString());
        unregistry(Resource1.class);
    }

    public void testListJobsPlainText() throws Exception {
        registry(Resource1.class);
        ContainerResponse response = launcher.service("GET", "/a?async=true", "", null, null, null);
        assertEquals(202, response.getStatus());
        MultivaluedMap<String, String> h = new MultivaluedMapImpl();
        h.putSingle("accept", "text/plain");
        ByteArrayContainerResponseWriter w = new ByteArrayContainerResponseWriter();
        response = launcher.service("GET", "/async", "", h, null, w, null);
        assertEquals(200, response.getStatus());
        assertEquals("text/plain", response.getContentType().toString());
        System.out.print(new String(w.getBody()));
        unregistry(Resource1.class);
    }

    public void testRemoveJob() throws Exception {
        registry(Resource1.class);
        ContainerResponse response = launcher.service("GET", "/a?async=true", "", null, null, null);
        assertEquals(202, response.getStatus());
        String jobUrl = (String)response.getEntity();
        response = launcher.service("DELETE", jobUrl, "", null, null, null);
        assertEquals(204, response.getStatus());
        // check job
        response = launcher.service("GET", jobUrl, "", null, null, null);
        assertEquals(404, response.getStatus());
        unregistry(Resource1.class);
    }

    public void testMimeTypeResolving() throws Exception {
        registry(Resource2.class);
        MultivaluedMap<String, String> h = new MultivaluedMapImpl();
        h.putSingle("accept", "application/json");
        ContainerResponse response = launcher.service("GET", "/b/sub?async=true", "", h, null, null);
        assertEquals(202, response.getStatus());
        String jobUrl = (String)response.getEntity();
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        response = getAsynchronousResponse(jobUrl, writer);
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType().toString());
        JsonParser parser = new JsonParser();
        parser.parse(new ByteArrayInputStream(writer.getBody()));
        Msg msg = ObjectBuilder.createObject(Msg.class, parser.getJsonObject());
        assertEquals("to be or not to be", msg.getMessage());
        unregistry(Resource2.class);
    }

    public void testProcessExceptions() throws Exception {
        registry(Resource3.class);
        ContainerResponse response = launcher.service("GET", "/c?async=true", "", null, null, null);
        assertEquals(202, response.getStatus());
        String jobUrl = (String)response.getEntity();
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        response = getAsynchronousResponse(jobUrl, writer);
        assertEquals(500, response.getStatus());
        assertEquals("text/plain", response.getContentType().toString());
        assertEquals("test process exceptions in asynchronous mode", new String(writer.getBody()));
        unregistry(Resource3.class);
    }

    public void testWithLocators() throws Exception {
        registry(Resource4.class);
        ContainerResponse response = launcher.service("GET", "/d/sub?async=true", "", null, null, null);
        assertEquals(202, response.getStatus());
        String jobUrl = (String)response.getEntity();
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        response = getAsynchronousResponse(jobUrl, writer);
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType().toString());
        JsonParser parser = new JsonParser();
        parser.parse(new ByteArrayInputStream(writer.getBody()));
        Msg msg = ObjectBuilder.createObject(Msg.class, parser.getJsonObject());
        assertEquals("to be or not to be", msg.getMessage());
        unregistry(Resource4.class);
    }

    private ContainerResponse getAsynchronousResponse(String jobUrl, ByteArrayContainerResponseWriter writer) throws Exception {
        ContainerResponse response;
        // Limit end time to avoid infinite loop if something going wrong.
        final long endTime = System.currentTimeMillis() + 5000;
        synchronized (this) {
            while ((response = launcher.service("GET", jobUrl, "", null, null, writer, null)).getStatus() == 202
                   && System.currentTimeMillis() < endTime) {
                wait(300);
                writer.reset();
            }
        }
        return response;
    }
}

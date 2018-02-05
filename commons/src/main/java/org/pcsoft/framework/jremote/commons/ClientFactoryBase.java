package org.pcsoft.framework.jremote.commons;

import org.apache.commons.lang.StringUtils;
import org.pcsoft.framework.jremote.api.exception.JRemoteException;
import org.pcsoft.framework.jremote.api.exception.JRemoteServiceInterfaceException;
import org.pcsoft.framework.jremote.api.factory.ClientFactory;
import org.pcsoft.framework.jremote.api.interf.RemoteRestService;
import org.pcsoft.framework.jremote.api.type.ClientConnection;
import org.pcsoft.framework.jremote.api.type.RestHeader;

import javax.ws.rs.*;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class ClientFactoryBase implements ClientFactory {

    @SuppressWarnings("unchecked")
    @Override
    public final <T extends RemoteRestService> T createClient(final Class<T> clazz, final URI remoteUri, final ClientConnection connection) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, (proxy, method, args) -> {
            final ParamHolder<PathParam>[] pathParams = extractParams(PathParam.class, method, args, PathParam::value);
            final ParamHolder<QueryParam>[] queryParams = extractParams(QueryParam.class, method, args, QueryParam::value);

            //Build URL
            final String baseUrl = remoteUri.toString();
            final String rootPath = getPath(clazz);
            final String plainLocalPath = getPath(method);
            final String localPath = replacePathParam(plainLocalPath, pathParams);
            final String query = generateQuery(queryParams);
            final String url = baseUrl + "/" + rootPath + localPath + query;

            final RestMethod restMethod = extractMethod(method);
            final RestHeader[] restHeaders = generateHeaders(method);
            final byte[] entity = extractEntity(method, args);

            final byte[] answerEntity = callService(url, restMethod, restHeaders, entity, connection);

            return null;
        });
    }

    protected abstract byte[] callService(final String url, final RestMethod method, final RestHeader[] headers, final byte[] entity, final ClientConnection connection) throws JRemoteException;

    //<editor-fold desc="Helper Methods">

    private String getPath(final AnnotatedElement annotatedElement) {
        final Path pathAnnotation = annotatedElement.getAnnotation(Path.class);
        return pathAnnotation == null ? StringUtils.EMPTY : (pathAnnotation.value() + "/");
    }

    private <T extends Annotation> ParamHolder<T>[] extractParams(final Class<T> clazz, final Method method, final Object[] args, final Function<T, String> nameExtractor) {
        final List<ParamHolder<T>> list = new ArrayList<>();

        for (int i = 0; i < method.getParameters().length; i++) {
            final Parameter parameter = method.getParameters()[i];
            final Object value = args[i];

            final T annotation = parameter.getAnnotation(clazz);
            if (annotation == null)
                continue;

            list.add(new ParamHolder<>(annotation, nameExtractor.apply(annotation), value));
        }

        return list.toArray(new ParamHolder[list.size()]);
    }

    private String replacePathParam(final String path, final ParamHolder<PathParam>[] pathParams) {
        String local = path;
        for (final ParamHolder<PathParam> pathParam : pathParams) {
            local = local.replace("{" + pathParam.getName() + "}", pathParam.value.toString()); //TODO: Value Mapper
        }

        return local;
    }

    private String generateQuery(final ParamHolder<QueryParam>[] queryParams) {
        if (queryParams == null || queryParams.length <= 0)
            return StringUtils.EMPTY;

        StringBuilder query = new StringBuilder("?");

        for (final ParamHolder<QueryParam> queryParam : queryParams) {
            query
                    .append(queryParam.getName())
                    .append("=")
                    .append(queryParam.getValue()) //TODO: Value Mapper
                    .append("&");
        }
        query = new StringBuilder(query.substring(0, query.length() - 1)); //Remove last '&'

        return query.toString();
    }

    private RestHeader[] generateHeaders(final Method method) {
        final List<RestHeader> headerList = new ArrayList<>();

        final Produces producesAnnotation = method.getAnnotation(Produces.class);
        if (producesAnnotation != null) {
            headerList.add(new RestHeader("Accept", StringUtils.join(producesAnnotation.value(), ",")));
        }

        final Consumes consumesAnnotation = method.getAnnotation(Consumes.class);
        if (consumesAnnotation != null) {
            headerList.add(new RestHeader("Content-Type", StringUtils.join(consumesAnnotation.value(), ",")));
        }

        return headerList.toArray(new RestHeader[headerList.size()]);
    }

    private RestMethod extractMethod(final Method method) throws JRemoteServiceInterfaceException {
        final HttpMethod httpMethod = method.getAnnotation(HttpMethod.class);
        if (httpMethod != null) {
            switch (httpMethod.value().toLowerCase()) {
                case "get":
                    return RestMethod.Get;
                case "post":
                    return RestMethod.Post;
                case "delete":
                    return RestMethod.Delete;
                case "put":
                    return RestMethod.Put;
                case "head":
                    return RestMethod.Head;
                case "options":
                    return RestMethod.Options;
                default:
                    throw new JRemoteServiceInterfaceException("Unknown method found: " + httpMethod.value() + "; known are: " + Arrays.toString(RestMethod.values()));
            }
        }

        if (method.getAnnotation(GET.class) != null)
            return RestMethod.Get;
        else if (method.getAnnotation(POST.class) != null)
            return RestMethod.Post;
        else if (method.getAnnotation(DELETE.class) != null)
            return RestMethod.Delete;
        else if (method.getAnnotation(PUT.class) != null)
            return RestMethod.Put;
        else if (method.getAnnotation(HEAD.class) != null)
            return RestMethod.Head;
        else if (method.getAnnotation(OPTIONS.class) != null)
            return RestMethod.Options;

        throw new JRemoteServiceInterfaceException("Unable to find any known HTTP method: " + Arrays.toString(RestMethod.values()));
    }

    private byte[] extractEntity(final Method method, final Object[] args) {
        for (int i=0; i<method.getParameters().length; i++) {
            final Parameter parameter = method.getParameters()[i];
            final Object value = args[i];

            if (parameter.getAnnotation(QueryParam.class) != null &&
                    parameter.getAnnotation(PathParam.class) != null &&
                    parameter.getAnnotation(FormParam.class) != null) {
                try {
                    return value.toString().getBytes("UTF-8"); //TODO: Value Mapper
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }

    //</editor-fold>

    //<editor-fold desc="Helper Classes">

    private static final class ParamHolder<T extends Annotation> {
        private final T annotation;
        private final String name;
        private final Object value;

        public ParamHolder(T annotation, String name, Object value) {
            this.annotation = annotation;
            this.name = name;
            this.value = value;
        }

        public T getAnnotation() {
            return annotation;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    protected enum RestMethod {
        Get,
        Post,
        Put,
        Delete,
        Options,
        Head
    }

    //</editor-fold>
}

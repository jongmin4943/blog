package com.min.testapi;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

public class GzipResponseCompressionFilter extends OncePerRequestFilter {
    private static final String CONTENT_ENCODING_GZIP = "gzip";

    // Filter 의 urlPattern 보다는, 좀 더 유연한 사용과 일관성을 위해 Spring 의 AntPathMatcher 를 사용했다.
    private final AntPathMatcher pathMatcher;
    // Controller 에서 @GzipResponse 를 가진 urlPattern 과 압축 레벨 정보를 담은 자료구조
    private final Map<String, GzipLevel> gzipPathInfo;

    public GzipResponseCompressionFilter(final RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.pathMatcher = new AntPathMatcher();
        // 모든 Controller 의 정보를 가진 RequestMappingHandlerMapping 에서 @GzipResponse 를 가진 메서드를 추출해 가공한다
        this.gzipPathInfo = requestMappingHandlerMapping.getHandlerMethods().entrySet()
                .stream()
                .filter(entry -> isGzipAnnotationPresent(entry.getValue()))
                .flatMap(entry -> entry.getKey()
                        .getPatternValues()
                        .stream()
                        .map(urlPattern -> Map.entry(urlPattern, new GzipLevel(entry.getValue().getMethod().getAnnotation(GzipResponse.class).level()))))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 메서드 혹은 클래스레벨에 @GzipResponse 이 있는지 확인한다
    private boolean isGzipAnnotationPresent(final HandlerMethod handlerMethod) {
        return handlerMethod.getMethod().isAnnotationPresent(GzipResponse.class) || handlerMethod.getBeanType().isAnnotationPresent(GzipResponse.class);
    }

    /**
     * acceptEncoding 에 GZIP 이 포함되어 있는지, 설정한 path 와 match 되는지 확인한다.
     */
    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) {
        return notAcceptGzipEncoding(request) || pathNotMatched(request);
    }

    private boolean notAcceptGzipEncoding(final HttpServletRequest request) {
        final String acceptEncoding = request.getHeader(HttpHeaders.ACCEPT_ENCODING);
        return acceptEncoding == null || !acceptEncoding.contains(CONTENT_ENCODING_GZIP);
    }

    private boolean pathNotMatched(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        return gzipPathInfo.keySet().stream().noneMatch(path -> pathMatcher.match(path, requestURI));
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        // gzipPathInfo 에서 requestURI 가 urlPattern 과 match 하는게 있는지 찾는다.
        final GzipLevel gzipLevel = gzipPathInfo.entrySet().stream()
                .filter(path -> pathMatcher.match(path.getKey(), request.getRequestURI()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("no matched uri for GzipResponseFilter"))
                .getValue();
        final GzipHttpServletResponseWrapper gzipResponse = new GzipHttpServletResponseWrapper(response, gzipLevel);
        gzipResponse.setHeader(HttpHeaders.CONTENT_ENCODING, CONTENT_ENCODING_GZIP);
        filterChain.doFilter(request, gzipResponse);
        gzipResponse.close();
    }

    static class GzipHttpServletResponseWrapper extends HttpServletResponseWrapper {
        private final GZIPOutputStream gzipOutputStream;
        private ServletOutputStream outputStream;
        private PrintWriter writer;

        /**
         * HttpServletResponse 의 outputStream 과 Writer 을 교체한다
         */
        public GzipHttpServletResponseWrapper(final HttpServletResponse response) throws IOException {
            this(response, GzipLevel.DEFAULT);
        }

        public GzipHttpServletResponseWrapper(final HttpServletResponse response, final GzipLevel gzipLevel) throws IOException {
            super(response);
            this.gzipOutputStream = new CustomGZIPOutputStream(response.getOutputStream(), gzipLevel.getLevel());
        }

        @Override
        public ServletOutputStream getOutputStream() {
            if (this.outputStream == null) {
                this.outputStream = new CustomServletOutputStream(this.gzipOutputStream);
            }
            return this.outputStream;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            if (this.writer == null) {
                this.writer = new PrintWriter(new OutputStreamWriter(this.gzipOutputStream, getCharacterEncoding()));
            }
            return this.writer;
        }

        @Override
        public void flushBuffer() throws IOException {
            if (this.writer != null) {
                this.writer.flush();
            }
            if (this.outputStream != null) {
                this.outputStream.flush();
            }
            this.gzipOutputStream.flush();
        }

        public void close() throws IOException {
            this.gzipOutputStream.close();
        }
    }

    /**
     * 압축 레벨 설정하는 기능이 없어 기존 GZIPOutputStream 을 상속해서 레벨 설정 기능 추가
     */
    static class CustomGZIPOutputStream extends GZIPOutputStream {
        public CustomGZIPOutputStream(final OutputStream out) throws IOException {
            this(out, Deflater.BEST_SPEED);
        }

        public CustomGZIPOutputStream(final OutputStream out, final int compressionLevel) throws IOException {
            super(out);
            setLevel(compressionLevel);
        }

        /**
         * 압축 레벨 설정
         * 높을수록 응답속도가 느려지는 대신 크기가 작아진다.
         */
        public void setLevel(final int level) {
            def.setLevel(level);
        }
    }

    static class CustomServletOutputStream extends ServletOutputStream {

        private final GZIPOutputStream gzipOutputStream;

        public CustomServletOutputStream(final GZIPOutputStream gzipOutputStream) {
            this.gzipOutputStream = gzipOutputStream;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(final WriteListener writeListener) {
        }

        @Override
        public void write(final int b) throws IOException {
            this.gzipOutputStream.write(b);
        }
    }

}

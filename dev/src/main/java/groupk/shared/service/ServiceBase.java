package groupk.shared.service;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class ServiceBase {

    protected Response voidOk() {
        return new Response(true, null);
    }

    protected Response voidError(String message) {
        return new Response(false, message);
    }

    protected <T> ResponseT<T> ok(T data) {
        return new ResponseT<T>(true, null, data);
    }

    protected <T> ResponseT<T> error(String error) {
        return new ResponseT<T>(false, error, null);
    }

    protected <T> ResponseT<T> responseFor(ThrowingFactory<T> lambda) {
        try {
            return ok(lambda.get());
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    protected Response responseForVoid(ThrowingRunnable lambda) {
        try {
            lambda.run();
            return voidOk();
        } catch (Exception e) {
            return voidError(e.getMessage());
        }
    }

    public static class Response {
        public final boolean success;
        public final String error;


        private Response(boolean success, String error) {
            this.success = success;
            this.error = error;
        }

        @Override
        public String toString() {
            return success ? "OK" : "Error: " + error;
        }
    }

    public static class ResponseT<T> extends Response {
        public final T data;

        private ResponseT(boolean success, String error, T data) {
            super(success, error);
            this.data = data;
        }

        @Override
        public String toString() {
            return success ? Objects.toString(data) : "Error: " + error;
        }
    }

    public static interface ThrowingFactory<T> {
        T get() throws Exception;
    }

    public static interface ThrowingRunnable {
        void run() throws Exception;
    }
}

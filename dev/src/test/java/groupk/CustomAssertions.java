package groupk;

import groupk.shared.business.Facade;
import groupk.shared.service.Response;
import groupk.shared.service.ServiceBase;

import static org.junit.jupiter.api.Assertions.*;
public class CustomAssertions {
    public static <T> T assertSuccess(Response<T> response) {
        assertFalse(response.isError(), response.getErrorMessage());
        return response.getValue();
    }

    public static <T> T assertSuccess(Facade.ResponseT<T> response) {
        assertTrue(response.success, response.error);
        return response.data;
    }

    public static void assertSuccess(Facade.SI_Response response) {
        assertTrue(response.success, response.error);
    }

    public static void assertSuccess(ServiceBase.Response response) {
        assertTrue(response.success, response.error);
    }

    public static <T> T assertFailure(Response<T> response) {
        assertTrue(response.isError(), "expected error, but succeeded with value: " + response.getValue());
        return response.getValue();
    }

    public static <T> T assertFailure(Facade.ResponseT<T> response) {
        assertFalse(response.success, "expected error, but succeeded with value: " + response.data);
        return response.data;
    }

    public static void assertFailure(Facade.SI_Response response) {
        assertFalse(response.success, "expected error, but succeeded");
    }

    public static void assertFailure(ServiceBase.Response response) {
        assertFalse(response.success, "Expected error but succeeded");
    }
}

package groupk.shared.service;

public class Response<T> {
    private String errorMessage;
    private T value;

    public Response(T value) {
        this.value= value;
    }

    public Response(String msg)
    {
        this.errorMessage = msg;
    }

    public boolean isError(){
        return errorMessage != null;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public T getValue() {
        if (!isError()) {
            return value;
        }
        return null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

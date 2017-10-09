/**
 * Created by Chris Cavazos on 10/8/2017.
 */
public class MethodUsage extends Usage{

    Method method;

    public MethodUsage(Method method) {
        this.method = method;
    }

    public Method getMethod() {

        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}

package com.ANZR.Ergo;

/**
 * Created by Chris Cavazos on 10/8/2017.
 */
class MethodUsage extends Usage {

    private Method method;

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

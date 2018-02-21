package com.ANZR.Ergo;

public class ProjectData<X, Y> {

    public final X parsedClasses;
    public final Y globalEnums;

    public ProjectData(X parsedClasses, Y globalEnums) {
        this.parsedClasses = parsedClasses;
        this.globalEnums = globalEnums;
    }

}

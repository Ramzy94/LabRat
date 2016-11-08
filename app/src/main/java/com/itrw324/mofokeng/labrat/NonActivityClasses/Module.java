package com.itrw324.mofokeng.labrat.NonActivityClasses;

/**
 * Created by Mofokeng on 08-Nov-16.
 */

public class Module {
    private String moduleCode;
    private String moduleDescription;
    private String moduleLecturer;

    public Module(String moduleCode, String moduleDescription, String moduleLecturer) {
        this.moduleCode = moduleCode;
        this.moduleDescription = moduleDescription;
        this.moduleLecturer = moduleLecturer;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public String getModuleLecturer() {
        return moduleLecturer;
    }
}

package com.csp.json2class.engine;

/**
 * @description:
 * @author: csp
 * @email:chengsipeng@ebaolife.com
 * @createDate: 2022-10-10 9:40
 * @version: 1.0
 */
public class MappingDefinition {
    private String targetLocation = "";
    private String converterReference = "com.csp.js.coter.DefaultConverter";
    private boolean isIgnore = false;

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public String getConverterReference() {
        return converterReference;
    }

    public void setConverterReference(String converterReference) {
        this.converterReference = converterReference;
    }

    public boolean isIgnore() {
        return isIgnore;
    }

    public void setIgnore(boolean ignore) {
        isIgnore = ignore;
    }
}

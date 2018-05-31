package cn.edu.zafu.openfiredemo.im;

import java.util.Map;

/**
 * xml建造器
 * Created by xrc on 18/5/25.
 */

public class XmlBuilder {

    private StringBuilder mStringBuilder;
    private static final String START_TAG = "<";
    private static final String END_TAG = ">";
    private static final String SYMBOL = "/";
    private static final String EQUAL = "=";
    private static final String SPACE = " ";
    private static final String QUOTATION_MARK = "'";
    private static final String SELF_END_TAG = SYMBOL + END_TAG;

    public XmlBuilder() {
        mStringBuilder = new StringBuilder();
    }

    public XmlBuilder addElement(String element) {
        mStringBuilder.append(START_TAG);
        mStringBuilder.append(element);
        mStringBuilder.append(END_TAG);
        mStringBuilder.append(START_TAG);
        mStringBuilder.append(SYMBOL);
        mStringBuilder.append(element);
        mStringBuilder.append(END_TAG);
        return this;
    }

    public XmlBuilder addElement(String element, String namespace) {
        mStringBuilder.append(START_TAG);
        mStringBuilder.append(element);
        mStringBuilder.append(" xmlns=");
        mStringBuilder.append(QUOTATION_MARK);
        mStringBuilder.append(namespace);
        mStringBuilder.append(QUOTATION_MARK);
        mStringBuilder.append(END_TAG);
        mStringBuilder.append(START_TAG);
        mStringBuilder.append(SYMBOL);
        mStringBuilder.append(element);
        mStringBuilder.append(END_TAG);
        return this;
    }

    public XmlBuilder addInnerElement(String targetElement, String element, String value) {
        int index = mStringBuilder.lastIndexOf(START_TAG + SYMBOL + targetElement + END_TAG);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(START_TAG);
        stringBuilder.append(element);
        stringBuilder.append(END_TAG);
        stringBuilder.append(value);
        stringBuilder.append(START_TAG);
        stringBuilder.append(SYMBOL);
        stringBuilder.append(element);
        stringBuilder.append(END_TAG);
        mStringBuilder.insert(index, stringBuilder);
        return this;
    }

    public XmlBuilder addSelfEndElement(String element) {
        mStringBuilder.append(START_TAG);
        mStringBuilder.append(element);
        mStringBuilder.append(SELF_END_TAG);
        return this;
    }

    public XmlBuilder withAttributes(String targetElement, Map<String, String> attributes) {
        if (attributes == null || attributes.size() == 0) {
            return this;
        }
        int index = mStringBuilder.indexOf(targetElement);
        StringBuilder stringBuilder = new StringBuilder();
        for (String attrName : attributes.keySet()) {
            stringBuilder.append(SPACE);
            stringBuilder.append(attrName);
            stringBuilder.append(EQUAL);
            stringBuilder.append(QUOTATION_MARK);
            stringBuilder.append(attributes.get(attrName));
            stringBuilder.append(QUOTATION_MARK);
        }
        mStringBuilder.insert(index + targetElement.length(), stringBuilder);
        return this;
    }

    public String build() {
        return mStringBuilder.toString();
    }

    public XmlBuilder withValue(String targetElement, String value) {
        int index = mStringBuilder.lastIndexOf(targetElement);
        mStringBuilder.insert(index - 2, value);
        return this;
    }
}

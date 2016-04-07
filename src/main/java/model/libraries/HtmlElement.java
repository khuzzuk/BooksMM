package model.libraries;

import model.HtmlContent;

public interface HtmlElement extends Iterable<HtmlElement> {
    HtmlElement getAllElements();
    HtmlElement getElementsByAttribute(String attributeName, String attributeValue);
    String getText(int i);

    HtmlElement select(String elementName);
    int size();
}

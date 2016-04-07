package model;

/**
 * This interface is provided to handle downloaded Html content which is organized in graph manner.
 * Because of that, all objects of that type have a children. There are no links to parent,
 * as long as this is a one-sided graph.
 */
public interface HtmlElement extends Iterable<HtmlElement> {
    /**
     * This methods will return another HtmlElement which should consist all child HtmlElements.
     * When list is empty, then parent of returned element have no children.
     * @return {@link HtmlElement} with all children elements.
     */
    HtmlElement getAllElements();

    /**
     * This method will return selected {@link HtmlElement} children of actual {@link HtmlElement}.
     * There will be onle elements which have pointed attribute and the attribute must have specific value.
     * @param attributeName {@link String} with attribute name.
     * @param attributeValue {@link String} with attribute value.
     * @return {@link HtmlElement} children elements which meet specified requirements.
     */
    HtmlElement getElementsByAttribute(String attributeName, String attributeValue);

    /**
     * This method will return text of specified element from the list. When this is whole page,
     * only one root element you can find there. Indexing starts with 0. There will be thrown
     * {@link ArrayIndexOutOfBoundsException} when i is greater or equal then size of the list.
     * To determine how many elements is in the list, use {@link HtmlElement#size()} method.
     * @param i number of element index.
     * @return {@link String} with text placed in the element.
     */
    String getText(int i);

    /**
     * This method will return {@link HtmlElement} with all children that have specified
     * element name. When list size is equal to zero, parent element have no children.
     * In order to check that use {@link HtmlElement#size()} method.
     * @param elementName {@link String} name of elements that will be returned.
     * @return {@link HtmlElement} with all children which name was specified in parameter.
     */
    HtmlElement select(String elementName);

    /**
     * This method will return the num ber of children of this {@link HtmlElement}.
     * @return number of children in this {@link HtmlElement}.
     */
    int size();
}

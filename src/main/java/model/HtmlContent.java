package model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class HtmlContent implements HtmlElement {
    Element page;

    public HtmlContent(Element page) {
        this.page = page;
    }

    @Override
    public HtmlElement getElementsByAttribute(String attributeName, String attributeValue){
        return new HtmlContentList(page.getElementsByAttributeValue(attributeName, attributeValue));
    }

    @Override
    public String getText(int i) {
        return page.getAllElements().get(i).ownText();
    }

    @Override
    public HtmlContentList select(String elementName) {
        return new HtmlContentList(page.select(elementName));
    }

    @Override
    public int size() {
        return page.getAllElements().size();
    }

    @Override
    public Iterator iterator() {
        return page.getAllElements().iterator();
    }

    @Override
    public HtmlElement getAllElements() {
        return new HtmlContentList(page.getAllElements());
    }

    public static class HtmlContentList implements HtmlElement{
        Elements elements;

        public HtmlContentList(Elements elements) {
            this.elements = elements;
        }
        @Override
        public Iterator<HtmlElement> iterator() {
            Collection<HtmlElement> iterable = new ArrayList<>();
            for (Element e : elements){
                iterable.add(new HtmlContent(e));
            }
            return iterable.iterator();
        }

        @Override
        public HtmlElement getElementsByAttribute(String attributeName, String attributeValue) {
            return new HtmlContentList(elements.get(0).getElementsByAttributeValue(attributeName, attributeValue));
        }

        @Override
        public String getText(int i) {
            return elements.get(i).ownText();
        }

        @Override
        public HtmlElement select(String elementName) {
            return new HtmlContentList(elements.select(elementName));
        }

        @Override
        public int size() {
            return elements.size();
        }

        @Override
        public HtmlElement getAllElements() {
            return new HtmlContentList(elements.get(0).getAllElements());
        }
    }
}

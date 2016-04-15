package model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HtmlContentTest {
    String attributeName = "name";
    String attributeValue = "value";
    @Test(groups = "fast")
    public void testGetElementsByAttribute() {
        //given
        Element mockedPage = mock(Element.class);
        Elements expectedElement = mock(Elements.class);
        when(mockedPage.getElementsByAttributeValue(attributeName, attributeValue)).thenReturn(expectedElement);
        HtmlElement testedElement = new HtmlContent(mockedPage);
        //when
        Elements returnedElement = ((HtmlContent.HtmlContentList)testedElement.getElementsByAttribute(attributeName, attributeValue)).elements;
        //then
        assertThat(returnedElement).isEqualTo(expectedElement);
    }

    @Test(groups = "fast")
    public void testExtractElementsFromList() {
        //given
        Elements MockedElements = mock(Elements.class);
        Elements expectedMockedElements = mock(Elements.class);
        Element mockedOneElement = mock(Element.class);
        when(MockedElements.get(0)).thenReturn(mockedOneElement);
        when(mockedOneElement.getElementsByAttributeValue(attributeName, attributeValue)).thenReturn(expectedMockedElements);
        //when
        HtmlElement element = new HtmlContent.HtmlContentList(MockedElements);
        Elements returnedElements = ((HtmlContent.HtmlContentList) element.getElementsByAttribute(attributeName, attributeValue)).elements;
        //then
        assertThat(returnedElements).isEqualTo(expectedMockedElements);
    }
}

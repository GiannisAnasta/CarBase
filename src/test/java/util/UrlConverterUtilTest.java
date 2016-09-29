package util;

import org.junit.Assert;
import org.junit.Test;

public class UrlConverterUtilTest {

    @Test
    public void testNormalizeWithOutHttpAndWww() {
        String a = "google.com";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);
    }

    @Test
    public void testNormalizeWithOutHttpAndWwwButSlash() {
        String a = "google.com/";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);
    }

    @Test
    public void testNormalizeNoChanges() {
        String a = "http://www.google.com";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);
    }

    @Test
    public void testNormalizeWithSlash() {
        String a = "http://www.google.com/";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);
    }

    @Test
    public void testNormalizeHttpWithSlashWithOutWww() {
        String a = "http://google.com/";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);
    }

    @Test
    public void testNormalizeHttpWithOutSlashAndWww() {
        String a = "http://google.com";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);
    }

    @Test
    public void testNormalizeWithtWwwAndSlash() {
        String a = "www.google.com/";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);

    }

    @Test
    public void testNormalizeWithtWwwWithOutSlash() {
        String a = "www.google.com";
        String normalized = UrlConverterUtil.normalize(a);
        Assert.assertEquals("http://www.google.com", normalized);

    }

}

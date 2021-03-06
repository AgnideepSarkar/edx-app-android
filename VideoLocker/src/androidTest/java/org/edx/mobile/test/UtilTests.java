package org.edx.mobile.test;

import android.content.Intent;
import android.net.Uri;

import org.edx.mobile.util.BrowserUtil;

public class UtilTests extends BaseTestCase {
    
    public void testBrowserOpenUrl() throws Exception {
        String url = "https://courses.edx.org/register";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        getInstrumentation().getTargetContext().startActivity(intent);

        print("finished open URL in browser");
    }

    public void testHostAndUrls() throws Exception {
        String host = "edx.org";

        assertTrue(BrowserUtil.isUrlOfHost("http://www.edx.org", host));
        assertTrue(BrowserUtil.isUrlOfHost("https://courses.edx.org", host));
        assertTrue(BrowserUtil.isUrlOfHost("https://edx.org/", host));
        assertFalse(BrowserUtil.isUrlOfHost("https://fake-domain.com/edx.org/", host));
        assertFalse(BrowserUtil.isUrlOfHost("https://fake-domain.com/xyz/", host));
    }
}

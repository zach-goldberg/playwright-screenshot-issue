package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Browser.NewContextOptions;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.ScreenshotOptions;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.ScreenshotType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class PlaywrightScreenshotIssue {

  private static final String HTML =
      """
      <html>
        <head>
          <title>Hang</title>
        </head>

        <body>
          <button onclick="loop()">Start infinite loop</button>
        </body>

        <script>
          function loop() {
            setTimeout(() => {
              while (true) {}
            }, 50)
          }
        </script>
      </html>
      """;

  public static void main(String[] args) throws IOException {
    try (Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch();
        BrowserContext browserContext =
            browser.newContext(
                new NewContextOptions()
                    .setViewportSize(1024, 768)
                    .setDeviceScaleFactor(2.0)
                    .setIsMobile(false)
                    .setLocale("en-US"));
        Page page = browserContext.newPage()) {

      byte[] screenshot = null;
      for (int i = 1; i <= 10; i++) {
        System.out.println("Iteration " + i);
        page.navigate(
            "data:text/html;base64,"
                + new String(Base64.getEncoder().encode(HTML.getBytes(StandardCharsets.UTF_8))));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start infinite loop"))
            .click();
        System.out.println("Taking screenshot " + i);
        screenshot =
            page.screenshot(
                new ScreenshotOptions()
                    .setType(ScreenshotType.JPEG)
                    .setQuality(75)
                    .setTimeout(1000D));
        System.out.println("Took screenshot " + i);
      }

      Files.write(Paths.get("test-image.jpg"), screenshot);
    }
  }
}

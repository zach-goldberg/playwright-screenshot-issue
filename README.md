# Playwright Screenshot Bug

## Steps to reproduce

Simply run the main method in `PlaywrightScreenshotIssue.java`. You will most likely see the program get stuck in calling `Page.screenshot()` after printing "Taking screenshot 1" and never terminate. 
The expected behavior is that Playwright respects the 1000ms timeout passed into the screenshot method and throws a timeout error.
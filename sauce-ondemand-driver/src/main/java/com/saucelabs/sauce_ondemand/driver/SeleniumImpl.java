/*
 * The MIT License
 *
 * Copyright (c) 2010, InfraDNA, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.saucelabs.sauce_ondemand.driver;

import com.thoughtworks.selenium.DefaultSelenium;

import java.lang.reflect.Field;

/**
 * @author Kohsuke Kawaguchi
 */
class SeleniumImpl extends DefaultSelenium implements SauceOnDemandSelenium {
    SeleniumImpl(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
        super(serverHost, serverPort, browserStartCommand, browserURL);
    }

    @Override
    public void start() {
        super.start();
        dumpSessionId();
    }

    @Override
    public void start(String optionsString) {
        super.start(optionsString);
        dumpSessionId();
    }

    @Override
    public void start(Object optionsObject) {
        super.start(optionsObject);
        dumpSessionId();
    }

    /**
     * Dump the session ID, so that it can be captured by the CI server.
     */
    private void dumpSessionId() {
        System.out.println("SauceOnDemandSessionID="+getSessionId());
    }
    
    public String getSessionId() {
        try {
            Field f = commandProcessor.getClass().getDeclaredField("sessionId");
            f.setAccessible(true);
            Object id = f.get(commandProcessor);
            if (id!=null)   return id.toString();
        } catch (NoSuchFieldException e) {
            // failed to retrieve the session ID
        } catch (IllegalAccessException e) {
            // failed to retrieve the session ID
        }
        return null;
    }
}
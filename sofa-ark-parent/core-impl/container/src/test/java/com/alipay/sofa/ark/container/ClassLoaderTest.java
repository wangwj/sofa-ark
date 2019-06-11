/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.ark.container;

import com.alipay.sofa.ark.common.util.EnvironmentUtils;
import com.alipay.sofa.ark.container.test.TestClassLoader;
import com.alipay.sofa.ark.spi.constant.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.net.URLClassLoader;

/**
 * @author qilong.zql
 * @since 3.1.0
 */
public class ClassLoaderTest extends BaseTest {

    @Test
    public void testDefaultDelegate() throws Throwable {
        // incompatible with JDK9+
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        TestClassLoader testClassLoader = new TestClassLoader("mock:1.0", urlClassLoader.getURLs(),
            urlClassLoader);

        Assert.assertEquals(urlClassLoader, testClassLoader
            .loadClass(Test.class.getCanonicalName()).getClassLoader());
        Assert.assertEquals(testClassLoader,
            testClassLoader.loadClass(ClassLoaderTest.class.getCanonicalName()).getClassLoader());
    }

    @Test
    public void testDelegateConfigure() throws Throwable {
        EnvironmentUtils.setProperty(Constants.FORCE_DELEGATE_TO_TEST_CLASSLOADER, Test.class
            .getPackage().getName());
        EnvironmentUtils.setProperty(Constants.FORCE_DELEGATE_TO_APP_CLASSLOADER,
            ClassLoaderTest.class.getPackage().getName());

        // incompatible with JDK9+
        URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        TestClassLoader testClassLoader = new TestClassLoader("mock:1.0", urlClassLoader.getURLs(),
            urlClassLoader);

        Assert.assertEquals(testClassLoader,
            testClassLoader.loadClass(Test.class.getCanonicalName()).getClassLoader());
        Assert.assertEquals(urlClassLoader,
            testClassLoader.loadClass(ClassLoaderTest.class.getCanonicalName()).getClassLoader());

        EnvironmentUtils.clearProperty(Constants.FORCE_DELEGATE_TO_APP_CLASSLOADER);
        EnvironmentUtils.clearProperty(Constants.FORCE_DELEGATE_TO_TEST_CLASSLOADER);
    }

}
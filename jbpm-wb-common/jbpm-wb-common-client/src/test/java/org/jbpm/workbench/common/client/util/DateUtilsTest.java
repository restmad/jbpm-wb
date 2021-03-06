/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.workbench.common.client.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GwtMockitoTestRunner.class)
public class DateUtilsTest {

    @Test
    public void testGetDateFormatMask() {
        String dateFormatMask = DateUtils.getDateFormatMask();
        assertEquals(dateFormatMask,
                     DateUtils.DEFAULT_DATE_FORMAT_MASK);
    }

    @Test
    public void testGetDateTimeFormatMask() {
        String dateTimeFormatMask = DateUtils.getDateFormatMask();
        assertEquals(dateTimeFormatMask,
                     DateUtils.DEFAULT_DATE_FORMAT_MASK);
    }

    @Test
    public void testGetDateStr() {
        Date now = new Date();
        String nowStr = (new SimpleDateFormat(DateUtils.getDateFormatMask()).format(now));

        assertEquals(nowStr,
                     DateUtils.getDateStr(now));
        assertEquals("",
                     DateUtils.getDateStr(null));
    }

    @Test
    public void testGetDateAndTimeStr() {
        DateUtils.getDateTimeStr(new Date());
        Date now = new Date();
        String nowStr = (new SimpleDateFormat(DateUtils.getDateTimeFormatMask()).format(now));

        assertEquals(nowStr,
                     DateUtils.getDateTimeStr(now));
        assertEquals("",
                     DateUtils.getDateTimeStr(null));
    }
}
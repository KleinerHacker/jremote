package org.pcsoft.framework.jremote.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pcsoft.framework.jremote.api.RemoteControlService;
import org.pcsoft.framework.jremote.commons.type.Contr;
import org.pcsoft.framework.jremote.commons.type.Interf1;
import org.pcsoft.framework.jremote.commons.type.Interf3;
import org.pcsoft.framework.jremote.commons.type.Interf4;

import java.util.List;

class ReflectionUtilsTest {

    @Test
    void testFindInterfaces() {
        final List<Class<?>> classList = ReflectionUtils.findInterfaces(Contr.class, clazz -> clazz.getAnnotation(RemoteControlService.class) != null);

        Assertions.assertNotNull(classList);
        Assertions.assertEquals(3, classList.size());
        Assertions.assertEquals(Interf1.class, classList.get(0));
        Assertions.assertEquals(Interf3.class, classList.get(1));
        Assertions.assertEquals(Interf4.class, classList.get(2));
    }

}
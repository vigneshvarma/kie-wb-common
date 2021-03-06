/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.core.backend.definition.adapter.reflect;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.core.backend.definition.adapter.BaseFooTestBean2;
import org.kie.workbench.common.stunner.core.backend.definition.adapter.FooTestBean;
import org.kie.workbench.common.stunner.core.backend.definition.adapter.FooTestBeanBaseGrandParent;
import org.kie.workbench.common.stunner.core.backend.definition.adapter.FooTestBeanBaseParent;
import org.kie.workbench.common.stunner.core.backend.definition.adapter.FooTestBeanNoParent;
import org.kie.workbench.common.stunner.core.definition.property.PropertyMetaTypes;
import org.kie.workbench.common.stunner.core.factory.graph.ElementFactory;
import org.kie.workbench.common.stunner.core.factory.graph.NodeFactory;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BackendDefinitionAdapterTest extends AbstractBackendAdapterTest {

    private BackendDefinitionAdapter<Object> tested;

    @Before
    public void setup() {
        super.setup();

        final Set p = new HashSet<Object>() {{
            add(instance.fooPropertySet.fooProperty);
        }};
        tested = new BackendDefinitionAdapter<>();
        when(adapterManager.forDefinition()).thenReturn(tested);
    }

    @Test
    public void testGetId() {
        final String id = tested.getId(instance).value();
        assertEquals(FooTestBean.class.getName(), id);
    }

    @Test
    public void testGetCategory() {
        final String category = tested.getCategory(instance);
        assertEquals(FooTestBean.CATEGORY, category);
    }

    @Test
    public void testGetTitle() {
        final String title = tested.getTitle(instance);
        assertEquals(FooTestBean.TITLE, title);
    }

    @Test
    public void testGetDescription() {
        final String description = tested.getDescription(instance);
        assertEquals(FooTestBean.DESCRIPTION, description);
    }

    @Test
    public void testGetLabels() {
        final String[] labels = tested.getLabels(instance);
        assertArrayEquals(FooTestBean.LABELS.toArray(), labels);
    }

    @Test
    public void testGraphFactory() {
        final Class<? extends ElementFactory> graphFactoryType = tested.getGraphFactoryType(instance);
        assertEquals(NodeFactory.class, graphFactoryType);
    }

    @Test
    public void baseTypeGrandParentTest() {
        String baseType = tested.getBaseType(FooTestBeanBaseGrandParent.class);
        assertEquals(baseType, BaseFooTestBean2.class.getName());
    }

    @Test
    public void baseTypeParentTest() {
        String baseType = tested.getBaseType(FooTestBeanBaseParent.class);
        assertEquals(baseType, BaseFooTestBean2.class.getName());
    }

    @Test
    public void baseTypeNoParentTest() {
        String baseType = tested.getBaseType(FooTestBeanNoParent.class);
        assertEquals(baseType, null);
    }

    @Test
    public void baseTypePrimitiveTest() {
        String baseType = tested.getBaseType(int.class);
        assertNull(baseType);
        baseType = tested.getBaseType(double.class);
        assertNull(baseType);
    }

    @Test
    public void testGetProperties() {
        String[] propertyFields = tested.getPropertyFields(instance);
        assertEquals(2, propertyFields.length);
        assertEquals("fooPropertySet.fooProperty", propertyFields[0]);
        assertEquals("fooProperty", propertyFields[1]);
        Optional<?> p0 = tested.getProperty(instance, "fooPropertySet.fooProperty");
        assertTrue(p0.isPresent());
        assertEquals(instance.fooPropertySet.fooProperty, p0.get());
        Optional<?> p1 = tested.getProperty(instance, "fooProperty");
        assertTrue(p1.isPresent());
        assertEquals(instance.fooProperty, p1.get());
    }

    @Test
    public void getNameField() {
        String nameField = tested.getMetaPropertyField(instance, PropertyMetaTypes.NAME);
        assertEquals("fooProperty", nameField);
    }
}

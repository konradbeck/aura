/*
 * Copyright (C) 2013 salesforce.com, inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.auraframework.components.ui;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.auraframework.def.ComponentDef;
import org.auraframework.def.EventType;
import org.auraframework.def.RegisterEventDef;
import org.auraframework.service.DefinitionService;
import org.auraframework.system.AuraContext.Authentication;
import org.auraframework.system.AuraContext.Format;
import org.auraframework.system.AuraContext.Mode;
import org.auraframework.test.util.AuraTestCase;
import org.junit.Test;
import org.springframework.context.annotation.Lazy;

/**
 * Automation for ui:input component.
 * 
 * @userStory a07B0000000Ftfw
 * @hierarchy Aura.Components.UI.Input
 * @priority medium
 * 
 * 
 * 
 * @since 0.0.99
 */
public class InputComponentsTest extends AuraTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        contextService.startContext(Mode.UTEST, Format.JSON, Authentication.AUTHENTICATED);
    }
    
    @Inject
    @Lazy
    private DefinitionService definitionService;

    /**
     * Verify that ui:input is registered to throw all the expected events. Also
     * verify that these events are component events. Very important that these
     * events be component events and not application event.
     * 
     * @throws Exception
     */
    @Test
    public void testDomEventsAreComponentEvents() throws Exception {
        HashMap<String, String> events = new HashMap<>();
        events.put("blur", "markup://ui:blur");
        events.put("change", "markup://ui:change");
        events.put("click", "markup://ui:click");
        events.put("dblclick", "markup://ui:dblclick");
        events.put("focus", "markup://ui:focus");
        events.put("mousedown", "markup://ui:mousedown");
        events.put("mouseup", "markup://ui:mouseup");
        events.put("mousemove", "markup://ui:mousemove");
        events.put("mouseout", "markup://ui:mouseout");
        events.put("mouseover", "markup://ui:mouseover");
        events.put("keydown", "markup://ui:keydown");
        events.put("keypress", "markup://ui:keypress");
        events.put("keyup", "markup://ui:keyup");
        events.put("select", "markup://ui:select");

        ComponentDef def = definitionService.getDefinition("ui:input", ComponentDef.class);
        assertNotNull("Failed to retrieve definition of ui:input", def);
        Map<String, RegisterEventDef> registeredEvents = def.getRegisterEventDefs();
        RegisterEventDef registeredEvent;
        for (String eventName : events.keySet()) {
            registeredEvent = registeredEvents.get(eventName);
            assertNotNull("ui:input is not registered to fire event named: " + eventName, registeredEvent);
            assertEquals(
                    "Expected ui:input to throw " + events.get(eventName) + " for eventname \"" + eventName + "\"",
                    events.get(eventName), registeredEvent.getReference().getQualifiedName());
            assertEquals("Expected " + registeredEvent.getReference().getQualifiedName()
                    + " event to be a component event but it is of type "
                    + definitionService.getDefinition(registeredEvent.getReference()).getEventType(),
                    EventType.COMPONENT,
                    definitionService.getDefinition(registeredEvent.getReference()).getEventType());
        }
    }
}

/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.language.assembler.plugins;

import com.google.common.collect.Maps;
import org.gradle.api.Incubating;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.internal.reflect.Instantiator;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.language.assembler.AssemblerSourceSet;
import org.gradle.language.assembler.internal.DefaultAssemblerSourceSet;
import org.gradle.language.assembler.plugins.internal.AssembleTaskConfig;
import org.gradle.language.base.internal.registry.LanguageRegistry;
import org.gradle.language.base.internal.SourceTransformTaskConfig;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;
import org.gradle.language.nativeplatform.internal.NativeLanguageRegistration;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.gradle.nativeplatform.internal.DefaultTool;

import java.util.Map;

/**
 * Adds core Assembler language support.
 */
@Incubating
public class AssemblerLangPlugin implements Plugin<Project> {

    public void apply(Project project) {
        project.getPluginManager().apply(ComponentModelBasePlugin.class);
    }

    /**
     * Model rules.
     */
    @SuppressWarnings("UnusedDeclaration")
    @RuleSource
    static class Rules {
        @Mutate
        void registerLanguage(LanguageRegistry languages, ServiceRegistry serviceRegistry) {
            languages.add(new Assembler(serviceRegistry.get(Instantiator.class)));
        }
    }

    private static class Assembler extends NativeLanguageRegistration<AssemblerSourceSet> {
        public Assembler(Instantiator instantiator) {
            super(instantiator);
        }

        public String getName() {
            return "asm";
        }

        public Class<AssemblerSourceSet> getSourceSetType() {
            return AssemblerSourceSet.class;
        }

        public Class<? extends AssemblerSourceSet> getSourceSetImplementation() {
            return DefaultAssemblerSourceSet.class;
        }

        public Map<String, Class<?>> getBinaryTools() {
            Map<String, Class<?>> tools = Maps.newLinkedHashMap();
            tools.put("assembler", DefaultTool.class);
            return tools;
        }

        public SourceTransformTaskConfig getTransformTask() {
            return new AssembleTaskConfig();
        }
    }
}

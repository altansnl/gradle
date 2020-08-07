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

package org.gradle.api.internal.initialization;

import org.gradle.api.internal.DomainObjectContext;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.internal.Factory;
import org.gradle.internal.model.CalculatedModelValue;
import org.gradle.internal.model.ModelContainer;
import org.gradle.util.Path;

import javax.annotation.Nullable;
import java.util.function.Function;

public class RootScriptDomainObjectContext implements DomainObjectContext, ModelContainer {

    public static final DomainObjectContext INSTANCE = new RootScriptDomainObjectContext();

    private RootScriptDomainObjectContext() {
    }

    @Override
    public Path identityPath(String name) {
        return Path.path(name);
    }

    @Override
    public Path projectPath(String name) {
        return Path.path(name);
    }

    @Override
    public Path getProjectPath() {
        return null;
    }

    @Nullable
    @Override
    public ProjectInternal getProject() {
        return null;
    }

    @Override
    public ModelContainer getModel() {
        return this;
    }

    @Override
    public boolean hasMutableState() {
        return true;
    }

    @Override
    public <T> T withMutableState(Factory<? extends T> factory) {
        return factory.create();
    }

    @Override
    public void withMutableState(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void withLenientState(Runnable runnable) {
        runnable.run();
    }

    @Override
    public Path getBuildPath() {
        return Path.ROOT;
    }

    @Override
    public boolean isScript() {
        return true;
    }

    @Override
    public <T> CalculatedModelValue<T> newCalculatedValue(@Nullable T initialValue) {
        return new CalculatedModelValueImpl<>(initialValue);
    }

    private static class CalculatedModelValueImpl<T> implements CalculatedModelValue<T> {
        private T value;

        CalculatedModelValueImpl(@Nullable T initialValue) {
            value = initialValue;
        }

        @Override
        public T get() throws IllegalStateException {
            T currentValue = getOrNull();
            if (currentValue == null) {
                throw new IllegalStateException("No value is available.");
            }
            return currentValue;
        }

        @Override
        public T getOrNull() {
            return value;
        }

        @Override
        public T update(Function<T, T> updateFunction) {
            T newValue = updateFunction.apply(value);
            value = newValue;
            return newValue;
        }
    }
}

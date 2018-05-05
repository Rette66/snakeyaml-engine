/**
 * Copyright (c) 2018, http://www.snakeyaml.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.snakeyaml.engine.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

import org.snakeyaml.engine.emitter.Emitter;
import org.snakeyaml.engine.nodes.Node;
import org.snakeyaml.engine.representer.StandardRepresenter;
import org.snakeyaml.engine.serializer.Serializer;

public class Dump {

    private DumpSettings settings;

    /**
     * @param settings - configuration
     */
    public Dump(DumpSettings settings) {
        Objects.requireNonNull(settings, "DumpSettings cannot be null");
        this.settings = settings;
    }

    public String dumpToString(Object yaml) {
        StandardRepresenter representer = new StandardRepresenter();
        StreamToString writer = new StreamToString();
        Serializer serializer = new Serializer(settings, new Emitter(settings, writer));
        serializer.open();
        Node node = representer.represent(yaml);
        serializer.serialize(node);
        serializer.close();
        return writer.getData();
    }

    public void dump(Object yaml, StreamDataWriter streamDataWriter) {
        StandardRepresenter representer = new StandardRepresenter();
        Node node = representer.represent(yaml);
        Serializer serializer = new Serializer(settings, new Emitter(settings, streamDataWriter));
        serializer.open();
        serializer.serialize(node);
        serializer.close();
        return;
    }
}

class StreamToString implements StreamDataWriter {
    private final StringWriter writer = new StringWriter();

    @Override
    public void flush() {
        writer.flush();
    }

    @Override
    public void write(char[] cbuf) {
        try {
            writer.write(cbuf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String str) {
        writer.write(str);
    }

    @Override
    public void write(String str, int off, int len) {
        writer.write(str, off, len);
    }

    public String getData() {
        return writer.toString();
    }
}





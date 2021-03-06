//_XXXXX_

//eagle/eagle-core/eagle-alert-parent/eagle-alert/alert-engine/src/main/java/org/apache/eagle/alert/engine/router/impl/StormOutputCollector.java
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.eagle.alert.engine.router.impl;

import backtype.storm.task.OutputCollector;
import org.apache.eagle.alert.engine.model.PartitionedEvent;
import org.apache.eagle.alert.engine.router.StreamOutputCollector;
import org.apache.eagle.alert.engine.serialization.PartitionedEventSerializer;

import java.util.Collections;
import java.util.List;

public class StormOutputCollector implements StreamOutputCollector {

    private final OutputCollector outputCollector;
    private final PartitionedEventSerializer serializer;

    public StormOutputCollector(OutputCollector outputCollector, PartitionedEventSerializer serializer) {
        this.outputCollector = outputCollector;
        this.serializer = serializer;
    }

    public StormOutputCollector(OutputCollector outputCollector) {
        this(outputCollector, null);
    }

    @Override
    public void emit(String streamId, PartitionedEvent partitionedEvent) throws Exception {
        if (this.serializer == null) {
            outputCollector.emit(streamId, Collections.singletonList(partitionedEvent.getAnchor()), Collections.singletonList(partitionedEvent));
        } else {
            outputCollector.emit(streamId, Collections.singletonList(partitionedEvent.getAnchor()), Collections.singletonList(serializer.serialize(partitionedEvent)));
        }
    }

    @Override
    public void emit(List<Object> tuple) {
        outputCollector.emit(tuple);
    }

    @Override
    public void _XXXXX_(PartitionedEvent partitionedEvent) {
        outputCollector.ack(partitionedEvent.getAnchor());
    }

    @Override
    public void fail(PartitionedEvent partitionedEvent) {
        this.outputCollector.fail(partitionedEvent.getAnchor());
    }
}

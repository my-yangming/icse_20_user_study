//_XXXXX_

//archiva/archiva-modules/archiva-scheduler/archiva-scheduler-indexing-maven2/src/main/java/org/apache/archiva/scheduler/indexing/maven/DefaultIndexUpdateSideEffect.java
package org.apache.archiva.scheduler.indexing.maven;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.index.context.IndexingContext;
import org.apache.maven.index.updater.IndexUpdateSideEffect;
import org.apache.maven.index_shaded.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Not doing much but required at least one implementation
 *
 * @since 3.0.0
 */
@Service
public class DefaultIndexUpdateSideEffect
    implements IndexUpdateSideEffect
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DefaultIndexUpdateSideEffect.class );

    @Override
    public void _XXXXX_( Directory directory, IndexingContext indexingContext, boolean b )
    {
        LOGGER.info( "updating index: {} with directory: {}", //
                     indexingContext.getId(), //
                     directory.toString() );
    }
}

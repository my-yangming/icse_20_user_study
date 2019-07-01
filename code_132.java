//_XXXXX_

//archiva/archiva-modules/plugins/stage-repository-merge/src/main/java/org/apache/archiva/stagerepository/merge/Maven2RepositoryMerger.java
package org.apache.archiva.stagerepository.merge;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.archiva.common.utils.VersionComparator;
import org.apache.archiva.common.utils.VersionUtil;
import org.apache.archiva.configuration.ArchivaConfiguration;
import org.apache.archiva.configuration.Configuration;
import org.apache.archiva.configuration.ManagedRepositoryConfiguration;
import org.apache.archiva.maven2.metadata.MavenMetadataReader;
import org.apache.archiva.metadata.model.ArtifactMetadata;
import org.apache.archiva.metadata.repository.MetadataRepository;
import org.apache.archiva.metadata.repository.MetadataRepositoryException;
import org.apache.archiva.metadata.repository.filter.Filter;
import org.apache.archiva.metadata.repository.storage.RepositoryPathTranslator;
import org.apache.archiva.model.ArchivaRepositoryMetadata;
import org.apache.archiva.repository.RepositoryException;
import org.apache.archiva.repository.metadata.RepositoryMetadataException;
import org.apache.archiva.repository.metadata.RepositoryMetadataWriter;
import org.apache.archiva.xml.XMLException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 *
 */
@Service ("repositoryMerger#maven2")
public class Maven2RepositoryMerger
    implements RepositoryMerger
{

    private Logger log = LoggerFactory.getLogger( getClass() );

    /**
     *
     */
    private ArchivaConfiguration configuration;

    /**
     *
     */
    private RepositoryPathTranslator pathTranslator;

    private static final String METADATA_FILENAME = "maven-metadata.xml";

    @Inject
    public Maven2RepositoryMerger(
        @Named (value = "archivaConfiguration#default") ArchivaConfiguration archivaConfiguration,
        @Named (value = "repositoryPathTranslator#maven2") RepositoryPathTranslator repositoryPathTranslator )
    {
        this.configuration = archivaConfiguration;
        this.pathTranslator = repositoryPathTranslator;
    }

    public void setConfiguration( ArchivaConfiguration configuration )
    {
        this.configuration = configuration;
    }

    @Override
    public void merge( MetadataRepository metadataRepository, String sourceRepoId, String targetRepoId )
        throws RepositoryMergerException
    {

        try
        {
            List<ArtifactMetadata> artifactsInSourceRepo = metadataRepository.getArtifacts( sourceRepoId );
            for ( ArtifactMetadata artifactMetadata : artifactsInSourceRepo )
            {
                artifactMetadata.setRepositoryId( targetRepoId );
                createFolderStructure( sourceRepoId, targetRepoId, artifactMetadata );
            }
        }
        catch ( MetadataRepositoryException e )
        {
            throw new RepositoryMergerException( e.getMessage(), e );
        }
        catch ( IOException e )
        {
            throw new RepositoryMergerException( e.getMessage(), e );
        }
        catch ( RepositoryException e )
        {
            throw new RepositoryMergerException( e.getMessage(), e );
        }
    }

    // TODO when UI needs a subset to merge
    @Override
    public void merge( MetadataRepository metadataRepository, String sourceRepoId, String targetRepoId,
                       Filter<ArtifactMetadata> filter )
        throws RepositoryMergerException
    {
        try
        {
            List<ArtifactMetadata> sourceArtifacts = metadataRepository.getArtifacts( sourceRepoId );
            for ( ArtifactMetadata metadata : sourceArtifacts )
            {
                if ( filter.accept( metadata ) )
                {
                    createFolderStructure( sourceRepoId, targetRepoId, metadata );
                }
            }
        }
        catch ( MetadataRepositoryException e )
        {
            throw new RepositoryMergerException( e.getMessage(), e );
        }
        catch ( IOException e )
        {
            throw new RepositoryMergerException( e.getMessage(), e );
        }
        catch ( RepositoryException e )
        {
            throw new RepositoryMergerException( e.getMessage(), e );
        }
    }

    private void createFolderStructure( String sourceRepoId, String targetRepoId, ArtifactMetadata artifactMetadata )
        throws IOException, RepositoryException
    {
        Configuration config = configuration.getConfiguration();

        ManagedRepositoryConfiguration targetRepoConfig = config.findManagedRepositoryById( targetRepoId );

        ManagedRepositoryConfiguration sourceRepoConfig = config.findManagedRepositoryById( sourceRepoId );

        Date lastUpdatedTimestamp = Calendar.getInstance().getTime();

        TimeZone timezone = TimeZone.getTimeZone( "UTC" );

        DateFormat fmt = new SimpleDateFormat( "yyyyMMdd.HHmmss" );

        fmt.setTimeZone( timezone );

        String timestamp = fmt.format( lastUpdatedTimestamp );

        String targetRepoPath = targetRepoConfig.getLocation();

        String sourceRepoPath = sourceRepoConfig.getLocation();

        String artifactPath = pathTranslator.toPath( artifactMetadata.getNamespace(), artifactMetadata.getProject(),
                                                     artifactMetadata.getProjectVersion(), artifactMetadata.getId() );

        Path sourceArtifactFile = Paths.get( sourceRepoPath, artifactPath );

        Path targetArtifactFile = Paths.get( targetRepoPath, artifactPath );

        log.debug( "artifactPath {}", artifactPath );

        int lastIndex = artifactPath.lastIndexOf( RepositoryPathTranslator.PATH_SEPARATOR );

        Path targetFile = Paths.get( targetRepoPath, artifactPath.substring( 0, lastIndex ) );

        if ( !Files.exists(targetFile) )
        {
            // create the folder structure when it does not exist
            Files.createDirectories(targetFile);
        }
        // artifact copying
        copyFile( sourceArtifactFile, targetArtifactFile );

        // pom file copying
        // TODO need to use path translator to get the pom file path
//        String fileName = artifactMetadata.getProject() + "-" + artifactMetadata.getVersion() + ".pom";
//
//        File sourcePomFile =
//            pathTranslator.toFile( new File( sourceRepoPath ), artifactMetadata.getId(), artifactMetadata.getProject(),
//                                   artifactMetadata.getVersion(), fileName );
//
//        String relativePathToPomFile = sourcePomFile.getAbsolutePath().split( sourceRepoPath )[1];
//        File targetPomFile = new File( targetRepoPath, relativePathToPomFile );

        //pom file copying  (file path is taken with out using path translator)

        String index = artifactPath.substring( lastIndex + 1 );
        int last = index.lastIndexOf( '.' );
        Path sourcePomFile = Paths.get( sourceRepoPath,
                                       artifactPath.substring( 0, lastIndex ) + "/" + artifactPath.substring(
                                           lastIndex + 1 ).substring( 0, last ) + ".pom" );
        Path targetPomFile = Paths.get( targetRepoPath,
                                       artifactPath.substring( 0, lastIndex ) + "/" + artifactPath.substring(
                                           lastIndex + 1 ).substring( 0, last ) + ".pom" );

        if ( !Files.exists(targetPomFile) && Files.exists(sourcePomFile) )
        {
            copyFile( sourcePomFile, targetPomFile );
        }

        // explicitly update only if metadata-updater consumer is not enabled!
        if ( !config.getRepositoryScanning().getKnownContentConsumers().contains( "metadata-updater" ) )
        {

            // updating version metadata files
            Path versionMetaDataFileInSourceRepo =
                pathTranslator.toFile( Paths.get( sourceRepoPath ), artifactMetadata.getNamespace(),
                                       artifactMetadata.getProject(), artifactMetadata.getVersion(),
                                       METADATA_FILENAME );

            if ( Files.exists(versionMetaDataFileInSourceRepo) )
            {//Pattern quote for windows path
                String relativePathToVersionMetadataFile =
                    versionMetaDataFileInSourceRepo.toAbsolutePath().toString().split( Pattern.quote( sourceRepoPath ) )[1];
                Path versionMetaDataFileInTargetRepo = Paths.get( targetRepoPath, relativePathToVersionMetadataFile );

                if ( !Files.exists(versionMetaDataFileInTargetRepo) )
                {
                    copyFile( versionMetaDataFileInSourceRepo, versionMetaDataFileInTargetRepo );
                }
                else
                {
                    updateVersionMetadata( versionMetaDataFileInTargetRepo, artifactMetadata, lastUpdatedTimestamp );

                }
            }

            // updating project meta data file
            Path projectDirectoryInSourceRepo = versionMetaDataFileInSourceRepo.getParent().getParent();
            Path projectMetadataFileInSourceRepo = projectDirectoryInSourceRepo.resolve(METADATA_FILENAME );

            if ( Files.exists(projectMetadataFileInSourceRepo) )
            {
                String relativePathToProjectMetadataFile =
                    projectMetadataFileInSourceRepo.toAbsolutePath().toString().split( Pattern.quote( sourceRepoPath ) )[1];
                Path projectMetadataFileInTargetRepo = Paths.get( targetRepoPath, relativePathToProjectMetadataFile );

                if ( !Files.exists(projectMetadataFileInTargetRepo) )
                {

                    copyFile( projectMetadataFileInSourceRepo, projectMetadataFileInTargetRepo );
                }
                else
                {
                    updateProjectMetadata( projectMetadataFileInTargetRepo, artifactMetadata, lastUpdatedTimestamp,
                                           timestamp );
                }
            }
        }

    }

    private void copyFile( Path sourceFile, Path targetFile )
        throws IOException
    {

        FileUtils.copyFile( sourceFile.toFile(), targetFile.toFile() );

    }

    private void updateProjectMetadata( Path projectMetaDataFileIntargetRepo, ArtifactMetadata artifactMetadata,
                                        Date lastUpdatedTimestamp, String timestamp )
        throws RepositoryMetadataException
    {
        ArrayList<String> availableVersions = new ArrayList<>();
        String latestVersion = artifactMetadata.getProjectVersion();

        ArchivaRepositoryMetadata projectMetadata = getMetadata( projectMetaDataFileIntargetRepo );

        if ( Files.exists(projectMetaDataFileIntargetRepo) )
        {
            availableVersions = (ArrayList<String>) projectMetadata.getAvailableVersions();

            Collections.sort( availableVersions, VersionComparator.getInstance() );

            if ( !availableVersions.contains( artifactMetadata.getVersion() ) )
            {
                availableVersions.add( artifactMetadata.getVersion() );
            }

            latestVersion = availableVersions.get( availableVersions.size() - 1 );
        }
        else
        {
            availableVersions.add( artifactMetadata.getProjectVersion() );
            projectMetadata.setGroupId( artifactMetadata.getNamespace() );
            projectMetadata.setArtifactId( artifactMetadata.getProject() );
        }

        if ( projectMetadata.getGroupId() == null )
        {
            projectMetadata.setGroupId( artifactMetadata.getNamespace() );
        }

        if ( projectMetadata.getArtifactId() == null )
        {
            projectMetadata.setArtifactId( artifactMetadata.getProject() );
        }

        projectMetadata.setLatestVersion( latestVersion );
        projectMetadata.setAvailableVersions( availableVersions );
        projectMetadata.setLastUpdated( timestamp );
        projectMetadata.setLastUpdatedTimestamp( lastUpdatedTimestamp );

        if ( !VersionUtil.isSnapshot( artifactMetadata.getVersion() ) )
        {
            projectMetadata.setReleasedVersion( latestVersion );
        }

        RepositoryMetadataWriter.write( projectMetadata, projectMetaDataFileIntargetRepo );

    }

    private void _XXXXX_( Path versionMetaDataFileInTargetRepo, ArtifactMetadata artifactMetadata,
                                        Date lastUpdatedTimestamp )
        throws RepositoryMetadataException
    {
        ArchivaRepositoryMetadata versionMetadata = getMetadata( versionMetaDataFileInTargetRepo );
        if ( !Files.exists(versionMetaDataFileInTargetRepo) )
        {
            versionMetadata.setGroupId( artifactMetadata.getNamespace() );
            versionMetadata.setArtifactId( artifactMetadata.getProject() );
            versionMetadata.setVersion( artifactMetadata.getProjectVersion() );
        }

        versionMetadata.setLastUpdatedTimestamp( lastUpdatedTimestamp );
        RepositoryMetadataWriter.write( versionMetadata, versionMetaDataFileInTargetRepo );
    }

    private ArchivaRepositoryMetadata getMetadata( Path metadataFile )
        throws RepositoryMetadataException
    {
        ArchivaRepositoryMetadata metadata = new ArchivaRepositoryMetadata();
        if ( Files.exists(metadataFile) )
        {
            try
            {
                metadata = MavenMetadataReader.read( metadataFile );
            }
            catch ( XMLException e )
            {
                throw new RepositoryMetadataException( e.getMessage(), e );
            }
        }
        return metadata;
    }

    @Override
    public List<ArtifactMetadata> getConflictingArtifacts( MetadataRepository metadataRepository, String sourceRepo,
                                                           String targetRepo )
        throws RepositoryMergerException
    {
        try
        {
            List<ArtifactMetadata> targetArtifacts = metadataRepository.getArtifacts( targetRepo );
            List<ArtifactMetadata> sourceArtifacts = metadataRepository.getArtifacts( sourceRepo );
            List<ArtifactMetadata> conflictsArtifacts = new ArrayList<>();

            for ( ArtifactMetadata targetArtifact : targetArtifacts )
            {
                for ( ArtifactMetadata sourceArtifact : sourceArtifacts )
                {
                    if ( isEquals( targetArtifact, sourceArtifact ) )
                    {
                        if ( !conflictsArtifacts.contains( sourceArtifact ) )
                        {
                            conflictsArtifacts.add( sourceArtifact );
                        }
                    }
                }
            }

            sourceArtifacts.removeAll( conflictsArtifacts );

            return conflictsArtifacts;
        }
        catch ( MetadataRepositoryException e )
        {
            throw new RepositoryMergerException( e.getMessage(), e );
        }
    }

    private boolean isEquals( ArtifactMetadata sourceArtifact, ArtifactMetadata targetArtifact )
    {
        boolean isSame = false;

        if ( ( sourceArtifact.getNamespace().equals( targetArtifact.getNamespace() ) )
            && ( sourceArtifact.getProject().equals( targetArtifact.getProject() ) ) && ( sourceArtifact.getId().equals(
            targetArtifact.getId() ) ) && ( sourceArtifact.getProjectVersion().equals(
            targetArtifact.getProjectVersion() ) ) )

        {
            isSame = true;

        }

        return isSame;
    }
}

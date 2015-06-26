package com.enonic.xp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.CharSource;

public class HandleRepoNodes
{
    private final static ImmutableSet<String> IGNORE_FILES = ImmutableSet.of( ".DS_Store" );

    private static final Predicate<Path> IGNORE_FILES_FILTER = ( repo ) -> !IGNORE_FILES.contains( repo.getFileName().toString() );

    private final Path root;

    private final String target;

    private final List<UpgradeModel> upgradeModels;

    private final Logger LOG = Logger.getLogger( HandleRepoNodes.class.getName() );

    private HandleRepoNodes( Builder builder )
    {
        this.root = builder.root;
        this.upgradeModels = builder.upgradeModels;
        this.target = builder.target;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public void execute()
    {
        verifyRoot();
        processRepositories( root );
    }

    private void processRepositories( final Path path )
    {
        LOG.info( "Process repositories" );

        if ( !Files.isDirectory( path ) )
        {
            throw new UpgradeException( "Expected repositories directory, found '" + path + "'" );
        }

        final Stream<Path> repositories = IOHelper.getChildren( path ).filter( IGNORE_FILES_FILTER );

        repositories.forEach( this::processRepository );
    }

    private void processRepository( final Path repository )
    {
        final String repoName = PathHelper.getLastElement( repository );

        LOG.info( "Process repository '" + repoName + "'" );

        if ( !Files.isDirectory( repository ) )
        {
            throw new UpgradeException( "Expected repository directory, found '" + repository + "'" );
        }

        final Stream<Path> branches = IOHelper.getChildren( repository ).filter( IGNORE_FILES_FILTER );

        branches.forEach( ( branch ) -> this.processBranch( branch, repoName ) );
    }

    private void processBranch( final Path branch, final String repositoryName )
    {
        final String branchName = PathHelper.getLastElement( branch );

        LOG.info( "Process branch '" + branchName + "'" );

        final Stream<Path> nodes = IOHelper.getChildren( branch );

        nodes.forEach( ( node ) -> processBranchNodes( node, repositoryName, branchName ) );
    }

    private void processBranchNodes( final Path path, final String repositoryName, final String branchName )
    {
        if ( Files.isDirectory( path ) )
        {
            IOHelper.getChildren( path ).forEach( ( child ) -> processBranchNodes( child, repositoryName, branchName ) );
        }
        else
        {
            upgradeFile( path, repositoryName, branchName );
        }
    }

    private void upgradeFile( final Path path, final String repositoryName, final String branchName )
    {
        CharSource source = IOHelper.getCharSource( path );

        for ( final UpgradeModel upgradeModel : upgradeModels )
        {
            if ( upgradeModel.supports( path, repositoryName, branchName ) )
            {
                final String upgraded = upgradeModel.upgrade( path, source );
                source = CharSource.wrap( upgraded );
            }
        }

        IOHelper.write( createTargetPath( path ), source );
    }

    private Path createTargetPath( final Path path )
    {
        return Paths.get( this.target, PathHelper.subtractPath( path, root ).toString() );
    }

    private void verifyRoot()
    {
        if ( !Files.exists( root ) )
        {
            throw new UpgradeException( "Upgrade root does not exist" );
        }

        if ( !Files.isDirectory( root ) )
        {
            throw new UpgradeException( "Upgrade root is not directory" );
        }
    }

    public static final class Builder
    {
        private Path root;

        private String target;

        private List<UpgradeModel> upgradeModels;

        private Builder()
        {
        }

        public Builder target( final String target )
        {
            this.target = target;
            return this;
        }

        public Builder sourceRoot( Path root )
        {
            this.root = root;
            return this;
        }

        public Builder upgradeModels( List<UpgradeModel> upgradeModels )
        {
            this.upgradeModels = upgradeModels;
            return this;
        }

        private void validate()
        {
            Preconditions.checkNotNull( this.root );
            Preconditions.checkNotNull( this.upgradeModels );
        }

        public HandleRepoNodes build()
        {
            return new HandleRepoNodes( this );
        }
    }

}

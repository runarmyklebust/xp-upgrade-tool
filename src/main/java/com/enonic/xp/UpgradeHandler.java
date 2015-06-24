package com.enonic.xp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.io.CharSource;

class UpgradeHandler
{
    private final Path root;

    private final static String TARGET = "upgraded";

    private final List<UpgradeModel> upgradeModels;

    private UpgradeHandler( Builder builder )
    {
        root = builder.root;
        upgradeModels = builder.upgradeModels;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public void execute()
    {
        verifyRoot();
        processChildren( root );
    }

    private void processChildren( final Path path )
    {
        if ( Files.isDirectory( path ) )
        {
            IOHelper.getChildren( path ).forEach( this::processChildren );
        }
        else
        {
            processFile( path, IOHelper.getCharSource( path ) );
        }
    }

    private void processFile( final Path path, CharSource source )
    {
        for ( final UpgradeModel upgradeModel : upgradeModels )
        {
            if ( upgradeModel.supports( path ) )
            {
                final String upgraded = upgradeModel.upgrade( path, source );
                source = CharSource.wrap( upgraded );
            }
        }

        IOHelper.write( createTargetPath( path ), source );
    }

    private Path createTargetPath( final Path path )
    {
        return Paths.get( TARGET, PathHelper.subtractPath( path, root ).toString() );
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

        private Path outputRoot;

        private List<UpgradeModel> upgradeModels;

        private Builder()
        {
        }

        public Builder sourceRoot( Path root )
        {
            this.root = root;
            return this;
        }

        public Builder outputRoot( Path outputRoot )
        {
            this.outputRoot = outputRoot;
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

        public UpgradeHandler build()
        {
            return new UpgradeHandler( this );
        }
    }
}

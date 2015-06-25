package com.enonic.xp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;

class UpgradeHandler
{
    private final Path root;

    private final static String TARGET = "upgraded";

    private final UpgradeTaskLocator upgradeTaskLocator;

    private final Logger LOG = Logger.getLogger( UpgradeHandler.class.getName() );

    private UpgradeHandler( Builder builder )
    {
        root = builder.root;
        upgradeTaskLocator = builder.upgradeTaskLocator;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public void execute()
    {
        verifyRoot();

        LOG.info( "Starting upgrade..." );

        HandleRepoNodes.create().
            sourceRoot( root ).
            upgradeModels( this.upgradeTaskLocator.getUpgradeModels() ).
            target( TARGET ).
            build().
            execute();
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

        private UpgradeTaskLocator upgradeTaskLocator;

        private Builder()
        {
        }

        public Builder sourceRoot( Path root )
        {
            this.root = root;
            return this;
        }

        public Builder upgradeModels( final UpgradeTaskLocator upgradeTaskLocator )
        {
            this.upgradeTaskLocator = upgradeTaskLocator;
            return this;
        }

        private void validate()
        {
            Preconditions.checkNotNull( this.root );
            Preconditions.checkNotNull( this.upgradeTaskLocator );
        }

        public UpgradeHandler build()
        {
            this.validate();
            return new UpgradeHandler( this );
        }
    }
}

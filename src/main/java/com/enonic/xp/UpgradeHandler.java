package com.enonic.xp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.common.io.CharSource;

class UpgradeHandler
{
    private final Path root;

    private final Path outputRoot;

    private final List<UpgradeModel> upgradeModels;

    public UpgradeHandler( final Path root, final Path outputRoot )
    {
        this.root = root;
        this.outputRoot = outputRoot;
        this.upgradeModels = new UpgradeTaskLocator().getTasks();
    }

    public void run()
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

        IOHelper.write( createOutputFilePath( path ), source );
    }

    private Path createOutputFilePath( final Path path )
    {
        return Paths.get( outputRoot.toString(), path.toString() );
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
}

package com.enonic.xp;

import java.io.File;
import java.util.logging.Logger;

class UpgradeTool
{
    private static final Logger LOG = Logger.getLogger( UpgradeTool.class.getName() );

    public static void main( String... args )
    {
        if ( args.length < 2 )
        {
            throw new UpgradeException( "Expected 2 parameters" );
        }

        final File sourceRoot = new File( args[0] );
        final File targetRoot = new File( args[1] );

        if ( !sourceRoot.exists() )
        {
            throw new UpgradeException( "Source-root does not exist" );
        }

        for ( final String arg : args )
        {
            System.out.println( arg );
        }

        final UpgradeHandler upgradeHandler = new UpgradeHandler( sourceRoot.toPath(), targetRoot.toPath() );
        upgradeHandler.run();


    }


}

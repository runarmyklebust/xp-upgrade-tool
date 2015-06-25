package com.enonic.xp.models;

import java.util.logging.Logger;

import com.enonic.xp.UpgradeModel;

abstract class AbstractUpgradeModel
    implements UpgradeModel
{
    private static final Logger LOG = Logger.getLogger( "upgrade" );


    protected void log( final String message )
    {
        LOG.info( message );
    }


}

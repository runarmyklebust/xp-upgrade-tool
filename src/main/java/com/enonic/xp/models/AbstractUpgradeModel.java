package com.enonic.xp.models;

import java.util.logging.Logger;

import com.enonic.xp.UpgradeModel;

abstract class AbstractUpgradeModel
    implements UpgradeModel
{

    private static final Logger LOG = Logger.getLogger( "upgrade" );

    AbstractUpgradeModel( final String descr )
    {
        LOG.info( "--------------------------------" );
        LOG.info( "descr" );
        LOG.info( "--------------------------------" );
    }


}

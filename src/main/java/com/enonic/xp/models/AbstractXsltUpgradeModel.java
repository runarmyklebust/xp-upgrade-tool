package com.enonic.xp.models;

import java.net.URL;
import java.nio.file.Path;

import com.google.common.io.CharSource;
import com.google.common.io.Resources;

import com.enonic.xp.UpgradeException;
import com.enonic.xp.XsltTransformer;

abstract class AbstractXsltUpgradeModel
    extends AbstractUpgradeModel
{
    private final XsltTransformer transformer;

    AbstractXsltUpgradeModel( final String xsl )
    {
        this.transformer = XsltTransformer.create( getResource( xsl ) );
    }

    private URL getResource( final String xsl )
    {

        return Resources.getResource( UpgradeModel002.class, xsl );
    }

    String transform( final Path path, final CharSource source )
    {
        try
        {
            return this.transformer.transform( source );
        }
        catch ( Exception e )
        {
            throw new UpgradeException( "Failed to upgrade model " + this.getClass().getName() + " for path '" + path + "'", e );
        }
    }


}


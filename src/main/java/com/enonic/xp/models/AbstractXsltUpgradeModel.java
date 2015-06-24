package com.enonic.xp.models;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.CharSource;
import com.google.common.io.Resources;

import com.enonic.xp.UpgradeException;
import com.enonic.xp.XsltTransformer;

abstract class AbstractXsltUpgradeModel
    extends AbstractUpgradeModel
{
    private final XsltTransformer transformer;

    AbstractXsltUpgradeModel( final String descr, final String xsl )
    {
        super( descr );
        this.transformer = XsltTransformer.create( getResource( xsl ) );
    }

    private Path getResource( final String xsl )
    {

        Resources.getResource(UpgradeModel002.class, xsl );

        return Paths.get( UpgradeModel002.class.getResource( xsl ).getPath() );
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


package com.enonic.xp.models;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.CharSource;

public class UpgradeModel001
    extends AbstractXsltUpgradeModel
{
    public UpgradeModel001()
    {
        super( "Replace input-types 'html_part' with 'string'", "UpgradeModel001.xsl" );
    }

    @Override
    public boolean supports( final Path path )
    {
        return path.endsWith( Paths.get( "_", "node.xml" ) );
    }

    @Override
    public String upgrade( final Path path, final CharSource source )
    {
        return this.transform( path, source );
    }

}

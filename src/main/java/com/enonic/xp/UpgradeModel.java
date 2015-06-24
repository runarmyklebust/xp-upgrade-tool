package com.enonic.xp;

import java.nio.file.Path;

import com.google.common.io.CharSource;

public interface UpgradeModel
{
    boolean supports( final Path path );

    String upgrade( final Path path, final CharSource source );

}

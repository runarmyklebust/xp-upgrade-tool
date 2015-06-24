package com.enonic.xp;

import java.util.List;

import com.google.common.collect.Lists;

import com.enonic.xp.models.UpgradeModel001;
import com.enonic.xp.models.UpgradeModel002;

final class UpgradeTaskLocator
{
    private final List<UpgradeModel> tasks;

    public UpgradeTaskLocator()
    {
        this.tasks = Lists.newArrayList();
        this.tasks.add( new UpgradeModel001() );
        this.tasks.add( new UpgradeModel002() );
    }

    public List<UpgradeModel> getTasks()
    {
        return tasks;
    }
}

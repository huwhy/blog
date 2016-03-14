/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.spring.jdbc;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.spring.jdbc.dialect.Dialect;

public class ActiveRecordPlugin implements IPlugin {

    public static ColumnNameStrategy nameStrategy = new DefaultNameStrategy();
    private Dialect dialect;

    public ActiveRecordPlugin setDialect(Dialect dialect) {
        if (dialect == null)
            throw new IllegalArgumentException("dialect can not be null");
        this.dialect = dialect;
        return this;
    }

    public boolean start() {
        TableMapping.me().setDialect(dialect);
        return true;
    }

    public boolean stop() {
        return true;
    }
}








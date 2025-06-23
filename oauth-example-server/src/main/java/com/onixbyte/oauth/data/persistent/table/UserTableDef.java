package com.onixbyte.oauth.data.persistent.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

public class UserTableDef extends TableDef {

    public final QueryColumn ID = new QueryColumn("", "id");

    public final QueryColumn USERNAME = new QueryColumn("", "username");

    public final QueryColumn PASSWORD = new QueryColumn("", "password");

    public final QueryColumn EMAIL = new QueryColumn("", "email");

    public final QueryColumn MSAL_OPEN_ID = new QueryColumn("", "msal_open_id");

    public final QueryColumn TOTP_SECRET = new QueryColumn("", "totp_secret");

    public final QueryColumn ALL_COLUMNS = new QueryColumn("", "*");

    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{
            ID, USERNAME, PASSWORD, EMAIL, MSAL_OPEN_ID, TOTP_SECRET
    };

    public UserTableDef() {
        super("", "users");
    }

    private UserTableDef(String schema, String tableName, String alias) {
        super(schema, tableName, alias);
    }

    public UserTableDef as(String alias) {
        var key = getNameWithSchema() + "." + alias;
        return getCache(key, (k) -> new UserTableDef("", "users", alias));
    }
}

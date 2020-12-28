package com.revature.data;

import com.revature.beans.EditorRole;

public interface EditorRoleDAO extends GenericDAO<EditorRole> {
    public EditorRole getByAbbrv(String abbrv);

}

package com.revature.data;

import com.revature.beans.Committee;
import com.revature.beans.Editor;

import java.util.Set;

public interface EditorDAO extends GenericDAO<Editor> {
    public Set<Committee> getAllCommittee(Editor editor);
}

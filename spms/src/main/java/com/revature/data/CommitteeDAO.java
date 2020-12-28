package com.revature.data;

import com.revature.beans.Committee;
import com.revature.beans.Editor;

import java.util.Set;

public interface CommitteeDAO extends GenericDAO<Committee>{
    public Set<Editor> getAllEditor();
}

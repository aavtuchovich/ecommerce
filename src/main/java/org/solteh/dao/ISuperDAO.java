package org.solteh.dao;

import java.util.List;

public interface ISuperDAO<T> {
    public List<T> getAll();
}

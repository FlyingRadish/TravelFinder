package org.liangxw.travelfinder.util.adapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by houxg on 2014/12/22.
 */
public abstract class DataLoader<T> {
    List<T> data;

    public DataLoader() {
        data = new ArrayList<>();
    }

    abstract public void loadHeader(LoaderAdapter adapter, int maxLoad);

    abstract public void loadFooter(LoaderAdapter adapter, int maxLoad);

    abstract public void loadAll(LoaderAdapter adapter);



    public void setData(List<T> data)
    {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }

    public T getItem(int position) {
        if (data == null) {
            return null;
        }
        return data.get(position);
    }

    public int getSize() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }
}

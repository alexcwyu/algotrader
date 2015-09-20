package com.unisoft.algotrader.model.id;

import com.google.common.base.Objects;

/**
 * Created by alex on 9/20/15.
 */
public class ExecId {

    public final int providerId;
    public final long execId;

    public ExecId(int providerId, long execId) {
        this.providerId = providerId;
        this.execId = execId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExecId)) return false;
        ExecId execId1 = (ExecId) o;
        return Objects.equal(providerId, execId1.providerId) &&
                Objects.equal(execId, execId1.execId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(providerId, execId);
    }

    @Override
    public String toString() {
        return "ExecId{" +
                "execId=" + execId +
                ", providerId=" + providerId +
                '}';
    }

    public long execId() {
        return execId;
    }

    public int providerId() {
        return providerId;
    }
}

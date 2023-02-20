/*
 * This file is generated by jOOQ.
 */
package com.zenith.database.dto.tables.records;


import com.zenith.database.dto.tables.Queuelength;
import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;

import java.time.OffsetDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class QueuelengthRecord extends TableRecordImpl<QueuelengthRecord> implements Record3<OffsetDateTime, Short, Short> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached QueuelengthRecord
     */
    public QueuelengthRecord() {
        super(Queuelength.QUEUELENGTH);
    }

    /**
     * Create a detached, initialised QueuelengthRecord
     */
    public QueuelengthRecord(OffsetDateTime time, Short prio, Short regular) {
        super(Queuelength.QUEUELENGTH);

        setTime(time);
        setPrio(prio);
        setRegular(regular);
    }

    /**
     * Getter for <code>public.queuelength.time</code>.
     */
    public OffsetDateTime getTime() {
        return (OffsetDateTime) get(0);
    }

    /**
     * Setter for <code>public.queuelength.time</code>.
     */
    public QueuelengthRecord setTime(OffsetDateTime value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.queuelength.prio</code>.
     */
    public Short getPrio() {
        return (Short) get(1);
    }

    /**
     * Setter for <code>public.queuelength.prio</code>.
     */
    public QueuelengthRecord setPrio(Short value) {
        set(1, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.queuelength.regular</code>.
     */
    public Short getRegular() {
        return (Short) get(2);
    }

    /**
     * Setter for <code>public.queuelength.regular</code>.
     */
    public QueuelengthRecord setRegular(Short value) {
        set(2, value);
        return this;
    }

    @Override
    public Row3<OffsetDateTime, Short, Short> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<OffsetDateTime, Short, Short> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<OffsetDateTime> field1() {
        return Queuelength.QUEUELENGTH.TIME;
    }

    @Override
    public Field<Short> field2() {
        return Queuelength.QUEUELENGTH.PRIO;
    }

    @Override
    public Field<Short> field3() {
        return Queuelength.QUEUELENGTH.REGULAR;
    }

    @Override
    public OffsetDateTime component1() {
        return getTime();
    }

    @Override
    public Short component2() {
        return getPrio();
    }

    @Override
    public Short component3() {
        return getRegular();
    }

    @Override
    public OffsetDateTime value1() {
        return getTime();
    }

    @Override
    public Short value2() {
        return getPrio();
    }

    @Override
    public Short value3() {
        return getRegular();
    }

    @Override
    public QueuelengthRecord value1(OffsetDateTime value) {
        setTime(value);
        return this;
    }

    @Override
    public QueuelengthRecord value2(Short value) {
        setPrio(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public QueuelengthRecord value3(Short value) {
        setRegular(value);
        return this;
    }

    @Override
    public QueuelengthRecord values(OffsetDateTime value1, Short value2, Short value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }
}
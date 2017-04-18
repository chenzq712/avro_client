package com.noway.avro.client.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;


public class Return extends HashMap< String, Object > {
    private static final long serialVersionUID = 2203513787220720192L;
    private static final Logger LOGGER = LoggerFactory.getLogger( Return.class );

    public enum Return_Fields {
        success, code, note
    }

    //////////////////////////////// create//////////////////////////////////
    public static Return create() {
        return new Return();
    }

    public static Return create( String key, Object value ) {
        return new Return().add( key, value );
    }

    /////////////////////////////////////////// SUCCESS/////////////////////////

    public static Return SUCCESS( Integer code, String note ) {
        Return jo = new Return();
        jo.put( Return_Fields.success.name(), true );
        jo.put( Return_Fields.code.name(), code );
        jo.put( Return_Fields.note.name(), note );
        return jo;
    }

    public static Return SUCCESS( CodeEnum code ) {
        return SUCCESS( code.code, code.note );
    }

    ///////////////////////////////////////////////// FAIL////////////////////////////
    public static Return FAIL( Integer code, String note ) {
        Return jo = new Return();
        jo.put( Return_Fields.success.name(), false );
        jo.put( Return_Fields.code.name(), code );
        jo.put( Return_Fields.note.name(), note );
        return jo;
    }

    public static Return FAIL( CodeEnum code ) {
        return FAIL( code.code, code.note );
    }

    public static Return FAIL( CodeEnum code, Exception e ) {
        return FAIL( code.code, e.getLocalizedMessage() );
    }

    //////////////////////////////////// GETTER SETTER///////////////////////////
    public Boolean is_success() {
        return ( Boolean ) this.getOrDefault( Return_Fields.success.name(), false );
    }

    public Integer get_code() {
        return ( Integer ) this.getOrDefault( Return_Fields.code.name(), CodeEnum.ERROR.code );
    }

    public String get_note() {
        return ( String ) this.getOrDefault( Return_Fields.note.name(), "" );
    }

    //////////////////////// @Override/////////////////////////////////////
    @Override
    public Return put( String key, Object value ) {
        super.put( key, value );
        return this;
    }

    public Return add( String key, Object value ) {
        super.put( key, value );
        return this;
    }

}


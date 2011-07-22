package org.twuni.common.orm.exception;

public class ObjectNotFoundException extends RuntimeException {
	
	public ObjectNotFoundException( Object object ) {
		super( String.format( "Object not found: %s", object ) );
	}

}

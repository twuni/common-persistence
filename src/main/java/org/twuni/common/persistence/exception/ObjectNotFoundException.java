package org.twuni.common.persistence.exception;

public class ObjectNotFoundException extends RuntimeException {
	
	public ObjectNotFoundException( Object object ) {
		super( String.format( "Object not found: %s", object ) );
	}

}

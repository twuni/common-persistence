package org.twuni.common.persistence.exception;

public class RollbackException extends RuntimeException {

	public RollbackException( Throwable throwable ) {
		super( throwable );
	}

}

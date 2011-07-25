package org.twuni.common.persistence;

public interface Transaction {

	public void perform( Session session );

}

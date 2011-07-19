package org.twuni.common.orm;

public interface Transaction {

	public void perform( Session session );

}

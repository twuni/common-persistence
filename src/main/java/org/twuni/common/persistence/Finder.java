package org.twuni.common.persistence;

import java.util.List;

public interface Finder<T> {

	public List<T> list( int limit );

	public List<T> list();

	public T unique();

}

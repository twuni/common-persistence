package org.twuni.common.persistence;

import java.util.ArrayList;
import java.util.List;

public class Migration implements Comparable<Migration> {

	public static List<Transaction> migrate( int oldVersion, int newVersion, Migration... history ) {

		List<Transaction> transactions = new ArrayList<Transaction>();

		if( oldVersion == newVersion ) {
			return transactions;
		}

		if( oldVersion < newVersion ) {

			for( Migration migration : history ) {

				int sequence = migration.getSequence();

				if( oldVersion < sequence && sequence <= newVersion ) {
					transactions.add( migration.forward() );
				}

			}

		} else if( oldVersion > newVersion ) {

			for( int i = 0; i < history.length; i++ ) {

				Migration migration = history[history.length - 1 - i];

				int sequence = migration.getSequence();

				if( newVersion < sequence && sequence <= oldVersion ) {
					transactions.add( migration.reverse() );
				}

			}

		}

		return transactions;

	}

	private final int sequence;
	private final Transaction forward;
	private final Transaction reverse;

	public Migration( int sequence, final String forwardSql, final String reverseSql ) {

		this.sequence = sequence;

		this.forward = new Transaction() {

			@Override
			public void perform( Session session ) {
				session.query( forwardSql );
			}

		};

		this.reverse = new Transaction() {

			@Override
			public void perform( Session session ) {
				session.query( reverseSql );
			}

		};

	}

	public int getSequence() {
		return sequence;
	}

	public Transaction forward() {
		return forward;
	}

	public Transaction reverse() {
		return reverse;
	}

	@Override
	public boolean equals( Object object ) {
		if( !( object instanceof Migration ) ) {
			return false;
		}
		return compareTo( (Migration) object ) == 0;
	}

	@Override
	public int compareTo( Migration migration ) {
		return sequence < migration.sequence ? -1 : sequence == migration.sequence ? 0 : 1;
	}

}

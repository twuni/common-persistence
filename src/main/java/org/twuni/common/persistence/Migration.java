package org.twuni.common.persistence;

public class Migration implements Comparable<Migration> {

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

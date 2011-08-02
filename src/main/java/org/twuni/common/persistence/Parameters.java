package org.twuni.common.persistence;

public interface Parameters {

	public static final Parameters NONE = new Parameters() {

		@Override
		public void apply( Parameterized target ) {
		}

	};

	public void apply( Parameterized target );

}

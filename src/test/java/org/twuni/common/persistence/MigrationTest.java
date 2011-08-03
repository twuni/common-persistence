package org.twuni.common.persistence;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class MigrationTest {

	private Migration [] migrations;
	private Session session;

	@Before
	public void setUp() {
		migrations = new Migration [] {
		    new Migration( 1, "test", "untest" ),
		    new Migration( 2, "another test", "not another test" ),
		    new Migration( 3, "12345", "54321" )
		};
		session = mock( Session.class );
	}

	@Test
	public void testFullForwardMigration() {
		migrate( 0, 3 );
		verify( session, times( 1 ) ).query( "test" );
		verify( session, times( 1 ) ).query( "another test" );
		verify( session, times( 1 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testFullReverseMigration() {
		migrate( 3, 0 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 1 ) ).query( "untest" );
		verify( session, times( 1 ) ).query( "not another test" );
		verify( session, times( 1 ) ).query( "54321" );
	}

	@Test
	public void testPartialForwardMigrationLowerBound() {
		migrate( 0, 1 );
		verify( session, times( 1 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testPartialForwardMigrationUpperBound() {
		migrate( 2, 3 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 1 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testPartialForwardMigrationInBounds() {
		migrate( 1, 2 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 1 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testPartialReverseMigrationLowerBound() {
		migrate( 1, 0 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 1 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testPartialReverseMigrationUpperBound() {
		migrate( 3, 2 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 1 ) ).query( "54321" );
	}

	@Test
	public void testPartialReverseMigrationInBounds() {
		migrate( 2, 1 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 1 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testNoMigrationLowerBound() {
		migrate( 1, 1 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testNoMigrationUpperBound() {
		migrate( 3, 3 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	@Test
	public void testNoMigrationInBounds() {
		migrate( 2, 2 );
		verify( session, times( 0 ) ).query( "test" );
		verify( session, times( 0 ) ).query( "another test" );
		verify( session, times( 0 ) ).query( "12345" );
		verify( session, times( 0 ) ).query( "untest" );
		verify( session, times( 0 ) ).query( "not another test" );
		verify( session, times( 0 ) ).query( "54321" );
	}

	private void migrate( int oldVersion, int newVersion ) {
		for( Transaction transaction : Migration.migrate( oldVersion, newVersion, migrations ) ) {
			transaction.perform( session );
		}
	}

}

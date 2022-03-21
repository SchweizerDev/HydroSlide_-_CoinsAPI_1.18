package ch.luca.hydroslide.coinsapi.mysql;

import ch.luca.hydroslide.coinsapi.mysql.row.Row;
import ch.luca.hydroslide.coinsapi.mysql.row.Rows;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MySQL {

    private ExecutorService pool;

    private BasicDataSource sqlPool;

    public MySQL( String host, String database, String user, String password, int port ) {
        // Init async pool
        this.pool = Executors.newCachedThreadPool();

        // Create mysql connection
        this.sqlPool = new BasicDataSource();

        this.sqlPool.setDriverClassName( "com.mysql.jdbc.Driver" );
        this.sqlPool.setUrl( "jdbc:mysql://" + host + ":" + port + "/" + database );
        this.sqlPool.setUsername( user );
        this.sqlPool.setPassword( password );

        this.sqlPool.setMaxIdle( 30 );
        this.sqlPool.setMinIdle( 5 );
        this.sqlPool.setDriverClassLoader( MySQL.class.getClassLoader() );
    }

    /**
     * Update async a statement
     *
     * @param statement to update
     * @param objects   as parameters for the statement
     */
    public void updateAsync( String statement, Object... objects ) {
        this.pool.execute( () -> {
            Connection connection = null;
            try {
                // Open connection
                connection = this.sqlPool.getConnection();

                // Open statement
                PreparedStatement preparedStatement = this.prepare( statement, objects, connection );

                // Execute the statement
                preparedStatement.executeUpdate();

                // Close
                preparedStatement.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            } finally {
                if ( connection != null ) {
                    try {
                        // Close connection
                        connection.close();
                    } catch ( SQLException e ) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }

    public void queryAsync(String statement, Consumer<Rows> consumer, Object... objects ) {
        this.pool.execute( () -> consumer.accept( this.query( statement, objects ) ) );
    }

    public Rows query( String statement, Object... objects ) {
        Connection connection = null;
        Rows rows = null;

        try {
            // Open connection
            connection = this.sqlPool.getConnection();

            // Open statement
            PreparedStatement preparedStatement = this.prepare( statement, objects, connection );

            // Execute query to get ResultSet
            ResultSet resultSet = preparedStatement.executeQuery();

            // Convert ResultSet to Row
            rows = getRows( resultSet );

            // Close PreparedStatement & ResultSet
            resultSet.close();
            preparedStatement.close();
        } catch ( SQLException e ) {
            e.printStackTrace();
        } finally {
            if ( connection != null ) {
                try {
                    // Close connection
                    connection.close();
                } catch ( SQLException e ) {
                    e.printStackTrace();
                }
            }
        }
        return rows != null ? rows : new Rows();
    }

    private PreparedStatement prepare( String statement, Object[] objects, Connection connection ) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement( statement );

        // Replace parameters
        for ( int i = 0; i < objects.length; i++ ) {
            int id = i + 1;
            preparedStatement.setObject( id, objects[i] );
        }
        return preparedStatement;
    }

    private Rows getRows( ResultSet resultSet ) throws SQLException {
        // Create empty list
        List<Row> rowList = new ArrayList<>();

        // Get metadata from result set
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get column count from meta data
        int columnCount = metaData.getColumnCount();

        // Create map
        Map<String, Object> row;

        // Iterate result set
        while ( resultSet.next() ) {
            // Init map with size of column count
            row = new HashMap<>( columnCount );

            // Iterate column count
            for ( int i = 1; i <= columnCount; i++ ) {
                // Put column name with result
                row.put( metaData.getColumnName( i ).toLowerCase(), resultSet.getObject( i ) );
            }
            // Add to list
            rowList.add( new Row( row ) );
        }
        return new Rows( rowList );
    }
}

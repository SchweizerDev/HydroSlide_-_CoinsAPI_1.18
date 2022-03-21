package ch.luca.hydroslide.coinsapi.mysql.row;

import lombok.Getter;

import java.util.Map;

public class Row {

    @Getter
    private Map<String, Object> values;

    public Row( Map<String, Object> results ) {
        this.values = results;
    }

    /**
     * Get String value by column name
     *
     * @param columnName of the String
     * @return the result. the result can be null
     */
    public String getString( String columnName ) {
        return this.get( columnName, String.class );
    }

    /**
     * Get Integer value by column name
     *
     * @param columnName of the Integer
     * @return the result. the result can be null
     */
    public int getInt( String columnName ) {
        return this.get( columnName, Integer.class );
    }

    /**
     * Get Double value by column name
     *
     * @param columnName of the Double
     * @return the result. the result can be null
     */
    public double getDouble( String columnName ) {
        return this.get( columnName, Double.class );
    }

    /**
     * Get Long value by column name
     *
     * @param columnName of the Long
     * @return the result. the result can be null
     */
    public long getLong( String columnName ) {
        return this.get( columnName, Long.class );
    }

    /**
     * Get Boolean value by column name
     *
     * @param columnName of the Boolean
     * @return the result. the result can be null
     */
    public boolean getBoolean( String columnName ) {
        return this.get( columnName, Boolean.class );
    }

    /**
     * Get the value by column name
     *
     * @param columnName of the value
     * @param clazz      of the value to cast
     * @param <T>        of the value to cast
     * @return the value. the result can be null if the name not found or the object can't cast to specify class
     */
    public <T> T get( String columnName, Class<T> clazz ) {
        // Make name lowercase
        columnName = columnName.toLowerCase();

        // Check if column name exists
        if ( !this.values.containsKey( columnName ) ) {
            return null;
        }
        // Get object from column name
        Object object = this.values.get( columnName );

        if(object == null) {
            return null;
        }

        // Check if object is instance of class
        if ( clazz.isInstance( object ) ) {
            // Cast object
            return clazz.cast( object );
        }
        return null;
    }
}

package ch.luca.hydroslide.coinsapi.mysql.row;

import java.util.Collections;
import java.util.List;

public class Rows {

    private List<Row> rows;

    public Rows( List<Row> rows ) {
        this.rows = rows;
    }

    public Rows() {
        this( Collections.emptyList() );
    }

    /**
     * Get first Row
     *
     * @return the first Row. the result can be null
     */
    public Row first() {
        // Check if first row exists
        if ( this.rows.size() > 0 ) {
            return this.rows.get( 0 );
        }
        return null;
    }

    /**
     * Get Row by index
     *
     * @param index of the Row
     * @return the Row. the result can be null if the index not found
     */
    public Row get( int index ) {
        // Check if index exists
        if ( index < 0 || index >= this.rows.size() ) {
            return null;
        }
        return this.rows.get( index );
    }

    /**
     * Get all Rows
     *
     * @return all Rows. the result cannot be null
     */
    public List<Row> all() {
        return this.rows;
    }
}

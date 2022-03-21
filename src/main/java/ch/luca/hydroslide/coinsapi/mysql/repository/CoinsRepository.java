package ch.luca.hydroslide.coinsapi.mysql.repository;

import ch.luca.hydroslide.coinsapi.mysql.MySQL;
import ch.luca.hydroslide.coinsapi.mysql.row.Rows;

import java.util.UUID;
import java.util.function.Consumer;

public class CoinsRepository {

    private MySQL mySQL;

    public CoinsRepository( MySQL mySQL ) {
        this.mySQL = mySQL;
    }

    /**
     * Get the Coins of a player
     *
     * @param uniqueId of the player
     * @param consumer with the coins
     */
    public void getCoins( UUID uniqueId, Consumer<Integer> consumer ) {
        this.mySQL.queryAsync( "SELECT amount FROM Coins WHERE UUID = ?", rows -> {
            if ( rows != null && rows.first() != null ) {
                consumer.accept( rows.first().getInt( "amount" ) );
                return;
            }
            consumer.accept( -1 );
        }, uniqueId.toString() );
    }

    /**
     * Get the Coins of a player
     *
     * @param uniqueId of the player
     */
    public int getCoins( UUID uniqueId ) {
        Rows rows = this.mySQL.query( "SELECT amount FROM Coins WHERE UUID = ?", uniqueId.toString() );
        if ( rows != null && rows.first() != null ) {
            return rows.first().getInt( "amount" );
        }
        return -1;
    }

    /**
     * Check if a player has the Coins
     *
     * @param uniqueId of the player
     * @param coins    to check
     * @param consumer with the result
     */
    public void hasCoins( UUID uniqueId, int coins, Consumer<Boolean> consumer ) {
        this.getCoins( uniqueId, playerCoins -> {
            if ( playerCoins == -1 ) {
                consumer.accept( false );
                return;
            }
            consumer.accept( playerCoins >= coins );
        } );
    }

    /**
     * Add Coins to a player
     *
     * @param uniqueId of the player
     * @param coins    to add
     */
    public void addCoins( UUID uniqueId, int coins ) {
        this.mySQL.updateAsync( "UPDATE Coins SET amount = amount + ? WHERE uuid = ?", coins, uniqueId.toString() );
    }

    /**
     * Remove Coins from a player
     *
     * @param uniqueId of the player
     * @param coins    to remove
     */
    public void removeCoins( UUID uniqueId, int coins ) {
        this.mySQL.updateAsync( "UPDATE Coins SET amount = amount - ? WHERE uuid = ?", coins, uniqueId.toString() );
    }
}


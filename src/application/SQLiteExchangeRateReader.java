package application;

import view.ExchangeRateReader;
import model.Currency;
import model.ExchangeRate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javaslang.control.Try;

public class SQLiteExchangeRateReader implements ExchangeRateReader {

    @Override
    public ExchangeRate get(Currency from, Currency to) {
        return Try.of(() -> DriverManager.getConnection("jdbc:sqlite:currencies.db"))
                .mapTry((Connection connection) -> connection.createStatement())
                .mapTry((Statement statement) -> getExchangeRate(statement, from, to))
                .get();
    }

    private ExchangeRate getExchangeRate(Statement statement, Currency from, Currency to) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM EXCHANGE_RATES "
                + "WHERE currency_from='" + from.getCode() + "'"
                + "AND currency_to='" + to.getCode() + "'");
        return new ExchangeRate(from, to, resultSet.getDouble("exchange_rate"));
    }

}

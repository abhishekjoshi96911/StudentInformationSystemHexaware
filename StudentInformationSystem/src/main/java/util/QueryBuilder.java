package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

    private StringBuilder query;
    private List<Object> parameters;

    public QueryBuilder() {
        this.query = new StringBuilder();
        this.parameters = new ArrayList<>();
    }

    public QueryBuilder select(String... columns) {
        query.append("SELECT ");
        appendColumns(columns);
        return this;
    }

    public QueryBuilder from(String table) {
        query.append(" FROM ").append(table);
        return this;
    }

    public QueryBuilder where(String condition) {
        query.append(" WHERE ").append(condition);
        return this;
    }

    public QueryBuilder orderBy(String column, boolean ascending) {
        query.append(" ORDER BY ").append(column);
        if (!ascending) {
            query.append(" DESC");
        }
        return this;
    }

    public QueryBuilder addParameter(Object parameter) {
        parameters.add(parameter);
        return this;
    }

    public <T> List<T> executeQuery(Connection connection, RowMapper<T> rowMapper) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            setParameters(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<T> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(rowMapper.mapRow(resultSet));
                }
                return result;
            }
        }
    }

    private void appendColumns(String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            query.append(columns[i]);
            if (i < columns.length - 1) {
                query.append(", ");
            }
        }
    }

    private void setParameters(PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
    }

    public interface RowMapper<T> {
        T mapRow(ResultSet resultSet) throws SQLException;
    }
}

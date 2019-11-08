package ai.ksense.jddl;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableDiffGenerator {
    private final Table actual;
    private final Table expected;
    private final String tableName;

    public TableDiffGenerator(Table actual, Table expected) {
        Preconditions.checkState(Objects.equals(actual.getName().toUpperCase(), expected.getName().toUpperCase()), "Different table names: %s != %s", actual.getName(), expected.getName());
        this.tableName = actual.getName();
        this.actual = actual;
        this.expected = expected;
    }

    public List<TableStatement> diff() {
        List<TableStatement> statements = new ArrayList<>();
        for (Column actualColumn : actual.getColumns()) {
            Column expectedColumn = expected.getColumnByName(actualColumn.getName());
            if (expectedColumn == null) {
                statements.add(new TableStatement.DeleteColumn(tableName, actualColumn.getName()));
            } else {
                checkCompatibility(expectedColumn, actualColumn);
            }
        }

        for (Column expectedColumn : expected.getColumns()) {
            Column actualColumn = actual.getColumnByName(expectedColumn.getName());
            if (actualColumn == null) {
                statements.add(new TableStatement.AddColumn(tableName, expectedColumn));
            } else {
                checkCompatibility(expectedColumn, actualColumn);
            }
        }
        return statements;
    }

    private void checkCompatibility(Column expectedColumn, Column column) {

    }
}
